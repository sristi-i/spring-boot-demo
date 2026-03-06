package com.springboot.demo.filter;

import com.springboot.demo.service.JwtService;
import com.springboot.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// OncePerRequestFilter = runs doFilterInternal() exactly once per HTTP request
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                throws ServletException, IOException{

        // 1. read authorization headr: "Bearer eyJhbGci..."
        final String authHeader = request.getHeader("Authorization");

        // 2. noheader or not a Bearer token -> skip this filter
        // public endpoint like /api/auth/login will pass through here
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. strip "Bearer " (7 chars includeing space) -> get raw JWT
        final String jwt = authHeader.substring(7);

        // 4. extract username from JWT "sub" claim
        final String username = jwtService.extractUsername(jwt);

        // 5. only proceed if username found and  not already authenticated
        // avoids processing the same request twice
        if(null != username &&
                null == SecurityContextHolder.getContext().getAuthentication()){
            // 6. load user from DB to get roles/authorities
            UserDetails userDetails = userService.loadUserByUsername(username);

            // 7. validate: username matches + token not expired
            if(jwtService.isTokenValid(jwt, userDetails)){

                // 8. 3-arg constructor = post-authentication (authenticated = true)
                // null credentials = already verified, no need to hold passsword
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 9. attach request details (IP address, session ID)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. store in SecurityContext - this request is now authenticated
                // controller reads this via SecurityContextHolder.getContext()
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. always continue the chain - never block here
        // if token was invalid, SecurityContext stays empty -> Spring returns 403
        filterChain.doFilter(request, response);
        
    }
}

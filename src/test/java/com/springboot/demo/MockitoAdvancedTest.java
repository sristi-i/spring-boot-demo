package com.springboot.demo;

import com.springboot.demo.product.ProductEntity;
import com.springboot.demo.product.ProductRepository;
import com.springboot.demo.product.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockitoAdvancedTest {
    
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // test 1 - doNothing() - mocking void methods
    // void methods cannot use when().thenReturn() because they return nothing
    // use - doNothing().when(mock).voidMethod()
    // for verify- verify(mock).voidMethod(args)

    // by default - mockito already does nothing for void mocks
    // but doNothing() is explicit and makes intent clear
    @Test
    @DisplayName("deleteProduct - verify deleteById was called")
    void testDeleteProduct_VerifyVoidMethod() {
        // ARRANGE — explicitly tell mock: do nothing when deleteById is called
        // doNothing() is DEFAULT for void mocks, but writing it shows intent
        doNothing().when(productRepository).deleteById(1L);
 
        // ACT — call the real service method
        productService.deleteProduct(1L);
 
        // VERIFY — confirm deleteById was called with id = 1L
        verify(productRepository, times(1)).deleteById(1L);
        // VERIFY — findById was NEVER called during delete
        verify(productRepository, never()).findById(anyLong());
    }

    // test 2 - ArgumentCaptor - capture what was passed to mock
    // ArgumentCaptor captures the actual argument passed to a mock method
    // use it when you want to inspect what value was passed - not just verify the method that the method was called
    // Syntax:
    //     ArgumentCaptor<Type> captor = ArgumentCaptor.forClass(Type.class);
    //     verify(mock).method(captor.capture());
    //     captor.getValue() → returns the captured argument
    @Test
    @DisplayName("saveProduct - capture and inspect saved product")
    void testSaveProduct_WithArgumentCaptor() {
        // ARRANGE
        ProductEntity product = new ProductEntity();
        product.setName("Headphones");
        product.setPrice(3000.0);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
 
        // ACT
        productService.saveProduct(product);
 
        // Create ArgumentCaptor to capture what was passed to save()
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
 
        // VERIFY — capture the argument that was passed to save()
        verify(productRepository).save(captor.capture());
 
        // Now inspect the captured value
        ProductEntity capturedProduct = captor.getValue();
        assertEquals("Headphones", capturedProduct.getName());
        assertEquals(3000.0, capturedProduct.getPrice());
    }

    // test 3 - @Captor annotation - same as above but cleaner
    // @Captor vs ArgumentCaptor.forClass()?
    //   @Captor is just a cleaner annotation-based way to declare a captor.
    //   Both do the same thing — @Captor avoids the manual forClass() call.
    @Captor
    private ArgumentCaptor<ProductEntity> productCaptor;
    @Test
    @DisplayName("saveProduct - using @Captor annotation")
    void testSaveProduct_WithCaptorAnnotation() {
        // ARRANGE
        ProductEntity product = new ProductEntity();
        product.setName("Speaker");
        product.setPrice(5000.0);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
 
        // ACT
        productService.saveProduct(product);
 
        // VERIFY using @Captor field (same result as ArgumentCaptor.forClass)
        verify(productRepository).save(productCaptor.capture());
        assertEquals("Speaker", productCaptor.getValue().getName());
    }

    // test 4 - @Spy - partial mock
    // @Mock - all methods are afaked, real code is never used - returns null/0/false unless stubbed
    // @Spy - creates a real obbject - real methods are called by deafult
    // you can selectively stub specific methods
    // Use @Spy when:
    //   - You want REAL behavior for most methods
    //   - But fake only ONE specific method
    //
    // Use @Mock when:
    //   - You want to fake ALL behavior (external dependency like DB, API)

    @Spy
    private ProductService spyProductService = new ProductService();
    // (passing null repository — we'll stub the method we don't want to call)
    @Test
    @DisplayName("@Spy - real method runs, stubbed method is faked")
    void testSpy_PartialMock() {
        // With @Spy, real methods run unless specifically stubbed.
        // Let's stub just one method and let others run normally.

        // doReturn().when() is preferred for @Spy (not when().thenReturn())
        // Why use doReturn() with @Spy instead of when().thenReturn()?
        //   when(spy.method()) actually CALLS the real method during setup.
        //   doReturn().when(spy).method() does NOT call real method during setup.
        //   → Use doReturn() with @Spy to avoid unwanted side effects.
        doReturn(new ProductEntity(1L, "Mocked Product", 999.0))
                .when(spyProductService).getProductById(1L);
 
        // This call is FAKED (returns mock data)
        ProductEntity faked = spyProductService.getProductById(1L);
        assertEquals("Mocked Product", faked.getName());
 
        // VERIFY the spied method was called
        verify(spyProductService, times(1)).getProductById(1L);

    }

    // test 5 - Argument Matchers — any(), anyLong(), anyString()
    //   Argument matchers allow flexible stubbing/verification
    //   when you don't care about the EXACT argument value.
    //
    //   any()           → any object (including null)
    //   any(Type.class) → any object of specific type
    //   anyLong()       → any long value
    //   anyInt()        → any int value
    //   anyString()     → any String value
    //   eq(value)       → exact value (use inside matchers context)

    // RULE: If you use a matcher for ONE argument, ALL arguments must use matchers.
    //   WRONG: when(repo.findByNameAndId("Laptop", anyLong()))  ← mixing!
    //   RIGHT: when(repo.findByNameAndId(eq("Laptop"), anyLong()))

    @Test
    @DisplayName("Argument matchers - any(), anyLong()")
    void testArgumentMatchers() {
        // ARRANGE — use anyLong() so stub works for ANY Long id
        ProductEntity product = new ProductEntity(5L, "Keyboard", 1500.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
 
        // ACT — these all hit the same stub because anyLong() matches anything
        ProductEntity r1 = productService.getProductById(1L);
        ProductEntity r2 = productService.getProductById(100L);
        ProductEntity r3 = productService.getProductById(999L);
 
        // ASSERT
        assertEquals("Keyboard", r1.getName());
        assertEquals("Keyboard", r2.getName());
        assertEquals("Keyboard", r3.getName());
 
        // VERIFY findById was called 3 times total
        verify(productRepository, times(3)).findById(anyLong());
    }

    // test 6 - @Mock vs @mockBean 
    // What is the difference between @Mock and @MockBean?
    //
    //   @Mock (Mockito annotation):
    //     - Creates mock WITHOUT Spring context
    //     - Used with @ExtendWith(MockitoExtension.class)
    //     - Fast — no Spring startup
    //     - Use for UNIT TESTS (testing service/class in isolation)
    //
    //   @MockBean (Spring Boot Test annotation):
    //     - Creates mock AND registers it in Spring ApplicationContext
    //     - Used with @SpringBootTest or @WebMvcTest
    //     - Slow — Spring context loads
    //     - Use for INTEGRATION TESTS (testing with Spring context)
    //
    // so:
    //   Unit test  → @Mock + @InjectMocks + @ExtendWith(MockitoExtension.class)
    //   Integration test → @MockBean + @SpringBootTest / @WebMvcTest



}

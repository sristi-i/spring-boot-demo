package com.springboot.demo;

//Mockito basics
// what
// mockito is a mocking framework that lets us test a class
// in ISOLATION by replacing its real dependencies with fake (mock) objects that we fully control

// why
// ProductService depends on ProductRepository (database)
// In unit tests we dont want a real DB
// -> mockito creates a fake ProductRepositry
// -> "when findById(1) is called, return this Product"
// -> we test ProductService logic without touching the database

//different from JUnit 5
// JUnit -> runs tests, provides assertions (what to check)
// mockito -> provides mocks and stubbing (how to fake dependencies)
// Together -> write fast, isolated unit tests

import com.springboot.demo.product.ProductEntity;
import com.springboot.demo.product.ProductRepository;
import com.springboot.demo.product.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) - what does t do?
// It activates Mockito annotation (@Mock, @InjectMocks, @Spy, @Captor)
// without this, those annotations are ignored and fields are null
// JUnit 4 euivalent - @RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class MockitoBasicsTest {
    
    // What is @Mock?
    // Creates a fakse (mock) object of ProductRepositry
    // all methods return default values (null objects, 0 for int, false for boolean)
    // unless we stub them with when().thenReturn()
    // -> does not hit real database
    @Mock
    private ProductRepository productRepository;

    // what is @InjectMocks?
    // creates a real instance of ProductService and automatically injext s the @Mock fields into it
    // -> ProductService gets the fake productRepositry, not a real one
    
    // difference betweemn @Mock and @InjectMocks?
    // @Mock -> creates a fake dependency (eg. ProductRepositry)
    // @InjectMocks -> creates  the real class under test + injects mocks into it
    @InjectMocks
    private ProductService productService;

    // test 1 - when()thenReturn() - stubbing
    // what is stubbing in mockito?
    // stubbing - telling a mock wht to return when a method is called
    // syntax: when(mock.methos(args)).thenReturn(value)
    // -> "when productrepositry.findId(1L) is called, return this product"
    @Test
    @DisplayName("getProductById - returns product when found")
    void testGetProductById_WhenFound(){
        // arrange - setup the fake data and stub the mock
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(5000.0);

        // stubbing - tell the mock what to return
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // act - call the real method on ProductService
        ProductEntity result = productService.getProductById(1L);

        // assert - verify the result is corect
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(5000.0, result.getPrice());

    }

    // test 2 - verify() - behaviour verification
    // verify() checks that a specific method was called on a mock
    // it is used to verify interactions, not just return values
    // syntax: verify(mock).methodName(args)
    // -> "I want tp confirm that productRepositry.finsById(1L" was actually called"
    @Test
    @DisplayName("getProductById - verify repositry was called")
    void testGetPrductById_VerifyInteraction(){
        // arraneg - 
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Phone");
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        //act
        productService.getProductById(1L);

        // verify - confirm the mock method was called exactly once
        // verify() default = called exactly 1 time
        verify(productRepository).findById(1L);

    }

    // test 3 - verify(times(n)) nad verify(never())
    // verify(mock, times(2)).method() -> called exactly 2 times
    // verify(mock never()).method -> never called
    // verify(mock, atLeastOnce()).method() -> called 1 or more times
    // verify(mock, atMost(3)).method() -> called 0 or 3 times
    @Test
    @DisplayName("getAllProducts - verify findAll called once")
    void testGetAllProducts_VerifyTimesAndNever() {
        // ARRANGE
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity(1L, "Laptop", 50000.0),
                new ProductEntity(2L, "Phone", 15000.0)
        );
        when(productRepository.findAll()).thenReturn(products);
 
        // ACT
        List<ProductEntity> result = productService.getAllProducts();
 
        // ASSERT result
        assertEquals(2, result.size());
 
        // VERIFY — findAll was called exactly 1 time
        verify(productRepository, times(1)).findAll();
 
        // VERIFY — save was NEVER called during getAllProducts
        verify(productRepository, never()).save(any());
    }

    // test 4 - thenThrow() - stubbing exceptions
    // hpw do you test excpetion scenarios in Mockito?
    // use thenthrow() to make the mock throw an exception
    // when(mock.method()).thenThrow(new RuntimeException("msg"))
    @Test
    @DisplayName("getProductById - throws exception when not found")
    void testGetProductById_ThrowsException() {
        // arrange — stub to return empty Optional (product not found)
        when(productRepository.findById(99L))
        .thenThrow(new RuntimeException("Product not found with id: 99"));
         
        // act & assert — ProductService should throw RuntimeException
        // This uses assertThrows from JUnit 5 (Day 14)
        //   + thenReturn(Optional.empty()) from Mockito (Day 15)
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.getProductById(99L));
 
        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    // test 5 - saveProduct - verify save was called with correct data
    @Test
    @DisplayName("saveProduct - saves and returns product")
    void testSaveProduct() {
        // ARRANGE
        ProductEntity product = new ProductEntity();
        product.setName("Tablet");
        product.setPrice(25000.0);
 
        // Stub: when save() is called with any Product, return that product
        // INTERVIEW: any() / any(Product.class) = argument matcher
        //   Use when you don't care about the exact argument value
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
 
        // ACT
        ProductEntity saved = productService.saveProduct(product);
 
        // ASSERT
        assertNotNull(saved);
        assertEquals("Tablet", saved.getName());
 
        // VERIFY save was called once
        verify(productRepository, times(1)).save(product);
    }

    // test 6 - thenTrhow() directly on mock
    @Test
    @DisplayName("saveProduct - throws exception when repository fails")
    void testSaveProduct_ThrowsException() {
        // ARRANGE — stub to throw exception when save() is called
        ProductEntity product = new ProductEntity(3, "Broken", -1.0);
        when(productRepository.save(any(ProductEntity.class)))
                .thenThrow(new RuntimeException("Database error"));
 
        // ACT & ASSERT
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productService.saveProduct(product));
 
        assertEquals("Database error", ex.getMessage());
 
        // VERIFY save was still attempted once before it threw
        verify(productRepository, times(1)).save(product);
    }

}

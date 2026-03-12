package com.springboot.demo;

// JUnit 5 lifecycle Annotations Demo

// what
// JUnit is a unit testing framework for java
// unit - one clas or one method - tested in isolation

//why 
// tests catches bugs before they reach production

// Junit architecture - 3 modules
// JUnit platform - runs tess\ts (used by Maven / IntelliJ)
// JUnit Jupiter - new annotations and assestions - developer use it
// JUnit vintage - runs old JUnit 3/4 tests - backward compatibility

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.springboot.demo.Calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AnnotationsTest {
    
    // BeforeAll - runs once before all test methods in this class
    // must be static - JVM calss it beofre any instance
    // use for - DB connection, loading config files, expensive on-time setup
    // JUnit 4 equivalent - @BeforeClass
    @BeforeAll
    static void BeforeAll(){
        System.out.println("@BeforeAll ---- runs once before all tests start");
    }

    // @BeforeEach - runs before every @Twest method
    // not static - each test gets a fresh object, so instance methods work
    // use for - resetting state so tests dont affect each other 
    // JUnit 4 equivalent - @Before
    @BeforeEach
    void beforeEach(){
        System.out.println("@BeforeEach - runs before each test");
    }

    // @Test - marks a method as a test case
    // Rules - void return, no paraameters, any method name is fine
    // JUnit 5 @Test does not take arguments (JUnit 4 had @Test(expected=...))
    @Test
    void testAddition(){
        Calculator calc = new Calculator();
        assertEquals(4, calc.add(2, 2));
        System.out.println("@Test ---- testAddition executed");
    }

    @Test
    void testSubtraction() {
        Calculator calc = new Calculator();
        assertEquals(6, calc.subtract(10, 4));             // 10-4 = 6 ✅
        System.out.println("@Test ---- testSubtraction executed");
    }

    @Test
    void testMultiplication(){
        Calculator calc = new Calculator();
        assertEquals(15, calc.multiply(3, 5));
        System.out.println("@Test ---- testMultiplication executed");
    }

    // @DisplayName - gives a human readable name to the test in the report
    // without it the report shows the method name
    // with it you can write a sentence describing what is tested
    // new in JUnit 5 - no JUnit 4 quivalent
    @Test
    @DisplayName("Division of 10 by 2 should return 5")
    void testDivision() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.divide(10,2));
        System.out.println("  @Test ---- testDivision executed");
    }

    // @Disabled - skips this test - it will not run
    // shows as "skipped" in the report, not a failure
    // use when: feature not built yet, test is temporarily broken, WIP
    // JUnit 4 equivalent - @Ignore
    @Test
    @Disabled("Feature not implemented yet - will enable later")
    void testSomeFutureFeature(){
        System.out.println("This line prints -- @Disabled skips the test");

    }

    // @AfterEach
    // runs after every @Test method
    // not static
    // use for - cleanup after test - clean a list, reset a counter
    // JUnit 4 equivalent @After
    @AfterEach
    void afterEaach(){
        System.out.println("@AfterEach --- runs fter each test");
    }

    // @AfterAll - runs once afrter all test methods finish
    // must be static - mirrors @BeforeAll
    // use for - closing DB connections, releasing shared resources
    // JUnit 4 equivalent: @AfterClass
    @AfterAll
    static void afterAll(){
        System.out.println("@AfterAll --- runs once after all tests finish");
    }
    
    
}

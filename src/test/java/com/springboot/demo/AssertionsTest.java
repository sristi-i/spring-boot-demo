package com.springboot.demo;

// Assertions 

// what 
// an assertion is a check inside a test
// say "I expect this result"
// if actual result nit equal to expected -> test fails
// if actual result = expected -> test passes

// where are all assertions?
// all JUnit 5 assestions are static methods
// with static import -> write assertEquals() directly instead of 
// Assertions.assertEquals() - cleaner code, same effect

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.springboot.demo.calculator.Calculator;

// static import — so we write assertEquals() not Assertions.assertEquals()
import static org.junit.jupiter.api.Assertions.*;

public class AssertionsTest {
    
    // @BeforeEach created a fresh calculator before every test
    Calculator calculator;

    @BeforeEach
    void setUp(){
        calculator = new Calculator(); // fresh instance before every @Test
    }

    // 1. assertEquals(expected, actual) - checks twwo values are equal using .equals
    @Test
    @DisplayName("asserEquals - checks value equality")
    void testAssertEquals(){
        assertEquals(4, calculator.add(2, 2));
        assertEquals(6, calculator.subtract(10, 4));
        assertEquals(15, calculator.multiply(3, 5));

        // 3rd argument - custom failure mesage - shown when test fails
        assertEquals(5, calculator.divide(10, 2), "10 divide by 2 must be 5");
    }

    // 2. assertNotEquals(expected, actual)
    // checks that two values are not equal
    @Test
    @DisplayName("assertNotEquals - value must be different")
    void testAssertNotEquals(){
        // 2+2 = 4, and is not equal to 5 - passes
        assertNotEquals(6, calculator.add(2, 2));
        assertNotEquals(0, calculator.multiply(3, 5));
    }

    // 3. assertTrue(condition) / assertFalse(condition)
    // checks if a boolean expression is true or false
    @Test
    @DisplayName("assertTrue / assertFalse - boolean checks")
    void testBooleans(){
        int result = calculator.add(5, 3); // 8

        assertTrue(result > 0); // true
        assertTrue(result == 8); // true
        assertFalse(result < 0); // false
        assertFalse(result == 99); // false

        // with custom failure messages
        assertTrue(result > 5, "result should be greater than that 5");

    }

    // 4. assertNull(object) / assertNotNull(object)
    // checks if an object is null or not null
    // very common in servoce tests - 
    // "After calling findById(), the returned obkject must not be null"
    @Test
    @DisplayName("assertNull / assertNotNull - null checks")
    void testNullChecks(){
        String name = null;
        assertNull(name); // true

        String username = "Sam";
        assertNotNull(username); // true
        assertNotNull(calculator); // true

        // with custom message
        assertNotNull(username, "Username must not be null after setup"); // true
    }

    // 5. assertSAme(expected, actual) / assertNotSame(expected, actual)
    // uses reference equality (==) - are both bject pointing to same object in memory - same address
    // new String("hello") creates a NEW object in heap memory
    @Test
    @DisplayName("assertSame vs assertEquals — reference vs value")
    void testSameVsEquals() {
        String a = "hello";
        String b = a;                      // b points to the SAME object as a

        assertSame(a, b);                  // same reference (==)
        assertEquals(a, b);               // same value (.equals())

        String c = new String("hello");    // NEW object — different memory, same text
        assertEquals(a, c);               // same VALUE "hello"
        assertNotSame(a, c);              // DIFFERENT reference (objects)
    }

    // 6. assertThrows(ExceptionClass, lambda)
    //    Verifies that a specific exception IS thrown
    //   "divide by zero MUST throw ArithmeticException"
    //   If NO exception is thrown → TEST FAILS
    //   Returns the thrown exception so you can inspect the message too
    @Test
    @DisplayName("assertThrows — verify exception is thrown on divide by zero")
    void testException() {
        // Syntax: assertThrows(ExpectedException.class, () -> code-that-must-throw)
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,          // the exception type we EXPECT
            () -> calculator.divide(10, 0)      // this line MUST throw it
        );

        // Can also verify the exception message
        assertEquals("/ by zero", exception.getMessage());
        System.out.println("Caught exception message: " + exception.getMessage());
    }

    // 7. assertAll("group label", lambda1, lambda2, ...)
    //    Groups multiple assertions — ALL run even if some fail

    //   Normal assertions  → STOP at first failure.
    //     If check 1 fails → checks 2, 3, 4 never run.
    //     You see only ONE error at a time.
    //
    //   assertAll → runs ALL assertions no matter what.
    //     You see ALL failures together → much easier debugging!
    //     Reports as MultipleFailuresError with every failed assertion listed.
    @Test
    @DisplayName("assertAll — run all assertions, report all failures")
    void testGrouped() {
        assertAll("calculator operations",  // group name
            () -> assertEquals(4,  calculator.add(2, 2)),             
            () -> assertEquals(6,  calculator.subtract(10, 4)),       
            () -> assertEquals(15, calculator.multiply(3, 5)),        
            () -> assertNotNull(calculator),           
            () -> assertTrue(calculator.add(1, 1) > 0)
        );
    }

    // 8. assertArrayEquals(expected[], actual[])
    //    Compares two arrays element-by-element
    @Test
    @DisplayName("assertArrayEquals — compare array contents element by element")
    void testArrays() {
        int[] expected = {2, 4, 6, 8, 10};
        int[] actual   = new int[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = calculator.multiply(2, i + 1);  // 2*1, 2*2 ... 2*5
        }
        assertArrayEquals(expected, actual);  // {2,4,6,8,10} 
    }

}

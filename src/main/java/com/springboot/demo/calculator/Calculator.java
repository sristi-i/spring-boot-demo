package com.springboot.demo.calculator;

// simple class to practice JUnit
// Real projects
public class Calculator {
    public int add(int a, int b) {return a+b;}
    public int subtract(int a, int b) {return a-b;}
    public int multiply(int a, int b){return a*b;}

    // divide (n. 0) throws ArthimeticException - test
    // this with assertThrows() in AssertionsTest.java
    public int divide(int a, int b) {return a/b;}
}

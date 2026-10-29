package com.ankith.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void addReturnsSum() {
        assertEquals(5.0, service.add(2, 3));
        assertEquals(-1.0, service.add(2, -3));
        assertEquals(0.3, service.add(0.1, 0.2), 1e-9);
    }

    @Test
    void subtractReturnsDifference() {
        assertEquals(-1.0, service.subtract(2, 3));
        assertEquals(5.0, service.subtract(2, -3));
    }

    @Test
    void multiplyReturnsProduct() {
        assertEquals(6.0, service.multiply(2, 3));
        assertEquals(0.0, service.multiply(5, 0));
        assertEquals(-6.0, service.multiply(2, -3));
    }

    @Test
    void divideReturnsQuotient() {
        assertEquals(2.5, service.divide(5, 2));
        assertEquals(-2.0, service.divide(6, -3));
    }

    @Test
    void divideByZeroThrowsArithmeticException() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> service.divide(10, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }
}

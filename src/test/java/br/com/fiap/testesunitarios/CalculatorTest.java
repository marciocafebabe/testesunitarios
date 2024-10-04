package br.com.fiap.testesunitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;

class CalculatorTest {
    
    @Test
    void shouldSum() {
        int result = Calculator.sum(1, 2);
        assertThat(result).isEqualTo(3);
    }

    @Test
    void shouldSubtract() {
        int result = Calculator.subtract(2, 1);
        assertEquals(1, result);
    }

    @Test
    void shouldMultiply() {
        int result = Calculator.multiply(2, 2);
        assertEquals(4, result);
    }
    
    @Test
    void shouldDivide() {
        int result = Calculator.divide(2, 2);
        assertEquals(1, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        Throwable ex = catchThrowable(() -> Calculator.divide(2, 0));
        assertThat(ex).isInstanceOf(ArithmeticException.class)
                .hasMessage("/ by zero");
    }

}

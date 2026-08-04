package com.example.test.control;

import com.example.test.control.exception.DivisionByZeroException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculatorServiceTest {
    private final CalculatorService calculatorService = new CalculatorService();

    @ParameterizedTest
    @DisplayName("sum should return correct value")
    @MethodSource("sumArguments")
    void returnSum(double val1, double val2, double expected) {
        assertThat(calculatorService.add(val1, val2))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("divide should return correct value")
    @MethodSource("divideArguments")
    void returnDiv(double val1, double val2, double expected) {
        assertThat(calculatorService.divide(val1, val2))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("divide should throw DivisionByZeroException when divisor is zero")
    void divideRejectsDivisionByZero() {
        assertThatThrownBy(() -> calculatorService.divide(5.0, 0.0))
                .isInstanceOf(DivisionByZeroException.class)
                .hasMessage("Divisor cannot be zero.");
    }

    private static Stream<Arguments> sumArguments() {
        return Stream.of(
                Arguments.arguments(12.0, 13.5, 25.5),
                Arguments.arguments(0.0, 13.5, 13.5),
                Arguments.arguments(12.25, 13.5, 25.75),
                Arguments.arguments(-12.0, 13.5, 1.5),
                Arguments.arguments(-12.25, -13.5, -25.75),
                Arguments.arguments(0.0, 0.0, 0.0)
        );
    }

    private static Stream<Arguments> divideArguments() {
        return Stream.of(
                Arguments.arguments(12.0, 2.0, 6.0),
                Arguments.arguments(0.0, 13.5, 0.0),
                Arguments.arguments(12.5, 2.0, 6.25),
                Arguments.arguments(1.0, 2.0, 0.5),
                Arguments.arguments(-12.25, -2.0, 6.125),
                Arguments.arguments(-12.25, 2.0, -6.125)
        );
    }

}

package com.example.test.control;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorServiceTest {
    private final CalculatorService calculatorService = new CalculatorService();


    @ParameterizedTest
    @MethodSource("sumArguments")
    void returnSum(double val1, double val2, double expected) {
        assertThat(calculatorService.add(val1, val2))
                .isEqualTo(expected);
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

}

package com.example.test.boundary.rest;

import com.example.test.control.CalculatorService;
import com.example.test.control.exception.DivisionByZeroException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CalculatorController.class,
        properties = "spring.jackson.mapper.allow-coercion-of-scalars=false"
)
class CalculatorControllerTest {
    private final static String ADD_ENDPOINT = "/add";
    private final static String DIV_ENDPOINT = "/div";
    private final static String VALUE = "$.value";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalculatorService calculatorService;

    @ParameterizedTest
    @DisplayName("POST add endpoint should return OK status with correct value")
    @MethodSource("sumArguments")
    void addShouldReturnCorrectValue(double val1, double val2, double expected, String requestBody) throws Exception {
        when(calculatorService.add(val1, val2)).thenReturn(expected);

        mockMvc.perform(post(ADD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(VALUE).value(expected));

    }

    @ParameterizedTest
    @DisplayName("POST add endpoint should return BAD REQUEST when passed value has invalid type or is missing")
    @MethodSource("sumArgumentsWithInvalidType")
    void addShouldReturnBadRequestWhenValueHasInvalidType(String requestBody, String expectedMessage) throws Exception {
        mockMvc.perform(post(ADD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(ADD_ENDPOINT));

    }

    @ParameterizedTest
    @DisplayName("POST add endpoint should return BAD REQUEST when invalid JSON was passed")
    @MethodSource("sumArgumentsWithInvalidJson")
    void addShouldReturnBadRequestWhenInvalidJsonWasPassed(String requestBody, String expectedMessage) throws Exception {
        mockMvc.perform(post(ADD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(ADD_ENDPOINT));

    }

    @ParameterizedTest
    @DisplayName("GET div endpoint should return OK status with correct value")
    @MethodSource("divideArguments")
    void divShouldReturnCorrectValue(double val1, double val2, double expected) throws Exception {
        when(calculatorService.divide(val1, val2)).thenReturn(expected);

        mockMvc.perform(get(DIV_ENDPOINT)
                        .param("val1", String.valueOf(val1))
                        .param("val2", String.valueOf(val2))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(VALUE).value(expected));

    }

    @ParameterizedTest
    @DisplayName("GET div endpoint should return BAD REQUEST when param is missing")
    @MethodSource("divideArgumentsWithMissingParam")
    void divShouldReturnBadRequestWhenParamIsMissing(MultiValueMap<String, String> params, String expectedMessage) throws Exception {
        mockMvc.perform(get(DIV_ENDPOINT)
                        .params(params)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(DIV_ENDPOINT));

    }


    @ParameterizedTest
    @DisplayName("GET div endpoint should return BAD REQUEST when both params have invalid type")
    @MethodSource("divideArgumentsBothWithInvalidType")
    void divShouldReturnBadRequestWhenBothParamsHaveInvalidType(String val1, String val2, List<String> expectedMessages) throws Exception {
        mockMvc.perform(get(DIV_ENDPOINT)
                        .param("val1", val1)
                        .param("val2", val2)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(2)))
                .andExpect(jsonPath("$.messages", containsInAnyOrder(expectedMessages.toArray())))
                .andExpect(jsonPath("$.path").value(DIV_ENDPOINT));

    }

    @ParameterizedTest
    @DisplayName("GET div endpoint should return BAD REQUEST when one of the params have invalid type")
    @MethodSource("divideArgumentsWithInvalidType")
    void divShouldReturnBadRequestWhenOneParamHaveInvalidType(String val1, String val2, String expectedMessage) throws Exception {
        mockMvc.perform(get(DIV_ENDPOINT)
                        .param("val1", val1)
                        .param("val2", val2)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(DIV_ENDPOINT));

    }

    @Test
    @DisplayName("GET div endpoint should return BAD REQUEST when divisor is zero")
    void divShouldReturnBadRequestWhenDivisorIsZero() throws Exception {
        when(calculatorService.divide(5, 0)).thenThrow(new DivisionByZeroException());

        mockMvc.perform(get(DIV_ENDPOINT)
                        .param("val1", "5")
                        .param("val2", "0")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorName").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages").value("Divisor cannot be zero."))
                .andExpect(jsonPath("$.path").value(DIV_ENDPOINT));

    }

    private static Stream<Arguments> sumArguments() {
        return Stream.of(
                Arguments.arguments(
                        12.0,
                        6.0,
                        2.0,
                        """
                        {
                            "val1": 12.0,
                            "val2": 6.0
                        }
                        """),
                Arguments.arguments(
                        0.0,
                        13.5,
                        13.5,
                        """
                        {
                            "val1": 0.0,
                            "val2": 13.5
                        }
                        """
                ),
                Arguments.arguments(
                        12.25,
                        13.5,
                        25.75,
                        """
                        {
                            "val1": 12.25,
                            "val2": 13.5
                        }
                        """
                ),
                Arguments.arguments(
                        -12.0,
                        13.5,
                        1.5,
                        """
                        {
                            "val1": -12.0,
                            "val2": 13.5
                        }
                        """
                ),
                Arguments.arguments(
                        -12.25,
                        -13.5,
                        -25.75,
                        """
                        {
                            "val1": -12.25,
                            "val2": -13.5
                        }
                        """
                ),
                Arguments.arguments(
                        0.0,
                        0.0,
                        0.0,
                        """
                        {
                            "val1": 0.0,
                            "val2": 0.0
                        }
                        """
                )
        );
    }

    private static Stream<Arguments> sumArgumentsWithInvalidType() {
        return Stream.of(
                Arguments.arguments(
                        """
                        {
                            "val1": "12.0",
                            "val2": 6.0
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": 12.0,
                            "val2": "6.0"
                        }
                        """,
                        "Field 'val2' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": true,
                            "val2": 6.0
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": 12.0,
                            "val2": false
                        }
                        """,
                        "Field 'val2' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": null,
                            "val2": 6.0
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": 12.0,
                            "val2": null
                        }
                        """,
                        "Field 'val2' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": "12.0",
                            "val2": "6.0"
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": true,
                            "val2": false
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val2": 6.0
                        }
                        """,
                        "Field 'val1' has invalid type."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": 6.0
                        }
                        """,
                        "Field 'val2' has invalid type."
                )
        );
    }

    private static Stream<Arguments> sumArgumentsWithInvalidJson() {
        return Stream.of(
                Arguments.arguments(
                        """
                        {
                            "val1": 12.0
                            "val2": 6.0
                        }
                        """,
                        "Request body contains invalid JSON."
                ),
                Arguments.arguments(
                        """
                        {
                            "val1": 12.0,
                            "val2": 6.0
                        """,
                        "Request body contains invalid JSON."
                )
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

    private static Stream<Arguments> divideArgumentsWithMissingParam() {
        return Stream.of(
                Arguments.arguments(
                        MultiValueMap.fromSingleValue(
                                Map.ofEntries(
                                        entry("val1", "2")
                                )
                        ),
                        "Validation failed for field: 'val2' message: Failed to convert value of type 'null' to required type 'double'; Failed to convert from type [null] to type [double] for value [null]"
                ),
                Arguments.arguments(
                        MultiValueMap.fromSingleValue(
                                Map.ofEntries(
                                        entry("val2", "2")
                                )
                        ),
                        "Validation failed for field: 'val1' message: Failed to convert value of type 'null' to required type 'double'; Failed to convert from type [null] to type [double] for value [null]"
                ),
                Arguments.arguments(
                        MultiValueMap.fromSingleValue(Map.of()),
                        "Validation failed for field: 'val1' message: Failed to convert value of type 'null' to required type 'double'; Failed to convert from type [null] to type [double] for value [null]"
                )
        );
    }

    private static Stream<Arguments> divideArgumentsBothWithInvalidType() {
        return Stream.of(
                Arguments.arguments(
                        "abc",
                        "abc",
                        List.of(
                                "Validation failed for field: 'val1' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"abc\"",
                                "Validation failed for field: 'val2' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"abc\""
                        )
                ),
                Arguments.arguments(
                        "null",
                        "null",
                        List.of(
                                "Validation failed for field: 'val1' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"null\"",
                                "Validation failed for field: 'val2' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"null\""
                        )
                ),
                Arguments.arguments(
                        "",
                        "",
                        List.of(
                                "Validation failed for field: 'val1' message: Failed to convert value of type 'java.lang.String' to required type 'double'; empty String",
                                "Validation failed for field: 'val2' message: Failed to convert value of type 'java.lang.String' to required type 'double'; empty String"
                        )
                )
        );
    }

    private static Stream<Arguments> divideArgumentsWithInvalidType() {
        return Stream.of(
                Arguments.arguments(
                        "abc",
                        "1",
                        "Validation failed for field: 'val1' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"abc\""
                ),
                Arguments.arguments(
                        "1",
                        "abc",
                        "Validation failed for field: 'val2' message: Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"abc\""
                ),
                Arguments.arguments(
                        "",
                        "1",
                        "Validation failed for field: 'val1' message: Failed to convert value of type 'java.lang.String' to required type 'double'; empty String"
                ),
                Arguments.arguments(
                        "1",
                        "",
                        "Validation failed for field: 'val2' message: Failed to convert value of type 'java.lang.String' to required type 'double'; empty String"
                )
        );
    }
}

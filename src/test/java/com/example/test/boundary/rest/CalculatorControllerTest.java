package com.example.test.boundary.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CalculatorControllerTest {
    private final WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    public CalculatorControllerTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void addShouldReturnCorrectValue() throws Exception{
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "val1": 12.0,
                            "val2": 6.0
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(18.0));

    }
}

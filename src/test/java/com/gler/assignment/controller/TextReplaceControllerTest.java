package com.gler.assignment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TextReplaceController.class)
class TextReplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Task 1: Length < 2 should return 400 Bad Request")
    void whenLengthIsLessThanTwo_thenReturns400() throws Exception {
        // Case: length 1
        mockMvc.perform(get("/replace").param("text", "a"))
                .andExpect(status().isBadRequest());

        // Case: empty string
        mockMvc.perform(get("/replace").param("text", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Task 1: Length == 2 should return 200 OK with empty body")
    void whenLengthIsTwo_thenReturns200WithEmptyBody() throws Exception {
        mockMvc.perform(get("/replace").param("text", "ab"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Task 1: Length > 2 should replace first and last characters with * and $")
    void whenLengthIsGreaterThanTwo_thenTransformsCorrectly() throws Exception {

        // elephant => *lephan$
        mockMvc.perform(get("/replace").param("text", "elephant"))
                .andExpect(status().isOk())
                .andExpect(content().string("*lephan$"));

        // home => *om$
        mockMvc.perform(get("/replace").param("text", "home"))
                .andExpect(status().isOk())
                .andExpect(content().string("*om$"));

        // abc#20xyz => *bc#20xy$ : URL encoded string (consumer has the responsability to encode the text param)
        mockMvc.perform(get("/replace").param("text", "abc#20xyz"))
                .andExpect(status().isOk())
                .andExpect(content().string("*bc#20xy$"));

        // abc => *b$
        mockMvc.perform(get("/replace").param("text", "abc"))
                .andExpect(status().isOk())
                .andExpect(content().string("*b$"));

        // TetingCodeAssignmentProject -> *estingCodeAssignmentProjec$
        mockMvc.perform(get("/replace").param("text", "TestingCodeAssignmentProject"))
                .andExpect(status().isOk())
                .andExpect(content().string("*estingCodeAssignmentProjec$"));

        // My Test Project => *y Test Projec$
        mockMvc.perform(get("/replace").param("text", "My Test Project"))
                .andExpect(status().isOk())
                .andExpect(content().string("*y Test Projec$"));
    }

}

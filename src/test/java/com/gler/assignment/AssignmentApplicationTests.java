package com.gler.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gler.assignment.dto.request.ForecastRequestDTO;
import com.gler.assignment.repository.ForecastRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ForecastRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {

	}

	// Integration test to cover the full flow
	@Test
	void testFullForecastFlow() throws Exception {
		ForecastRequestDTO request = new ForecastRequestDTO(true, true, true);

		mockMvc.perform(post("/forecast")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());

		assertTrue(repository.count() > 0);
	}

}

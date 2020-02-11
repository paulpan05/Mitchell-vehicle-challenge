package com.mitchell.challenge.vehicle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class VehicleApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(1)
	public void expectEmptyArray() throws Exception {
		mockMvc.perform(get("/vehicles"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[]")));
	}

	@Test
	@Order(2)
	public void expectTwoVehiclesInserted() throws Exception {
	    Vehicle vehicle_1 = new Vehicle(1, 2012, "Tesla", "S");
	    Vehicle vehicle_2 = new Vehicle(2, 2015, "Tesla", "X");

	    mockMvc.perform(
	    		post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mockMvc.perform(get("/vehicles"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						containsString(
								"[{\"id\":1,\"year\":2012,\"make\":\"Tesla\",\"model\":\"S\"}," +
										"{\"id\":2,\"year\":2015,\"make\":\"Tesla\",\"model\":\"X\"}]")
				));
	}

	@Test
	@Order(3)
	public void expectInsertionConflict() throws Exception {
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Tesla", "S");
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(status().reason("ID of vehicle already exists in database"));
	}

	@Test
	@Order(4)
	public void expectInsertionYearOutOfRange() throws Exception {
		Vehicle vehicle_1 = new Vehicle(3, 1900, "Tesla", "S");
		Vehicle vehicle_2 = new Vehicle(3, 2100, "Tesla", "S");
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Vehicle year must be between 1950 and 2050"));
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Vehicle year must be between 1950 and 2050"));
	}

}

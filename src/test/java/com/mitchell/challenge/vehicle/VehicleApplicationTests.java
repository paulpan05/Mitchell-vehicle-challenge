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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests class for the back-end service, including endpoints testing and error cases testing
 */
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class VehicleApplicationTests {

	// A mock of the mvc to do pick request to the back-end being tested
	@Autowired
	private MockMvc mockMvc;

	// Injected object used to convert object to JSON strings
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Tries to perform get request on empty database, expects empty array
	 *
	 * @throws Exception if the get request fails to perform, or the expected value differ from the actual
	 */
	@Test
	@Order(1)
	public void expectEmptyArray() throws Exception {
		mockMvc.perform(get("/vehicles"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[]")));
	}

	/**
	 * Tries to perform two post requests followed by one get request on empty database, expects array of two objects
	 *
	 * @throws Exception if the get/post requests fails to perform, or the expected values differ from the actual
	 */
	@Test
	@Order(2)
	public void expectTwoVehiclesInserted() throws Exception {
	    Vehicle vehicle_1 = new Vehicle(1, 2012, "Tesla", "S");
	    Vehicle vehicle_2 = new Vehicle(2, 2015, "Tesla", "X");

	    // Insert the first vehicle
	    mockMvc.perform(
	    		post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	    // Insert the second vehicle
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Gets two vehicles from database
		mockMvc.perform(get("/vehicles"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						containsString(
								"[{\"id\":1,\"year\":2012,\"make\":\"Tesla\",\"model\":\"S\"}," +
										"{\"id\":2,\"year\":2015,\"make\":\"Tesla\",\"model\":\"X\"}]")
				));
	}

	/**
	 * Tries to perform conflicting post by inserting already existing id, expects conflict error to throw
	 *
	 * @throws Exception if the post request fails to perform, or if the type of response is not conflict
	 */
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

	/**
	 * Tries to insert out of range year, should expect error of invalid request body
	 *
	 * @throws Exception If the actual error type is not expected, or post request fails
	 */
	@Test
	@Order(4)
	public void expectInsertionYearOutOfRange() throws Exception {
		Vehicle vehicle_1 = new Vehicle(3, 1900, "Tesla", "S");
		Vehicle vehicle_2 = new Vehicle(3, 2100, "Tesla", "S");

		// Vehicle with year too small
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Vehicle year must be between 1950 and 2050"));

		// Vehicle with year to big
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Vehicle year must be between 1950 and 2050"));
	}

	/**
	 * Tries to insert invalid constructed vehicle, should return bad request with invalid body
	 *
	 * @throws Exception If the actual error type is not expected, or post request fails
	 */
	@Test
	@Order(5)
	public void expectBadRequestBodyInsert() throws Exception {
		Vehicle vehicle_1 = new Vehicle(null, 1980, "Tesla", "S");
		Vehicle vehicle_2 = new Vehicle(3, null, "Tesla", "S");
		Vehicle vehicle_3 = new Vehicle(3, 1980, null, "S");
		Vehicle vehicle_4 = new Vehicle(3, 1980, "Tesla", null);
		String errorStr = "Request body invalid, must be in the form" +
				"{id: int, year: int, make: string, model: string}";

		// Tries inserting vehicle without id
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(errorStr));

		// Tries inserting vehicle without year
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(errorStr));

		// Tries inserting vehicle without make
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_3))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(errorStr));

		// Tries inserting vehicle without model
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_4))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(errorStr));
	}

	/**
	 * Tries to get vehicle with id not yet in database, should return not found error
	 *
	 * @throws Exception If the actual error type is not expected, or get request fails
	 */
	@Test
	@Order(6)
	public void expectIdNotFoundGet() throws Exception {
		mockMvc.perform(get("/vehicles/4"))
				.andExpect(status().isNotFound())
				.andExpect(status().reason("Cannot get non-existent vehicle"));
	}

	/**
	 * Tries to delete vehicle with id not yet in database, should return not found error
	 *
	 * @throws Exception If the actual error type is not expected, or delete request fails
	 */
	@Test
	@Order(7)
	public void expectIdNotFoundDelete() throws Exception {
		mockMvc.perform(delete("/vehicles/4"))
				.andExpect(status().isNotFound())
				.andExpect(status().reason("Cannot delete non-existent vehicle"));
	}

	/**
	 * Tries to get vehicle with id in database, should succeed with expected value
	 *
	 * @throws Exception If the status is not ok from get, response is not expected, or get request fails
	 */
	@Test
	@Order(8)
	public void expectIdFoundGet() throws Exception {
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Tesla", "S");
		mockMvc.perform(get("/vehicles/1"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicle_1))));
	}

	/**
	 * Tries to delete vehicle with id in database, should succeed with ok status
	 *
	 * @throws Exception If the status is not ok from delete
	 */
	@Test
	@Order(9)
	public void expectIdFoundDelete() throws Exception {
		mockMvc.perform(delete("/vehicles/1"))
				.andExpect(status().isOk());
	}

	/**
	 * Tries to update vehicle with invalid year in database via put request
	 *
	 * @throws Exception If the put request has status code other than bad request, or reason is invalid
	 */
	@Test
	@Order(10)
	public void expectUpdateVehicleYearInvalid() throws Exception {
		Vehicle updatedVehicle = new Vehicle(2, 2122, null, null);
		mockMvc.perform(
				put("/vehicles")
						.content(objectMapper.writeValueAsString(updatedVehicle))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Vehicle year must be between 1950 and 2050"));
	}

	/**
	 * Tries to update vehicle with invalid id in database via put request
	 *
	 * @throws Exception If the put request has status code other than bad request, or reason is invalid
	 */
	@Test
	@Order(11)
	public void expectUpdateVehicleIdInvalid() throws Exception {
		Vehicle updatedVehicle = new Vehicle(5, 2012, null, null);
		mockMvc.perform(
				put("/vehicles")
						.content(objectMapper.writeValueAsString(updatedVehicle))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(status().reason("ID of vehicle does not exist in the database"));
	}

	/**
	 * Tries to update vehicle with no id in database via put request
	 *
	 * @throws Exception If the put request has status code other than bad request, or reason is invalid
	 */
	@Test
	@Order(12)
	public void expectUpdateVehicleNoId() throws Exception {
		Vehicle updatedVehicle = new Vehicle(null, 2012, "Tesla", "S");
		mockMvc.perform(
				put("/vehicles")
						.content(objectMapper.writeValueAsString(updatedVehicle))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Cannot change vehicle properties without ID"));
	}

	/**
	 * Tries to update vehicle with existing id in database
	 *
	 * @throws Exception If the update fails with status code not ok
	 */
	@Test
	@Order(13)
	public void expectUpdateVehicleSuccess() throws Exception {
		Vehicle updatedVehicle = new Vehicle(2, 2012, "Tesla", "S");
		mockMvc.perform(
				put("/vehicles")
						.content(objectMapper.writeValueAsString(updatedVehicle))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	/**
	 * Tries to get vehicles in database by filtering year
	 *
	 * @throws Exception If the retrieval fails for get request, or post requests fail for insertions
	 */
	@Test
	@Order(14)
	public void expectGetVehiclesByYear() throws Exception {
		Vehicle originalVehicle = new Vehicle(2, 2012, "Tesla", "S");
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Toyota", "S");
		Vehicle vehicle_2 = new Vehicle(3, 2015, "Toyota", "S");

		// Insert the first new vehicle
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_1))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		// Insert the second new vehicle
		mockMvc.perform(
				post("/vehicles")
						.content(objectMapper.writeValueAsString(vehicle_2))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		// Performs get for vehicle of year 2012, should return JSON string with two vehicles
		mockMvc.perform(get("/vehicles?year=2012"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[" +
						objectMapper.writeValueAsString(vehicle_1) + "," +
						objectMapper.writeValueAsString(originalVehicle) + "]")));
	}

	/**
	 * Tries to get vehicles in database by filtering make
	 *
	 * @throws Exception If the retrieval fails for get request, or post requests fail for insertions
	 */
	@Test
	@Order(15)
	public void expectGetVehicleByMake() throws Exception {
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Toyota", "S");
		Vehicle vehicle_2 = new Vehicle(3, 2015, "Toyota", "S");

		// Get vehicles that have Toyota as make, should return JSON string with two vehicle objects
		mockMvc.perform(get("/vehicles?make=Toyota"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[" +
						objectMapper.writeValueAsString(vehicle_1) + "," +
						objectMapper.writeValueAsString(vehicle_2) + "]")));
	}

	/**
	 * Tries to get vehicles in database by filtering model
	 *
	 * @throws Exception If the retrieval fails for get request, or post requests fail for insertions
	 */
	@Test
	@Order(16)
	public void expectGetVehicleByModel() throws Exception {
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Toyota", "S");
		Vehicle vehicle_2 = new Vehicle(2, 2012, "Tesla", "S");
		Vehicle vehicle_3 = new Vehicle(3, 2015, "Toyota", "S");

		// Get vehicles that have S as model, should return JSON string with three vehicle objects
		mockMvc.perform(get("/vehicles?model=S"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[" +
						objectMapper.writeValueAsString(vehicle_1) + "," +
						objectMapper.writeValueAsString(vehicle_2) + "," +
						objectMapper.writeValueAsString(vehicle_3) + "]")));
	}

	/**
	 * Tries to get vehicles in database by filtering year, make, and model
	 *
	 * @throws Exception If the retrieval fails for get request, or post requests fail for insertions
	 */
	@Test
	@Order(17)
	public void expectGetVehicleByYearMakeModel() throws Exception {
		Vehicle vehicle_1 = new Vehicle(1, 2012, "Toyota", "S");
		Vehicle vehicle_2 = new Vehicle(2, 2012, "Tesla", "S");
		Vehicle vehicle_3 = new Vehicle(3, 2015, "Toyota", "S");

		// Get vehicles that have 2012 as year or Tesla as make or S as model,
		// should return JSON string with three vehicle objects
		mockMvc.perform(get("/vehicles?year=2012&make=Tesla&model=S"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("[" +
						objectMapper.writeValueAsString(vehicle_1) + "," +
						objectMapper.writeValueAsString(vehicle_2) + "," +
						objectMapper.writeValueAsString(vehicle_3) + "]")));
	}

}

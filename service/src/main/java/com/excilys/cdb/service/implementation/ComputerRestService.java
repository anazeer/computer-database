package com.excilys.cdb.service.implementation;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dto.implementation.ComputerDto;
import com.excilys.cdb.pagination.util.PageRequest;

@Service
public class ComputerRestService {

	private static final Client CLIENT = ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build();
	private static final String URL = "http://localhost:8080/computer-database/computer/";

	/**
	 *
	 * @param id the object reference
	 * @return the object referenced by id
	 */
	public ComputerDto findById(Long id) {
		WebTarget target = CLIENT.target(URL + "get/" + id);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		ComputerDto computer = response.readEntity(ComputerDto.class);
		response.close();
		return computer;
	}

	/**
	 * 
	 * @param query the object containing the query constraints 
	 * @param currentPage the number of the requested page
	 * @return the list containing the result
	 */
	//public List<ComputerDto> getPage(Constraint constraint) {
	public List<ComputerDto> getPage(PageRequest pageRequest) {
		WebTarget target = CLIENT.target(URL + "list");
		Response response = target.request().post(Entity.entity(pageRequest, MediaType.APPLICATION_JSON));
		System.out.println(response);
		List<ComputerDto> computers = response.readEntity(new GenericType<List<ComputerDto>>() {
		});
		response.close();
		return computers;
	}

	/**
	 * Persist the object
	 * @param obj the object to persist
	 * @return the object with the new id
	 */
	public ComputerDto create(ComputerDto computer) {
		WebTarget target = CLIENT.target(URL + "create");
		System.out.println(Entity.entity(computer, MediaType.APPLICATION_JSON));
		Response response = target.request().post(Entity.entity(computer, MediaType.APPLICATION_JSON));
		System.out.println(response);
		ComputerDto comp = response.readEntity(ComputerDto.class);
		response.close();
		return comp;
	}

	/**
	 * Update the object
	 * @param obj the object with an existing id and the new state
	 */
	public ComputerDto update(ComputerDto computer) {
		WebTarget target = CLIENT.target(URL + "edit");
		Response response = target.request().post(Entity.entity(computer, "application/json"));
		ComputerDto comp = response.readEntity(ComputerDto.class);
		response.close();
		return comp;
	}
	
	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 */
	public String delete(Long id) {
		WebTarget target = CLIENT.target(URL + "delete/" + id);
		Response response = target.request().get();
		String result = response.toString();
		response.close();
		return result;
	}
}

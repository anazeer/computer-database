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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dto.implementation.ComputerDto;
import com.excilys.cdb.pagination.util.PageRequest;

@Service
public class ComputerRestService {

	private Logger log = LoggerFactory.getLogger(getClass());
	private static final Client CLIENT = ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build();
	private static final String URL = "http://localhost:8080/computer-database/";

	/**
	 * Get the computer referenced by id
	 * @param id the computer reference
	 * @return the computer referenced by id
	 */
	public ComputerDto findById(Long id) {
		WebTarget target = CLIENT.target(URL + "get/" + id);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		ComputerDto computer = response.readEntity(ComputerDto.class);
		log.info("Get by id {} : {}", id, response);
		response.close();
		return computer;
	}

	/**
	 * Get the computers corresponding to the post request constraints
	 * @param pageRequest the object containing the page constraints
	 * @return the list containing the result
	 */
	public List<ComputerDto> getPage(PageRequest pageRequest) {
		WebTarget target = CLIENT.target(URL + "list");
		Response response = target.request().post(Entity.entity(pageRequest, MediaType.APPLICATION_JSON));
		List<ComputerDto> computers = response.readEntity(new GenericType<List<ComputerDto>>() {
		});
		log.info("Get page : {}", response);
		response.close();
		return computers;
	}

	/**
	 * Persist a computer in the database
	 * @param computer the computer to persist
	 * @return the persisted computer with its new id
	 */
	public ComputerDto create(ComputerDto computer) {
		WebTarget target = CLIENT.target(URL + "create");
		Response response = target.request().post(Entity.entity(computer, MediaType.APPLICATION_JSON));
		ComputerDto comp = response.readEntity(ComputerDto.class);
		log.info("Create computer : {}", response);
		response.close();
		return comp;
	}

	/**
	 * Update the computer
	 * @param computer the computer with an existing id and the new state
	 */
	public ComputerDto update(ComputerDto computer) {
		WebTarget target = CLIENT.target(URL + "edit");
		Response response = target.request().put(Entity.entity(computer, "application/json"));
		ComputerDto comp = response.readEntity(ComputerDto.class);
		log.info("Update computer : {}", response);
		response.close();
		return comp;
	}
	
	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 */
	public String delete(Long id) {
		WebTarget target = CLIENT.target(URL + "delete/" + id);
		Response response = target.request().delete();
		String result = response.toString();
		log.info("Delete computer : {}", response);
		response.close();
		return result;
	}
}
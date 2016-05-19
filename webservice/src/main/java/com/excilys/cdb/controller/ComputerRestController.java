package com.excilys.cdb.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.IDto;
import com.excilys.cdb.dto.implementation.ComputerDto;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.implementation.ComputerService;

@RestController
public class ComputerRestController {

	// Service
	@Autowired
	private ComputerService computerService;

	// Mapper
	@Autowired
	private ComputerMapper computerMapper;

	/**
	 * By default, the index shows the total count of computers
	 * @return the singleton map containing the count of computers
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<?, ?> index() {
		int count = computerService.count(null);
		return Collections.singletonMap("count", count);
	}

	/**
	 * Get the computer referenced by id
	 * @param id the computer reference
	 * @return the computer referenced by id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerDto findById(@PathVariable Long id) {
		Computer computer = computerService.findById(id);
		ComputerDto dto = (ComputerDto) computerMapper.getFromModel(computer);
		System.out.println("ID : " + id + ", " + dto);
		return dto;
	}

	/**
	 * Get the computers corresponding to the post request constraints
	 * @param pageRequest the object containing the page constraints
	 * @return the list containing the result
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IDto> getPage(@RequestBody PageRequest pageRequest) {
		return computerService.getPage(pageRequest).getElements();
	}

	/**
	 * Persist a computer in the database
	 * @param dto the computer to persist
	 * @return the persisted computer with its new id
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerDto create(@RequestBody @Valid ComputerDto dto) {
		Computer computer = computerMapper.getFromDTO(dto);
		computer = computerService.create(computer);
		return (ComputerDto) computerMapper.getFromModel(computer);
	}

	/**
	 * Update the computer
	 * @param dto the computer with an existing id and the new state
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerDto update(@RequestBody @Valid ComputerDto dto) {
		Computer computer = computerMapper.getFromDTO(dto);
		computerService.update(computer);
		return (ComputerDto) computerMapper.getFromModel(computer);
	}

	/**
	 * Delete the computer referenced by id
	 * @param id the computer reference
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable Long id) {
		boolean success = computerService.delete(id);
		String message = success ? "computer deleted" : "computer not found";
		int status = success ? 200 : 400;
		Map<String, Object> result = new HashMap<>();
		result.put("response", message);
		result.put("status", status);
		return result;
	}
}
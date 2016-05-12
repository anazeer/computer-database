package com.excilys.cdb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.implementation.ComputerDto;
import com.excilys.cdb.mapper.implementation.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.pagination.implementation.ComputerPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.implementation.ComputerService;

@RestController
@RequestMapping("/computer/")
public class ComputerRestController {

	// Service
	@Autowired
	private ComputerService computerService;
	
	// Mapper
	@Autowired
	private ComputerMapper computerMapper;

	/**
	 *
	 * @param id the object reference
	 * @return the object referenced by id
	 */
	@RequestMapping(value = "get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerDto findById(@PathVariable Long id) {
		Computer computer = computerService.findById(id);
        ComputerDto dto = (ComputerDto) computerMapper.getFromModel(computer);
        System.out.println("ID : " + id + ", " + dto);
        return dto;
	}

	/**
	 * 
	 * @param query the object containing the query constraints 
	 * @param currentPage the number of the requested page
	 * @return the list containing the result
	 */
	@RequestMapping(value = "get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerPage getPage(@RequestBody PageRequest pageRequest) {
		return computerService.getPage(pageRequest);
	}

	/**
	 * Persist the object
	 * @param obj the object to persist
	 * @return the object with the new id
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ComputerDto create(@RequestBody @Valid ComputerDto dto) {
		Computer computer = computerMapper.getFromDTO(dto);
		computer = computerService.create(computer);
		return (ComputerDto) computerMapper.getFromModel(computer);
	}

	/**
	 * Update the object
	 * @param obj the object with an existing id and the new state
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Computer update(@RequestBody @Valid ComputerDto dto) {
		Computer computer = computerMapper.getFromDTO(dto);
		computerService.update(computer);
		return computer;
	}

	/**
	 * Delete the row referenced by id
	 * @param id the row reference
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

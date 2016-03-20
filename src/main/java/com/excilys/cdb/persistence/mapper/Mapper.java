package com.excilys.cdb.persistence.mapper;

import com.excilys.cdb.service.dto.DTO;

import java.sql.ResultSet;

/**
 * Generic interface for mapping object
 * @param <T>
 */
 public interface Mapper<T> {

    /**
     * Maps a SQL row into an object
     * @param result : the result set containing the row
     * @return the fresh mapped object
     */
	T getFromResultSet(ResultSet result);

    /**
     * Maps a DTO object into its model implementation
     * @param dto the DTO implementation of the model
     * @return the model from the dto
     */
    T getFromDTO(DTO dto);

    /**
     * Maps a model object into a DTO
     * @param model the object model
     * @return the DTO from the model
     */
    DTO getFromModel(T model);

}
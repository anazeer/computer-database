package com.excilys.cdb.mapper;

import java.sql.ResultSet;

import com.excilys.cdb.dto.IDTO;

/**
 * Generic interface for mapping object
 * @param <T>
 */
 public interface IMapper<T> {

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
    T getFromDTO(IDTO dto);

    /**
     * Maps a model object into a DTO
     * @param model the object model
     * @return the DTO from the model
     */
    IDTO getFromModel(T model);

}
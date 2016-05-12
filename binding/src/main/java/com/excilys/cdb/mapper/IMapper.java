package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.IDto;

/**
 * Generic interface for mapping object
 * @param <T>
 */
 public interface IMapper<T> {

    /**
     * Maps a DTO object into its model implementation
     * @param dto the DTO implementation of the model
     * @return the model from the dto
     */
    T getFromDTO(IDto dto);

    /**
     * Maps a model object into a DTO
     * @param model the object model
     * @return the DTO from the model
     */
    IDto getFromModel(T model);

}
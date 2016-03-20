package com.excilys.cdb.persistence.mapper;

/**
 * Mapper factory to centralize mappers instantiation
 */
public class MapperFactory {

    /**
     *
     * @return an instance of company mapper
     */
    public static CompanyMapper getCompanyMapper() {
        return CompanyMapper.getInstance();
    }

    /**
     *
     * @return an instance of computer mapper
     */
    public static ComputerMapper getComputerMapper() {
        return ComputerMapper.getInstance();
    }
}
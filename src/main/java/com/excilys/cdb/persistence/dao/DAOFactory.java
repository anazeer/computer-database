package com.excilys.cdb.persistence.dao;

/**
 * DAO factory to centralize DAOs instantiation
 */
public class DAOFactory {

    /**
     *
     * @return an instance of company DAO
     */
    public static CompanyDAO getCompanyDAO() {
        return CompanyDAO.getInstance();
    }

    /**
     *
     * @return an instance of computer DAO
     */
    public static ComputerDAO getComputerDAO() {
        return ComputerDAO.getInstance();
    }
}
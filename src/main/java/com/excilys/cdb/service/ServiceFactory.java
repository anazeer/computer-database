package com.excilys.cdb.service;

/**
 * Service factory to centralize services instantiation
 */
public class ServiceFactory {

    /**
     *
     * @return computer service instance
     */
    public static ComputerService getComputerService() {
        return ComputerService.getInstance();
    }

    /**
     *
     * @return company service instance
     */
    public static CompanyService getCompanyService() {
        return CompanyService.getInstance();
    }
}
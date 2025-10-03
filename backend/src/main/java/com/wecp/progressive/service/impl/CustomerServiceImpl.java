package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// import com.wecp.progressive.dao.CustomerDAO;
import com.wecp.progressive.dao.CustomerDAOImpl;
import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDAOImpl customerDAO;
    private static List<Customers> customersList = new ArrayList<>();

    public CustomerServiceImpl(CustomerDAOImpl customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        customersList = customerDAO.getAllCustomers();
        return customersList;
    }

    @Override
    public int addCustomer(Customers customers) throws SQLException {
        return customerDAO.addCustomer(customers);
    }

    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        List<Customers> sortedCustomers = getAllCustomers();
        Collections.sort(sortedCustomers);
        return sortedCustomers;
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException {
        customerDAO.updateCustomer(customers);
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        customerDAO.deleteCustomer(customerId);
    }

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        return customerDAO.getCustomerById(customerId);
    }
}
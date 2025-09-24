package com.wecp.progressive.service.impl;

import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.repository.CustomerRepository;
import com.wecp.progressive.service.CustomerService;

@Service
public class CustomerServiceImplJpa implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImplJpa(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customers> getAllCustomers() throws SQLException {
        return customerRepository.findAll();
    }

    @Override
    public int addCustomer(Customers customers) throws SQLException {
        return customerRepository.save(customers).getCustomerId();
    }

    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        List<Customers> sortedCustomers = getAllCustomers();
        Collections.sort(sortedCustomers);
        return sortedCustomers;
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException {
        customerRepository.save(customers);
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        return customerRepository.findById(customerId).get();
    }
    
}
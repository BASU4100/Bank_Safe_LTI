package com.wecp.progressive.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.exception.AccountNotFoundException;
import com.wecp.progressive.exception.CustomerAlreadyExistsException;
import com.wecp.progressive.repository.AccountRepository;
import com.wecp.progressive.repository.CustomerRepository;
import com.wecp.progressive.repository.TransactionRepository;
import com.wecp.progressive.service.CustomerService;

@Service
public class CustomerServiceImplJpa implements CustomerService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

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
        if (customers.getRole()==null) {
            throw new CustomerAlreadyExistsException("Role cannot be NULL");
        }
        if (customerRepository.findByEmail(customers.getEmail())==null) {
            return customerRepository.save(customers).getCustomerId();
        }
        throw new CustomerAlreadyExistsException("Customer already exists with the email = "+customers.getEmail());
    }

    @Override
    public List<Customers> getAllCustomersSortedByName() throws SQLException {
        List<Customers> sortedCustomers = getAllCustomers();
        Collections.sort(sortedCustomers);
        return sortedCustomers;
    }

    @Override
    public void updateCustomer(Customers customers) throws SQLException {
        if (customers.getRole().isEmpty() || customers.getRole()==null) {
            throw new CustomerAlreadyExistsException("Role cannot be empty");
        }
        Customers fetchedCustomers = customerRepository.findByEmail(customers.getEmail());
        if (fetchedCustomers!=null && fetchedCustomers.getCustomerId()!=customers.getCustomerId()) {
            throw new CustomerAlreadyExistsException("This customers email is already associated with some other customer");
        }
        customerRepository.save(customers);
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        transactionRepository.deleteByCustomerId(customerId);
        accountRepository.deleteByCustomerId(customerId);
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customers getCustomerById(int customerId) throws SQLException {
        return customerRepository.findById(customerId).orElse(null);
    }
}
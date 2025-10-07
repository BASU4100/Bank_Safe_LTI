package com.wecp.progressive.service;


import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.exception.AccountNotFoundException;
import com.wecp.progressive.exception.CustomerAlreadyExistsException;
import com.wecp.progressive.repository.CustomerRepository;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerLoginService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerLoginService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Customers> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customers> getCustomerById(Integer customer) {
        return customerRepository.findById(customer);
    }

    public Customers getCustomerByName(String name) {
        return customerRepository.findByUsername(name);
    }

    public Customers createCustomer(Customers customer) {
        if (getCustomerByName(customer.getUsername())!=null) {
            throw new CustomerAlreadyExistsException("Username not available");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public Customers updateCustomer(Customers customer) {
        Customers fetchedCustomers = customerRepository.findByEmail(customer.getEmail());
        if (fetchedCustomers!=null && fetchedCustomers.getCustomerId()!=customer.getCustomerId()) {
            throw new CustomerAlreadyExistsException("This customers email is already associated with some other customer");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public void deleteUser(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customers customer = customerRepository.findByUsername(username);
        if (customer==null) {
            throw new AccountNotFoundException("Account not found");
        }
        return new User(customer.getUsername(), customer.getPassword(), AuthorityUtils.createAuthorityList(customer.getRole()));
    }
}
package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;

/**
 * Service class for interacting with Customer model, performing CRUD tasks with
 * database.
 *
 * @author Kai Presler-Marshall
 * @param <T>
 *            Type of user
 *
 */
@Component
@Transactional
public class CustomerService <T extends User> extends UserService<Customer> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private CustomerRepository<Customer> repository;

    @Override
    protected JpaRepository<Customer, String> getRepository () {
        return repository;
    }

}
package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * Service class for interacting with Personnel model, performing CRUD tasks
 * with database.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class StaffService extends UserService<Staff> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private StaffRepository<Staff> repository;

    @Override
    protected JpaRepository<Staff, String> getRepository () {
        return repository;
    }

}

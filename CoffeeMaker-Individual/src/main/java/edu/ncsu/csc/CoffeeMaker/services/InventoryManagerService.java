package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.InventoryManager;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryManagerRepository;

/**
 * Service class for interacting with Personnel model, performing CRUD tasks
 * with database.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class InventoryManagerService extends UserService<InventoryManager> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private InventoryManagerRepository<InventoryManager> repository;

    @Override
    protected JpaRepository<InventoryManager, String> getRepository () {
        return repository;
    }

}

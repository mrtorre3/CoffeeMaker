package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.DrinkOrderRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author mrtorre3
 *
 */
@Component
@Transactional
public class DrinkOrderService extends Service<DrinkOrder, Long> {

    /**
     * InventoryRepository, to be autowired in by Spring and provide CRUD
     * operations on Inventory model.
     */
    @Autowired
    private DrinkOrderRepository drinkOrderRepository;

    @Override
    protected JpaRepository<DrinkOrder, Long> getRepository () {
        return drinkOrderRepository;
    }

    /**
     * Retrieves the singleton Inventory instance from the database, creating it
     * if it does not exist.
     *
     * @return the Inventory, either new or fetched
     */
    public List<DrinkOrder> getOrders () {
        final List<DrinkOrder> orderList = findAll();
        return orderList;
    }

    /**
     * Find a recipe with the provided name
     *
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public DrinkOrder findBycustomerName ( final String customerName ) {
        return drinkOrderRepository.findBycustomerName( customerName );
    }

}

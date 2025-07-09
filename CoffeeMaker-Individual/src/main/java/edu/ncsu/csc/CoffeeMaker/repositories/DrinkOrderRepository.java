package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;

/**
 * InventoryRepository is used to provide CRUD operations for the Inventory
 * model. Spring will generate appropriate code with JPA.
 *
 * @author mrtorre3
 *
 */
public interface DrinkOrderRepository extends JpaRepository<DrinkOrder, Long> {

    DrinkOrder findBycustomerName ( String customerName );

}

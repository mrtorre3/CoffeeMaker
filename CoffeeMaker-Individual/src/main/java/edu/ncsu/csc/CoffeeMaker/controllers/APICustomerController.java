package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.services.DrinkOrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author mrtorre3
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private DrinkOrderService drinkOrderService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the DrinkOrder to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/order/{name}" )
    public List<DrinkOrder> getOrder ( @PathVariable final String name ) {
        final List<DrinkOrder> sendBack = drinkOrderService.getOrders();
        for ( final DrinkOrder item : sendBack ) {
            if ( !item.getCustomerName().equals( name ) ) {
                sendBack.remove( item );
            }
        }
        return sendBack;
    }

}

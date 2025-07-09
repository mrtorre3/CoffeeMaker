package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.services.DrinkOrderService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

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
public class APIDrinkOrderController extends APIController {

    /**
     * DrinkOrderService object, to be autowired in by Spring to allow for
     * manipulating the DrinkOrder model
     */
    @Autowired
    private DrinkOrderService drinkOrderService;

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService       userService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the DrinkOrder to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/order" )
    public List<DrinkOrder> getOrder () {
        return drinkOrderService.getOrders();
    }

    @PutMapping ( BASE_PATH + "/order" )
    public ResponseEntity updateOrder ( @RequestBody final String json ) {

        final JsonParser parse = new BasicJsonParser();

        final Map drinkOrderBit = parse.parseMap( json );

        DrinkOrder DO;

        try {

            final String username = (String) drinkOrderBit.get( "customerName" );

            final String DORecipeName = (String) drinkOrderBit.get( "recipeName" );

            final int amtPaid = Integer.parseInt( drinkOrderBit.get( "amountPaid" ).toString() );

            final int id = Integer.parseInt( drinkOrderBit.get( "id" ).toString() );

            DO = new DrinkOrder();

            DO.setAmountPaid( amtPaid );
            DO.setCustomerName( username );
            DO.setRecipeName( DORecipeName );
            DO.setId( (long) id );

            DO.markFilled( true );

            final Customer c = (Customer) userService.findByName( username );

            c.updateDrinkOrder( DO );

            userService.save( c );

        }
        catch ( final IllegalArgumentException e ) {
            throw new IllegalArgumentException( "Invalid input. Enter a positive number" );
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid inventory json/implementation has failed." );
        }

        return new ResponseEntity(
                successResponse( DO.getRecipeName() + " for " + DO.getCustomerName() + " successfully fulfilled!" ),
                HttpStatus.OK );
    }

    @DeleteMapping ( BASE_PATH + "/order/{id}" )
    public ResponseEntity pickupOrder ( @PathVariable final int id ) {
        DrinkOrder DO = null;
        Customer u;
        final List<DrinkOrder> masterOrders = drinkOrderService.getOrders();
        for ( int i = 0; i < masterOrders.size(); i++ ) {
            if ( Integer.parseInt( masterOrders.get( i ).getId().toString() ) == id ) {
                DO = masterOrders.get( i );
                u = (Customer) userService.findByName( DO.getCustomerName() );
                u.pickupDrinkOrder( DO );
                userService.save( u );
                drinkOrderService.delete( DO );
                break;
            }
        }
        if ( DO == null ) {
            return new ResponseEntity( errorResponse( "error picking up order" ), HttpStatus.CONFLICT );
        }

        return new ResponseEntity( successResponse( "Successfully picked up " + DO.getRecipeName() + "!" ),
                HttpStatus.OK );

    }
}

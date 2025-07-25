package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.DrinkOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 *
 * The APICoffeeController is responsible for making coffee when a user submits
 * a request to do so.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICoffeeController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  inventoryService;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService     recipeService;

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService       userService;

    /**
     * DrinkOrderService object, to be autowired in by Spring to allow for
     * manipulating the DrinkOrder model
     */
    @Autowired
    private DrinkOrderService drinkOrderService;

    /**
     * REST API method to make coffee by completing a POST request with the ID
     * of the recipe as the path variable and the amount that has been paid as
     * the body of the response
     *
     * @param name
     *            recipe name
     * @param amtPaid
     *            amount paid
     * @return The change the customer is due if successful
     */
    @PostMapping ( BASE_PATH + "/makecoffee/{name}" )
    public ResponseEntity makeCoffee ( @PathVariable ( "name" ) final String name,
            @RequestBody final String orderRaw ) {
        final Recipe recipe = recipeService.findByName( name );
        if ( recipe == null ) {
            return new ResponseEntity( errorResponse( "No recipe selected" ), HttpStatus.NOT_FOUND );
        }

        final JsonParser parse = new BasicJsonParser();

        final Map drinkOrderBits = parse.parseMap( orderRaw );

        DrinkOrder DO = null;

        int amtPaid = 0;

        int change;

        try {

            final String username = (String) drinkOrderBits.get( "customerName" );

            if ( null == userService.findByName( username ) ) {
                return new ResponseEntity( errorResponse( "User with the id " + username + " doesn't exist" ),
                        HttpStatus.CONFLICT );
            }

            final String DORecipeName = (String) drinkOrderBits.get( "recipeName" );

            amtPaid = Integer.parseInt( drinkOrderBits.get( "amountPaid" ).toString() );

            change = makeCoffee( recipe, amtPaid );
            if ( change == amtPaid ) {
                if ( amtPaid < recipe.getPrice() ) {
                    return new ResponseEntity( errorResponse( "Not enough money paid" ), HttpStatus.CONFLICT );
                }

                else {
                    return new ResponseEntity( errorResponse( "Not enough inventory" ), HttpStatus.CONFLICT );
                }
            }

            DO = new DrinkOrder( username, DORecipeName, amtPaid );

            // only customers have access to this page.
            final Customer c = (Customer) userService.findByName( username );

            c.addDrinkOrder( DO );

            userService.save( c );

        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid json" );
        }
        return new ResponseEntity<String>( successResponse( String.valueOf( change ) ), HttpStatus.OK );

    }

    /**
     * Helper method to make coffee
     *
     * @param toPurchase
     *            recipe that we want to make
     * @param amtPaid
     *            money that the user has given the machine
     * @return change if there was enough money to make the coffee, throws
     *         exceptions if not
     */
    public int makeCoffee ( final Recipe toPurchase, final int amtPaid ) {

        // start with the amount entered
        int change = amtPaid;
        final Inventory inventory = inventoryService.getInventory();

        // if the recipe is null or otherwise does not exist
        if ( toPurchase == null ) {
            throw new IllegalArgumentException( "Recipe not found" );
        }

        // check if the user has enough money
        else if ( toPurchase.getPrice() <= amtPaid ) {

            // check if there are enough ingredients in inventory
            if ( inventory.useIngredients( toPurchase ) ) {

                // save the contents of the inventory after using ingredients
                inventoryService.save( inventory );

                // subtract cost of beverage from amount paid
                change = amtPaid - toPurchase.getPrice();
            }
        }

        // return the change whether or not the recipe was made
        return change;
    }
}

package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIInventoryController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService service;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = service.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity updateInventory ( @RequestBody final String json ) {

        final JsonParser parse = new BasicJsonParser();

        final Map inventoryBits = parse.parseMap( json );

        final Inventory inventory = new Inventory();

        final Inventory inventoryCurrent = service.getInventory();

        try {

            final ArrayList<Map<String, Integer>> ingredientList = (ArrayList<Map<String, Integer>>) inventoryBits
                    .get( "customIngredients" );

            final List<Ingredient> iList = new ArrayList<Ingredient>();

            for ( int i = 0; i < ingredientList.size(); i++ ) {

                final Map ingredientBits = ingredientList.get( i );

                final String ingredientName = (String) ingredientBits.get( "name" );

                final int ingredientAmount = Integer.parseInt( ingredientBits.get( "amount" ).toString() );

                final Ingredient I = new Ingredient();

                I.setName(
                        ingredientName.substring( 0, 1 ).toUpperCase() + ingredientName.substring( 1 ).toLowerCase() );
                I.setAmount( ingredientAmount );

                iList.add( I );
            }

            for ( int i = 0; i < iList.size(); i++ ) {
                inventory.addIngredient( iList.get( i ) );
            }

        }
        catch ( final IllegalArgumentException e ) {
            throw new IllegalArgumentException( "Invalid input. Enter a positive number" );
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid inventory json/implementation has failed." );
        }

        for ( final Ingredient curr : inventory.getIngredients() ) {
            boolean found = false;
            for ( final Ingredient ingredient : inventoryCurrent.getIngredients() ) {
                if ( ingredient.getName().equals( curr.getName() ) && curr.getAmount() >= 0 ) {
                    ingredient.setAmount( curr.getAmount() + ingredient.getAmount() );
                    found = true;
                }
            }
            if ( !found && curr.getAmount() >= 0 ) {
                inventoryCurrent.addIngredient( curr );
            }
        }
        service.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }

    /**
     * THIS IS THE SOLE METHOD OF ADDING NEW INGREDIENTS TO THE INVENTORY.
     * INGREDIENT SERVICE IS PRESENT ONLY TO MANAGE DUPLICATES.
     *
     * REST API endpoint to provide add ingredient access to CoffeeMaker's
     * singleton Inventory. This will update the Inventory of the CoffeeMaker by
     * adding the new ingredient provided to coffeeMaker's inventory.
     *
     * @param ingredient
     *            ingredient to add to inventory
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity addIngredientToInventory ( @RequestBody final Ingredient ingredient ) {
        final Inventory inventoryCurrent = service.getInventory();

        ingredient.setName( ingredient.getName().substring( 0, 1 ).toUpperCase()
                + ingredient.getName().substring( 1 ).toLowerCase() );

        inventoryCurrent.addIngredient( ingredient );

        service.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }
}

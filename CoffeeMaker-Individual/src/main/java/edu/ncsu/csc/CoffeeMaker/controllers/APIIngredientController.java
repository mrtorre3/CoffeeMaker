package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * InventoryService object, used to update inventory on when new ingredients
     * are made or if ingredients are deleted.
     */
    @Autowired
    private InventoryService  inventoryService;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients in the form of a
     *         List(Ingredient) object
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return ingredientService.findAll();
    }

    /**
     * REST API method to provide GET access to the ingredient with the given
     * name
     *
     * @param name
     *            the name of the ingredient to get
     * @return JSON representation of an ingredient in the form of a
     *         ResponseEntity
     */
    @GetMapping ( BASE_PATH + "ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable final String name ) {

        final Ingredient ingredient = ingredientService.findByName( name );

        if ( null == ingredient ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * THIS IS FOR CREATING DUPLICATE INGREDIENTS, NOT NEW INGREDIENTS. NEW
     * INGREDIENTS WILL BE CREATED IN APIINVENTORY.
     *
     * REST API method to provide POST functionality for adding an ingredient to
     * the list of ingredients
     *
     * @param ingredient
     *            the ingredient to add
     * @return error or success response depending on whether the ingredient was
     *         successfully added
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        ingredient.setName( ingredient.getName().substring( 0, 1 ).toUpperCase()
                + ingredient.getName().substring( 1 ).toLowerCase() );
        if ( null != ingredientService.findByName( ingredient.getName() ) ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( ingredientService.findAll().size() < 1000000000 ) {
            ingredientService.save( ingredient );

            return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ),
                    HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in ingredient book for ingredient " + ingredient.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to provide DELETE functionality for removing a given
     * ingredient from the list of ingredients
     *
     * THIS METHOD WILL REMOVE INGREDIENTS FROM THE INVENTORY IF NO DUPLICATES
     * ARE FOUND
     *
     * @param name
     *            the name of the ingredient to remove
     * @return error or success response depending on whether the ingredient was
     *         successfully removed
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity deleteIngredient ( @PathVariable final long id ) {
        final Ingredient ingredient = ingredientService.findById( id );
        if ( null == ingredient ) {
            return new ResponseEntity( errorResponse( "There are no ingredients to delete" ), HttpStatus.NOT_FOUND );
        }
        else {
            ingredientService.delete( ingredient );

            // the rest of this code checks to see whether other duplicates
            // exist. if no more duplicates
            // do not exist, remove this ingredient by name from the inventory.
            final List<Ingredient> currentIngredients = ingredientService.findAll();

            boolean inventoryFlag = true;
            for ( final Ingredient i : currentIngredients ) {
                if ( i.getName().equals( ingredient.getName() ) ) {
                    inventoryFlag = false;
                    break;
                }
            }
            if ( inventoryFlag ) {
                final Inventory updateInventory = inventoryService.getInventory();

                for ( final Ingredient i : updateInventory.getIngredients() ) {
                    if ( i.getName().equals( ingredient.getName() ) ) {
                        updateInventory.removeIngredient( i );
                        break;
                    }
                }
            }
        }

        return new ResponseEntity( successResponse( ingredient.getName() + " was deleted successfully" ),
                HttpStatus.OK );
    }

}

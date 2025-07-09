package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
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
public class APIRecipeController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService service;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipes
     */
    @GetMapping ( BASE_PATH + "/recipes" )
    public List<Recipe> getRecipes () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity getRecipe ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return null == recipe
                ? new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( recipe, HttpStatus.OK );
    }

    /**
     * REST API method to provide PUT access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity putRecipe ( @RequestBody final String json ) {
        final JsonParser parse = new BasicJsonParser();

        final Map recipeBits = parse.parseMap( json );

        final Recipe r = new Recipe();

        try {

            String recipeName = (String) recipeBits.get( "name" );
            recipeName = recipeName.substring( 0, 1 ).toUpperCase() + recipeName.substring( 1 ).toLowerCase();

            if ( null == service.findByName( recipeName ) ) {
                return new ResponseEntity( errorResponse( "Recipe with the name " + recipeName + " does not exist" ),
                        HttpStatus.CONFLICT );
            }

            final int recipePrice = Integer.parseInt( recipeBits.get( "price" ).toString() );

            if ( recipePrice < 1 ) {
                return new ResponseEntity( errorResponse( "The recipe must have a positive currency" ),
                        HttpStatus.CONFLICT );
            }

            final ArrayList<Map<String, Integer>> ingredientList = (ArrayList<Map<String, Integer>>) recipeBits
                    .get( "customIngredients" );

            if ( ingredientList.size() <= 0 ) {
                return new ResponseEntity( errorResponse( "Atleast one ingredient must be selected" ),
                        HttpStatus.CONFLICT );
            }
            final List<Ingredient> rList = new ArrayList<Ingredient>();

            for ( int i = 0; i < ingredientList.size(); i++ ) {

                final Map ingredientBits = ingredientList.get( i );

                final String ingredientName = (String) ingredientBits.get( "name" );

                final int ingredientAmount = Integer.parseInt( ingredientBits.get( "amount" ).toString() );

                if ( ingredientAmount <= 0 ) {
                    return new ResponseEntity( errorResponse( "The ingredients must have positve amounts" ),
                            HttpStatus.CONFLICT );
                }

                final Ingredient I = new Ingredient();

                I.setName( ingredientName );
                I.setAmount( ingredientAmount );

                rList.add( I );
            }

            r.setName( recipeName );
            r.setPrice( recipePrice );

            for ( int i = 0; i < rList.size(); i++ ) {
                r.addIngredient( rList.get( i ) );
            }

            final Recipe oldRecipe = service.findByName( recipeName );
            oldRecipe.updateRecipe( r );
            service.save( oldRecipe );
            return new ResponseEntity( successResponse( r.getName() + "successfully edited" ), HttpStatus.OK );

        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "The price must be a positive integer!" );
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid recipe json" );
        }

    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final String json ) {

        final JsonParser parse = new BasicJsonParser();

        final Map recipeBits = parse.parseMap( json );

        final Recipe r = new Recipe();

        try {

            String recipeName = (String) recipeBits.get( "name" );
            recipeName = recipeName.substring( 0, 1 ).toUpperCase() + recipeName.substring( 1 ).toLowerCase();

            if ( null != service.findByName( recipeName ) ) {
                return new ResponseEntity( errorResponse( "Recipe with the name " + recipeName + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            final int recipePrice = Integer.parseInt( recipeBits.get( "price" ).toString() );

            if ( recipePrice < 1 ) {
                return new ResponseEntity( errorResponse( "The recipe must have a positive currency" ),
                        HttpStatus.CONFLICT );
            }

            final ArrayList<Map<String, Integer>> ingredientList = (ArrayList<Map<String, Integer>>) recipeBits
                    .get( "customIngredients" );

            if ( ingredientList.size() <= 0 ) {
                return new ResponseEntity( errorResponse( "Atleast one ingredient must be selected" ),
                        HttpStatus.CONFLICT );
            }
            final List<Ingredient> rList = new ArrayList<Ingredient>();

            for ( int i = 0; i < ingredientList.size(); i++ ) {

                final Map ingredientBits = ingredientList.get( i );

                final String ingredientName = (String) ingredientBits.get( "name" );

                final int ingredientAmount = Integer.parseInt( ingredientBits.get( "amount" ).toString() );

                if ( ingredientAmount <= 0 ) {
                    return new ResponseEntity( errorResponse( "The ingredients must have positve amounts" ),
                            HttpStatus.CONFLICT );
                }

                final Ingredient I = new Ingredient();

                I.setName( ingredientName );
                I.setAmount( ingredientAmount );

                rList.add( I );
            }

            r.setName( recipeName );
            r.setPrice( recipePrice );

            for ( int i = 0; i < rList.size(); i++ ) {
                r.addIngredient( rList.get( i ) );
            }

        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "The price must be a positive integer!" );
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid recipe json" );
        }

        if ( service.findAll().size() < 3 ) {
            if ( r.getIngredients().size() > 0 ) {
                service.save( r );
            }
            return new ResponseEntity( successResponse( r.getName() + "successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Insufficient space in recipe book for recipe " + r.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }
    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( recipe );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }
}

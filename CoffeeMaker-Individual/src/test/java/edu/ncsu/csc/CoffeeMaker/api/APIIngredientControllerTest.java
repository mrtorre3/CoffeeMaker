package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.controllers.APIIngredientController;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientControllerTest {

    @Autowired
    private APIIngredientController ingHTTP;

    @Autowired
    private IngredientService       service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {

        service.deleteAll();

    }

    @Test
    @Transactional
    public void testGetIngredients () {

        // Empty ingredients
        assertEquals( "[]", ingHTTP.getIngredients().toString() );

        // Update ingredients
        service.save( new Ingredient( "Coffee", 50 ) );
        service.save( new Ingredient( "Milk", 60 ) );
        service.save( new Ingredient( "Sugar", 70 ) );
        service.save( new Ingredient( "Chocolate", 80 ) );

        assertEquals( "Coffee", ingHTTP.getIngredients().get( 0 ).getName() );
        assertEquals( 50, (int) ingHTTP.getIngredients().get( 0 ).getAmount() );
        assertEquals( "Milk", ingHTTP.getIngredients().get( 1 ).getName() );
        assertEquals( 60, (int) ingHTTP.getIngredients().get( 1 ).getAmount() );
        assertEquals( "Sugar", ingHTTP.getIngredients().get( 2 ).getName() );
        assertEquals( 70, (int) ingHTTP.getIngredients().get( 2 ).getAmount() );
        assertEquals( "Chocolate", ingHTTP.getIngredients().get( 3 ).getName() );
        assertEquals( 80, (int) ingHTTP.getIngredients().get( 3 ).getAmount() );

    }

    @Test
    @Transactional
    public void testGetIngredient () {

        // Empty ingredients
        assertEquals( "ResponseEntity", ingHTTP.getIngredient( null ).getClass().getSimpleName() );

        // Update ingredients
        final Ingredient coffee = new Ingredient( "Coffee", 50 );
        final Ingredient milk = new Ingredient( "Milk", 60 );
        final Ingredient sugar = new Ingredient( "Sugar", 70 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 80 );
        service.save( coffee );
        service.save( milk );
        service.save( sugar );
        service.save( chocolate );

        assertEquals( "Ingredient [id=" + coffee.getId() + ", name=Coffee]",
                ingHTTP.getIngredient( "Coffee" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + milk.getId() + ", name=Milk]",
                ingHTTP.getIngredient( "Milk" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + sugar.getId() + ", name=Sugar]",
                ingHTTP.getIngredient( "Sugar" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + chocolate.getId() + ", name=Chocolate]",
                ingHTTP.getIngredient( "Chocolate" ).getBody().toString() );
    }

    @Test
    @Transactional
    public void testCreateIngredient () {

        // Empty ingredients
        assertEquals( "[]", ingHTTP.getIngredients().toString() );

        // Create ingredients
        final Ingredient coffee = new Ingredient( "Coffee", 50 );
        final Ingredient milk = new Ingredient( "Milk", 60 );
        final Ingredient sugar = new Ingredient( "Sugar", 70 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 80 );

        assertEquals( "{\"status\":\"success\",\"message\":\"Coffee successfully created\"}",
                ingHTTP.createIngredient( coffee ).getBody().toString() );
        assertEquals( "{\"status\":\"success\",\"message\":\"Milk successfully created\"}",
                ingHTTP.createIngredient( milk ).getBody().toString() );
        assertEquals( "{\"status\":\"success\",\"message\":\"Sugar successfully created\"}",
                ingHTTP.createIngredient( sugar ).getBody().toString() );
        assertEquals( "{\"status\":\"success\",\"message\":\"Chocolate successfully created\"}",
                ingHTTP.createIngredient( chocolate ).getBody().toString() );

        assertEquals( "{\"status\":\"failed\",\"message\":\"Ingredient with the name Coffee already exists\"}",
                ingHTTP.createIngredient( coffee ).getBody().toString() );

        assertEquals( "Ingredient [id=" + coffee.getId() + ", name=Coffee]",
                ingHTTP.getIngredient( "Coffee" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + milk.getId() + ", name=Milk]",
                ingHTTP.getIngredient( "Milk" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + sugar.getId() + ", name=Sugar]",
                ingHTTP.getIngredient( "Sugar" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + chocolate.getId() + ", name=Chocolate]",
                ingHTTP.getIngredient( "Chocolate" ).getBody().toString() );
    }

    @Test
    @Transactional
    public void testDeleteIngredient () {

        // Empty ingredients
        assertEquals( "ResponseEntity", ingHTTP.getIngredient( null ).getClass().getSimpleName() );

        // Update ingredients
        final Ingredient coffee = new Ingredient( "Coffee", 50 );
        final Ingredient milk = new Ingredient( "Milk", 60 );
        final Ingredient sugar = new Ingredient( "Sugar", 70 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 80 );
        service.save( coffee );
        service.save( milk );
        service.save( sugar );
        service.save( chocolate );

        assertEquals( "{\"status\":\"success\",\"message\":\"Coffee was deleted successfully\"}",
                ingHTTP.deleteIngredient( coffee.getId() ).getBody().toString() );

        assertEquals( 3, ingHTTP.getIngredients().size() );
        assertEquals( "Ingredient [id=" + milk.getId() + ", name=Milk]",
                ingHTTP.getIngredient( "Milk" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + sugar.getId() + ", name=Sugar]",
                ingHTTP.getIngredient( "Sugar" ).getBody().toString() );
        assertEquals( "Ingredient [id=" + chocolate.getId() + ", name=Chocolate]",
                ingHTTP.getIngredient( "Chocolate" ).getBody().toString() );
    }
}

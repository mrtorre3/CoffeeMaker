package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", 4 );
        final Ingredient sugar = new Ingredient( "Sugar", 8 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );

        final Recipe r = new Recipe();
        r.addIngredient( chocolate );
        r.addIngredient( coffee );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );
        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );
    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", 1 );
        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0 );

        final String name = "Coffee";

        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( chocolate );
        r1.setPrice( 50 );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( name );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 0 ) );
        r2.setPrice( 50 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", 1 );
        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( chocolate );
        r1.setPrice( 50 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.setPrice( 50 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r3.setPrice( 60 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.setName( "Hot Chocolate" );
        r4.addIngredient( new Ingredient( "Coffee", 4 ) );
        r4.addIngredient( new Ingredient( "Milk", 2 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );
        r4.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r4.setPrice( 75 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    @Test
    @Transactional
    public void testEditRecipe () throws Exception {

        service.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );
        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Delicious Not-Coffee" );
        recipe1.addIngredient( new Ingredient( "Pumpkin", 5 ) );
        recipe1.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe1.addIngredient( new Ingredient( "Sugar", 100 ) );
        recipe1.setPrice( 90 );

        mvc.perform( put( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        Assertions.assertEquals( 3, service.findByName( "Delicious Not-Coffee" ).getIngredients().size() );
        Assertions.assertEquals( "Pumpkin",
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( 5,
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( "Milk",
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( 20,
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( "Sugar",
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( 100,
                service.findByName( "Delicious Not-Coffee" ).getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 90, service.findByName( "Delicious Not-Coffee" ).getPrice() );

        recipe1.setPrice( 0 );

        mvc.perform( put( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ) ).andExpect( status().isConflict() );

        recipe1.setPrice( 1 );

        recipe1.getIngredients().get( 0 ).setAmount( 0 );

        mvc.perform( put( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ) ).andExpect( status().isConflict() );

        recipe1.getIngredients().get( 0 ).setAmount( 5 );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( recipe1.getName() );
        recipe2.setPrice( recipe1.getPrice() );
        mvc.perform( put( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    public void testDeleteRecipe () throws Exception {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient coffee = new Ingredient( "Coffee", 4 );
        final Ingredient milk = new Ingredient( "Milk", 3 );
        final Ingredient sugar = new Ingredient( "Sugar", 2 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 1 );

        final Recipe r1 = new Recipe();
        r1.setName( "Recipe 01" );
        r1.setPrice( 10 );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( chocolate );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Recipe 02" );
        r2.setPrice( 15 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 3 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 3 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Recipe 03" );
        r3.setPrice( 20 );
        r3.addIngredient( new Ingredient( "Coffee", 4 ) );
        r3.addIngredient( new Ingredient( "Milk", 4 ) );
        r3.addIngredient( new Ingredient( "Sugar", 4 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 4 ) );
        service.save( r3 );

        /* Test successfully deleting a recipe. */
        mvc.perform( delete( "/api/v1/recipes/Recipe 03" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.count() );

        /* Test removing a recipe that was never in the database. */
        final Recipe r4 = new Recipe();
        r4.setName( "Does Not Exist" );
        r4.setPrice( 10 );
        r4.addIngredient( new Ingredient( "Coffee", 0 ) );
        r4.addIngredient( new Ingredient( "Milk", 2 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );
        r4.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( delete( "/api/v1/recipes/Does Not Exist" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isNotFound() );
        Assertions.assertEquals( 2, service.count() );
    }

    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient coffee = new Ingredient( "Coffee", 4 );
        final Ingredient milk = new Ingredient( "Milk", 3 );
        final Ingredient sugar = new Ingredient( "Sugar", 2 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 1 );

        final Recipe r1 = new Recipe();
        r1.setName( "Recipe 01" );
        r1.setPrice( 10 );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( chocolate );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Recipe 02" );
        r2.setPrice( 15 );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 3 ) );
        r2.addIngredient( new Ingredient( "Sugar", 3 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 3 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Recipe 03" );
        r3.setPrice( 20 );
        r3.addIngredient( new Ingredient( "Coffee", 4 ) );
        r3.addIngredient( new Ingredient( "Milk", 4 ) );
        r3.addIngredient( new Ingredient( "Sugar", 4 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 4 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        /* Test successfully getting a recipe. */
        mvc.perform( get( "/api/v1/recipes/Recipe 03" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        /* Test getting a recipe that was never in the database. */
        final Recipe r4 = new Recipe();
        r4.setName( "Does Not Exist" );
        r4.setPrice( 10 );
        r4.addIngredient( new Ingredient( "Coffee", 0 ) );
        r4.addIngredient( new Ingredient( "Milk", 2 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );
        r4.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( get( "/api/v1/recipes/Does Not Exist" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isNotFound() );

    }
}

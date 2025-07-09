package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    // added this from looking for help from post 67 on piazza
    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        // from post 67 on piazza, modified it so it deletes only recipes
        while ( !service.findAll().isEmpty() ) {
            service.delete( service.findAll().get( 0 ) );
        }
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {

        final Ingredient coffee = new Ingredient( "Coffee", 5 );
        final Ingredient milk = new Ingredient( "Milk", 5 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe mocha = new Recipe();
            mocha.addIngredient( chocolate );
            mocha.addIngredient( coffee );
            mocha.addIngredient( milk );
            mocha.addIngredient( sugar );
            mocha.setPrice( 5 );
            mocha.setName( "Mocha" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( mocha ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe.contains( "Mocha" ) );

        final Inventory sendout = new Inventory();
        sendout.addIngredient( new Ingredient( "Coffee", 50 ) );
        sendout.addIngredient( new Ingredient( "Chocolate", 50 ) );
        sendout.addIngredient( new Ingredient( "Milk", 50 ) );
        sendout.addIngredient( new Ingredient( "Sugar", 50 ) );
        // sendout.addIngredient( new Ingredient( "Chocolate", 50 ) );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( sendout ) ) ).andExpect( status().isOk() );

        final DrinkOrder testOrder = new DrinkOrder();
        testOrder.setAmountPaid( 10 );
        testOrder.setCustomerName( "customer" );
        testOrder.setRecipeName( "Mocha" );

        final MvcResult result = mvc.perform( post( String.format( "/api/v1/makecoffee/Mocha" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( testOrder ) ) )
                .andDo( print() ).andReturn();

        System.out.print( result.toString() );
    }
}

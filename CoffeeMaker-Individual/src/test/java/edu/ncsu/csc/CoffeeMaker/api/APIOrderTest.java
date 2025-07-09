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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.controllers.APIDrinkOrderController;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.services.DrinkOrderService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc                 mvc;

    @Autowired
    private WebApplicationContext   context;

    @Autowired
    private DrinkOrderService       service;

    @Autowired
    private APIDrinkOrderController oController;

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
    public void testGetOrder () {

        // Create new orders to add to the list of orders.
        final DrinkOrder o1 = new DrinkOrder( "name", "recipe", 4 );
        final DrinkOrder o2 = new DrinkOrder( "anotherName", "recipe2", 6 );
        final DrinkOrder o3 = new DrinkOrder( "anotherAnotherName", "recipe3", 7 );

        service.save( o1 );
        service.save( o2 );
        service.save( o3 );

        assertEquals( 3, oController.getOrder().size() );

        assertEquals( "name", oController.getOrder().get( 0 ).getCustomerName() );
        assertEquals( "recipe2", oController.getOrder().get( 1 ).getRecipeName() );
        assertEquals( 7, oController.getOrder().get( 2 ).getAmountPaid() );
    }
}

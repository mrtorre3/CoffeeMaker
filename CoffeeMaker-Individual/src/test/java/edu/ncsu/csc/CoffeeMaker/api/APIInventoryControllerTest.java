package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.controllers.APIInventoryController;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIInventoryControllerTest {

    @Autowired
    private APIInventoryController invHTTP;

    @Autowired
    private InventoryService       service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {

        service.deleteAll();

    }

    @Test
    @Transactional
    public void testGetInventory () {

        // Empty inventory
        assertEquals( "", invHTTP.getInventory().getBody().toString() );

        // Update inventory
        final Inventory tempIvt = service.getInventory();
        tempIvt.addIngredient( new Ingredient( "Coffee", 50 ) );
        tempIvt.addIngredient( new Ingredient( "Milk", 60 ) );
        tempIvt.addIngredient( new Ingredient( "Sugar", 70 ) );
        tempIvt.addIngredient( new Ingredient( "Chocolate", 80 ) );
        service.save( tempIvt );

        // Check updated inventory
        assertEquals( "Coffee: 50\n" + "Milk: 60\n" + "Sugar: 70\n" + "Chocolate: 80\n",
                invHTTP.getInventory().getBody().toString() );
    }

    @Test
    @Transactional
    public void testUpdateInventory () {

        final Ingredient coffee = new Ingredient( "Coffee", 50 );
        final Ingredient milk = new Ingredient( "Milk", 60 );
        final Ingredient sugar = new Ingredient( "Sugar", 70 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 80 );

        // Empty inventory
        assertEquals( "", invHTTP.getInventory().getBody().toString() );

        // Update inventory
        Inventory tempIvt = new Inventory();
        tempIvt.addIngredient( coffee );
        tempIvt.addIngredient( milk );
        tempIvt.addIngredient( sugar );
        tempIvt.addIngredient( chocolate );
        invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );

        // Check updated inventory
        assertEquals( "Coffee: 50\n" + "Milk: 60\n" + "Sugar: 70\n" + "Chocolate: 80\n",
                invHTTP.getInventory().getBody().toString() );

        tempIvt = new Inventory();
        // Update inventory - different values
        tempIvt.addIngredient( new Ingredient( "Coffee", 20 ) );
        tempIvt.addIngredient( new Ingredient( "Milk", 30 ) );
        tempIvt.addIngredient( new Ingredient( "Sugar", 700 ) );
        tempIvt.addIngredient( new Ingredient( "Chocolate", 8000 ) );
        invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );

        // Check updated inventory - values should have added to previous
        assertEquals( "Coffee: 70\n" + "Milk: 90\n" + "Sugar: 770\n" + "Chocolate: 8080\n",
                invHTTP.getInventory().getBody().toString() );

        tempIvt = new Inventory();
        // Update inventory - no additions
        tempIvt.addIngredient( new Ingredient( "Coffee", 0 ) );
        tempIvt.addIngredient( new Ingredient( "Milk", 0 ) );
        tempIvt.addIngredient( new Ingredient( "Sugar", 0 ) );
        tempIvt.addIngredient( new Ingredient( "Chocolate", 0 ) );
        invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );

        // Check updated inventory - no additions
        assertEquals( "Coffee: 70\n" + "Milk: 90\n" + "Sugar: 770\n" + "Chocolate: 8080\n",
                invHTTP.getInventory().getBody().toString() );

        try {
            tempIvt.addIngredient( new Ingredient( "Coffee", 0 ) );
            tempIvt.addIngredient( new Ingredient( "Milk", 0 ) );
            tempIvt.addIngredient( new Ingredient( "Sugar", 0 ) );
            tempIvt.addIngredient( new Ingredient( "Chocolate", -1 ) );
            invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );
        }
        catch ( final IllegalArgumentException iae ) {

        }
    }

    @Test
    @Transactional
    public void testInsufficientInventory () {

        final Ingredient coffee = new Ingredient( "Coffee", 0 );
        final Ingredient milk = new Ingredient( "Milk", 0 );
        final Ingredient sugar = new Ingredient( "Sugar", 0 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0 );

        // Update inventory
        Inventory tempIvt = new Inventory();
        tempIvt.addIngredient( coffee );
        tempIvt.addIngredient( milk );
        tempIvt.addIngredient( sugar );
        tempIvt.addIngredient( chocolate );
        invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );
        // Empty inventory
        assertEquals( "Coffee: 0\n" + "Milk: 0\n" + "Sugar: 0\n" + "Chocolate: 0\n",
                invHTTP.getInventory().getBody().toString() );

        // string used to check the name of the recipe to ensure it remains the
        // same
        final String coocha = "CooCha";

        // recipe used to create a 'CooCha' recipe
        final Recipe cooChaRecipe = new Recipe();
        cooChaRecipe.setName( coocha );
        cooChaRecipe.setPrice( 9 );
        cooChaRecipe.addIngredient( new Ingredient( "Coffee", 6 ) );
        cooChaRecipe.addIngredient( new Ingredient( "Milk", 8 ) );
        cooChaRecipe.addIngredient( new Ingredient( "Sugar", 7 ) );
        cooChaRecipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        // recipe used to test checkRecpie()
        final Recipe emptyRecipe = new Recipe();
        emptyRecipe.setName( "empty :(" );
        emptyRecipe.setPrice( 5 );

        tempIvt = new Inventory();
        // set the inventory to 5 for all fields
        tempIvt.addIngredient( new Ingredient( "Coffee", 5 ) );
        tempIvt.addIngredient( new Ingredient( "Milk", 5 ) );
        tempIvt.addIngredient( new Ingredient( "Sugar", 5 ) );
        tempIvt.addIngredient( new Ingredient( "Chocolate", 5 ) );
        invHTTP.updateInventory( TestUtils.asJsonString( tempIvt ) );

        // check that enoughIngredients functions properly
        assertFalse( tempIvt.enoughIngredients( cooChaRecipe ) );
        assertTrue( tempIvt.enoughIngredients( emptyRecipe ) );

        final Inventory testIvt = new Inventory();
        // test that setting values to negative doesn't affect inventory
        assertThrows( IllegalArgumentException.class, () -> testIvt.addIngredient( new Ingredient( "Coffee", -1 ) ) );
        assertThrows( IllegalArgumentException.class, () -> testIvt.addIngredient( new Ingredient( "Milk", -1 ) ) );
        assertThrows( IllegalArgumentException.class, () -> testIvt.addIngredient( new Ingredient( "Sugar", -1 ) ) );
        assertThrows( IllegalArgumentException.class,
                () -> testIvt.addIngredient( new Ingredient( "Chocolate", -1 ) ) );
        // assertThrows( ConstraintViolationException.class, () ->
        // invHTTP.updateInventory( testIvt ) );

        tempIvt = service.getInventory();
        // check that the values haven't changed.
        assertEquals( 5, tempIvt.getIngredients().get( 0 ).getAmount() );
        assertEquals( 5, tempIvt.getIngredients().get( 1 ).getAmount() );
        assertEquals( 5, tempIvt.getIngredients().get( 2 ).getAmount() );
        assertEquals( 5, tempIvt.getIngredients().get( 3 ).getAmount() );
    }
}

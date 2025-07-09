package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.DrinkOrderService;

/**
 * Test class for the DrinkOrder class
 *
 * @author mrtorre3
 * @author zebrentz
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class DrinkOrderTest {

    @Autowired
    private DrinkOrderService drinkService;

    @BeforeEach
    public void setup () {
        drinkService.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateDrinkOrder () {
        final DrinkOrder dO = new DrinkOrder( "name", "recipe", 4 );

        // Test that the order was created correctly.
        assertEquals( "name", dO.getCustomerName() );
        assertEquals( "recipe", dO.getRecipeName() );
        assertEquals( 4, dO.getAmountPaid() );
        assertFalse( dO.isFilled() );

        // Test setter methods.
        dO.setCustomerName( "brandNewName" );
        assertEquals( "brandNewName", dO.getCustomerName() );
        dO.setRecipeName( "newRecipeName" );
        assertEquals( "newRecipeName", dO.getRecipeName() );
        dO.setAmountPaid( 17 );
        assertEquals( 17, dO.getAmountPaid() );
        dO.markFilled( true );
        assertTrue( dO.isFilled() );
    }

    @Test
    @Transactional
    public void testToString () {

        // "DrinkOrder [customerName=" + customerName + ", recipeName=" +
        // recipeName + ", amountPaid="
        // + amountPaid + ", isFilled=" + isFilled + ", id=" + id + "]";
        final DrinkOrder dO = new DrinkOrder( "name", "recipe", 4 );

        final String expStr = "DrinkOrder [customerName=name, recipeName=recipe, amountPaid=4, isFilled=false, id="
                + dO.getId() + "]";
        assertEquals( expStr, dO.toString() );
    }

    @Test
    @Transactional
    public void testEquals () {
        final DrinkOrder o1 = new DrinkOrder( "name", "recipe", 4 );
        final DrinkOrder o2 = new DrinkOrder( "name", "otherRecipe", 4 );
        final DrinkOrder o3 = new DrinkOrder( "diffName", "recipe", 4 );
        final DrinkOrder o4 = new DrinkOrder( "name", "recipe", 5 );
        final DrinkOrder o5 = new DrinkOrder( "name", "recipe", 5 );
        o5.markFilled( true );

        assertTrue( o1.equals( o1 ) );
        assertTrue( o2.equals( o2 ) );
        assertTrue( o3.equals( o3 ) );
        assertTrue( o4.equals( o4 ) );
        assertTrue( o5.equals( o5 ) );
        assertFalse( o1.equals( o2 ) );
        assertFalse( o2.equals( o1 ) );
        assertFalse( o1.equals( o3 ) );
        assertFalse( o3.equals( o1 ) );
        assertFalse( o1.equals( o4 ) );
        assertFalse( o4.equals( o1 ) );
        assertFalse( o1.equals( o5 ) );
        assertFalse( o5.equals( o1 ) );

        final Recipe recipe = new Recipe();

        assertFalse( o1.equals( null ) );
        assertFalse( o1.equals( recipe ) );
    }
}

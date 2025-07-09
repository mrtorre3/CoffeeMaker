package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    private Ingredient       coffee;

    private Ingredient       chocolate;

    private Ingredient       sugar;

    private Ingredient       milk;

    private Ingredient       cinnamon;

    @BeforeEach
    public void setup () {
        inventoryService.deleteAll();
        final Inventory ivt = inventoryService.getInventory();

        coffee = new Ingredient( "Coffee", 500 );
        chocolate = new Ingredient( "Chocolate", 500 );
        sugar = new Ingredient( "Sugar", 500 );
        milk = new Ingredient( "Milk", 500 );
        cinnamon = new Ingredient( "Cinnamon", 500 );

        ivt.addIngredient( coffee );
        ivt.addIngredient( chocolate );
        ivt.addIngredient( sugar );
        ivt.addIngredient( milk );
        ivt.addIngredient( cinnamon );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );

        recipe.setPrice( 5 );

        // Make sure there are enough ingredients to make the
        // recipe
        Assertions.assertTrue( i.enoughIngredients( recipe ) );
        i.useIngredients( recipe );

        // Make sure that all of the inventory fields are now
        // properly updated
        Assertions.assertEquals( "Coffee", i.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Chocolate", i.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Sugar", i.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Milk", i.getIngredients().get( 3 ).getName() );
        Assertions.assertEquals( 490, (int) i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 480, (int) i.getIngredients().get( 3 ).getAmount() );
        Assertions.assertEquals( 495, (int) i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 499, (int) i.getIngredients().get( 0 ).getAmount() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        coffee = ivt.getIngredients().get( 0 );
        milk = ivt.getIngredients().get( 1 );
        sugar = ivt.getIngredients().get( 2 );
        chocolate = ivt.getIngredients().get( 3 );

        coffee.setAmount( coffee.getAmount() + 5 );
        milk.setAmount( milk.getAmount() + 3 );
        sugar.setAmount( sugar.getAmount() + 7 );
        chocolate.setAmount( chocolate.getAmount() + 2 );

        // Save and retrieve again to update with DB
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredients().get( 0 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredients().get( 1 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredients().get( 2 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredients().get( 3 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values chocolate" );
    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( new Ingredient( "Coffee", -5 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testSetInventoryID () {
        final Inventory ivt = inventoryService.getInventory();

        // Test setId //
        ivt.setId( (long) 107 );
        Assertions.assertEquals( 107, ivt.getId() );
    }

    @Test
    @Transactional
    public void testEnoughIngredients () {
        final Inventory ivt = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );

        Assertions.assertTrue( ivt.enoughIngredients( recipe ) );
        // The limiting ingredient for the recipe is milk. The
        // recipe can be made 25 times before there are no
        // longer enough ingredients to make any more.
        for ( int i = 0; i < 25; i++ ) {
            ivt.useIngredients( recipe );
        }

        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
    }
}

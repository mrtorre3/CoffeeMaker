package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService     recipeService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private InventoryService  invService;

    private final Inventory   inv       = new Inventory();

    private final Ingredient  coffee    = new Ingredient( "coffee", 10 );

    private final Ingredient  chocolate = new Ingredient( "chocolate", 10 );

    private final Ingredient  sugar     = new Ingredient( "sugar", 10 );

    private final Ingredient  milk      = new Ingredient( "milk", 10 );

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
        ingredientService.deleteAll();

        ingredientService.save( chocolate );
        ingredientService.save( coffee );
        ingredientService.save( sugar );
        ingredientService.save( milk );

    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {

        final Recipe r = new Recipe();
        // Set fields
        r.setName( "Great Coffee" );
        r.setPrice( 10 );
        r.addIngredient( chocolate );
        r.addIngredient( coffee );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        recipeService.save( r ); // Save recipe

        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );
        assertEquals( r.getName(), dbRecipe.getName() );

        assertEquals( r.getIngredients().size(), dbRecipe.getIngredients().size() );

        for ( int i = 0; i < r.getIngredients().size(); i++ ) {

            assertEquals( r.getIngredients().get( i ), dbRecipe.getIngredients().get( i ) );
        }

    }

    // @Test
    // @Transactional
    // public void testIngredients () {
    //
    // // try adding an ingredient to the inventory
    // final Ingredient ing = new Ingredient();
    // ing.setName( "Ingredient 01" );
    // ing.setAmount( 8 );
    //
    // // add the ingredient to the inventory
    // inv.addIngredient( ing );
    // invService.save( inv );
    //
    // // // check the database list of ingredients against the local list of
    // // // ingredients
    // // List<Ingredient> dbIngredients = invService.findAll().get( 0
    // // ).getIngredients();
    // // assertEquals( 1, dbIngredients.size() );
    //
    // Ingredient dbIng = dbIngredients.get( 0 );
    // assertEquals( ing.getName(), dbIng.getName() );
    // assertEquals( ing.getAmount(), dbIng.getAmount() );
    //
    // // try adding more ingredients to the inventory
    // Ingredient ing2 = new Ingredient();
    // ing2.setName( "Ingredient 02" );
    // ing2.setAmount( 15 );
    //
    // final Ingredient ing3 = new Ingredient();
    // ing3.setName( "Ingredient 03" );
    // ing3.setAmount( 27 );
    //
    // // add the ingredients to the inventory
    // inv.addIngredient( ing2 );
    // inv.addIngredient( ing3 );
    // invService.save( inv );
    //
    // // check the database list of ingredients against the local list of
    // // ingredients
    // dbIngredients = invService.findAll().get( 0 ).getIngredients();
    // assertEquals( 3, dbIngredients.size() );
    //
    // dbIng = dbIngredients.get( 0 );
    // assertEquals( ing.getName(), dbIng.getName() );
    // assertEquals( ing.getAmount(), dbIng.getAmount() );
    //
    // Ingredient dbIng2 = dbIngredients.get( 1 );
    // final Ingredient dbIng3 = dbIngredients.get( 2 );
    //
    // assertEquals( ing2.getName(), dbIng2.getName() );
    // assertEquals( ing3.getName(), dbIng3.getName() );
    // assertEquals( ing2.getAmount(), dbIng2.getAmount() );
    // assertEquals( ing3.getAmount(), dbIng3.getAmount() );
    //
    // // test adding a duplicate ingredient
    // final Exception e = assertThrows( IllegalArgumentException.class,
    // () -> inv.addIngredient( new Ingredient( "Ingredient 01", 4 ) ) );
    // assertEquals( "Duplicate ingredients are not allowed!", e.getMessage() );
    // assertEquals( 3, invService.findAll().get( 0 ).getIngredients().size() );
    //
    // // test removing an ingredient
    // ing2 = invService.getInventory().getIngredients().get( 1 );
    // Assertions.assertTrue( inv.removeIngredient( ing2 ) );
    // invService.save( inv );
    //
    // System.out.println( inv.toString() );
    //
    // // check the database list of ingredients against the local list of
    // // ingredients
    // // dbIngredients = invService.findAll().get( 0 ).getIngredients();
    // dbIng2 = dbIngredients.get( 1 );
    // assertEquals( 2, dbIngredients.size() );
    // assertEquals( ing.getName(), dbIng.getName() );
    // assertEquals( ing.getAmount(), dbIng.getAmount() );
    // assertEquals( ing3.getName(), dbIng2.getName() );
    // assertEquals( ing3.getAmount(), dbIng2.getAmount() );
    // }

}

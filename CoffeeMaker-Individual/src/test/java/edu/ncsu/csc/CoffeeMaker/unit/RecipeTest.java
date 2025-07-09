package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    private Ingredient    coffee;

    private Ingredient    chocolate;

    private Ingredient    sugar;

    private Ingredient    milk;

    private Ingredient    cinnamon;

    @BeforeEach
    public void setup () {
        service.deleteAll();

        coffee = new Ingredient( "coffee", 0 );
        chocolate = new Ingredient( "chocolate", 0 );
        sugar = new Ingredient( "sugar", 0 );
        milk = new Ingredient( "milk", 0 );
        cinnamon = new Ingredient( "cinnamon", 0 );
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        coffee.setAmount( 5 );
        r1.addIngredient( coffee );
        service.save( r1 );

        final Ingredient coffee2 = new Ingredient( "coffee", 5 );
        final Ingredient milk2 = new Ingredient( "milk", 5 );
        final Ingredient sugar2 = new Ingredient( "sugar", 5 );
        final Ingredient chocolate2 = new Ingredient( "chocolate", 5 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
        Assertions.assertEquals( r2, recipes.get( 1 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        coffee.setAmount( -12 );
        r1.addIngredient( coffee );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient coffee2 = new Ingredient( "coffee", 1 );
        r2.addIngredient( coffee2 );
        milk.setAmount( 1 );
        r2.addIngredient( milk );
        sugar.setAmount( 1 );
        r2.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r2.addIngredient( chocolate );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }
    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */
    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( -50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        coffee.setAmount( -3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( -1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( -1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        chocolate.setAmount( -1 );
        r1.addIngredient( chocolate );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testAddRecipe8 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        cinnamon.setAmount( 1 );
        r1.addIngredient( cinnamon );
        service.save( r1 );

        final Ingredient coffee2 = new Ingredient( "coffee", 3 );
        final Ingredient milk2 = new Ingredient( "milk", 1 );
        final Ingredient sugar2 = new Ingredient( "sugar", 1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        sugar.setAmount( 1 );
        r2.addIngredient( sugar2 );
        chocolate.setAmount( 2 );
        r2.addIngredient( chocolate );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );
    }

    @Test
    @Transactional
    public void testAddRecipe9 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        cinnamon.setAmount( 1 );
        r1.addIngredient( cinnamon );
        service.save( r1 );

        final Ingredient coffee2 = new Ingredient( "coffee", 3 );
        final Ingredient milk2 = new Ingredient( "milk", 1 );
        final Ingredient sugar2 = new Ingredient( "sugar", 1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        chocolate.setAmount( 2 );
        r2.addIngredient( chocolate );
        service.save( r2 );

        final Ingredient coffee3 = new Ingredient( "coffee", 3 );
        final Ingredient milk3 = new Ingredient( "milk", 2 );
        final Ingredient sugar3 = new Ingredient( "sugar", 2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.addIngredient( coffee3 );
        r3.addIngredient( milk3 );
        r3.addIngredient( sugar3 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        cinnamon.setAmount( 1 );
        r1.addIngredient( cinnamon );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        cinnamon.setAmount( 1 );
        r1.addIngredient( cinnamon );
        service.save( r1 );

        final Ingredient coffee2 = new Ingredient( "coffee", 3 );
        final Ingredient milk2 = new Ingredient( "milk", 1 );
        final Ingredient sugar2 = new Ingredient( "sugar", 1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        chocolate.setAmount( 2 );
        r2.addIngredient( chocolate );
        service.save( r2 );

        final Ingredient coffee3 = new Ingredient( "coffee", 3 );
        final Ingredient milk3 = new Ingredient( "milk", 2 );
        final Ingredient sugar3 = new Ingredient( "sugar", 2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.addIngredient( coffee3 );
        r3.addIngredient( milk3 );
        r3.addIngredient( sugar3 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe3 () {

        List<Recipe> dbRecipes = service.findAll();
        Assertions.assertEquals( 0, dbRecipes.size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Recipe 01" );
        r1.setPrice( 10 );
        coffee.setAmount( 4 );
        r1.addIngredient( coffee );
        milk.setAmount( 3 );
        r1.addIngredient( milk );
        sugar.setAmount( 2 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r1.addIngredient( chocolate );
        service.save( r1 );
        dbRecipes = service.findAll();
        Assertions.assertEquals( 1, dbRecipes.size(), "There should be one recipe in the CoffeeMaker" );

        final Ingredient coffee2 = new Ingredient( "coffee", 3 );
        final Ingredient milk2 = new Ingredient( "milk", 3 );
        final Ingredient sugar2 = new Ingredient( "sugar", 3 );
        final Ingredient chocolate2 = new Ingredient( "chocolate", 3 );

        final Recipe r2 = new Recipe();
        r2.setName( "Recipe 02" );
        r2.setPrice( 15 );
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );
        service.save( r2 );
        dbRecipes = service.findAll();
        Assertions.assertEquals( 2, dbRecipes.size(), "There should be two recipes in the CoffeeMaker" );

        final Ingredient coffee3 = new Ingredient( "coffee", 4 );
        final Ingredient milk3 = new Ingredient( "milk", 4 );
        final Ingredient sugar3 = new Ingredient( "sugar", 4 );
        final Ingredient chocolate3 = new Ingredient( "chocolate", 4 );

        final Recipe r3 = new Recipe();
        r3.setName( "Recipe 03" );
        r3.setPrice( 20 );
        r3.addIngredient( coffee3 );
        r3.addIngredient( milk3 );
        r3.addIngredient( sugar3 );
        r3.addIngredient( chocolate3 );
        service.save( r3 );
        dbRecipes = service.findAll();
        Assertions.assertEquals( 3, dbRecipes.size(), "There should be three recipes in the CoffeeMaker" );

        Recipe dbRecipe1 = dbRecipes.get( 0 );
        Assertions.assertEquals( r1.getName(), dbRecipe1.getName() );

        final Recipe dbRecipe2 = dbRecipes.get( 1 );
        Assertions.assertEquals( r2.getName(), dbRecipe2.getName() );

        Recipe dbRecipe3 = dbRecipes.get( 2 );
        Assertions.assertEquals( r3.getName(), dbRecipe3.getName() );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.delete( r2 );
        dbRecipes = service.findAll();

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        dbRecipe1 = dbRecipes.get( 0 );
        Assertions.assertEquals( r1.getName(), dbRecipe1.getName() );
        dbRecipe3 = dbRecipes.get( 1 );
        Assertions.assertEquals( r3.getName(), dbRecipe3.getName() );
    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        service.save( r1 );

        Recipe retrieved = service.findByName( "Coffee" );
        Assertions.assertEquals( 50, (int) retrieved.getPrice() );

        r1.setPrice( 70 );

        service.save( r1 );

        retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredients().get( 2 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );
    }

    @Test
    @Transactional
    public void testHashCode () {

        // Create recipes
        final String name0 = "Recipe0";
        final String name1 = "Recipe1";
        final String name2 = "Recipe1";
        final String name3 = "Recipe3";

        final Recipe r0 = new Recipe();
        r0.setName( name0 );
        r0.setPrice( 5 );
        coffee.setAmount( 1 );
        r0.addIngredient( coffee );
        milk.setAmount( 3 );
        r0.addIngredient( milk );
        sugar.setAmount( 5 );
        r0.addIngredient( sugar );
        chocolate.setAmount( 10 );
        r0.addIngredient( chocolate );

        final Recipe r1 = new Recipe();
        r1.setName( name1 );
        r1.setPrice( 5 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 20 );
        r1.addIngredient( milk );
        sugar.setAmount( 4 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r1.addIngredient( chocolate );

        final Recipe r2 = new Recipe();
        r2.setName( name2 );
        r2.setPrice( 5 );
        coffee.setAmount( 3 );
        r2.addIngredient( coffee );
        milk.setAmount( 20 );
        r2.addIngredient( milk );
        sugar.setAmount( 4 );
        r2.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r2.addIngredient( chocolate );

        final Recipe r3 = new Recipe();
        r3.setName( name3 );
        r3.setPrice( 5 );
        coffee.setAmount( 3 );
        r3.addIngredient( coffee );
        milk.setAmount( 20 );
        r3.addIngredient( milk );
        sugar.setAmount( 4 );
        r3.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r3.addIngredient( chocolate );

        final Recipe r4 = new Recipe();
        r4.setName( name1 );
        r4.setPrice( 1 );
        coffee.setAmount( 3 );
        r4.addIngredient( coffee );
        milk.setAmount( 20 );
        r4.addIngredient( milk );
        sugar.setAmount( 4 );
        r4.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r4.addIngredient( chocolate );

        final Recipe r5 = new Recipe();
        r5.setName( name1 );
        r5.setPrice( 5 );
        coffee.setAmount( 1 );
        r5.addIngredient( coffee );
        milk.setAmount( 20 );
        r5.addIngredient( milk );
        sugar.setAmount( 4 );
        r5.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r5.addIngredient( chocolate );

        final Recipe r6 = new Recipe();
        r6.setName( name1 );
        r6.setPrice( 5 );
        coffee.setAmount( 3 );
        r6.addIngredient( coffee );
        milk.setAmount( 1 );
        r6.addIngredient( milk );
        sugar.setAmount( 4 );
        r6.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r6.addIngredient( chocolate );

        final Recipe r7 = new Recipe();
        r7.setName( name1 );
        r7.setPrice( 5 );
        coffee.setAmount( 3 );
        r7.addIngredient( coffee );
        milk.setAmount( 20 );
        r7.addIngredient( milk );
        sugar.setAmount( 1 );
        r7.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r7.addIngredient( chocolate );

        final Recipe r8 = new Recipe();
        r8.setName( name1 );
        r8.setPrice( 5 );
        coffee.setAmount( 3 );
        r8.addIngredient( coffee );
        milk.setAmount( 20 );
        r8.addIngredient( milk );
        sugar.setAmount( 4 );
        r8.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r8.addIngredient( chocolate );

        // Check recipes against themselves
        assertEquals( r0.hashCode(), r0.hashCode() );
        assertEquals( r1.hashCode(), r1.hashCode() );
        assertEquals( r2.hashCode(), r2.hashCode() );
        assertEquals( r3.hashCode(), r3.hashCode() );
        assertEquals( r4.hashCode(), r4.hashCode() );
        assertEquals( r5.hashCode(), r5.hashCode() );
        assertEquals( r6.hashCode(), r6.hashCode() );
        assertEquals( r7.hashCode(), r7.hashCode() );
        assertEquals( r8.hashCode(), r8.hashCode() );

        // Recipes with different names should not be equal
        assertNotEquals( r0.hashCode(), r1.hashCode() );
        assertNotEquals( r1.hashCode(), r3.hashCode() );

        // Recipes with the same name should be equal, even if their other
        // values differ
        assertEquals( r1.hashCode(), r2.hashCode() );
        assertEquals( r1.hashCode(), r4.hashCode() );
        assertEquals( r1.hashCode(), r5.hashCode() );
        assertEquals( r1.hashCode(), r6.hashCode() );
        assertEquals( r1.hashCode(), r7.hashCode() );
        assertEquals( r1.hashCode(), r8.hashCode() );

    }

    @Test
    @Transactional
    public void testEquals () {

        // Create recipes
        final String name0 = "Recipe0";
        final String name1 = "Recipe1";
        final String name2 = "Recipe1";
        final String name3 = "Recipe3";

        final Recipe r0 = new Recipe();
        r0.setName( name0 );
        r0.setPrice( 5 );
        coffee.setAmount( 1 );
        r0.addIngredient( coffee );
        milk.setAmount( 3 );
        r0.addIngredient( milk );
        sugar.setAmount( 5 );
        r0.addIngredient( sugar );
        chocolate.setAmount( 10 );
        r0.addIngredient( chocolate );

        final Recipe r1 = new Recipe();
        r1.setName( name1 );
        r1.setPrice( 5 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 20 );
        r1.addIngredient( milk );
        sugar.setAmount( 4 );
        r1.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r1.addIngredient( chocolate );

        final Recipe r2 = new Recipe();
        r2.setName( name2 );
        r2.setPrice( 5 );
        coffee.setAmount( 3 );
        r2.addIngredient( coffee );
        milk.setAmount( 20 );
        r2.addIngredient( milk );
        sugar.setAmount( 4 );
        r2.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r2.addIngredient( chocolate );

        final Recipe r3 = new Recipe();
        r3.setName( name3 );
        r3.setPrice( 5 );
        coffee.setAmount( 3 );
        r3.addIngredient( coffee );
        milk.setAmount( 20 );
        r3.addIngredient( milk );
        sugar.setAmount( 4 );
        r3.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r3.addIngredient( chocolate );

        final Recipe r4 = new Recipe();
        r4.setName( name1 );
        r4.setPrice( 1 );
        coffee.setAmount( 3 );
        r4.addIngredient( coffee );
        milk.setAmount( 20 );
        r4.addIngredient( milk );
        sugar.setAmount( 4 );
        r4.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r4.addIngredient( chocolate );

        final Recipe r5 = new Recipe();
        r5.setName( name1 );
        r5.setPrice( 5 );
        coffee.setAmount( 1 );
        r5.addIngredient( coffee );
        milk.setAmount( 20 );
        r5.addIngredient( milk );
        sugar.setAmount( 4 );
        r5.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r5.addIngredient( chocolate );

        final Recipe r6 = new Recipe();
        r6.setName( name1 );
        r6.setPrice( 5 );
        coffee.setAmount( 3 );
        r6.addIngredient( coffee );
        milk.setAmount( 1 );
        r6.addIngredient( milk );
        sugar.setAmount( 4 );
        r6.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r6.addIngredient( chocolate );

        final Recipe r7 = new Recipe();
        r7.setName( name1 );
        r7.setPrice( 5 );
        coffee.setAmount( 3 );
        r7.addIngredient( coffee );
        milk.setAmount( 20 );
        r7.addIngredient( milk );
        sugar.setAmount( 1 );
        r7.addIngredient( sugar );
        chocolate.setAmount( 100 );
        r7.addIngredient( chocolate );

        final Recipe r8 = new Recipe();
        r8.setName( name1 );
        r8.setPrice( 5 );
        coffee.setAmount( 3 );
        r8.addIngredient( coffee );
        milk.setAmount( 20 );
        r8.addIngredient( milk );
        sugar.setAmount( 4 );
        r8.addIngredient( sugar );
        chocolate.setAmount( 1 );
        r8.addIngredient( chocolate );

        // Check recipes against themselves
        assertTrue( r0.equals( r0 ) );
        assertTrue( r1.equals( r1 ) );
        assertTrue( r2.equals( r2 ) );
        assertTrue( r3.equals( r3 ) );
        assertTrue( r4.equals( r4 ) );
        assertTrue( r5.equals( r5 ) );
        assertTrue( r6.equals( r6 ) );
        assertTrue( r7.equals( r7 ) );
        assertTrue( r8.equals( r8 ) );

        // Recipes with different names should not be equal
        assertFalse( r0.equals( r1 ) );
        assertFalse( r1.equals( r3 ) );

        // Recipes with the same name should be equal no matter the other values
        assertTrue( r1.equals( r2 ) );
        assertTrue( r1.equals( r4 ) );
        assertTrue( r1.equals( r5 ) );
        assertTrue( r1.equals( r6 ) );
        assertTrue( r1.equals( r7 ) );
        assertTrue( r1.equals( r8 ) );

        // Null test
        assertFalse( r0.equals( null ) );

        // Test against other object type
        assertFalse( r0.equals( new Object() ) );

        // Create null recipes
        final Recipe recipeNull = new Recipe();
        recipeNull.setName( null );
        recipeNull.setPrice( 5 );
        coffee.setAmount( 1 );
        recipeNull.addIngredient( coffee );
        milk.setAmount( 3 );
        recipeNull.addIngredient( milk );
        sugar.setAmount( 5 );
        recipeNull.addIngredient( sugar );
        chocolate.setAmount( 10 );
        recipeNull.addIngredient( chocolate );

        final Recipe recipeNull2 = new Recipe();
        recipeNull2.setName( null );
        recipeNull2.setPrice( 50 );
        coffee.setAmount( 1 );
        recipeNull2.addIngredient( coffee );
        milk.setAmount( 3 );
        recipeNull2.addIngredient( milk );
        sugar.setAmount( 5 );
        recipeNull2.addIngredient( sugar );
        chocolate.setAmount( 10 );
        recipeNull2.addIngredient( chocolate );

        // Check against null recipes
        assertTrue( recipeNull.equals( recipeNull ) );
        assertFalse( recipeNull.equals( r0 ) );
        assertTrue( recipeNull.equals( recipeNull2 ) );

    }

    @Test
    @Transactional
    public void testUpdateRecipe () {
        // string used to check the name of the recipe to ensure it remains the
        // same
        final String chacha = "ChaCha";

        // recipe used to create the original version of 'chacha'
        final Recipe chachaRecipe = new Recipe();
        chachaRecipe.setName( chacha );
        chachaRecipe.setPrice( 9 );
        coffee.setAmount( 1 );
        chachaRecipe.addIngredient( coffee );
        milk.setAmount( 8 );
        chachaRecipe.addIngredient( milk );
        sugar.setAmount( 7 );
        chachaRecipe.addIngredient( sugar );
        chocolate.setAmount( 10 );
        chachaRecipe.addIngredient( chocolate );

        // recipe used to update the chachaRecipe variable.
        final Recipe checkChachaRecipe = new Recipe();
        checkChachaRecipe.setName( chacha );
        checkChachaRecipe.setPrice( 9 );
        coffee.setAmount( 3 );
        checkChachaRecipe.addIngredient( coffee );
        milk.setAmount( 8 );
        checkChachaRecipe.addIngredient( milk );
        sugar.setAmount( 7 );
        checkChachaRecipe.addIngredient( sugar );
        chocolate.setAmount( 10 );
        checkChachaRecipe.addIngredient( chocolate );

        chachaRecipe.updateRecipe( checkChachaRecipe );

        assertTrue( chachaRecipe.toString().toString().equals( checkChachaRecipe.toString() ) );
        assertTrue( chachaRecipe.equals( checkChachaRecipe ) );
    }

    @Test
    @Transactional
    public void testCheckRecpie () {
        // string used to check the name of the recipe to ensure it remains the
        // same
        final String chacha = "ChaCha";

        // recipe used to create a 'chacha' recipe
        final Recipe chachaRecipe = new Recipe();
        chachaRecipe.setName( chacha );
        chachaRecipe.setPrice( 9 );
        coffee.setAmount( 1 );
        chachaRecipe.addIngredient( coffee );
        milk.setAmount( 8 );
        chachaRecipe.addIngredient( milk );
        sugar.setAmount( 7 );
        chachaRecipe.addIngredient( sugar );
        chocolate.setAmount( 10 );
        chachaRecipe.addIngredient( chocolate );

        final Ingredient coffee2 = new Ingredient( "coffee", 0 );
        final Ingredient milk2 = new Ingredient( "milk", 0 );
        final Ingredient sugar2 = new Ingredient( "sugar", 0 );
        final Ingredient chocolate2 = new Ingredient( "chocolate", 0 );

        // recipe used to test checkRecipe()
        final Recipe emptyRecipe = new Recipe();
        emptyRecipe.setName( "empty :(" );
        emptyRecipe.setPrice( 5 );
        emptyRecipe.addIngredient( coffee2 );
        emptyRecipe.addIngredient( milk2 );
        emptyRecipe.addIngredient( sugar2 );
        emptyRecipe.addIngredient( chocolate2 );

        assertFalse( chachaRecipe.checkRecipe() );
        assertTrue( emptyRecipe.checkRecipe() );
    }

    @Test
    @Transactional
    public void testDeleteIngredient () {

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        service.save( r1 );

        assertEquals( 3, r1.getIngredients().size() );

        r1.deleteIngredient( milk );
        service.save( r1 );

        assertEquals( 2, r1.getIngredients().size() );
    }

    @Test
    @Transactional
    public void testSetIngredient () {

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        coffee.setAmount( 3 );
        r1.addIngredient( coffee );
        milk.setAmount( 1 );
        r1.addIngredient( milk );
        sugar.setAmount( 1 );
        r1.addIngredient( sugar );
        service.save( r1 );

        assertEquals( 3, r1.getIngredients().get( 0 ).getAmount() );

        final Ingredient coffee2 = new Ingredient( "coffee 2", 5 );

        r1.setIngredient( coffee, coffee2 );
        service.save( r1 );

        assertEquals( 3, r1.getIngredients().size() );
        assertEquals( "coffee 2", r1.getIngredients().get( 0 ).getName() );
        assertEquals( 5, r1.getIngredients().get( 0 ).getAmount() );
    }
}

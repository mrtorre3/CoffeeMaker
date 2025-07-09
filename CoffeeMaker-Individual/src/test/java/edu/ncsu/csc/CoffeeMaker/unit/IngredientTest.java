package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddIngredient () {

        // Add first ingredient
        final Ingredient ing1 = new Ingredient();
        ing1.setName( "ingredient 01" );
        ing1.setAmount( 1 );
        // check that ingredient is not yet added
        assertNull( service.findById( ing1.getId() ) );
        service.save( ing1 );

        // check if ingredient is successfully added
        assertTrue( service.existsById( ing1.getId() ) );
        assertEquals( ing1, service.findById( ing1.getId() ) );

        // Add second ingredient
        final Ingredient ing2 = new Ingredient();
        ing2.setName( "ingredient 02" );
        ing2.setAmount( 2 );
        service.save( ing2 );

        // Add third ingredient
        final Ingredient ing3 = new Ingredient();
        ing3.setName( "ingredient 03" );
        ing3.setAmount( 3 );
        service.save( ing3 );

        // Should be 3 ingredients saved
        final List<Ingredient> ingredients = service.findAll();
        Assertions.assertEquals( 3, ingredients.size() );

        // Ensure the right three ingredients are saved
        Assertions.assertEquals( ing1, ingredients.get( 0 ) );
        Assertions.assertEquals( "ingredient 01", ingredients.get( 0 ).getName() );
        Assertions.assertEquals( ing2, ingredients.get( 1 ) );
        Assertions.assertEquals( "ingredient 02", ingredients.get( 1 ).getName() );
        Assertions.assertEquals( ing3, ingredients.get( 2 ) );
        Assertions.assertEquals( "ingredient 03", ingredients.get( 2 ).getName() );
    }

    @Test
    @Transactional
    public void testRemoveIngredient () {

        // Add first ingredient
        final Ingredient ing1 = new Ingredient();
        ing1.setName( "ingredient 01" );
        service.save( ing1 );

        // Add second ingredient
        final Ingredient ing2 = new Ingredient();
        ing2.setName( "ingredient 02" );
        service.save( ing2 );

        // Add third ingredient
        final Ingredient ing3 = new Ingredient();
        ing3.setName( "ingredient 03" );
        service.save( ing3 );

        // Should be 3 ingredients saved
        final List<Ingredient> ingredients = service.findAll();
        Assertions.assertEquals( 3, ingredients.size() );

        // Ensure the right three ingredients are saved
        Assertions.assertEquals( ing1, ingredients.get( 0 ) );
        Assertions.assertEquals( ing2, ingredients.get( 1 ) );
        Assertions.assertEquals( ing3, ingredients.get( 2 ) );

        // Remove an ingredient from the list
        ingredients.remove( ing2 );

        // Ensure the right ingredient is removed
        Assertions.assertEquals( 2, ingredients.size() );
        Assertions.assertEquals( ing1, ingredients.get( 0 ) );
        Assertions.assertEquals( ing3, ingredients.get( 1 ) );
    }

    @Test
    @Transactional
    public void testRemoveAllIngredients () {

        // Add first ingredient
        final Ingredient ing1 = new Ingredient();
        ing1.setName( "ingredient 01" );
        service.save( ing1 );

        // Add second ingredient
        final Ingredient ing2 = new Ingredient();
        ing2.setName( "ingredient 02" );
        service.save( ing2 );

        // Add third ingredient
        final Ingredient ing3 = new Ingredient();
        ing3.setName( "ingredient 03" );
        service.save( ing3 );

        // Should be 3 ingredients saved
        List<Ingredient> ingredients = service.findAll();
        Assertions.assertEquals( 3, ingredients.size() );

        // Remove all ingredients from the list
        service.deleteAll();
        ingredients = service.findAll();

        // Ensure the right ingredient is removed
        Assertions.assertEquals( 0, ingredients.size() );
        Assertions.assertEquals( 0, service.count() );
    }

    @Test
    @Transactional
    public void testEditIngredient () {
        Assertions.assertEquals( 0, service.findAll().size() );

        // Add ingredient
        final Ingredient ing1 = new Ingredient( "coffee", 1 );
        service.save( ing1 );

        // Ensure that the ingredient has the correct name
        Assertions.assertEquals( "coffee", ing1.getName() );
        Assertions.assertEquals( 1, ing1.getAmount() );

        // Edit name of ingredient
        ing1.setName( "milk" );
        ing1.setAmount( 2 );
        service.save( ing1 );

        // Ensure that the ingredient has the correct changed name
        Assertions.assertEquals( "milk", ing1.getName() );
        Assertions.assertEquals( 2, ing1.getAmount() );

        // Ensure ingredient was updated and not posted twice
        Assertions.assertEquals( 1, service.count() );
    }

    @Test
    @Transactional
    public void testToString () {
        Assertions.assertEquals( 0, service.findAll().size() );

        // Add ingredient
        final Ingredient ing1 = new Ingredient( "coffee", 1 );
        service.save( ing1 );

        // Ensure that the ingredient has the correct name
        Assertions.assertEquals( "coffee", ing1.getName() );
        Assertions.assertEquals( 1, ing1.getAmount() );

        final long id = ing1.getId();

        Assertions.assertEquals( "Ingredient [id=" + id + ", name=coffee]", ing1.toString() );
    }
}

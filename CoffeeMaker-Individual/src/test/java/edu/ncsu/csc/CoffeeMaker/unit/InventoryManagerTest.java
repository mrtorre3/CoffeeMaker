package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.forms.InventoryManagerForm;
import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.InventoryManager;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Test class for the InventoryManager class
 *
 * @author mrtorre3
 */
public class InventoryManagerTest {

    /** inventoryManager variable */
    @Autowired
    private InventoryManager inventoryManager;

    /** variable for the userform */
    @Autowired
    private final UserForm   uf = new UserForm( "coffeeluver", "p@ss", Role.ROLE_INVENTORYMANAGER, 1 );

    /**
     * Sets up the variables before each test
     */
    @BeforeEach
    public void setup () {
        inventoryManager = new InventoryManager( uf );
    }

    /**
     * Tests the inventoryManager class and its methods
     */
    @Test
    @Transactional
    public void testInventoryManager () {
        // assert that customer name is not changed
        assertEquals( "coffeeluver", inventoryManager.getUsername() );
        // create new customer to update
        final InventoryManager cust = new InventoryManager( uf );
        cust.setUsername( "luvercoffee" );
        cust.setPassword( "p@ss" );
        cust.setEnabled( 1 );
        final InventoryManagerForm newUF = new InventoryManagerForm( cust );
        inventoryManager.update( newUF );
        // assert customer name is changed
        assertEquals( "luvercoffee", inventoryManager.getUsername() );
        // test error path by trying to add customer to a inventoryManager role
        final UserForm sf = new UserForm();
        sf.setUsername( "bill.custo" );
        sf.setPassword( "p@ss" );
        sf.addRole( "Customer" );
        assertThrows( IllegalArgumentException.class, () -> {
            new InventoryManager( sf );
        }, "Attempted to create a customer record for a non-customer user!" );
        // testing toString
        assertEquals( "luvercoffee", inventoryManager.toString() );

    }
}

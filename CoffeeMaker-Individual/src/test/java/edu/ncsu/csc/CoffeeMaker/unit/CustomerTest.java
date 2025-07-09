package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.DrinkOrder;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Test class for the Customer class
 *
 * @author mrtorre3
 */
public class CustomerTest {
    /** customer variable */
    @Autowired
    private Customer       customer;

    /** variable for the userform */
    @Autowired
    private final UserForm uf = new UserForm( "bill.custo", "p@ss", Role.ROLE_CUSTOMER, 1 );

    /**
     * Sets up the variables before each test
     */
    @BeforeEach
    public void setup () {
        customer = new Customer( uf );
    }

    /**
     * Tests the customer class and its methods
     */
    @Test
    @Transactional
    public void testCustomer () {
        // assert that customer name is not changed
        assertEquals( "bill.custo", customer.getUsername() );

        // test error path by trying to add customer to a staff role
        final UserForm sf = new UserForm();
        sf.setUsername( "coffeluver" );
        sf.setPassword( "p@ss" );
        sf.addRole( "Staff" );
        assertThrows( IllegalArgumentException.class, () -> {
            new Customer( sf );
        }, "Attempted to create a staff record for a non-staff user!" );

        final UserForm pf = new UserForm();
        pf.setUsername( "coffeluver" );
        pf.setPassword( "p@ss" );
        pf.addRole( "pork" );
        assertThrows( IllegalArgumentException.class, () -> {
            new Customer( pf );
        }, "Attempted to assign incorrect role to customer!" );

    }

    /**
     * tests ordering of drinks for this object
     */
    @Test
    @Transactional
    public void testDrinkOrderForCustomer () {

        assertThrows( IllegalArgumentException.class, () -> {
            customer.updateDrinkOrder( null );
        }, "list is empty" );

        final DrinkOrder DO = new DrinkOrder();
        DO.setAmountPaid( 10 );
        DO.setCustomerName( customer.getUsername() );
        DO.setId( (long) 1 );
        DO.setRecipeName( "Matcha" );

        customer.addDrinkOrder( DO );

        final DrinkOrder DDO = new DrinkOrder();
        DDO.setAmountPaid( 10 );
        DDO.setCustomerName( customer.getUsername() );
        DDO.setId( (long) 2 );
        DDO.setRecipeName( "Fasta" );

        customer.addDrinkOrder( DDO );

        assertEquals( DO.getId(), customer.getDrinkOrders().get( 0 ).getId() );

        customer.updateDrinkOrder( DDO );

        assertTrue( customer.getDrinkOrders().get( 1 ).isFilled() );

        customer.pickupDrinkOrder( DDO );

        assertEquals( customer.getDrinkOrders().size(), 1 );
    }
}

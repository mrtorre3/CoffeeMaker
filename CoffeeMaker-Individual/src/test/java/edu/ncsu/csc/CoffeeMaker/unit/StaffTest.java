package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.forms.StaffForm;
import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Test class for the Staff class
 *
 * @author mrtorre3
 */
public class StaffTest {

    /** staff variable */
    @Autowired
    private Staff          staff;

    /** variable for the userform */
    @Autowired
    private final UserForm uf = new UserForm( "coffeeluver", "p@ss", Role.ROLE_STAFF, 1 );

    /**
     * Sets up the variables before each test
     */
    @BeforeEach
    public void setup () {
        staff = new Staff( uf );
    }

    /**
     * Tests the staff class and its methods
     */
    @Test
    @Transactional
    public void testStaff () {
        // assert that customer name is not changed
        assertEquals( "coffeeluver", staff.getUsername() );
        // create new customer to update
        final Staff cust = new Staff( uf );
        cust.setUsername( "luvercoffee" );
        cust.setPassword( "p@ss" );
        cust.setEnabled( 1 );
        final StaffForm newUF = new StaffForm( cust );
        staff.update( newUF );
        // assert customer name is changed
        assertEquals( "luvercoffee", staff.getUsername() );
        // test error path by trying to add customer to a staff role
        final UserForm sf = new UserForm();
        sf.setUsername( "bill.custo" );
        sf.setPassword( "p@ss" );
        sf.addRole( "Customer" );
        assertThrows( IllegalArgumentException.class, () -> {
            new Staff( sf );
        }, "Attempted to create a customer record for a non-customer user!" );
        // testing toString
        assertEquals( "luvercoffee", staff.toString() );

    }
}

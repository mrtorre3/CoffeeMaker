/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Tests the methods of the User class which is associated with the User Roles
 * requirement.
 *
 * @author zebrentz
 * @author atcreech
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class UserTest {

    private Role customerRole;

    private Role staffRole;

    private User customerUser;

    private User staffUser;

    @BeforeEach
    void setUp () {

        customerRole = Role.ROLE_CUSTOMER;
        staffRole = Role.ROLE_STAFF;

        customerUser = new User();
        staffUser = new User();
    }

    /**
     * Test method for setting and getting the name of the User.
     */
    @Test
    @Transactional
    void testUserName () {

        assertDoesNotThrow( () -> new User( "name", "password", customerRole, 1 ) );
        assertDoesNotThrow( () -> new User( "name", "password", staffRole, 1 ) );

        // Test that the user's name is null before it is set.
        assertNull( customerUser.getUsername() );
        assertNull( staffUser.getUsername() );
        assertNull( customerUser.getUsername() );
        assertNull( staffUser.getUsername() );

        // Change the user's name and test that it is set correctly.
        customerUser.setUsername( "bill.custo" );
        assertEquals( "bill.custo", customerUser.getUsername() );
        assertEquals( "bill.custo", customerUser.getUsername() );
        staffUser.setUsername( "coffeeluver" );
        assertEquals( "coffeeluver", staffUser.getUsername() );
        assertEquals( "coffeeluver", staffUser.getUsername() );
    }

    /**
     * Test method for setting and getting the password of the User.
     */
    @Test
    @Transactional
    void testUserPass () {

        // Test that the user's pass is null before it is set.
        assertNull( customerUser.getPassword() );
        assertNull( staffUser.getPassword() );

        // Change the user's pass and test that it is set correctly.
        customerUser.setPassword( "p@ss" );
        assertEquals( "p@ss", customerUser.getPassword() );
        staffUser.setPassword( "w0rd" );
        assertEquals( "w0rd", staffUser.getPassword() );
    }

    /**
     * Test method for checking if the User is logged in.
     */
    @Test
    @Transactional
    void testEnabled () {

        // Test that the user is not logged in at first.
        assertEquals( 0, customerUser.getEnabled() );
        assertEquals( 0, staffUser.getEnabled() );

        // Set the user to enabled and check that it worked.
        customerUser.setEnabled( 1 );
        assertEquals( 1, customerUser.getEnabled() );
        staffUser.setEnabled( 1 );
        assertEquals( 1, staffUser.getEnabled() );
    }

    /**
     * Test method for getting and setting the Roles of each user.
     */
    @Test
    @Transactional
    void testUserRoles () {

        // Test that the user does not have a role at first.
        assertEquals( 0, customerUser.getRoles().size() );
        assertEquals( 0, staffUser.getRoles().size() );

        // Give each user one role and check their list of roles to make
        // sure it was added correctly.
        customerUser.addRole( customerRole );
        assertTrue( customerUser.getRoles().contains( customerRole ) );
        assertFalse( customerUser.getRoles().contains( staffRole ) );
        assertEquals( 1, customerUser.getRoles().size() );
        staffUser.addRole( staffRole );
        assertTrue( staffUser.getRoles().contains( staffRole ) );
        assertFalse( staffUser.getRoles().contains( customerRole ) );
        assertEquals( 1, staffUser.getRoles().size() );

        assertThrows( IllegalArgumentException.class, () -> customerUser.addRole( staffRole ) );
        assertThrows( IllegalArgumentException.class, () -> staffUser.addRole( customerRole ) );

        final HashSet<Role> roles = new HashSet<Role>();
        roles.add( customerRole );

        staffUser.setRoles( roles );
        assertTrue( staffUser.getRoles().contains( customerRole ) );
        assertFalse( staffUser.getRoles().contains( staffRole ) );
        assertEquals( 1, staffUser.getRoles().size() );

        roles.add( staffRole );

        assertThrows( IllegalArgumentException.class, () -> customerUser.setRoles( roles ) );

        /*
         * customerUser.setId( 99999 ); assertEquals( 99999,
         * customerUser.getId() ); assertNull( customerUser.getAuthorities() );
         * assertNull( staffUser.getAuthorities() ); assertTrue(
         * customerUser.isAccountNonExpired() ); assertTrue(
         * staffUser.isAccountNonExpired() ); assertTrue(
         * customerUser.isAccountNonLocked() ); assertTrue(
         * staffUser.isAccountNonLocked() ); assertTrue(
         * customerUser.isCredentialsNonExpired() ); assertTrue(
         * staffUser.isCredentialsNonExpired() ); // Try to add another role to
         * each user and make sure an exception is // thrown.
         * customerRole.setId( 9999 ); assertEquals( 9999, customerRole.getId()
         * );
         */

        assertNull( customerUser.getUsername() );
        assertNull( staffUser.getUsername() );

        assertEquals( Role.ROLE_CUSTOMER, customerRole );
        assertEquals( Role.ROLE_STAFF, staffRole );

        // final User test = new User();

    }

    /**
     * Test method for checking if Users are equal.
     */
    @Test
    @Transactional
    void testUserEquals () {

        customerUser.setUsername( "bill.custo" );
        staffUser.setUsername( "coffeeluver" );

        assertTrue( customerUser.equals( customerUser ) );
        assertTrue( staffUser.equals( staffUser ) );
        assertFalse( customerUser.equals( staffUser ) );
        assertFalse( staffUser.equals( customerUser ) );

        assertFalse( customerUser.equals( null ) );
        assertFalse( staffUser.equals( null ) );

        assertFalse( customerUser.equals( customerRole ) );
        assertFalse( staffUser.equals( staffRole ) );

        assertTrue( customerUser.hashCode() == customerUser.hashCode() );
    }

}

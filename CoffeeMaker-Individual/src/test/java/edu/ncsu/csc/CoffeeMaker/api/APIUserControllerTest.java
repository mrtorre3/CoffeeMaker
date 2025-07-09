package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.controllers.APIUserController;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * @author brent
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIUserControllerTest {

    @Autowired
    private MockMvc           mvc;

    @Autowired
    private APIUserController uController;

    @Autowired
    private UserService<User> service;

    /**
     * Sets up the tests.
     *
     * @throws Exception
     */
    @BeforeEach
    void setUp () throws Exception {
        service.deleteAll();
    }

    /**
     * Test method for getUsers.
     */
    @Test
    @Transactional
    void testGetUsers () {

        // Check that the list of users is empty.
        assertEquals( "[]", uController.getUsers().toString() );

        // Update the list of users.
        service.save( new User( "userOne", "p@ss", Role.ROLE_CUSTOMER, 0 ) );
        service.save( new User( "userTwo", "w0rd", Role.ROLE_STAFF, 0 ) );
        service.save( new User( "userThree", "p@ssw0rd", Role.ROLE_CUSTOMER, 1 ) );

        // Saves to database in alphabetical order.
        assertEquals( 3, uController.getUsers().size() );
        assertEquals( "userOne", uController.getUsers().get( 0 ).getUsername() );
        assertEquals( "userThree", uController.getUsers().get( 1 ).getUsername() );
        assertEquals( "userTwo", uController.getUsers().get( 2 ).getUsername() );
    }

    /**
     * Test method for getUser when the user is a Staff member.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "STAFF" } )
    void testGetUserStaff () throws Exception {

        // Test that the database returns the username of the currently logged
        // in user.
        assertEquals( "{\"status\":\"success\",\"message\":\"admin\"}", uController.getUser().getBody().toString() );
        assertEquals( 200, uController.getUser().getStatusCodeValue() );

        // Check that the request was successful.
        mvc.perform( MockMvcRequestBuilders.get( "/api/v1/user" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.status().isOk() );
    }

    /**
     * Test method for getUser when the user is a Customer.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "buyer", roles = { "CUSTOMER" } )
    void testGetUserCustomer () throws Exception {

        // Test that the database returns the username of the currently logged
        // in user.
        assertEquals( "{\"status\":\"success\",\"message\":\"buyer\"}", uController.getUser().getBody().toString() );
        assertEquals( 200, uController.getUser().getStatusCodeValue() );

        // Check that the request was successful.
        mvc.perform( MockMvcRequestBuilders.get( "/api/v1/user" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.status().isOk() );

    }

    /**
     * Test method for createUser.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void testCreateUser () throws Exception {

        // Check that the list of users is empty.
        assertEquals( 0, uController.getUsers().size() );

        // Create a JSON string for a valid Customer user.
        final String u1JSON = "{\"username\":\"userOne\",\"password\":\"p@ss\",\"role\":\"CUSTOMER\"}";

        // Test that the request is successful when creating the user.
        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( u1JSON ) ).andExpect( MockMvcResultMatchers.status().isOk() );
        assertEquals( 1, uController.getUsers().size() );

        // Create a JSON string for a valid Staff user.
        final String u2JSON = "{\"username\":\"userTwo\",\"password\":\"w0rd\",\"role\":\"STAFF\"}";

        // Test that the request is successful when creating the user.
        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( u2JSON ) ).andExpect( MockMvcResultMatchers.status().isOk() );
        assertEquals( 2, uController.getUsers().size() );

        // Check that calling the method directly also works.
        service.deleteAll();
        uController.createUser( u1JSON );
        assertEquals( 1, uController.getUsers().size() );
        assertEquals( "[ROLE_CUSTOMER]", uController.getUsers().get( 0 ).getRoles().toString() );

        uController.createUser( u2JSON );
        assertEquals( 2, uController.getUsers().size() );
        assertEquals( "[ROLE_STAFF]", uController.getUsers().get( 1 ).getRoles().toString() );

        // Try to create a user with the same username as an existing user.
        final String u2Dup = "{\"username\":\"userOne\",\"password\":\"p@ssw0rd\",\"role\":\"CUSTOMER\"}";

        // Test that the request returns a conflict error when creating the
        // user.
        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( u2Dup ) ).andExpect( MockMvcResultMatchers.status().isConflict() );
        assertEquals( 2, uController.getUsers().size() );

        // Try to create a user with an invalid role.
        final String u2WeirdRole = "{\"username\":\"userThree\",\"password\":\"w0rdp@ss\",\"role\":\"WEIRDROLE\"}";

        // Test that trying to create the user throws an exception.
        assertThrows( IllegalArgumentException.class, () -> uController.createUser( u2WeirdRole ) );
        assertEquals( 2, uController.getUsers().size() );
    }

    /**
     * Test method for generateUsers.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "STAFF" } )
    void testDeleteUser () throws Exception {

        final User user = new User( "user", "drowssap", Role.ROLE_CUSTOMER, 0 );
        service.save( user );

        assertEquals( 1, uController.getUsers().size() );

        uController.deleteUser( "user" );
        assertEquals( 0, uController.getUsers().size() );
    }
}

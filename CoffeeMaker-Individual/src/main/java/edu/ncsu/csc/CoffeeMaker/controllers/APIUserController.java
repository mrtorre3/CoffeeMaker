package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.InventoryManager;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Class that provides multiple API endpoints for interacting with the Users
 * model.
 *
 * @author Kai Presler-Marshall
 * @author Lauren Murillo
 * @author Alex Bowen
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIUserController extends APIController {

    /** User service */
    @Autowired
    private UserService userService;

    /**
     * Retrieves and returns a list of all Users in the system, regardless of
     * their classification (including all Patients, all Personnel, and all
     * users who do not have a further status specified)
     *
     * @return list of users
     */
    @GetMapping ( BASE_PATH + "/users" )
    public List<User> getUsers () {
        return userService.findAll();
    }

    /**
     * Retrieves and returns the user with the username provided
     *
     * @param id
     *            The username of the user to be retrieved
     * @return response
     */
    @GetMapping ( BASE_PATH + "/user" )
    public ResponseEntity getUser () {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity( successResponse( authentication.getName() ), HttpStatus.OK );
    }

    /**
     * Retrieves and returns the user with the username provided
     *
     * @param id
     *            The username of the user to be retrieved
     * @return response
     */
    @GetMapping ( BASE_PATH + "/user/{name}" )
    public ResponseEntity getUserByName ( @PathVariable final String name ) {
        return new ResponseEntity( userService.findByName( name ), HttpStatus.OK );
    }

    /**
     * Creates a new user from the RequestBody provided, validates it, and saves
     * it to the database.
     *
     * @param userF
     *            The user to be saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final String json ) {

        final JsonParser parse = new BasicJsonParser();

        final Map userBits = parse.parseMap( json );

        User u = null;

        try {

            final String username = (String) userBits.get( "username" );

            if ( null != userService.findByName( username ) ) {
                return new ResponseEntity( errorResponse( "User with the id " + username + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            final String password = (String) userBits.get( "password" );

            final String role = (String) userBits.get( "role" );

            if ( role.equalsIgnoreCase( "customer" ) ) {
                u = new Customer( new UserForm( username, password, Role.ROLE_CUSTOMER, 1 ) );
            }
            else if ( role.equalsIgnoreCase( "staff" ) ) {
                u = new Staff( new UserForm( username, password, Role.ROLE_STAFF, 1 ) );
            }
            else if ( role.equalsIgnoreCase( "inventorymanager" ) ) {
                u = new InventoryManager( new UserForm( username, password, Role.ROLE_INVENTORYMANAGER, 1 ) );
            }
            else {
                throw new IllegalArgumentException( "Invalid role for user" );
            }

            userService.save( u );
            return new ResponseEntity( successResponse( "user created" ), HttpStatus.OK );

        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid user json" );
        }
    }

    /**
     * Deletes the user with the id matching the given id. Requires staff
     * permissions.
     *
     * @param id
     *            the id of the user to delete
     * @return the id of the deleted user
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @DeleteMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity deleteUser ( @PathVariable final String id ) {
        final User user = userService.findByName( id );
        try {
            if ( null == user ) {
                return new ResponseEntity( errorResponse( "No user found for id " + id ), HttpStatus.NOT_FOUND );
            }
            userService.delete( user );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Could not delete " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}

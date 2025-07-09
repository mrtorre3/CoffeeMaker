package edu.ncsu.csc.CoffeeMaker.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Class that includes all methods for any user of CoffeeMaker, including
 * logging in, logging out, getting a list of roles, etc.
 *
 * @author Kai Presler-Marshall
 * @author mrtorre3
 * @author zebrentz
 */
@Entity
@JsonIgnoreProperties ( value = { "password" } )
public class User extends DomainObject {

    /** User's username */
    @Id
    private String    username;

    /** User's password */
    private String    password;

    /**
     * Whether or not the user is logged in.
     */
    private int       enabled;

    /**
     * The role of the user
     *
     * Credit to iTrust2v11 for this header
     */
    @ElementCollection ( targetClass = Role.class, fetch = FetchType.EAGER )
    @Enumerated ( EnumType.STRING )
    private Set<Role> roles = new HashSet<>();

    /**
     * All-argument constructor for User.
     *
     * @param username
     *            the user's username
     * @param password
     *            The _already encoded_ password of the user.
     * @param role
     *            the user's role
     * @param enabled
     *            true if the user is logged in, false if not
     */

    public User ( final String username, final String password, final Role role, final int enabled ) {
        setUsername( username );
        setPassword( password );
        addRole( role );
        setEnabled( enabled );
    }

    // do not remove this. this is here to please the spring gods.
    public User () {

    }

    /**
     * Create a new user based off of the UserForm
     *
     * @param form
     *            the filled-in user form with user information
     */
    protected User ( final UserForm form ) {
        setUsername( form.getUsername() );
        if ( !form.getPassword().equals( form.getPassword2() ) ) {
            throw new IllegalArgumentException( "Passwords do not match!" );
        }
        final PasswordEncoder pe = new BCryptPasswordEncoder();
        setPassword( pe.encode( form.getPassword() ) );
        setEnabled( null != form.getEnabled() ? 1 : 0 );
        setRoles( form.getRoles().stream().map( Role::valueOf ).collect( Collectors.toSet() ) );

    }

    public void addRole ( final Role role ) {

        // If the user already has an assigned role, throw an exception.
        if ( this.roles.size() != 0 ) {
            throw new IllegalArgumentException( "User can only have one role." );
        }

        // Add the role to the user.
        this.roles.add( role );
    }

    /**
     * Gets the user's username.
     *
     * @return the user's name
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets the user's username to the given username.
     *
     * @param username
     *            the username to set
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets the user's password to the given password.
     *
     * @param password
     *            the password to set
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Returns whether or not the user is logged in.
     *
     * @return enabled
     */
    public Integer getEnabled () {
        return enabled;
    }

    /**
     * Sets whether the user is logged in.
     *
     * @param enabled
     */
    public void setEnabled ( final Integer enabled ) {
        this.enabled = enabled;
    }

    /**
     * Gets the collection of roles for the user.
     *
     * @return the collection of the user's roles
     */
    public Set<Role> getRoles () {
        return roles;
    }

    /**
     * This method sets the roles
     *
     * @param roles
     *            the list of roles for the user
     */
    public void setRoles ( final Set<Role> roles ) {

        // Check that the list of roles does not exceed one user role.
        if ( roles.size() > 1 ) {
            throw new IllegalArgumentException( "User can only have one role." );
        }

        // Set the list of roles for the user.
        this.roles = roles;
    }

    public void setRoles ( final Role role ) {
        this.roles.add( role );
    }

    /**
     * Hash code method for the User class.
     *
     * @return hashed Objects
     */
    @Override
    public int hashCode () {
        return Objects.hash( enabled, password, roles, username );
    }

    /**
     * Checks if two users are equal by checking their usernames.
     *
     * @param obj
     *            the object to check equality for
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals( username, other.username );
    }

    @Override
    public String getId () {
        return getUsername();
    }

}

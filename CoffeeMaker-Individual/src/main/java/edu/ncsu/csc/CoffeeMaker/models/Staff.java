package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.CoffeeMaker.forms.StaffForm;
import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Personnel class represents a User of the system who is not a Patient
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
public class Staff extends User {

    /**
     * For Hibernate
     */
    public Staff () {

    }

    /**
     * Constructor to build a Personnel out of a UserForm
     *
     * @param uf
     *            Form to build Personnel
     */
    public Staff ( final UserForm uf ) {
        super( uf );
        if ( !getRoles().contains( Role.ROLE_STAFF ) ) {
            throw new IllegalArgumentException( "Attempted to assign invalid role for Staff!" );
        }
    }

    /**
     * The username of the staff
     */
    @Length ( max = 20 )
    private String username;

    /**
     * Create a new personnel based off of the PersonnelForm
     *
     * @param form
     *            the filled-in personnel form with personnel information
     * @return `this` Personnel, after updating from form
     */
    public Staff update ( final StaffForm form ) {

        setUsername( form.getUsername() );

        return this;
    }

    /**
     * Retrieves the first name of this personnel
     *
     * @return the first name of this personnel
     */
    @Override
    public String getUsername () {
        return username;
    }

    /**
     * Set the first name of this personnel
     *
     * @param firstName
     *            the first name to set this personnel to
     */
    @Override
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * To string method
     *
     * @return string rep. of Personnel.
     */
    @Override
    public String toString () {
        final String s = this.username;
        return s;
    }

}

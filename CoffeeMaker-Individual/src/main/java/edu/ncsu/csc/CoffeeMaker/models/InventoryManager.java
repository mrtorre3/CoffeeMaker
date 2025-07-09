package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.CoffeeMaker.forms.InventoryManagerForm;
import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Personnel class represents a User of the system who is not a Patient
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
public class InventoryManager extends User {

    /**
     * For Hibernate
     */
    public InventoryManager () {

    }

    /**
     * Constructor to build a Personnel out of a UserForm
     *
     * @param uf
     *            Form to build Personnel
     */
    public InventoryManager ( final UserForm uf ) {
        super( uf );
        if ( !getRoles().contains( Role.ROLE_INVENTORYMANAGER ) ) {
            throw new IllegalArgumentException( "Attempted assign incorrect role to InventoryManager!" );
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
    public InventoryManager update ( final InventoryManagerForm form ) {

        setUsername( form.getUsername() );

        return this;
    }

    /**
     * Retrieves the first name of this InventoryManager
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

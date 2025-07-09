package edu.ncsu.csc.CoffeeMaker.forms;

import edu.ncsu.csc.CoffeeMaker.models.Staff;

/**
 * Form for registering a user as an iTrust2 personnel or for editing their
 * existing information. Used for all non-patient types of users
 *
 * @author Kai Presler-Marshall
 *
 */
public class StaffForm {

    /**
     * Username of the iTrust2 personnel to make a staff object for
     */
    private String username;

    /**
     * Creates a PersonnelForm object. For initializing a blank form
     */
    public StaffForm () {

    }

    /**
     * Populate the staff form from a staff object
     *
     * @param staff
     *            the staff object to set the form with
     */
    public StaffForm ( final Staff staff ) {
        if ( null == staff ) {
            return; /* Nothing to do here */
        }
        setUsername( staff.getUsername() );
    }

    /**
     * Get Username of the personnel
     *
     * @return The Personnel's username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set username of the Personenl
     *
     * @param username
     *            The personnel's username
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

}

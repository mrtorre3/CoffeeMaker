package edu.ncsu.csc.CoffeeMaker.forms;

import edu.ncsu.csc.CoffeeMaker.models.InventoryManager;

/**
 * Form for registering a user as a coffeemaker inventory manager or for editing
 * their existing information. Used for all non-patient types of users
 *
 * @author Kai Presler-Marshall
 *
 */
public class InventoryManagerForm {

    /**
     * Username of the coffeemaker inventory manager to make a inventoryManager
     * object for
     */
    private String username;

    /**
     * Creates a PersonnelForm object. For initializing a blank form
     */
    public InventoryManagerForm () {

    }

    /**
     * Populate the inventoryManager form from a inventoryManager object
     *
     * @param inventoryManager
     *            the inventoryManager object to set the form with
     */
    public InventoryManagerForm ( final InventoryManager inventoryManager ) {
        if ( null == inventoryManager ) {
            return; /* Nothing to do here */
        }
        setUsername( inventoryManager.getUsername() );
    }

    /**
     * Get Username of the inventory manager
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
     *            The inventory manager's username
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

}

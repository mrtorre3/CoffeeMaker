package edu.ncsu.csc.CoffeeMaker.forms;

import edu.ncsu.csc.CoffeeMaker.models.Customer;

/**
 * Form for user to fill out to add a customer to the system.
 *
 * @author Kai Presler-Marshall
 * @author Lauren Murillo
 *
 */
public class CustomerForm {

    /**
     * For Spring
     */
    public CustomerForm () {

    }

    /** Username of this customer **/
    private String username;

    /**
     * Populate the customer form from a customer object
     *
     * @param customer
     *            the customer object to set the form with
     */
    public CustomerForm ( final Customer customer ) {
        if ( null == customer ) {
            return; /* Nothing to do here */
        }
        setUsername( customer.getUsername() );
    }

    /**
     * Get the username of the customer
     *
     * @return the username of the customer
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the username of the customer
     *
     * @param username
     *            the username of the customer
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

}

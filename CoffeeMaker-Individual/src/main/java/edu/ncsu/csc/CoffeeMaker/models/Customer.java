package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import edu.ncsu.csc.CoffeeMaker.forms.UserForm;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Customer for coffeemaker
 *
 */
@Entity
public class Customer extends User {

    /**
     * The username of customer
     */
    private String           username;

    /**
     * the order list for this specific customer
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<DrinkOrder> customDrinkOrders;

    /**
     * For Hibernate
     */
    public Customer () {

    }

    /**
     * Creates a customer from the provided UserForm
     *
     * @param uf
     *            UserForm to build a customer from
     */
    public Customer ( final UserForm uf ) {
        super( uf );
        if ( !getRoles().contains( Role.ROLE_CUSTOMER ) ) {
            throw new IllegalArgumentException( "Attempted to assign incorrect role to customer!" );
        }
    }

    /**
     * adds a drink order to the list
     *
     * @param DO
     *            drink order to add
     */
    public void addDrinkOrder ( final DrinkOrder DO ) {
        if ( customDrinkOrders == null ) {
            customDrinkOrders = new ArrayList<DrinkOrder>();
        }
        customDrinkOrders.add( DO );
    }

    /**
     * marks order as fulfilled
     *
     * @param DO
     *            order to modify
     */
    public void updateDrinkOrder ( final DrinkOrder DO ) {
        if ( customDrinkOrders == null || customDrinkOrders.size() == 0 ) {
            throw new IllegalArgumentException( "list is empty" );
        }
        for ( int i = 0; i < customDrinkOrders.size(); i++ ) {
            if ( customDrinkOrders.get( i ).getId().toString().equals( DO.getId().toString() ) ) {
                customDrinkOrders.get( i ).markFilled( true );
                break;
            }
        }
    }

    public List<DrinkOrder> getDrinkOrders () {
        return this.customDrinkOrders;
    }

    /**
     * removes order due to pickup
     *
     * @param DO
     *            order to remove
     */
    public void pickupDrinkOrder ( final DrinkOrder DO ) {
        customDrinkOrders.remove( DO );
    }

    /**
     * Gets the username of this customer
     *
     * @return Username name
     */
    @Override
    public String getUsername () {
        return username;
    }

    /**
     * Sets the username of this customer
     *
     * @param Username
     *            New Username
     */
    @Override
    public void setUsername ( final String username ) {
        this.username = username;
    }

}

package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class that represents a customer order.
 *
 * @author zebrentz
 * @author mrtorre3
 */
@Entity
public class DrinkOrder extends DomainObject {

    private String  customerName;

    private String  recipeName;
    private int     amountPaid;
    private boolean isFilled;

    @Id
    @GeneratedValue
    private Long    id;

    public DrinkOrder ( final String customerName, final String recipeName, final int amountPaid ) {
        super();
        this.customerName = customerName;
        this.recipeName = recipeName;
        this.amountPaid = amountPaid;
        this.isFilled = false;
    }

    public DrinkOrder () {

    }

    /**
     *
     *
     * @return the customerName
     */
    public String getCustomerName () {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName ( final String customerName ) {
        this.customerName = customerName;
    }

    /**
     * @return the recipeName
     */
    public String getRecipeName () {
        return recipeName;
    }

    /**
     * @param recipeName
     *            the recipeName to set
     */
    public void setRecipeName ( final String recipeName ) {
        this.recipeName = recipeName;
    }

    /**
     * @return the amountPaid
     */
    public int getAmountPaid () {
        return amountPaid;
    }

    /**
     * @param amountPaid
     *            the amountPaid to set
     */
    public void setAmountPaid ( final int amountPaid ) {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the isFilled
     */
    public boolean isFilled () {
        return isFilled;
    }

    /**
     * @param isFilled
     *            the isFilled to set
     */
    public void markFilled ( final boolean isFilled ) {
        this.isFilled = isFilled;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        return "DrinkOrder [customerName=" + customerName + ", recipeName=" + recipeName + ", amountPaid=" + amountPaid
                + ", isFilled=" + isFilled + ", id=" + id + "]";
    }

    @Override
    public int hashCode () {
        return Objects.hash( amountPaid, customerName, id, isFilled, recipeName );
    }

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
        final DrinkOrder other = (DrinkOrder) obj;
        return amountPaid == other.amountPaid && Objects.equals( customerName, other.customerName )
                && Objects.equals( id, other.id ) && isFilled == other.isFilled
                && Objects.equals( recipeName, other.recipeName );
    }
}

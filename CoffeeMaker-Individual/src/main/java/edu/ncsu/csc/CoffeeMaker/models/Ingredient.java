package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * An ingredient for the CoffeeMaker. Each ingredient object is tied to a remote
 * database using Hibernate.
 *
 * @author atcreech
 * @author jdnguye4
 * @author zebrentz
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Name of the Ingredient */
    private String  name;

    /** The amount of the Ingredient */
    @Min ( 0 )
    private Integer amount;

    /**
     * Empty constructor
     */
    public Ingredient () {
        super();
    }

    /**
     * This constructor creates an Ingredient object with a given name
     *
     * @param name
     *            name of ingredient
     * @param amount
     *            amount of the ingredient
     */
    public Ingredient ( final String name, final Integer amount ) {
        super();
        this.name = name;
        this.amount = amount;
    }

    /**
     * Returns the name of the ingredient
     *
     * @return name of the ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the ingredient to a given name
     *
     * @param name
     *            - name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the amount of the ingredient
     *
     * @return amount of ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient
     *
     * @param amount
     *            - amount to set
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    /**
     * Returns the id of the ingredient
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of the ingredient.
     *
     * @param id
     *            - id to set
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * toString method for the Ingredient object
     *
     * @returns String representation of the Ingredient object
     */
    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", name=" + name + "]";
    }
}

package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** List of ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> customIngredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        customIngredients = new ArrayList<Ingredient>();
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return the id of the entry in the database
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the list of custom ingredients in the inventory
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return customIngredients;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        final boolean isEnough = true;
        boolean ingredientFound = false;
        for ( final Ingredient ingredient : r.getIngredients() ) {
            ingredientFound = false;
            for ( final Ingredient current : customIngredients ) {
                if ( current.getName().equalsIgnoreCase( ingredient.getName() ) ) {
                    if ( current.getAmount() < ingredient.getAmount() ) {
                        return false;
                    }
                    ingredientFound = true;
                }
            }
            if ( !ingredientFound ) {
                return false;
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final Ingredient ingredient : r.getIngredients() ) {
                for ( final Ingredient current : customIngredients ) {
                    if ( current.getName().equalsIgnoreCase( ingredient.getName() ) ) {
                        current.setAmount( current.getAmount() - ingredient.getAmount() );
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredient
     *            - Ingredient to add
     *
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final Ingredient ingredient ) {

        if ( ingredient.getAmount() < 0 ) {
            throw new IllegalArgumentException( "Must be a positive amount!" );
        }

        boolean ingredientFlag = true;
        for ( final Ingredient item : customIngredients ) {
            if ( item.getName().toString().toLowerCase() == ingredient.getName().toString().toLowerCase()
                    || item.getName().equals( ingredient.getName() ) ) {
                ingredientFlag = false;
                break;
            }
        }
        if ( ingredientFlag ) {
            customIngredients.add( ingredient );
            return true;
        }
        else {
            throw new IllegalArgumentException( "Duplicate ingredients are not allowed!" );
        }

    }

    /**
     * removes ingredient from the inventory
     *
     * @param ingredient
     *            - Ingredient to remove
     *
     * @return true if successful, false if not
     */
    public boolean removeIngredient ( final Ingredient ingredient ) {

        if ( !customIngredients.contains( ingredient ) ) {
            throw new IllegalArgumentException( "no such ingredient is present." );
        }

        customIngredients.remove( ingredient );

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient ingredient : customIngredients ) {
            buf.append( ingredient.getName() );
            buf.append( ": " );
            buf.append( ingredient.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }
}

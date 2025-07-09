package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /** List of ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> customIngredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.customIngredients = new ArrayList<Ingredient>();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        if ( customIngredients.size() == 0 ) {
            return true;
        }

        for ( final Ingredient ingredient : customIngredients ) {
            if ( ingredient.getAmount() > 0 ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Adds a single Ingredient to the list of ingredients.
     *
     * @param ingredient
     *            the ingredient to add
     * @return true if ingredient was added
     */
    public boolean addIngredient ( final Ingredient ingredient ) {
        return customIngredients.add( ingredient );
    }

    /**
     * Removes an Ingredient from the map of ingredients.
     *
     * @param ingredient
     *            - ingredient to remove
     * @return true if the ingredient was deleted
     */
    public boolean deleteIngredient ( final Ingredient ingredient ) {
        return customIngredients.remove( ingredient );
    }

    /**
     * Sets the amount of an ingredient used in a recipe to the specified
     * amount.
     *
     * @param ingredient
     *            - ingredient to modify
     * @param newIngredient
     *            - new ingredient
     */
    public void setIngredient ( final Ingredient ingredient, final Ingredient newIngredient ) {
        customIngredients.set( customIngredients.indexOf( ingredient ), newIngredient );
    }

    /**
     * Returns the list of ingredients for the given recipe.
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return customIngredients;
    }

    /**
     * Updates the recipe to match a given recipe
     *
     * @param r
     *            - recipe to match
     */
    public void updateRecipe ( final Recipe r ) {
        setName( r.getName() );
        setPrice( r.getPrice() );
        customIngredients = r.getIngredients();
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        String string = name + " [";
        for ( final Ingredient ingredient : customIngredients ) {
            string += " " + ingredient.getName().toString();
        }

        return string;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
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
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}

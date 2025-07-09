
package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the appropriate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "", "/" } )
    @PreAuthorize ( "hasAnyRole('ROLE_STAFF', 'ROLE_CUSTOMER', 'ROLE_INVENTORYMANAGER')" )
    public String index ( final Model model ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List< ? extends GrantedAuthority> auths = (List< ? extends GrantedAuthority>) authentication
                .getAuthorities();
        for ( int i = 0; i < auths.size(); i++ ) {
            if ( ( "ROLE_CUSTOMER" ).equalsIgnoreCase( auths.get( i ).getAuthority() ) ) {
                return "indexCustomer";
            }
            else if ( ( "ROLE_STAFF" ).equalsIgnoreCase( auths.get( i ).getAuthority() ) ) {
                return "indexStaff";
            }
            else if ( ( "ROLE_INVENTORYMANAGER" ).equalsIgnoreCase( auths.get( i ).getAuthority() ) ) {
                return "indexInventoryManager";
            }
        }
        return "error";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/addrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/addrecipe.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_STAFF')" )
    public String addRecipePage ( final Model model ) {
        return "addrecipe";
    }

    /**
     * On a GET request to /ingredients, the IngredientController will return
     * /src/main/resources/templates/addingredients.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/ingredients", "/addingredients.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_INVENTORYMANAGER')" )
    public String addIngredientPage ( final Model model ) {
        return "addingredients";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_STAFF')" )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_STAFF')" )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/addinventory.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_INVENTORYMANAGER')" )
    public String inventoryForm ( final Model model ) {
        return "addinventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     *
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    @PreAuthorize ( "hasAnyRole('ROLE_CUSTOMER')" )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * On a GET request to /vieworders, the ViewOrdersController will return
     * /src/main/resources/templates/viewOrdersCustomer.html or
     * /src/main/resources/templates/viewOrdersStaff.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/vieworders" } )
    @PreAuthorize ( "hasAnyRole('ROLE_STAFF', 'ROLE_CUSTOMER')" )
    public String viewOrders ( final Model model ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List< ? extends GrantedAuthority> auths = (List< ? extends GrantedAuthority>) authentication
                .getAuthorities();
        for ( int i = 0; i < auths.size(); i++ ) {
            if ( ( "ROLE_CUSTOMER" ).equalsIgnoreCase( auths.get( i ).getAuthority() ) ) {
                return "viewOrdersCustomer";
            }
            else if ( ( "ROLE_STAFF" ).equalsIgnoreCase( auths.get( i ).getAuthority() ) ) {
                return "viewOrdersStaff";
            }
        }
        return "error";
    }

    /**
     * On a GET request to /login, coffemaker will return
     * /src/main/resources/templates/login.html.
     *
     *
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/login", "/login.html" } )
    public String loginForm ( final Model model ) {
        return "login";
    }

    /**
     * On a GET request to /error, coffemaker will return
     * /src/main/resources/templates/error.html.
     *
     *
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/error", "/error.html" } )
    public String errorForm ( final Model model ) {
        return "error";
    }

    /**
     * On a GET request to /register, coffemaker will return
     * /src/main/resources/templates/register.html.
     *
     *
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @RequestMapping ( value = "/register" )
    public String registerForm ( final Model model ) {
        return "register";
    }
}

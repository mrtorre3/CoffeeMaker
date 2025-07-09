// package edu.ncsu.csc.CoffeeMaker.api;
//
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import javax.transaction.Transactional;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.MockMvc;
//
// import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
// import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
// import edu.ncsu.csc.CoffeeMaker.models.Inventory;
// import edu.ncsu.csc.CoffeeMaker.models.Recipe;
// import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
// import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
// import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
//
// @ExtendWith ( SpringExtension.class )
// @SpringBootTest
// @AutoConfigureMockMvc
// public class APICoffeeTest {
//
// @Autowired
// private MockMvc mvc;
//
// @Autowired
// private RecipeService service;
//
// @Autowired
// private InventoryService inventoryService;
//
// @Autowired
// private IngredientService ingredientService;
//
// /**
// * Sets up the tests.
// */
// @BeforeEach
// public void setup () {
// service.deleteAll();
// inventoryService.deleteAll();
//
// final Inventory ivt = inventoryService.getInventory();
//
// final Ingredient coffee = new Ingredient( "Coffee", 15 );
// ingredientService.save( coffee );
//
// final Ingredient milk = new Ingredient( "Milk", 15 );
// ingredientService.save( milk );
//
// final Ingredient sugar = new Ingredient( "Sugar", 15 );
// ingredientService.save( sugar );
//
// final Ingredient chocolate = new Ingredient( "Chocolate", 15 );
// ingredientService.save( chocolate );
//
// ivt.addIngredient( coffee );
// ivt.addIngredient( milk );
// ivt.addIngredient( sugar );
// ivt.addIngredient( chocolate );
//
// inventoryService.save( ivt );
//
// final Recipe recipe = new Recipe();
// recipe.setName( "Coffee" );
// recipe.setPrice( 50 );
// recipe.addIngredient( new Ingredient( "Coffee", 3 ) );
// recipe.addIngredient( new Ingredient( "Milk", 1 ) );
// recipe.addIngredient( new Ingredient( "Sugar", 1 ) );
// service.save( recipe );
// }
//
// @Test
// @Transactional
// public void testPurchaseBeverage1 () throws Exception {
// final String name = "Coffee";
//
// final Inventory ivt = inventoryService.getInventory();
//
// Assertions.assertEquals( 4, ivt.getIngredients().size() );
// Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 0 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 0 ).getAmount() );
// Assertions.assertEquals( "Milk", ivt.getIngredients().get( 1 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 1 ).getAmount() );
// Assertions.assertEquals( "Sugar", ivt.getIngredients().get( 2 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 2 ).getAmount() );
// Assertions.assertEquals( "Chocolate", ivt.getIngredients().get( 3 ).getName()
// );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 3 ).getAmount() );
//
// // try to purchase a beverage
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 60 ) ) ).andExpect(
// status().is2xxSuccessful() )
// .andExpect( jsonPath( "$.message" ).value( "10" ) ).andReturn();
//
// // check that the inventory was successfully updated
// Assertions.assertEquals( 4, ivt.getIngredients().size() );
// Assertions.assertEquals( 12, ivt.getIngredients().get( 0 ).getAmount() );
// Assertions.assertEquals( 14, ivt.getIngredients().get( 1 ).getAmount() );
// Assertions.assertEquals( 14, ivt.getIngredients().get( 2 ).getAmount() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 3 ).getAmount() );
//
// // try to purchase another beverage and make sure only one drink is
// // purchased
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 105 ) ) ).andExpect(
// status().is2xxSuccessful() )
// .andExpect( jsonPath( "$.message" ).value( "55" ) ).andReturn();
//
// // check that the inventory was successfully updated
// Assertions.assertEquals( 4, ivt.getIngredients().size() );
// Assertions.assertEquals( 9, ivt.getIngredients().get( 0 ).getAmount() );
// Assertions.assertEquals( 13, ivt.getIngredients().get( 1 ).getAmount() );
// Assertions.assertEquals( 13, ivt.getIngredients().get( 2 ).getAmount() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 3 ).getAmount() );
// }
//
// @Test
// @Transactional
// public void testPurchaseBeverage2 () throws Exception {
//
// /* Insufficient amount paid */
// final String name = "Coffee";
//
// // try to purchase a beverage without enough money
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 40 ) ) ).andExpect(
// status().is4xxClientError() )
// .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );
//
// final Inventory ivt = inventoryService.getInventory();
//
// // check that the inventory was not updated
// Assertions.assertEquals( 4, ivt.getIngredients().size() );
// Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 0 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 0 ).getAmount() );
// Assertions.assertEquals( "Milk", ivt.getIngredients().get( 1 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 1 ).getAmount() );
// Assertions.assertEquals( "Sugar", ivt.getIngredients().get( 2 ).getName() );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 2 ).getAmount() );
// Assertions.assertEquals( "Chocolate", ivt.getIngredients().get( 3 ).getName()
// );
// Assertions.assertEquals( 15, ivt.getIngredients().get( 3 ).getAmount() );
// }
//
// @Test
// @Transactional
// public void testPurchaseBeverage3 () throws Exception {
//
// /* Insufficient inventory */
// final Ingredient coffee =
// inventoryService.getInventory().getIngredients().get( 0 );
// final Ingredient chocolate =
// inventoryService.getInventory().getIngredients().get( 3 );
// inventoryService.deleteAll();
//
// Inventory ivt = inventoryService.getInventory();
// inventoryService.save( ivt );
//
// final String name = "Coffee";
//
// // try to purchase a beverage when there are no ingredients in inventory
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 50 ) ) ).andExpect(
// status().is4xxClientError() )
// .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );
//
// // check that the inventory still has no ingredients
// ivt = inventoryService.getInventory();
// Assertions.assertEquals( 0, ivt.getIngredients().size() );
//
// // add insufficient ingredients to inventory
// chocolate.setAmount( 2 );
// ivt.addIngredient( chocolate );
// coffee.setAmount( 5 );
// ivt.addIngredient( coffee );
// inventoryService.save( ivt );
//
// // try to purchase a beverage when there are not enough ingredients in
// // inventory
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 50 ) ) ).andExpect(
// status().is4xxClientError() )
// .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );
//
// // check that the inventory has only the insufficient ingredients that
// // were added
// ivt = inventoryService.getInventory();
// Assertions.assertEquals( 2, ivt.getIngredients().size() );
// Assertions.assertEquals( "Chocolate", ivt.getIngredients().get( 0 ).getName()
// );
// Assertions.assertEquals( 2, ivt.getIngredients().get( 0 ).getAmount() );
// Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 1 ).getName() );
// Assertions.assertEquals( 5, ivt.getIngredients().get( 1 ).getAmount() );
// }
//
// @Test
// @Transactional
// public void testPurchaseBeverage4 () throws Exception {
// /* Recipe does not exist */
// final String order = "{}";
//
// final String name = "DNE";
//
// // try to purchase a beverage with a name that is not in recipes
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
// ).contentType( MediaType.APPLICATION_JSON )
// .content( TestUtils.asJsonString( 50 ) ) ).andExpect(
// status().is4xxClientError() )
// .andExpect( jsonPath( "$.message" ).value( "No recipe selected" ) );
//
// // try to purchase a null beverage
// mvc.perform( post( String.format( "/api/v1/makecoffee/%s", (String) null ) )
// .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString(
// 50 ) ) )
// .andExpect( status().is4xxClientError() )
// .andExpect( jsonPath( "$.message" ).value( "No recipe selected" ) );
// }
// }

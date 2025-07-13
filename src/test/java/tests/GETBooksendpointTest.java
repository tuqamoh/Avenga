package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class GETBooksendpointTest extends POSTBooksEndpointTest {
	
	@Test(priority = 3, dependsOnMethods = {"tests.POSTBooksEndpointTest.createNewBookTest"}, description = "Happy Scenario: Retrieve book by ID and validate fields")
	public void getBookById() {

		Response response = Books.retrieveBookById(BookId);
		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "GET request failed");

		// Assert that Same BookId is retrieved in the response
		int actualId = response.jsonPath().getInt("id");
		System.out.println("Expected ID: " + BookId + " | Actual ID: " + actualId);
		Assert.assertEquals(actualId, BookId, "Book ID does not match expected value");
	}

	@Test(priority = 4, description = "Happy Scenario: Retrieve All Books in the database including the newly posted one")
	public void retrieveAllBooks() {

		Response response = Books.retrieveAllBooks();

		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "GET request failed");

		// Validate that response length has several values (>1), if not throw an error
		List<Object> books = response.jsonPath().getList("$");
		Assert.assertTrue(books.size() > 1, "Expected more than 1 book in the response, but found: " + books.size());

		// Retrieve List of Ids to validate that the posted ID is added to the list
		List<Integer> allIds = response.jsonPath().getList("id");

		// Assert the created ID exists in the list
		Assert.assertTrue(allIds.contains(BookId), "Created book ID not found in book list");

	}

}

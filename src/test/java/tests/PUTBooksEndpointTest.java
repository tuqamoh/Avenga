package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PUTBooksEndpointTest extends POSTBooksEndpointTest{
	
	@Test(priority = 5,dependsOnMethods = {"tests.POSTBooksEndpointTest.createNewBookTest"}, description = "Happy Scenario: Update Book by Id")
	public void updateBookById() {
		setScenario(2); // third object in the jsonFile - updated data
		Response response = Books.updateBook(BookId, payload);

		// retrieve jsonResponse data
		JsonPath actualJson = response.jsonPath();

		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "PUT request failed");

		// Assert that Same BookId is retrieved in the response
		int actualId = response.jsonPath().getInt("id");
		System.out.println("Expected ID: " + BookId + " | Actual ID: " + actualId);
		Assert.assertEquals(actualId, BookId, "Book ID does not match expected value");

		assertBookFieldsMatch(actualJson);

	}

}

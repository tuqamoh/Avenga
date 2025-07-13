package tests;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import main.BaseClass;
import main.CRUDBooksEndpoints;

public class POSTBooksEndpointTest extends BaseClass {

	// Initialize global data
	protected final CRUDBooksEndpoints Books = new CRUDBooksEndpoints();



	@Test(priority = 1, description = "Happy Scenario: Create a new book and validate response")
	public void createNewBookTest() {
		setScenario(0); // first Scenario in the jsonFile (Full JsonBody)
		// Execute POST Books Endpoint to create a new book with json request payload
		Response firstTestCaseResponse = Books.createNewBook(payload);

		// Assert that status code is success = 200
		Assert.assertEquals(firstTestCaseResponse.getStatusCode(), 200, "POST request failed");

		// Store BookId - will be needed in other endpoints
		JsonPath actualJson = firstTestCaseResponse.jsonPath();
		BookId = actualJson.getInt("id");

		System.out.println("Created Book ID: " + BookId);

		assertBookFieldsMatch(actualJson);
		
		// Test second scenario Create New book without Id or Title

		setScenario(1); // second Scenario in the jsonFile (without Id and Title)

		// Execute POST Books Endpoint to create a new book with json request payload
		Response secondTestCaseResponse = Books.createNewBook(payload);

		// Assert that status code is success = 200
		Assert.assertEquals(secondTestCaseResponse.getStatusCode(), 200, "POST request failed");

		// Assert that Same Id will be 0 by default if not passed
		int actualId = secondTestCaseResponse.jsonPath().getInt("id");
		Assert.assertEquals(actualId, 0, "Book ID does not match expected value");

		// Assert that Same Title will be null
		String actualTitle = secondTestCaseResponse.jsonPath().getString("title");
		Assert.assertEquals(actualTitle, null);

	}
	
	@Test(priority = 2, description = "Happy Scenario: Test second scenario Create New book without Id or Title")
	public void createNewBookTestWithoutIdOrTitle() {

		setScenario(1); // second Scenario in the jsonFile (without Id and Title)

		// Execute POST Books Endpoint to create a new book with json request payload
		Response secondTestCaseResponse = Books.createNewBook(payload);

		// Assert that status code is success = 200
		Assert.assertEquals(secondTestCaseResponse.getStatusCode(), 200, "POST request failed");

		// Assert that Same Id will be 0 by default if not passed
		int actualId = secondTestCaseResponse.jsonPath().getInt("id");
		Assert.assertEquals(actualId, 0, "Book ID does not match expected value");

		// Assert that Same Title will be null
		String actualTitle = secondTestCaseResponse.jsonPath().getString("title");
		Assert.assertEquals(actualTitle, null);

	}


	// Method to compare all fields from expected JSON to actual response
	public void assertBookFieldsMatch(JsonPath actualJson) {
		for (String field : new String[] { "title", "description", "pageCount", "excerpt", "publishDate" }) {
			Object expected = expectedJson.get(field);
			Object actual = actualJson.get(field);

			Assert.assertEquals(String.valueOf(actual), String.valueOf(expected), "Mismatch in field: " + field);
		}

	}

}

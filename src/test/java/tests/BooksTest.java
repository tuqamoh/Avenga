package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import main.CRUDBooksEndpoints;

public class BooksTest {

	// Initialize global data
	private static final String JSON_FILE_PATH = "BookData.json";
	private final CRUDBooksEndpoints Books = new CRUDBooksEndpoints();

	private List<Map<String, Object>> scenarios;
	private Map<String, Object> currentScenario;
	private String payload;
	private JsonPath expectedJson;
	private int BookId;

	// Create Faker instance
	Faker faker = new Faker();

	@BeforeTest
	public void init() throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
		JsonPath root = new JsonPath(json);
		scenarios = root.getList("scenarios");

		// Default to scenario 0
		setScenario(0);
	}

	// Switch scenario inside any test
	private void setScenario(int index) {
		currentScenario = scenarios.get(index);
		payload = new com.google.gson.Gson().toJson(currentScenario); // turn map into JSON
		expectedJson = new JsonPath(payload);
	}

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

		// -------------------------------------------------------------------------------------
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

	@Test(priority = 2, description = "Happy Scenario: Retrieve book by ID and validate fields")
	public void getBookById() {

		Response response = Books.retrieveBookById(BookId);
		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "GET request failed");

		// Assert that Same BookId is retrieved in the response
		int actualId = response.jsonPath().getInt("id");
		System.out.println("Expected ID: " + BookId + " | Actual ID: " + actualId);
		Assert.assertEquals(actualId, BookId, "Book ID does not match expected value");
	}

	@Test(priority = 3, description = "Happy Scenario: Retrieve All Books in the database including the newly posted one")
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

	@Test(priority = 4, description = "Happy Scenario: Update Book by Id")
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

	@Test(priority = 5, description = "Happy Scenario: Delete Book by Id")
	public void deleteBookById() {
		Response response = Books.DeleteBook(BookId);

		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "Delete request failed");

	}

	@Test(priority = 6, description = "Negative Scenarios")
	public void negativeScenarios() {

		// Scenario 1: invalid ID format
		setScenario(3); // loads invalid ID payload
		Response postResp = Books.createNewBook(payload);
		validateErrorResponse(postResp, 400, "One or more validation errors occurred.",
				"The JSON value could not be converted to System.Int32.", "'$.id'");

		Response putResp = Books.updateBook(BookId, payload);
		validateErrorResponse(putResp, 400, "One or more validation errors occurred.",
				"The JSON value could not be converted to System.Int32.", "'$.id'");

		// -------------------------------------------------------------------------------------
		// Scenario 2: invalid publishDate format
		setScenario(4);
		postResp = Books.createNewBook(payload);
		validateErrorResponse(postResp, 400, "One or more validation errors occurred.", 
				"The JSON value could not be converted to System.DateTime.", "'$.publishDate'");

		putResp = Books.updateBook(BookId, payload);
		validateErrorResponse(putResp, 400, "One or more validation errors occurred.", 
				"The JSON value could not be converted to System.DateTime.", "'$.publishDate'");

		// -------------------------------------------------------------------------------------
		// Scenario 3: empty request body
		String emptyPayload = "";
		postResp = Books.createNewBook(emptyPayload);
		validateErrorResponse(postResp, 400, "One or more validation errors occurred.", "A non-empty request body is required.", "''");

		putResp = Books.updateBook(BookId, emptyPayload);
		validateErrorResponse(putResp, 400, "One or more validation errors occurred.", "A non-empty request body is required.", "''");

		// -------------------------------------------------------------------------------------
		// Scenario 4: GET Books by Not Found Id

		// Generate random ID from Faker library
		int incorrectId = faker.number().numberBetween(500, 1000);

		Response NotFoundTestResponse = Books.retrieveBookById(incorrectId);
		// Assert that status code is Not Found = 404
		Assert.assertEquals(NotFoundTestResponse.getStatusCode(), 404, "Expected StatusCode is 404");

		// Assert on error message
		String NotFounderrorMessage = NotFoundTestResponse.jsonPath().getString("title");
		Assert.assertEquals(NotFounderrorMessage, "Not Found", "Error message mismatch");
		
		
		// -------------------------------------------------------------------------------------
		// Scenario 5: Delete Books without mandatory pathParam (Id)


		Response EmptyIdTestResponse = Books.DeleteBookNegativeScenario();
		// Assert that status code is Methon Not Allowed = 405
		Assert.assertEquals(EmptyIdTestResponse.getStatusCode(), 405, "Expected StatusCode is 405 Method Not Allowed");
		
		// -------------------------------------------------------------------------------------
		// Scenario 6: Update Books without mandatory pathParam (Id)

		setScenario(1);
		Response EmptyIdTestPUTResponse = Books.PUTBookNegativeScenario(payload);
		// Assert that status code is Methon Not Allowed = 405
		Assert.assertEquals(EmptyIdTestPUTResponse.getStatusCode(), 405, "Expected StatusCode is 405 Method Not Allowed");


	}

	// Method to compare all fields from expected JSON to actual response
	private void assertBookFieldsMatch(JsonPath actualJson) {
		for (String field : new String[] { "title", "description", "pageCount", "excerpt", "publishDate" }) {
			Object expected = expectedJson.get(field);
			Object actual = actualJson.get(field);

			Assert.assertEquals(String.valueOf(actual), String.valueOf(expected), "Mismatch in field: " + field);
		}

	}

	// common method for POST and PUT Scenarios As both have the same output;
	// supports dynamic error key
	private void validateErrorResponse(Response response, int expectedStatusCode, String expectedTitle,
			String expectedErrorMsgPart, String errorKey) {

		Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
		String actualTitle = response.jsonPath().getString("title");
		Assert.assertEquals(actualTitle, expectedTitle, "Error title mismatch");
		String errorMsg = response.jsonPath().getString("errors." + errorKey);
		Assert.assertTrue(errorMsg.contains(expectedErrorMsgPart), "Error message mismatch");
	}

}

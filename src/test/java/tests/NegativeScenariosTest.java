package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.response.Response;
import main.BaseClass;
import main.CRUDBooksEndpoints;

public class NegativeScenariosTest extends BaseClass {

	// Create Faker instance
	Faker faker = new Faker();
	// Initialize global data
	protected final CRUDBooksEndpoints Books = new CRUDBooksEndpoints();

	@Test(priority = 7, description = "Negative Scenarios")
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
		validateErrorResponse(postResp, 400, "One or more validation errors occurred.",
				"A non-empty request body is required.", "''");

		putResp = Books.updateBook(BookId, emptyPayload);
		validateErrorResponse(putResp, 400, "One or more validation errors occurred.",
				"A non-empty request body is required.", "''");

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
		Assert.assertEquals(EmptyIdTestPUTResponse.getStatusCode(), 405,
				"Expected StatusCode is 405 Method Not Allowed");

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

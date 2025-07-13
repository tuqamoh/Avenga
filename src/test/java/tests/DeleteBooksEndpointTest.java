package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class DeleteBooksEndpointTest extends POSTBooksEndpointTest {
	

	@Test(priority = 6,dependsOnMethods = {"tests.POSTBooksEndpointTest.createNewBookTest"},  description = "Happy Scenario: Delete Book by Id")
	public void deleteBookById() {
		//Step to insure the BookId is correct
		System.out.println("Required Book ID: " + BookId);
		
		Response response = Books.DeleteBook(BookId);

		// Assert that status code is success = 200
		Assert.assertEquals(response.getStatusCode(), 200, "Delete request failed");

	}

}

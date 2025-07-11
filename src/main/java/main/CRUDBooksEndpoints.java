package main;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CRUDBooksEndpoints {

	public Response createNewBook(String payload) {

		// Prepare request with headers and body
		RequestSpecification request = RestAssured.with().contentType(ContentType.JSON).accept(ContentType.JSON)
				.body(payload);

		// Send POST request and return response
		Response response = request.post(configurations.POST_New_Book);

		return response;

	}

	public Response retrieveAllBooks() {

		Response response = RestAssured.get(configurations.GET_All_Books);

		return response;

	}

	public Response retrieveBookById(int id) {

		RequestSpecification request = RestAssured.with().pathParam("id", id);

		Response response = request.get(configurations.GET_Book_ById);

		return response;

	}

	public Response updateBook(int id, String payload) {

		RequestSpecification request = RestAssured.with().pathParam("id", id).contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(payload);

		Response response = request.put(configurations.PUT_Book_ById);

		return response;

	}

	public Response DeleteBook(int id) {

		RequestSpecification request = RestAssured.with().pathParam("id", id);
		Response response = request.delete(configurations.DELETE_Book);

		return response;

	}

	public Response DeleteBookNegativeScenario() {

		RequestSpecification request = RestAssured.given();
		Response response = request.delete(configurations.Negative_Scenarios_URL);

		return response;

	}

	public Response PUTBookNegativeScenario(String payload) {

		RequestSpecification request = RestAssured.with().contentType(ContentType.JSON).accept(ContentType.JSON)
				.body(payload);
		Response response = request.put(configurations.Negative_Scenarios_URL);

		return response;
}
	
}

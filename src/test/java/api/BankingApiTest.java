package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BankingApiTest {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test(description = "Test Get User Profile")
    public void testGetUserProfile() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .when()
                .get("/users/1");

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code");
        Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
    }

    @Test(description = "Test Get Account Details")
    public void testGetAccountDetails() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .when()
                .get("/posts/1");

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code");
        Assert.assertTrue(response.getBody().asString().length() > 0, "Response should have data");
    }

    @Test(description = "Test Get User Transactions")
    public void testGetUserTransactions() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .queryParam("userId", 1)
                .when()
                .get("/posts");

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body Length: " + response.getBody().asString().length());

        Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code");
        Assert.assertTrue(response.getBody().asString().contains("["), "Should return array of transactions");
    }

    @Test(description = "Test Create User Post (Simulating Transfer Request)")
    public void testCreateTransferRequest() {
        String requestBody = "{\n" +
                "  \"title\": \"Transfer Request\",\n" +
                "  \"body\": \"Transfer amount 1000 to account 9876543210\",\n" +
                "  \"userId\": 1\n" +
                "}";

        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/posts");

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 201, "Should return 201 status code for successful creation");
    }

    @Test(description = "Test Get Account Balance")
    public void testGetAccountBalance() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .when()
                .get("/users/1");

        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code");

        String balance = response.jsonPath().getString("id");
        Assert.assertNotNull(balance, "Balance should be available in response");
    }

    @Test(description = "Test Invalid Request")
    public void testInvalidRequest() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .when()
                .get("/invalid-endpoint");

        System.out.println("Status Code: " + response.getStatusCode());
        // JSONPlaceholder returns 404 for invalid endpoints
        Assert.assertEquals(response.getStatusCode(), 404, "Should return 404 for invalid endpoint");
    }

    @Test(description = "Test Response Headers")
    public void testResponseHeaders() {
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .when()
                .get("/users/1");

        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code");
        Assert.assertNotNull(response.getHeader("Content-Type"), "Content-Type header should be present");
    }

    @Test(description = "Test Multiple User Requests")
    public void testMultipleUserRequests() {
        for (int i = 1; i <= 3; i++) {
            Response response = RestAssured
                    .given()
                    .header("Accept", "application/json")
                    .when()
                    .get("/users/" + i);

            System.out.println("User " + i + " Status Code: " + response.getStatusCode());
            Assert.assertEquals(response.getStatusCode(), 200, "Should return 200 status code for user " + i);
        }
    }
}

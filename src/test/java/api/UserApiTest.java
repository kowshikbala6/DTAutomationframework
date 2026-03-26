package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserApiTest {

    @Test

    public void getUsers() {

        // Use JSONPlaceholder - a reliable public API for testing
        Response res = RestAssured
                .given()
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0 (Windows NT) TestAutomationClient")
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts?userId=1");

        // Log body for easier debugging when things go wrong
        String body = res.getBody().asString();
        System.out.println("Response status: " + res.getStatusCode());
        System.out.println("Response body length: " + body.length());

        Assert.assertEquals(res.getStatusCode(),200, "Expected HTTP 200 from JSONPlaceholder but got " + res.getStatusCode());

    }

}
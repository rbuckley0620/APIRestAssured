package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.readConfig;

public class Authentication {
	String baseURI; 
	String authEndPoint; 
	String authBodyFilePath;
	String headerContentType;
	static long responseTime;
	public static String bearerToken;
	
	public Authentication() {
		baseURI = readConfig.getProperty("baseURI");
		authEndPoint = readConfig.getProperty("authEndPoint");
		authBodyFilePath = "src\\main\\java\\data\\authBody.json";
		headerContentType = readConfig.getProperty("contentType");
		
	}
	
	public boolean compareResponseTime() {
		boolean withinRange = false;
		if(responseTime <= 3000) {
			System.out.println("Response Time is within range.");
			withinRange= true;
		}else {
			System.out.println("Response Time is NOT within range.");
			withinRange = false;
		}
		
		return withinRange;
		
	}
	
	@Test
	public void generateBearerToken() {
		
	Response response =
		
	given()
		.baseUri(baseURI)
		.header("Content-Type",headerContentType)
		.body(new File(authBodyFilePath))
		.log().all().
	when()
		.post(authEndPoint).
	then()
		.log().all()
		.extract().response();
	
	int statusCode = response.getStatusCode();
	System.out.println("Status Code:" + statusCode);
	Assert.assertEquals(statusCode, 201,"Status codes are Not matching!");

	
	responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
	System.out.println("Response Time:" + responseTime);
	Assert.assertEquals(compareResponseTime(), true, "Times are NOT in expected range!");
	
//	String contentType = response.getHeader("Content_Type");
	String contentType = response.getContentType();
	System.out.println("Response Content Type" + contentType);
	Assert.assertEquals(contentType, headerContentType, "Content Types are NOT matching");
	
	String responseBody = response.getBody().asString();
	System.out.println("Response Body:" + responseBody);
	
	JsonPath jp = new JsonPath(responseBody);
	bearerToken = jp.get("access_token");
	System.out.println("access_token:" + bearerToken);
	
//	return bearerToken;
	
	}		

}

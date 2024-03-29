package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.readConfig;
import static io.restassured.RestAssured.*;


public class GetAllAccounts extends Authentication{
	String getAllAccountsEndPointFromConfig;
	String firstAccountId;
	
	
	public GetAllAccounts() {
		getAllAccountsEndPointFromConfig = readConfig.getProperty("getAllAccoutnsEndPoint");
	}
	
	@Test
	public void getAllAccounts() {
		
		Response response =
			given()
				.baseUri(baseURI)
				.header("Content-Type",headerContentType)
				.header("Authorization" , "Bearer " + generateBearerToken()).
			when()
				.get(getAllAccountsEndPointFromConfig).
			then()
				.log().all()
				.extract.response();
		
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:" + statusCode);
		Assert.assertEquals(statusCode, 200,"Status codes are Not matching!");
		
		
		
		String contentType = response.getContentType();
		System.out.println("Response Content Type" + contentType);
		Assert.assertEquals(contentType, headerContentType, "Content Types are NOT matching");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.get("records[0].account_id");
		System.out.println("First_account_id:" + firstAccountId);
		
		if(firstAccountId != null) {
			System.out.println("First account ID is NOT null");	
		}else {
			System.out.println("First account ID is null");
			
		}
		
	}

}

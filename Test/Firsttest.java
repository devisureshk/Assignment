package Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Firsttest {

	RequestSpecification httpRequest = RestAssured.given();
	Response response=null;
	JsonPath jsonResponseBody=null;
	String url="http://api.intigral-ott.net/popcorn-api-rs-7.9.10/v1";
	
	
	
	
	public int getNumofPromotions() {
		return response.jsonPath().getList("promotions").size();
	}
	public boolean isString(String s) {
		boolean numeric = false;
		try {
            Double num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            numeric = true;
        }
		return numeric;
	}
	public void validatePromotionId(ArrayList<String> val) {
		/*
		 * This function will check if any promotion ID is missing in any promotion
		 * and it will check if the promotion ID is blank
		 * 
		 * Assert promotionId: any string value,
		 */
		if( getNumofPromotions()==val.size()){
			for (int i=0;i<val.size();i++) {
				if (val.get(i).isBlank()) {
					Assert.fail("PromotionId is not avalaibale at "+(i+1)+"th Promotion");
				}
				else {
					if(!(isString(val.get(i)))){
						Assert.fail("The Promotion Id is not a string");
					}
				}
			}
		}
		else {
			Assert.fail("The number of promotion is not equal to number of promotionid");
		}
	}
	
	public void validateOrderId(ArrayList<Integer> val) {
		/*
		 * This function will check if any promotion ID is missing in any promotion
		 * and it will check if the promotion ID is blank
		 */
		if( getNumofPromotions()==val.size()){
			for (int i=0;i<val.size();i++) {
				if (val.get(i).toString().isBlank()) {
					Assert.fail("OrderID is not avalaibale at "+(i+1)+"th Promotion");
				}
			}
		}
		else {
			Assert.fail("The number of promotion is not equal to number of OrderId");
		}
	}
	public void validatePromoArea(ArrayList val) {
		/*
		 * This function will check if any PromoArea is missing in any promotion
		 * and it will check if the PromoArea is blank
		 */
		if( getNumofPromotions()==val.size()){
			for (int i=0;i<val.size();i++) {
				if (val.get(i).toString().isBlank()) {
					Assert.fail("PromoArea is not avalaibale at "+(i+1)+"th Promotion");
				}
			}
		}
		else {
			Assert.fail("The number of promotion is not equal to number of PromoArea");
		}
	}
	public void validatePromoType(ArrayList<String> val) {
		/*
		 * This function will check if any Promo Type is missing in any promotion
		 * and it will check if the Promo Type is blank
		 * 
		 * Assert programType: EPISODE or MOVIE or SERIES or SEASON 
		 */
		if( getNumofPromotions()==val.size()){
			for (int i=0;i<val.size();i++) {
				if (val.get(i).isBlank()) {
					Assert.fail("Promo Type is not avalaibale at "+(i+1)+"th Promotion");
				}
				
			}
			if(!(val.contains("EPISODE"))&& !(val.contains("SEASON")) && !(val.contains("SERIES")) && !(val.contains("MOVIE"))) {
				Assert.fail("Invalid Promo Type value");
			}
		}
		else {
			Assert.fail("The number of promotion is not equal to number of Promo Type");
		}
	}
	
	@Test
	public void validateResponse() {
		/*
		 * Solition for 1 and 2 option is given here
		 */
		ArrayList<String> promotions = response.path("promotions");
		ArrayList<String> promotionid = response.path("promotions.promotionId");
		ArrayList<Integer> orderId = response.path("promotions.orderId");
		ArrayList promoArea = response.path("promotions.promoArea");
		ArrayList<String> promoType = response.path("promotions.promoType");
		ArrayList<Boolean> showPrice = response.path("promotions.showPrice");
		ArrayList<Boolean> showText = response.path("promotions.showText");
		ArrayList<String> localizedTexts = response.path("promotions.localizedTexts");
		ArrayList<String> ar = response.path("promotions.localizedTexts.ar");
		ArrayList<String> en = response.path("promotions.localizedTexts.en");
		Assert.assertNotNull(promotions, "Promotions is Null");
		Assert.assertNotNull(promotionid, "Promotion id is Null");
		Assert.assertNotNull(orderId, "orderId is Null");
		Assert.assertNotNull(promoArea, "promoAreais Null");
		Assert.assertNotNull(promoType, "promoType is Null");
		Assert.assertNotNull(showPrice, "showPrice is Null");
		Assert.assertNotNull(showText, "showText is Null");
		Assert.assertNotNull(localizedTexts, "localizedTexts is Null");
		Assert.assertNotNull(ar, "localizedTexts.ar is Null");
		Assert.assertNotNull(en, "localizedTexts.en is Null");
		//System.out.println(localizedTexts);
		validatePromotionId(promotionid);
		validateOrderId(orderId);
		validatePromoArea(promoArea);
		validatePromoType(promoType);
		
	}
	@Test
	public void validateResonseCode() {
		int statuscode;
		if (null==response) {
			Assert.fail("Json Response is null");
		}
		else {
			statuscode=response.getStatusCode();
			Assert.assertEquals(statuscode, 200,"HTTP Status code should be 200");
			/*
			 * Test with invalid apikey
			 */
			RestAssured.baseURI=url;
			RequestSpecification httpRequest = RestAssured.given().queryParam("apikey","GDMSTGExy0sVDlZMzNDdUy");
			response = httpRequest.request(Method.GET, "/promotions");
			statuscode=response.getStatusCode();
			Assert.assertEquals(statuscode, 403,"HTTP Status code 403");
			String id = response.path("error.requestId");
			String message = response.path("error.message");
			String code = response.path("error.code");
			Assert.assertNotNull(id,"Request Id is NULL");
			Assert.assertNotNull(message,"Message is NULL");
			Assert.assertNotNull(code,"Code is NULL");
			
			
		}
		
	}
 
  @BeforeMethod
  public void beforeTest() {
	  System.out.println("Befor test");
	  RestAssured.baseURI=url;
		RequestSpecification httpRequest = RestAssured.given().queryParam("apikey","GDMSTGExy0sVDlZMzNDdUyZ");
		response = httpRequest.request(Method.GET, "/promotions");
		
		jsonResponseBody = response.jsonPath();
  }

  

}

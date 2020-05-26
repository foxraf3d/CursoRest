package Core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.After;
import org.junit.BeforeClass;

public class BaseTest {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;
    public static RequestSpecBuilder reqBuilder;
    public static ResponseSpecBuilder resBuilder;

    @BeforeClass
    public static void testInitialize(){
        RestAssured.baseURI="https://restapi.wcaquino.me";
        //RestAssured.port= 0;
        //RestAssured.basePath = "";

        reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();

        resBuilder = new ResponseSpecBuilder();
        //resBuilder.expectStatusCode(200);
        resSpec = resBuilder.build();

        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = resSpec;

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @After
    public void clearTests(){

    }

}

package Tests;

import Core.BaseTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.regex.Matcher;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnvioDadosTest extends BaseTest {

    @Test
    public void deveenviarVAlorViaQuery(){
        given()
                .log().all()
                .when()
                .get("/v2/users?format=json")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void deveenviarVAlorViaQueryViaParam(){
        given()
                .log().all()
                .queryParam("format", "xml")
                .when()
                .get("/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void deveenviarVAlorViaHeader(){
        given()
                .log().all()
                .accept(ContentType.XML)
                .when()
                .get("/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
        ;
    }

}

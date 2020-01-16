package Tests;

import Core.BaseTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OlaMundoTest extends BaseTest {

    @Test
    public void testOlaMundo(){
        Response response =  request(Method.GET, "ola");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        assertTrue(response.statusCode() == 200);

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured(){
        //get("http://restapi.wcaquino.me/ola").then().statusCode(200);*/
        given()
                //pré - condições
                .when()
                .get("/ola")
                .then()
                .statusCode(200);
    }

    @Test
    public void devoConhecerMatchersHamcrest(){
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d,isA(Double.class));
        assertThat(128d,greaterThan(120d));
        assertThat(128d,lessThan(130d));

        List<Integer> impares = Arrays.asList(1,3,5,7,9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1,3,5,7,9));
        assertThat(impares, containsInAnyOrder(1,3,5,9,7));
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1,5));

        assertThat("Maria", is(not("João")));
        assertThat("Maria", not("João"));
        assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"),containsString("qui")));

    }

    @Test
    public void devoValidarBody(){
        given()
                //pré - condições
                .when()
                .get("/ola")
                .then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(is(not(nullValue())));
    }


}

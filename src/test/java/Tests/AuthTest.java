package Tests;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTest {

    @Test
    public void deveAcessarPokeApi() {
        given()
                .log().all()
                .when()
                .get("https://pokeapi.co/api/v2/pokemon/charmander")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("charmander"))
        ;
    }

    @Test
    public void deveObterClima(){
        given()
                .log().all()
                .queryParam("q", "Sao Paulo,BR")
                .queryParam("appid", "9d258b05bff4d3a175380763804f3204")
                .queryParam("units", "metric")
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("São Paulo"))
                .body("coord.lon", is(-46.64f))
                .body("main.temp", greaterThan(10f))

        ;
    }

    @Test
    public void nãoDeveAcessarSemSenha(){
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(401)
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica(){
        given()
                .log().all()
                .when()
                .get("http://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaGiven(){
        given()
                .log().all()
                .auth().basic("admin","senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge(){
        given()
                .log().all()
                .auth().preemptive().basic("admin","senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){

        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "rafael_sistema27@hotmail.com");
        login.put("senha", "123456");

        //login na api
        //receber o token
        String token = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("http://barrigarest.wcaquino.me/signin")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("token")
                ;
        //Obter as contas
        given()
                .log().all()
                .header("Authorization", "JWT " + token)
                .when()
                .get("http://barrigarest.wcaquino.me/contas")
                .then()
                .log().all()
                .statusCode(200)
                .body("nome", hasItem("Conta de Teste"))
        ;
    }




    //9d258b05bff4d3a175380763804f3204
    //http://api.openweathermap.org/data/2.5/weather?q=Sao%20Paulo,BR&appid=9d258b05bff4d3a175380763804f3204&units=metric






}

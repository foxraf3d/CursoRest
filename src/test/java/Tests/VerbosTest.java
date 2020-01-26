package Tests;

import Core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerbosTest extends BaseTest {

    @Test
    public void deveSalvarUsuario(){
        given()
                .contentType("application/json")
                .body("{ \"name\":\"Jose\", \"age\":50 }")
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Jose"))
                .body("age", is(50))
        ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
                .contentType("application/json")
                .body("{ \"age\":50 }")
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;

    }


    @Test
    public void deveAlterarUsuario(){
        given()
                .contentType("application/json")
                .body("{ \"name\":\"Usuário Alterado\", \"age\":80 }")
                .when()
                .put("/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuário Alterado"))
                .body("age", is(80))
                .body("salary", is(1234.5678f))
        ;
    }



    @Test
    public void deveCustomizarURL(){
        given()
                .contentType("application/json")
                .body("{ \"name\":\"Usuário Alterado\", \"age\":80 }")
                .pathParam("entidade","users")
                .pathParam("userId","1")
                .when()
                .put("/{entidade}/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuário Alterado"))
                .body("age", is(80))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXML(){
        given()
                .contentType(ContentType.XML)
                .body("<user><name>Jose</name><age>50</age></user>")
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Jose"))
                .body("user.age", is("50"))
        ;
    }

    @Test
    public void deveRemoverUsuario(){
        given()
                .log().all()
                .when()
                .delete("/users/1")
                .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void naoDeveRemoverUsuarioInexistente(){
        given()
                .log().all()
                .when()
                .delete("/users/1000")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }



}

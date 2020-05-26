package Tests;

import Core.BaseTest;
import Entity.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
    public void deveSalvarUsuarioUsandoMAP(){
        /*Nota: Para Serialiar ou Deserializar usando MAP,
        é necessario utilizar o pacote GSON que se encontra
        no MavenRepository */
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuário via MAP");
        params.put("age", 25);

        given()
                .contentType("application/json")
                .body(params)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuário via MAP"))
                .body("age", is(25))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){
        /*Nota: Para Serialiar ou Deserializar usando Objeto,
        é necessario utilizar o pacote GSON que se encontra
        no MavenRepository */
        User user = new User("Usuario via Objeto", 25);

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via Objeto"))
                .body("age", is(25))
        ;
    }


    @Test
    public void deveDeserializarObjetoAoSalvarUsuario(){
        /*Nota: Para Serialiar ou Deserializar usando Objeto,
        é necessario utilizar o pacote GSON que se encontra
        no MavenRepository */
        User user = new User("Usuario deserializado", 25);

        User usuarioInserido =
                given()
                        .contentType("application/json")
                        .body(user)
                        .when()
                        .post("/users")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract().body().as(User.class)
                ;
        System.out.println(usuarioInserido);
        assertThat(usuarioInserido.getId(), notNullValue() );
        assertEquals("Usuario deserializado", usuarioInserido.getName());
        assertThat(usuarioInserido.getAge(), is(25));
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

    @Test
    public void deveSalvarUsuarioViaXMLUsandoObjeto(){
        User user = new User("Usuario XML", 40);
        given()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Usuario XML"))
                .body("user.age", is("40"))
        ;
    }

    @Test
    public void deveDeserializarXMLAoSalvarUsuario(){
        User user = new User("Usuario XML", 40);
        User usuarioInserido =
                given()
                        .contentType(ContentType.XML)
                        .body(user)
                        .when()
                        .post("/usersXML")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract().body().as(User.class)
                ;
        assertThat(usuarioInserido.getId(), notNullValue());
        assertThat(usuarioInserido.getName(), is("Usuario XML"));
        assertThat(usuarioInserido.getAge(), is(40));
        assertThat(usuarioInserido.getSalary(), nullValue());
    }



}

package Tests;

import Core.BaseTest;
import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserXMLTest extends BaseTest {



    @Test
    public void devoTrabalharComXML(){
        given()
                .when()
                .get("/usersXML/3")
                .then()
                .statusCode(200)

                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))

                .rootPath("user.filhos")
                .body("name.size()", is(2))

                .detachRootPath("filhos")
                .body("filhos.name[0]", is("Zezinho"))
                .body("filhos.name[1]", is("Luizinho"))

                .appendRootPath("filhos")
                .body("name", hasItem("Luizinho"))
                .body("name", hasItems("Luizinho", "Zezinho"))
        ;

    }

}

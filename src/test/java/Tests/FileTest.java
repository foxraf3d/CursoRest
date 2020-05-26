package Tests;

import Core.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTest extends BaseTest {

    @Test
    public void deveObrigarEnvioArquivo(){
        given()
                .log().all()
                .when()
                .post("/upload")
                .then()
                .log().all()
                .statusCode(404)
                .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadArquivo(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/img.jpg"))
                .when()
                .post("/upload")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("img.jpg"))
        ;
    }

    @Test
    public void naoDeveFazerUploadArquivoGrand(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/skywalker.jpg"))
                .when()
                .post("/upload")
                .then()
                .log().all()
                .time(lessThan(4000L))
                .statusCode(413)
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image =
                given()
                        .log().all()
                        .when()
                        .get("/download")
                        .then()
                        .statusCode(200)
                        .extract().asByteArray()
                ;
        File img = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(img);
        out.write(image);
        out.close();
        Assert.assertThat(img.length(), lessThan(1000000L));


    }

}

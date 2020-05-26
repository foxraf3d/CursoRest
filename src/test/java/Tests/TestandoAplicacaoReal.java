package Tests;

import Entity.Movimentacao;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestandoAplicacaoReal {

    String uri = "https://barrigarest.wcaquino.me";
    private String token;

//    @BeforeClass
//    public static void createToken(){
//        String TOKEN = given()
//                .contentType(ContentType.JSON)
//                .body(paramLogin())
//                .when()
//                .post("https://barrigarest.wcaquino.me/signin")
//                .then()
//                .log().all()
//                .statusCode(200).extract().path("token");
//        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
//    }

    @Test
    public void naoDeveAcessarSemToken(){
//        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
//        req.removeHeader("Authorization");
        given()
                .when()
                .post(uri+"/contas")
                .then()
                .statusCode(401)
        ;
    }

    @Test
    public void deveIncluirContaComSucesso(){
        //Autenticando
        autenticandoAplicacao();
        //Inserindo Conta
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .body(paramConta("Conta de Teste Seção 200"))
                .when()
                .post(uri + "/contas")
                .then()
                .statusCode(201)
                .body("nome", is("Conta de Teste Seção 200"))
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){
        autenticandoAplicacao();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .body(paramConta("Conta Alterada"))
                .pathParam("idConta", "158232")
                .when()
                .put(uri + "/contas/{idConta}")
                .then()
                .statusCode(200)
                .body("id", is(158232))
                .body("nome", is("Conta Alterada"))
        ;
    }

    @Test
    public void naoIncluirContaComNomeRepetido(){
        autenticandoAplicacao();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .body(paramConta("Conta Alterada"))
                .when()
                .post(uri + "/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void deveInserirMovimentacaoComSucesso(){
        autenticandoAplicacao();

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .header("Authorization", "JWT " + token)
                .body(paramMovimentacao())
                .when()
                .post(uri + "/transacoes")
                .then()
                .log().all()
                .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosDaMovimentacao(){
        autenticandoAplicacao();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .body("{}")
                .when()
                .post(uri + "/transacoes")
                .then()
                .statusCode(400)
                .body("$",hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório" ))


        ;
    }

    @Test
    public void naoDeveCadastrarMovimentacaoFutura() {
        autenticandoAplicacao();
        Movimentacao mov = paramMovimentacao();
        mov.setData_transacao("30/05/3000");
        mov.setData_pagamento("30/05/3000");
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .body(mov)
                .when()
                .post(uri + "/transacoes")
                .then()
                .statusCode(400)
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemovercontaComMovimentacao(){
        autenticandoAplicacao();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT " + token)
                .pathParam("idConta", "158232")
                .when()
                .delete(uri + "/contas/{idConta}")
                .then()
                .log().all()
                .statusCode(500)
                .body("name", is("error"))
                .body("detail", is("Key (id)=(158232) is still referenced from table \"transacoes\"."))
                .body("constraint", is("transacoes_conta_id_foreign"))

        ;
    }

    @Test
    public void deveCalcularSaldoDasContas(){
        autenticandoAplicacao();
        given()
                .header("Authorization", "JWT " + token)
                .when()
                .get(uri + "/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == 158232}.saldo", is("4500.00"))

        ;
    }

    @Test
    public void deveRemoverMovimentacao() {
        autenticandoAplicacao();
        given()
                .header("Authorization", "JWT " + token)
                .pathParam("id", 138324)
                .when()
                .delete(uri + "/transacoes/{id}")
                .then()
                .statusCode(204)
        ;
    }




    public String autenticandoAplicacao(){
        //Autenticando
        token = given()
                .contentType(ContentType.JSON)
                .body(paramLogin())
                .when()
                .post(uri + "/signin")
                .then()
                .log().all()
                .statusCode(200).extract().path("token")
        ;
        return token;
    }

    public static Map<String, String> paramLogin(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "rafael_sistema27@hotmail.com");
        login.put("senha", "123456");
        return login;
    }

    public Map<String, String> paramConta(String nome){
        Map<String, String> conta = new HashMap<String, String>();
        conta.put("nome", nome);
        return conta;
    }

    public Movimentacao paramMovimentacao(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(158232);
        mov.setDescricao("Descricao da Movimentação");
        mov.setEnvolvido("Envolvido na Mov");
        mov.setTipo("REC");
        mov.setData_transacao("24/05/2020");
        mov.setData_pagamento("24/05/2020");
        mov.setValor(1500f);
        mov.setStatus(true);
        return mov;
    }

}

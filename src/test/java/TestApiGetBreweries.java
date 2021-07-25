import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;

public class TestApiGetBreweries
{

    private static final Logger LOGGER = Logger.getLogger("InfoLogging");

    @Before
       public void setBaseUrl() {
       RestAssured.baseURI = "https://api.openbrewerydb.org/breweries";

    }

    @Test
    public void testApiBreweriesStatus200()
    {
        LOGGER.info("Se valida el eestado 200 del request");
        given().
                queryParam("query", "lagunitas")
                .get(baseURI + "/search")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testApiBreweriesValidationNameandState(){

       LOGGER.info("Se valida que la respuesta contenga Lagunitas Brewing Co y estado California");
       String body =  given().
                queryParam("query", "Lagunitas Brewing Co")
                .get(baseURI + "/search")
                .getBody()
                .asString();

       assertTrue(body.contains("Lagunitas Brewing Co"));
       assertTrue(body.contains("California"));

    }

    @Test
    public void testApiBreweriesValidationComplete(){

        LOGGER.info("Se valida el id, el nombre, la calle y el teléfono de la cervecería");
        given()
                .get(baseURI + "/12040")
                .then()
                .body("id", equalTo(12040))
                .body("name", equalTo("Lagunitas Brewing Co"))
                .body("street", equalTo("1280 N McDowell Blvd"))
                .body("phone", equalTo("7077694495"));
    }
}



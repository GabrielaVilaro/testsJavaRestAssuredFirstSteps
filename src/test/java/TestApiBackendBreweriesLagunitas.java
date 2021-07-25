import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestApiBackendBreweriesLagunitas {

    private static final Logger LOGGER = Logger.getLogger("InfoLogging");

    @Before
    public void setUpClass() {
        RestAssured.baseURI = "https://api.openbrewerydb.org/breweries";
    }

    @Test
    public void testApiBreweriesLagunitas()
    {
        LOGGER.info("Se busca por queryParam las que contengan lagunitas");
        Response response = given().
                queryParam("query", "lagunitas")
                .get(baseURI + "/autocomplete")
                .then().extract().response();

        LOGGER.info("Se filtra por nombre Lagunitas Brewing Co");
        JsonPath jsonPath = response.jsonPath();
        int count = jsonPath.getInt("name.size()");
        String name = "Lagunitas Brewing Co";

        for(int i = 0; i < count; i++)
        {
            String search = jsonPath.getString("name["+ i +"]");
            if(search.equalsIgnoreCase(name))
            {
                jsonPath.getString("name["+ i +"]");
            }
        }

        LOGGER.info("Se filtra por id, aquellas que contengan de estado California");
        Response response_state = given()
                .get(baseURI + "/12040")
                .then()
                .extract()
                .response();

        JsonPath jsonPath_state = response_state.jsonPath();
        int count_state = jsonPath.getInt("state.size()");
        String state = "California";

        for(int i = 0; i < count_state; i++)
        {
            String search = jsonPath_state.getString("state["+i+"]");
            if(search.equalsIgnoreCase(state))
            {
                LOGGER.info("Se validan los datos");
                jsonPath_state.getString("state["+i+"]");
                response_state
                        .then()
                        .body("id", equalTo(12040))
                        .body("name", equalTo("Lagunitas Brewing Co"))
                        .body("street", equalTo("1280 N McDowell Blvd"))
                        .body("phone", equalTo("7077694495"));
            }
        }
    }

    @After
    public void afterClass() {
        System.out.println("Fin del test");
    }
}

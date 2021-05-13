package TestCases;
import Payloads.createBoardPayload;
import Resources.createBoard;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;


public class counterTrelloTest {

	String trelloId = "";
	String key = "";
	String token = "";
	String updateBoardPath = "";



	// Open the environment properties file
	public Properties prop = new Properties();

	@BeforeTest
	public void getBaseUrl() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//Environment//enviro.properties");
		prop.load(fis);

		// Open the baseURL
		RestAssured.baseURI = prop.getProperty("BaseURL");
		 key = prop.getProperty("key");
		 token = prop.getProperty("token");


	}

	// This test is to ensure that create board, update board and delete board tests can be executed 5 times each.

	@Test
	public void recurringTest (){

		for(int i=0; i<5; i++){
			createBoard();
			updateBoard();
			vdeleteBoard();
		}

	}




   //Method to create Board
	public void createBoard() {

		String createBoardPath = createBoard.boardCreate() +"?"+"key=" + key + "&"+"token="+token;
		Response res = given().
				//Input the header
						header("Content-Type", "application/json").

				//Input the create board payload
						body(createBoardPayload.createBoardDetails()).
						when().


				//Input the createBoard path
						post( createBoard.boardCreate() +"?"+"key=" + key + "&"+"token="+token).

				//Run an assertion
						then().assertThat().statusCode(200).and().contentType(ContentType.JSON).

				//extract response of body
						extract().response();


		//Put response in a string
		String responseString = res.asString();


		//Convert id to json format
		JsonPath js = new JsonPath(responseString);
		trelloId = js.get("id");




	}

	//Method to update Board


	public void updateBoard() {

		updateBoardPath = createBoard.boardCreate() + trelloId+"?"+"key=" + key + "&"+"token="+token;


		Response res = given().
				//Input the header
						header("Content-Type", "application/json").

				//Input the updated body payload
						body(createBoardPayload.updateBoardDetails()).
						when().


				//Input the createBoard path
						put(updateBoardPath).

				//Run an assertion
						then().assertThat().statusCode(200).and().contentType(ContentType.JSON).

				//extract response of body
						extract().response();

	}


//Method to delete Board



	public void vdeleteBoard() {

		Response res = given().
				//Input the header
						header("Content-Type", "application/json").


						body("").
						when().

				//Input the update board path to be deleted
						delete(updateBoardPath).

				//Run an assertion
						then().assertThat().statusCode(200).and().contentType(ContentType.JSON).

				//extract response of body
						extract().response();

	}





}

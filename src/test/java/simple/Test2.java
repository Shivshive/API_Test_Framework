package simple;

import org.apache.commons.io.output.WriterOutputStream;
import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.xsom.impl.scd.Iterators.Map;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import pojo.UserData;
import reporter.ExtentManager;
import reporter.ExtentTestManager;
import reporter.TestListener;

import static org.hamcrest.Matchers.*;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
@Listeners(TestListener.class)
public class Test2 {

	final String schema_path = "C:\\Users\\Chandra-Kautilya\\Documents\\VSCODE\\_seleniumjs\\schema.json";
	final String ws_path = "https://reqres.in";
	UserData userData;
	//	ExtentReports extent;

	StringWriter requestWriter;
	PrintStream requestCapture ;
	ByteArrayOutputStream reqos;
	ByteArrayOutputStream resos;

	StringWriter responseWriter;
	PrintStream responseCapture;

	static int TestCounter = 1;

	java.util.Map<String,ExtentTest> testmap = new HashMap<String, ExtentTest>();


	protected String formatAPIAndLogInReport(String content) {

		String prettyPrint = content.replace("\\n", "</br>");
		return "<code>" + prettyPrint + "</code>";

	}


	@BeforeSuite
	public void setUp() {
		baseURI = ws_path;
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeTest(ITestContext context) {
		
		requestWriter = new StringWriter();
		reqos = new ByteArrayOutputStream();
		requestCapture  = new PrintStream(reqos);

		responseWriter = new StringWriter();
		resos = new ByteArrayOutputStream();
		responseCapture  = new PrintStream(resos);

	}

	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext context) {

	}

	@Test(priority = 0)
	public void checkQueryParam() throws IOException {
		System.out.println("Testing : https://reqres.in/api/users?page=2");

		Response response =  given().accept(ContentType.JSON).queryParam("page", 2)
				.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
				.log().all()
				.when()
				.get("/api/users")
				.then()
				.assertThat()
				.statusCode(200).extract().response();

		requestCapture.flush();

		Markup m = MarkupHelper.createCodeBlock(reqos.toString(), response.prettyPrint());

		ExtentTestManager.getTest().createNode("Request and Response Information").pass(m);
		
		reqos.flush();
		
//		requestWriter.flush();

		//		ExtentTestManager.getTest().createNode("Request").info(formatAPIAndLogInReport(requestWriter.toString()));
		//		ExtentTestManager.getTest().createNode("Response").info(formatAPIAndLogInReport(response.prettyPrint()));

	}

	@Test(priority = 1)
	public void JsonSchemaValidator() throws IOException {
		System.out.println("Testing : "+ws_path);
		Response response =  given()
				.relaxedHTTPSValidation()
				.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
				.when()
				.get("/api/users/2")
				.then()
				.assertThat().body("data", allOf(hasKey("id"), hasKey("email"), hasKey("first_name"), hasKey("last_name"), hasKey("avatar")))
				.body(JsonSchemaValidator.matchesJsonSchema(new File(schema_path))).extract().response();	
		requestCapture.flush();

		Markup m = MarkupHelper.createCodeBlock(reqos.toString(), response.prettyPrint());

		ExtentTestManager.getTest().createNode("Request and Response Information").pass(m);
//		requestWriter.flush();
		reqos.flush();

	}

	@Test(priority = 2)
	public void checkHeader() throws IOException {
		System.out.println("Response header is application/json");

		Response response = given()
				.relaxedHTTPSValidation()
				.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
				.when()
				.get("/api/users/2")
				.then()
				.header("content-type", equalToIgnoringCase("application/json; charset=utf-8")).extract().response();

		requestCapture.flush();

		Markup m = MarkupHelper.createCodeBlock(reqos.toString(), response.prettyPrint());

		ExtentTestManager.getTest().createNode("Request and Response Information").pass(m);
//		requestWriter.flush();
		reqos.flush();
	}

	@Test(enabled = true, priority = 3)
	public void deserializationTest() throws IOException {
		System.out.println("deserialization Test");

		userData = given()
				.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
				.when()
				.get("/api/users/2")
				.then()
				.extract()
				.body()
				.as(UserData.class, ObjectMapperType.JACKSON_2);


		System.out.println("---------- Data ----------------");
		System.out.println(userData.getData().getId());
		System.out.println(userData.getData().getEmail());
		System.out.println(userData.getData().getFirstName());
		System.out.println(userData.getData().getLastName());
		System.out.println(userData.getData().getAvatar());

		System.out.println("---------- Support ----------------");
		System.out.println(userData.getSupport().getUrl());
		System.out.println(userData.getSupport().getText());


		requestCapture.flush();

		Markup m = MarkupHelper.createCodeBlock(reqos.toString(), userData.toString());

		ExtentTestManager.getTest().createNode("Request and Response Information").pass(m);
//		requestWriter.flush();
		reqos.flush();
	}

	@Test(priority = 4)
	public void serializationTest() throws JsonProcessingException {

		System.out.println("serialization Test");
		ObjectMapper mapper = new ObjectMapper();
		String userd = mapper.writeValueAsString(userData);
		System.out.println(userd);

		// this userd can be send as a json payload in the request body

	}

	@Test(priority = 5)
	public void jsonPathCheck() throws IOException {

		System.out.println("JsonPath Check");

		Response response =  given()
		.relaxedHTTPSValidation()
		.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
		.when()
		.get("/api/users")
		.then()
		.body("data.find {it.id.toInteger() == 1}.first_name", equalTo("George")).extract().response();

		requestCapture.flush();

		Markup m = MarkupHelper.createCodeBlock(reqos.toString(), userData.toString());

		ExtentTestManager.getTest().createNode("Request and Response Information").pass(m);
//		requestWriter.flush();
		reqos.flush();

	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws IOException, URISyntaxException {

		String path = System.getProperty("user.dir")+File.separator+"extent-reports"+File.separator+"extent-report.html";
		System.out.println(path);

		Desktop.getDesktop().browse(new File(path).toURI());

	}

}

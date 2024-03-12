package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import sg.okayfoods.lunchbunch.application.AuthenticationService;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.RegistrationRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;

import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LunchPlanControllerTest {

    static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:8.3"
    );
    @Autowired
    AuthenticationService authenticationService;

    private static String lunchPlanUuid = "";

    @LocalServerPort
    private Integer port;
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeAll
    static void beforeAll(){
        mysql.start();
    }
    @AfterAll
    static void afterAll() {
        mysql.stop();
    }
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    void registerUser(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@test.com");
        registrationRequest.setPassword("password");
        registrationRequest.setName("test");
        authenticationService.register(registrationRequest);
    }
    String getAccessToken(){
        var response = given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder()
                        .email("test@test.com").password("password")
                        .build())
                .when()
                .post("/login")
                .then()
                .extract();

        var loginResponse = response.as(GenericResponse.class);
        LinkedHashMap<String,String> dataResp = ((LinkedHashMap<String,String>) loginResponse.data());

        return dataResp.get("accessToken");
    }
    @Test
    @Order(1)
    void validRequest_createLunchPlan_responseOkay() {

        registerUser();
        String accessToken = getAccessToken();
        LunchPlanRequestDTO lunchPlanRequestDTO = LunchPlanRequestDTO.builder()
                .date("2024-03-12")
                .description("Birthday Celebration")
                .build();
        var resp = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .body(lunchPlanRequestDTO)
                .when()
                .post("/lunch-plan")
                .then().extract();
        var response = resp.as(GenericResponse.class);
        assertEquals(200, response.status());
        LinkedHashMap<String,String> dataResp = ((LinkedHashMap<String,String>) response.data());
        lunchPlanUuid = dataResp.get("uuid");
    }

    @Test
    @Order(2)
    void validRequest_getLunch_responseOkay() {
        String accessToken = getAccessToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .get("/lunch-plan/"+lunchPlanUuid)
                .then()
                .statusCode(200);
    }
    @Test
    @Order(2)
    void requestNotExisting_getLunch_response404() {
        String accessToken = getAccessToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .get("/lunch-plan/not-existing")
                .then()
                .statusCode(404);
    }
}
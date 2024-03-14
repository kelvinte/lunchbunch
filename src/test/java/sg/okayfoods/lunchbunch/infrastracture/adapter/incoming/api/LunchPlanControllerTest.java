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
import sg.okayfoods.lunchbunch.common.constant.UserStatus;
import sg.okayfoods.lunchbunch.domain.entity.AppRole;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.RegistrationRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.PaginatedResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.util.ContainerUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LunchPlanControllerTest {

    @BeforeAll
    static void beforeAll(){
        ContainerUtils.startContainers();
    }
    @AfterAll
    static void afterAll() {
        ContainerUtils.stopContainer();
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        ContainerUtils.setPropertySource(registry);
    }

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private LunchPlanRepository lunchPlanRepository;

    @LocalServerPort
    private Integer port;

    private String userEmail = "test@test.com";
    private String userPassword = "hello123";
    private String userEncryptedPassword="$2a$10$tvBKf7WUdSeXkUlQ.WO5EeBY6t9uKU59CDMxxdF2CmGdifiMyTV9.";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        registerUser();
    }

    @AfterEach
    void after(){
        deleteAll();
    }

    void registerUser(){
        AppUser existingUser = AppUser.builder()
                .email(userEmail)
                .name("existing")
                .password(userEncryptedPassword) //hello123
                .status(UserStatus.ACTIVE)
                .appRole(AppRole.builder().id(1).build())
                .build();
        appUserRepository.save(existingUser);
    }
    void deleteAll(){
        lunchPlanRepository.deleteAll();
        appUserRepository.deleteAll();
    }
    String getAccessToken(){
        var response = given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder()
                        .email(userEmail).password(userPassword)
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
    void validRequest_createLunchPlan_responseOkay() {

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
        String lunchPlanUuid = dataResp.get("uuid");
        var lunchPlan =lunchPlanRepository.findByUuid(lunchPlanUuid);
        assertTrue(lunchPlan.isPresent());
        assertEquals("Birthday Celebration", lunchPlan.get().getDescription());
        assertEquals(LocalDate.parse("2024-03-12"), lunchPlan.get().getDate());

    }

    @Test
    void validRequest_getLunch_responseOkay() {

        var user = appUserRepository.findByEmail(userEmail);
        LocalDate localDate = LocalDate.now();
        String uuid = UUID.randomUUID().toString();
        LunchPlan lunchPlan = LunchPlan.builder()
                .uuid(uuid)
                .description("Test")
                .date(localDate)
                .initiatedBy(user.get())
                .build();

        lunchPlanRepository.save(lunchPlan);

        String accessToken = getAccessToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .get("/lunch-plan/"+uuid)
                .then()
                .statusCode(200)
                .assertThat().
                body(  "success", equalTo(true),
                        "status", equalTo(200),
                        "title", equalTo("Request Processed successfully"),
                        "data.id", equalTo(lunchPlan.getId().toString()),
                        "data.uuid", equalTo(uuid),
                        "data.description",equalTo("Test") // Assert access token is not null
                );
    }
    @Test
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


    @Test
    void haveLunchPlan_getLunchPLan_returnPaginatedLunchPlan(){
        var user = appUserRepository.findByEmail(userEmail);
        LocalDate localDate = LocalDate.now();
        String uuid = UUID.randomUUID().toString();
        for(int i = 0 ; i < 20 ; i ++) {
            LunchPlan lunchPlan = LunchPlan.builder()
                    .uuid(uuid)
                    .description("Test")
                    .date(localDate)
                    .initiatedBy(user.get())
                    .build();

            lunchPlanRepository.save(lunchPlan);
        }

        String accessToken = getAccessToken();
        var response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .get("/lunch-plan?page=1")
                .then()
                .statusCode(200)
                .extract();

        var genResp = response.as(GenericResponse.class);
        assertEquals("Request Processed successfully", genResp.title());
        assertEquals(200, genResp.status());
        var data = (LinkedHashMap)genResp.data();
        assertEquals(20,data.get("totalItems"));
        assertEquals(2, data.get("totalPage"));
        assertEquals(10, data.get("size"));
        assertEquals(1, data.get("page"));

        var result = (ArrayList<LinkedHashMap>) data.get("result");
        assertEquals("Test", result.get(0).get("description"));
    }


}
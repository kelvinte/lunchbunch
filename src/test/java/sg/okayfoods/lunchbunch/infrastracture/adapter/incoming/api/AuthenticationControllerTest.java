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
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.RegistrationRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {
    static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:8.3"
    );

    @LocalServerPort
    private Integer port;
    @BeforeAll
    static void beforeAll(){
        mysql.start();
    }
    @AfterAll
    static void afterAll() {
        mysql.stop();
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    AppUserRepository appUserRepository;
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
    @Test
    @Order(1)
    void validRequest_register_responseOkay() {
        appUserRepository.deleteAll();
        given()
                .contentType(ContentType.JSON)
                .body(RegistrationRequest.builder()
                        .email("hello@hello.com").password("hello123").name("hello")
                        .build())
                .when()
                .post("/register")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void validLogin_shouldBeOkay() {

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder()
                        .email("hello@hello.com").password("hello123")
                        .build())
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .assertThat().body(
                    "success", equalTo(true),
                    "status", equalTo(200),
                    "title", equalTo("Request Processed successfully"),
                    "data.email", equalTo("hello@hello.com"),
                    "data.role", equalTo("USER"),
                    "data.accessToken", notNullValue() // Assert access token is not null
                );
        appUserRepository.deleteAll();
    }
}
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
import sg.okayfoods.lunchbunch.common.constant.UserStatus;
import sg.okayfoods.lunchbunch.domain.entity.AppRole;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.RegistrationRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;
import sg.okayfoods.lunchbunch.util.ContainerUtils;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
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

    @LocalServerPort
    private Integer port;


    @Autowired
    AppUserRepository appUserRepository;
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
    @Test
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

        Optional<AppUser> user= appUserRepository.findByEmail("hello@hello.com");
        assertTrue(user.isPresent());
        assertEquals("hello@hello.com", user.get().getEmail());
    }

    @Test
    void noEmailInRequest_register_shouldReturn400(){
        appUserRepository.deleteAll();
        given()
                .contentType(ContentType.JSON)
                .body(RegistrationRequest.builder()
                        .email("").password("hello123").name("hello")
                        .build())
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .assertThat().body(
                        "success", equalTo(false),
                        "status", equalTo(400),
                        "title", equalTo("email: Email cannot be empty")
                );

    }

    @Test
    void noPasswordInRequest_register_shouldReturn400(){
        appUserRepository.deleteAll();
        given()
                .contentType(ContentType.JSON)
                .body(RegistrationRequest.builder()
                        .email("email@email.com").password("").name("hello")
                        .build())
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .assertThat().body(
                        "success", equalTo(false),
                        "status", equalTo(400),
                        "title", containsString("password: Password cannot be empty"),
                "title",containsString("password: Password must be between 6 and 20 characters")
                        );

    }
    @Test
    void userIsExisting_register_respondShouldHaveError() {
        appUserRepository.deleteAll();

        AppUser existingUser = AppUser.builder()
                .email("existing@existing.com")
                .name("existing")
                .password("existing")
                .status(UserStatus.ACTIVE)
                .appRole(AppRole.builder().id(1).build())
                .build();
        appUserRepository.save(existingUser);

        var response = given()
                .contentType(ContentType.JSON)
                .body(RegistrationRequest.builder()
                        .email("existing@existing.com").password("hello123").name("hello")
                        .build())
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .extract();

        var error = response.as(GenericResponse.class);
        assertEquals("AUTH002 Email Already in use", error.title());
        assertEquals(400, error.status());
        assertFalse(error.success());
    }




    @Test
    void validLogin_shouldBeOkay() {
        appUserRepository.deleteAll();

        AppUser existingUser = AppUser.builder()
                .email("valid@valid.com")
                .name("existing")
                .password("$2a$10$tvBKf7WUdSeXkUlQ.WO5EeBY6t9uKU59CDMxxdF2CmGdifiMyTV9.") //hello123
                .status(UserStatus.ACTIVE)
                .appRole(AppRole.builder().id(2).build())
                .build();

        appUserRepository.save(existingUser);

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder()
                        .email("valid@valid.com").password("hello123")
                        .build())
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .assertThat().body(
                    "success", equalTo(true),
                    "status", equalTo(200),
                    "title", equalTo("Request Processed successfully"),
                    "data.email", equalTo("valid@valid.com"),
                    "data.role", equalTo("USER"),
                    "data.accessToken", notNullValue() // Assert access token is not null
                );
    }

    @Test
    void wrongPassword_shouldReturn403() {
        appUserRepository.deleteAll();

        AppUser existingUser = AppUser.builder()
                .email("valid@valid.com")
                .name("existing")
                .password("$2a$10$tvBKf7WUdSeXkUlQ.WO5EeBY6t9uKU59CDMxxdF2CmGdifiMyTV9.") //hello123
                .status(UserStatus.ACTIVE)
                .appRole(AppRole.builder().id(2).build())
                .build();

        appUserRepository.save(existingUser);

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder()
                        .email("valid@valid.com").password("wrongpassword")
                        .build())
                .when()
                .post("/login")
                .then()
                .statusCode(403)
                .assertThat().body(
                        "success", equalTo(false),
                        "status", equalTo(403),
                        "title", equalTo("Invalid Username or Password")
                );
    }
}
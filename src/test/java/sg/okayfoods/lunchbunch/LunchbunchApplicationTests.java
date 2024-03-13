package sg.okayfoods.lunchbunch;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import sg.okayfoods.lunchbunch.util.ContainerUtils;

@SpringBootTest
class LunchbunchApplicationTests {


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

	@Test
	void contextLoads() {
	}

}

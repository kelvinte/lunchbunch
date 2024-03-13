package sg.okayfoods.lunchbunch.util;

import com.redis.testcontainers.RedisContainer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class ContainerUtils {
    private static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:8.3"
    );

    private static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:6.2.6"));

    public static void startContainers(){
        mysql.start();
        redis.start();
    }

    public static void stopContainer(){
        mysql.stop();
        redis.stop();
    }

    public static MySQLContainer getMysqlContainer(){
        return mysql;
    }
    public static RedisContainer getRedisContainer(){
        return redis;
    }

    public static void setPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);

        registry.add("redis.host",redis::getHost);
        registry.add("redis.port",redis::getRedisPort);
    }


}

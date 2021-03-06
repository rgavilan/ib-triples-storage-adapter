package es.um.asio.back.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import es.um.asio.service.mapper.MapperConfig;

@SpringBootApplication
@EnableAutoConfiguration
@Import(MapperConfig.class)
public class TestApplication {
    /**
     * Main method for embedded deployment.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}

package iut.chronoclash.chronoclash_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChronoClashApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChronoClashApiApplication.class, args);
    }

}

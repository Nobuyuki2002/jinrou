package oit.is.ouchi.jinrou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class JinrouApplication {

  public static void main(String[] args) {
    SpringApplication.run(JinrouApplication.class, args);
  }

}

package dev.vality.scrooge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ScroogeApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScroogeApplication.class, args);
    }

}

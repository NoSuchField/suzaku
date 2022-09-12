package com.omgxy.technote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.omgxy.technote.*.mapper")
public class TechNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechNoteApplication.class, args);
    }

}

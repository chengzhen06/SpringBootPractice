package com.crawler.springbootproject1;

import com.crawler.springbootproject1.bean.User;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableMongoRepositories(basePackageClasses = dbms.UserRepo.class)
@SpringBootApplication

public class SpringBootProject1Application implements CommandLineRunner {

    @Autowired
    UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for(User user: userRepo.findAll())
        {
            System.out.println(user);
        }
    }
}

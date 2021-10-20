package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest(classes = SareetaApplication.class)
//webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//properties = "server_port=0")
public class UserControllerTests extends AbstractTestNGSpringContextTests {
    @Autowired
    UserController userController;

    @Test
    public void canCreateUser(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("canCreateUserTest");
        req.setPassword("1234");
        req.setConfirmPassword("1234");
        ResponseEntity<User> res = userController.createUser(req);
        Assert.assertTrue(res.getBody().getId() > 0);
        Assert.assertTrue(res.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void canDetectWrongConfirmPasswordWhenCreatingUser(){

    }
}

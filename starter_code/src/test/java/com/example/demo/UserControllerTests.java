package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest(classes = SareetaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests extends AbstractTestNGSpringContextTests {
    @LocalServerPort
    private int port;

    private static String BASE_URL;
    private static String LOGIN_URL;

    @Autowired
    UserController userController;

    RestTemplate restTemplate;

    @BeforeClass
    public void beforeClass(){
        restTemplate = new RestTemplate();
        BASE_URL = "http://localhost:" + port + "/";
        LOGIN_URL = BASE_URL + "login";
        createUser();
    }

    @Test
    public void canCreateUser(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("canCreateUserTest");
        req.setPassword("1234");
        req.setConfirmPassword("1234");
        ResponseEntity<User> res = userController.createUser(req);
        System.out.println("password:" + res.getBody().getPassword());
        Assert.assertTrue(res.getBody().getId() > 0);
        Assert.assertTrue(res.getStatusCode().is2xxSuccessful());
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class }, expectedExceptionsMessageRegExp = ".*password.*")
    public void canDetectWrongConfirmPasswordWhenCreatingUser(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("canCreateUserTest");
        req.setPassword("1234");
        req.setConfirmPassword("4321");
        ResponseEntity<User> res = userController.createUser(req);
    }

    @Test
    public void canLogin(){
//        createUser();
        LoginRequest req = new LoginRequest();
        req.setUsername("created_user");
        req.setPassword("1234");

        ResponseEntity<User> res = restTemplate.postForEntity(LOGIN_URL, req, User.class);
        Assert.assertEquals(res.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(res.getHeaders().containsKey("Authorization"));
    }

    @Test(expectedExceptions = { HttpClientErrorException.Unauthorized.class })
    public void canBlockInvalidLogin(){
//        createUser();
        LoginRequest req = new LoginRequest();
        req.setUsername("absent user");
        req.setPassword("1234");

        ResponseEntity<User> res = restTemplate.postForEntity(LOGIN_URL, req, User.class);
        Assert.assertTrue(res.getStatusCode().is4xxClientError());
        Assert.assertFalse(res.getHeaders().containsKey("Authorization"));

    }

    private User createUser(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("created_user");
        req.setPassword("1234");
        req.setConfirmPassword("1234");
        ResponseEntity<User> res = userController.createUser(req);
        return (User) res.getBody();
    }
}

package com.example.demo;

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

@SpringBootTest(classes = SareetaApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = "server_port=0")
public class UserControllerTests extends AbstractTestNGSpringContextTests {

    // 不能在constructor取得，因為PORT還沒有INIT好
    // 放在BeforeClass or BeforeMethod比較妥當的
    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    String USER_CONTROLLER_URL = "";

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Test
    public void given0AsServerPort_whenReadWebAppCtxt_thenGetThePort() {
        int port = webServerAppCtxt.getWebServer().getPort();
        Assert.assertTrue(port > 1023);
    }

    public UserControllerTests() {
    }

    @BeforeClass
    public void beforeMethod(){
        System.out.println("local port: " + port);
        restTemplate = new RestTemplate();
        int port = webServerAppCtxt.getWebServer().getPort();
        USER_CONTROLLER_URL = "http://localhost:" + port + "/api/user/";
    }

    @Test
    public void canCreateUser(){
        String url = USER_CONTROLLER_URL + "create";
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("canCreateUserTest");
        ResponseEntity<User> rest = restTemplate.postForEntity(url,req, User.class);
        Assert.assertEquals(rest.getStatusCodeValue(), 200);
    }
}

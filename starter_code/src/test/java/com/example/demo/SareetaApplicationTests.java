package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SareetaApplicationTests extends AbstractTestNGSpringContextTests {

	@Test
	public void contextLoads() {
	}
}

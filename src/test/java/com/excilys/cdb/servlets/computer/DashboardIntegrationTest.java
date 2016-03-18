package com.excilys.cdb.servlets.computer;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DashboardIntegrationTest {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080/";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test
	public void testPageNavigation() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.linkText("2")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("3")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=3&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("2")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("1")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("»")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("»")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=3&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("«")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("«")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=10", driver.getCurrentUrl());
		driver.findElement(By.linkText("1")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=10", driver.getCurrentUrl());
	}
	
	@Test
	public void testPageLimit() throws Exception {
	    driver.get(baseUrl + "/computer-database/computer");
	    driver.findElement(By.cssSelector("button.btn.btn-default")).click();
	    assertEquals(baseUrl + "/computer-database/computer?page=1&limit=10", driver.getCurrentUrl());
	    driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=20", driver.getCurrentUrl());
	    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=50", driver.getCurrentUrl());
	    driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=20", driver.getCurrentUrl());
	    driver.findElement(By.linkText("2")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=20", driver.getCurrentUrl());
	    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=50", driver.getCurrentUrl());
	    driver.findElement(By.linkText("»")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=2&limit=50", driver.getCurrentUrl());
	    driver.findElement(By.linkText("1")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=50", driver.getCurrentUrl());
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=1&limit=10", driver.getCurrentUrl());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}

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
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testList() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.linkText("2")).click();
		driver.findElement(By.linkText("3")).click();
		driver.findElement(By.linkText("2")).click();
		driver.findElement(By.linkText("1")).click();
		driver.findElement(By.linkText("»")).click();
		driver.findElement(By.linkText("»")).click();
		driver.findElement(By.linkText("«")).click();
		driver.findElement(By.linkText("«")).click();
		driver.findElement(By.linkText("59")).click();
		driver.findElement(By.linkText("1")).click();
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

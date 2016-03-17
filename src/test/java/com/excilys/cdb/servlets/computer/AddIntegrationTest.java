package com.excilys.cdb.servlets.computer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.excilys.cdb.validation.Validator;

public class AddIntegrationTest {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080/";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Ignore
	@Test
	public void testBadName() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("");
		new Select(driver.findElement(By.id("companyId"))).selectByVisibleText("Samsung Electronics");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		String errorText = driver.findElement(By.id("computerNameError")).getText();
		assertEquals(Validator.EMPTY_NAME, errorText);
	}

	@Ignore
	@Test
	public void testBadDateIntro() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("Samsung Galaxy S7");
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("introduced")).sendKeys("20160311");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		String errorText = driver.findElement(By.id("computerIntroError")).getText();
		assertEquals(Validator.INCORRECT_DATE, errorText);
	}

	@Ignore
	@Test
	public void testBadDateDiscontinued() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("Samsung Galaxy S7");
		driver.findElement(By.id("discontinued")).clear();
		driver.findElement(By.id("discontinued")).sendKeys("20160311");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		String errorText = driver.findElement(By.id("computerDiscontinuedError")).getText();
		assertEquals(Validator.INCORRECT_DATE, errorText);
	}

	@Ignore
	@Test
	public void testFailAdd() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("Samsung Galaxy S7");
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("introduced")).sendKeys("1111-11-11");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		String errorText = driver.findElement(By.id("computerError")).getText();
		assertEquals(Validator.COMP_ERROR, errorText);
	}

	@Ignore
	@Test
	public void testBadDateIntroAfterDiscontinued() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("Samsung Galaxy S7");
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("introduced")).sendKeys("2016-03-11");
		driver.findElement(By.id("discontinued")).clear();
		driver.findElement(By.id("discontinued")).sendKeys("2016-03-10");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		String errorText = driver.findElement(By.id("computerDiscontinuedError")).getText();
		assertEquals("discontinued date should be after introduced date", errorText);
	}

	@Test
	public void testAddGood() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.id("computerName")).clear();
		driver.findElement(By.id("computerName")).sendKeys("Samsung Galaxy S7");
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("introduced")).sendKeys("2016-03-11");
		new Select(driver.findElement(By.id("companyId"))).selectByVisibleText("Samsung Electronics");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		Logger log = Logger.getLogger(getClass());
		log.error(driver.getCurrentUrl());
		log.error(baseUrl + "/computer-database/computer");
		assertEquals(baseUrl + "/computer-database/computer", driver.getCurrentUrl());
	}	

	@Ignore
	@Test
	public void testCancel() throws Exception {
		driver.get(baseUrl + "/computer-database/computer");
		driver.findElement(By.id("addComputer")).click();
		driver.findElement(By.linkText("Cancel")).click();
		assertEquals(baseUrl + "/computer-database/computer?page=&limit=", driver.getCurrentUrl());
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

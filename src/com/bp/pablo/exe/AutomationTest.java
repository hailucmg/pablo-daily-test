package com.bp.pablo.exe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.dailytest.PabloDailyTest;
import com.bp.pablo.selenium.util.IOUTIL;
import com.bp.pablo.selenium.util.SendMailSSL;
import com.thoughtworks.selenium.Selenium;

public class AutomationTest {
	public static void main(String[] arg) throws InterruptedException{
		PabloSite ps = null;
		TestAccount acc = null;
		WebDriver driver = null;
		Selenium selenium = null;
		try {
			ps = IOUTIL.loadAllUrl();
			acc = IOUTIL.loadAccountTest();
		} catch (Exception e) {
			SendMailSSL.sendMailCMG("Please help me to check 2 file account.properties and site.properties!", "Pablo server missing some file");
		}
		try {
			driver	 = new FirefoxDriver();
			selenium = new WebDriverBackedSelenium(driver, ps.getMain_url());
		} catch (Exception e) {
			SendMailSSL.sendMailCMG("There are missing some jar file : selenium-server-standalone or selenium-java.jar or selenium-java-srcs.jar !", "Pablo server missing some file");
		}
		
		try {
			PabloDailyTest dailyTest = new PabloDailyTest();
			dailyTest.runTest(ps, acc, driver, selenium);
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
		
	}
}

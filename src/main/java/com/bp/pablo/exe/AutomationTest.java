package com.bp.pablo.exe;

import java.net.InetAddress;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;


import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.dailytest.PabloDailyTest;
import com.bp.pablo.selenium.util.IOUTIL;
import com.bp.pablo.selenium.util.SendMailSSL;
import com.bp.pablo.selenium.util.WriteLogFile;
import com.thoughtworks.selenium.Selenium;



public class AutomationTest {
	public static void main(String[] arg) throws InterruptedException{
		PabloSite ps = null;
		TestAccount acc = null;
		WebDriver driver = null;
		Selenium selenium = null;
		boolean reachable = false;
		try {
			ps = IOUTIL.loadAllUrl();
			acc = IOUTIL.loadAccountTest();
		} catch (Exception e) {
			SendMailSSL.sendMailCMG("Please help me to check 2 file account.properties and site.properties!", "Pablo automation test missing some file");
		}
		try {
			driver	 = new FirefoxDriver();
			selenium = new WebDriverBackedSelenium(driver, ps.getMain_url());
		} catch (Exception e) {
			e.printStackTrace();
			SendMailSSL.sendMailCMG("There are missing some jar file : selenium-server-standalone or selenium-java.jar or selenium-java-srcs.jar !", "Pablo automation test server missing some file");
		}
			try {
				InetAddress address = InetAddress.getByName("google.com.vn");
				reachable = address.isReachable(10000);
			} catch (Exception e) {
				reachable = false;
				e.printStackTrace();
			}
			try {
			if(reachable){
				WriteLogFile.logger.info("Connection to internet successfully");
				PabloDailyTest dailyTest = new PabloDailyTest();
				selenium.setTimeout("120000");
				driver.manage().window().maximize();
				dailyTest.runTest(ps, acc, driver, selenium);
			}else{
				WriteLogFile.logger.info("Lost connection to internet");
				driver.quit();
			}
		} catch (Exception e) {
			PabloDailyTest dailyTest = new PabloDailyTest();
			selenium.setTimeout("120000");
			driver.manage().window().maximize();
			dailyTest.runTest(ps, acc, driver, selenium);
		}
		
	}
}

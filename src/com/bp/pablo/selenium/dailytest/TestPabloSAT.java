package com.bp.pablo.selenium.dailytest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.util.IOUTIL;
import com.bp.pablo.selenium.util.SendMailSSL;
import  com.bp.pablo.selenium.util.WriteLogFile;
import com.thoughtworks.selenium.Selenium;

public class TestPabloSAT {
	
	public void testSAT(){
		
		PabloSite ps = IOUTIL.loadAllUrl();
		String bodyText = "";
		WebDriver driver = new FirefoxDriver();
		//checking open server
		Selenium selenium = new WebDriverBackedSelenium(driver, ps.getMain_url());
		selenium.setTimeout("210000");
		selenium.open(ps.getMain_url());
		if(selenium.isTextPresent("Access to Pablo")){
			 bodyText ="<h3>Beginning of automation test for SAT: https://ukpensionsint.bp.com</h3><br>";
			 WriteLogFile.logger.info("Beginning of automation test for SAT");
		}else{
			bodyText = "<h3 style=\"color:red\">Server was broken. Cannot open website</h3>";
			WriteLogFile.logger.info("Server was broken. Can not open website");
			SendMailSSL.sendMailCMG(bodyText, "Pablo server broken");
		}
		//end check server
		
		//start check login with account 
		TestAccount acc = IOUTIL.loadAccountTest();
		selenium.open(ps.getLogin_url());
		selenium.type("_request_username", acc.getUsername_normal());
		selenium.type("_request_password", acc.getPassword_normal());
		selenium.click("check_term");
		selenium.click("doauth");
		selenium.waitForPageToLoad("3000");
		if(selenium.getTitle().equalsIgnoreCase("Team pages")){
			  	bodyText += " -> Login function: PASSED" + "<br>";
	            WriteLogFile.logger.info(" -> Login function: PASSED");
		}else{
			   bodyText += " -> Login function: FAILED" + "<br>";
	           WriteLogFile.logger.info(" -> Login function: FAILED");
	           //SendMailSSL.sendMailCMG(bodyText, "Can not login");
		}
		driver.quit();
	}
	
	public static void main(String arg[]){
		TestPabloSAT test = new TestPabloSAT();
		test.testSAT();
	}
}

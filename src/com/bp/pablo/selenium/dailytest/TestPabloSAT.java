package com.bp.pablo.selenium.dailytest;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.util.IOUTIL;
import com.bp.pablo.selenium.util.SendMailSSL;
import  com.bp.pablo.selenium.util.WriteLogFile;
import com.thoughtworks.selenium.Selenium;

public class TestPabloSAT {
	
	public void testSAT() throws InterruptedException{
		
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
		String title = selenium.getTitle();
		System.out.println(title);
	/*	try {
			WebElement attr1 = driver.findElement(By.xpath("//div[@id='foote1rnav']/span[2][@class='fakelink']/a"));
			String attr = attr1.getText();
			System.out.println(attr);
		} catch (Exception e) {
			System.out.println("exception " + e);
		}*/
		/*if(selenium.getTitle().equalsIgnoreCase("Team pages")){
				
			
			  	bodyText += " -> Login function: PASSED" + "<br>";
	            WriteLogFile.logger.info(" -> Login function: PASSED");
		}else{
			   bodyText += " -> Login function: FAILED" + "<br>";
	           WriteLogFile.logger.info(" -> Login function: FAILED");
	           //SendMailSSL.sendMailCMG(bodyText, "Can not login");
		}*/
		/*selenium.open(ps.getDropbox_url());
		selenium.waitForPageToLoad("1000");
		selenium.click("xpath=//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1][@class='even']/td[5][@class='blank']/input[@class='dropboxInfo']");
		selenium.click("btnAddUser");
		selenium.type("listMail", "lan.ta@c-mg.com");
		selenium.click("btnCheckMail");
		selenium.click("btnOK");
		String result = selenium.getText("result");
		String table = selenium.getTable("dropbox.1.2").toString();
		System.out.println(table);*/
		
		selenium.open(ps.getMainview_diary_url());
		Thread.sleep(25000);
		try {
			WebElement caseWork = driver.findElement(By.id("DiaryArea"));
			WebElement dropdowList = driver.findElement(By.id("departmentList"));
			/*List<WebElement> options = dropdowList.findElements(By.tagName("option"));*/
		/*	List<String> valueOfdropdowList = new ArrayList<String>();
			WebElement dropdowList = driver.findElement(By.id("departmentList"));*/
			List<WebElement> options = dropdowList.findElements(By.tagName("option"));
			for(WebElement option : options){
			    if(option.getText().equalsIgnoreCase("  team2")){
			        option.click();
			        Thread.sleep(25000);
			    	WebElement tableCase = caseWork.findElement(By.id("diary_table"));
					if(tableCase.isDisplayed()){
						int rolesTableRowCount = selenium.getXpathCount("//table[@id='diary_table']/tbody/tr").intValue();
						if(rolesTableRowCount > 0 ){
							selenium.click("xpath=//tr[@id='diary_row_id0' and @class='DiaryTraffic3']/td[2][@class='DiaryCell DiaryRow']");
							Thread.sleep(1000);
							WebElement popup = driver.findElement(By.id("casenoForm"));
							String att = popup.getAttribute("style");
							System.out.println(att);
							if(popup.isDisplayed()){
								System.out.println("begin");
								WebElement table = popup.findElement(By.className("caseno_form_table"));
								if(table.isDisplayed()){
									selenium.click("checkPriority");
									Thread.sleep(1000);
									WebElement popupPriority = driver.findElement(By.id("actionForm"));
									if(popupPriority.isDisplayed()){
										selenium.click("toggleViewReal");
										Thread.sleep(1000);
										WebElement ListUserMapping = driver.findElement(By.id("listUserMapping"));
										List<WebElement> optionsOfUserMapping = ListUserMapping.findElements(By.tagName("option"));
										if(optionsOfUserMapping == null){
											bodyText = "<h3 style=\"color:red\">The dropdown list is empty when click to the button Reallocate To in " + ps.getMainview_diary_url() +" </h3>";
											WriteLogFile.logger.info(">The dropdown list is empty when click to the button Reallocate To in " + ps.getMainview_diary_url());
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}else{
											System.out.println("ok");
										}
									}else{
										bodyText = "<h3 style=\"color:red\">The popup is not display when click to the button priority  in " + ps.getMainview_diary_url() +" </h3>";
										WriteLogFile.logger.info("The popup is not display when click  to the button priority  in" + ps.getMainview_diary_url());
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
									
									System.out.println("begin 1");
									boolean checkTitle = true;
									String titleA = selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[1]/td[1]/b");
									System.out.println(titleA);
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[1]/td[1]/b").equalsIgnoreCase("Username:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[2]/td[1]/b").equalsIgnoreCase("Business Group:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[3]/td[1]/b").equalsIgnoreCase("Case Number:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[4]/td[1]/b").equalsIgnoreCase("Current Status:")){
										checkTitle = false;
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[5]/td[1]/b").equalsIgnoreCase("Date case was created:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[6]/td[1]/b").equalsIgnoreCase("Disclosure:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[7]/td[1]/b").equalsIgnoreCase("Case Notes:")){
										checkTitle = false;
										System.out.println(checkTitle);
									}
									
										System.out.println(checkTitle);
										boolean checkData = true;
										String username = selenium.getText("caseno_username") ;
										System.out.println(username);
										if(selenium.getText("caseno_username") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_bgroup") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_case_number") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_current_status") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_date_create") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_disclosure") != null){
											checkData = false;
										}
										if(selenium.getText("caseno_case_notes") != null){
											checkData = false;
										}
										System.out.println(checkData);
								}else{
									System.out.println(" table is not display");
								}
							}else{
								
							}
						}
						System.out.println(rolesTableRowCount);
						break;
					}
			       
			    }
			}
			/*if(options!=null){
				for(WebElement option : options){
					if(!option.getAttribute("value").equalsIgnoreCase(acc.getUsername_admin())){
						System.out.println(option.getAttribute("value"));
						valueOfdropdowList.add(option.getText());
					}
				}
			}else{
				bodyText = "<h3 style=\"color:red\">Cannot loading data in dropdowlist in  " + ps.getMainview_diary_url() +" </h3>";
				WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
				SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
			}*/
		} catch (Exception e) {
			bodyText = "<h3 style=\"color:red\">Cannot loading data in dropdowlist in  " + ps.getMainview_diary_url() +" </h3>";
			WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
			SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
		}
		/*try {
			WebElement checkCaseWord = driver.findElement(By.id("DiaryArea"));
			String test = checkCaseWord.getText();
			if(test == null || test.length() == 0){
				System.out.println("null cmnr");
			}
			System.out.println(test);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}*/
		/*String[] options = selenium.getSelectOptions("departmentList");
		for(String op : options){
			System.out.println(op);
		}*/
	/*	selenium.click("searchButton");
		selenium.waitForPageToLoad("10000");*/
/*		String attr = driver.findElement(By.xpath("//div[@id='searchFormDiv']")).getAttribute("style");
		System.out.println(attr);*/
		//selenium.mouseOver("xpath=//table[@id='diary_table' and @class='DiaryTable']/tbody/tr[2][@class='DiaryTraffic3']");
		
		
		driver.quit();
		//System.out.println(attr1);
		//driver.quit();
		
	}
	
	public static void main(String arg[]) throws InterruptedException{
		TestPabloSAT test = new TestPabloSAT();
		test.testSAT();
	}
}

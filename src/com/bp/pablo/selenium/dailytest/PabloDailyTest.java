package com.bp.pablo.selenium.dailytest;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.util.SendMailSSL;
import com.bp.pablo.selenium.util.WriteLogFile;
import com.thoughtworks.selenium.Selenium;

public class PabloDailyTest {

	public  void runTest(PabloSite ps, TestAccount acc, WebDriver driver, Selenium selenium) throws InterruptedException{
		String bodyText = "";		
		if(ps!=null && acc!=null){
			/*********checking server is still alive **********/
			boolean continueCheck = false;
	/*		selenium.setTimeout("210000");*/
			selenium.open(ps.getMain_url());
			if(selenium.isTextPresent("Access to Pablo")){
				 bodyText ="<h3>Beginning of automation test for Pablo: "+ ps.getMain_url()+" </h3><br>";
				 WriteLogFile.logger.info("Beginning of automation test for Pablo");
				 continueCheck = true;
			}else{
				bodyText = "<h3 style=\"color:red\">Server was broken. Cannot open website "+ps.getMain_url()+"</h3>";
				WriteLogFile.logger.info("Server was broken. Can not open website");
				SendMailSSL.sendMailCMG(bodyText, "Pablo server broken");
			}
			/********* end checking server is still alive **********/
			
			if(continueCheck){
				
				/*********checking login function is still alive **********/
				
				selenium.open(ps.getLogin_url());
				selenium.type("_request_username", acc.getUsername_admin());
				selenium.type("_request_password", acc.getPassword_admin());
				selenium.click("check_term");
				selenium.click("doauth");
				selenium.waitForPageToLoad("3000");
				try {
					WebElement logout = driver.findElement(By.xpath("//div[@id='footernav']/span[2][@class='fakelink']/a"));
					String attr = logout.getText();
					if(attr.equalsIgnoreCase("logout")){
						 bodyText ="<h3>Test function login passed </h3><br>";
						 WriteLogFile.logger.info("Test function login passed");
						 try {
							 
							 /*********checking homepage function is still alive **********/
							selenium.open(ps.getHome_url());
							selenium.waitForPageToLoad("10000");
							WebElement payrollNextFurture = driver.findElement(By.xpath("//div[@id='block-1' and @class='block']/h1[@class='draghandle']"));
							String payrollTitle = payrollNextFurture.getText();
							if(!payrollTitle.equals("Next payroll cut-off")){
								SendMailSSL.sendMailCMG("<h2>The Title of payroll in homepage is not equal with 'Next payroll cut-off'</h2><br>Please help me to check this!", "Pablo server have problem");
							}else{
								 bodyText ="<h3>Test function homepage passed </h3><br>";
								 WriteLogFile.logger.info("Test function homepage passed");
							}
						} catch (Exception e) {
							SendMailSSL.sendMailCMG("<h2>The Title of payroll in homepage is not existed</h2><br>Please help me to check this!", "Pablo server have problem");
						}
						 
					}else{
						SendMailSSL.sendMailCMG("<h2>The Link of logout is not equal with 'logout'</h2><br>Thank you", "Pablo server have problem");
					}
				} catch (Exception e) {
					SendMailSSL.sendMailCMG("<h2>Function login have a problem!It may be your accout test is wrong or the login handler have a issue!</h2><br>Please help me to check this", "Pablo server have problem");
				}
				
				/********* end checking login function is still alive **********/
				
				
				
				/********* checking diary main view function is still alive **********/
				
				selenium.open(ps.getMainview_diary_url());
				if(selenium.isTextPresent("Diary centre")){
					 bodyText ="<h3>Test function main diary </h3><br>";
					 WriteLogFile.logger.info("Test function main diary");
					/**** checking dropdowlist is working well *****/
					List<String> ListvalueOfdropdowList = new ArrayList<String>();
					try {
						bodyText ="<h3>Test function main diary dropdow list at top </h3><br>";
						WriteLogFile.logger.info("Test function main diary dropdow list at top");
						WebElement dropdowList = driver.findElement(By.id("departmentList"));
						List<WebElement> options = dropdowList.findElements(By.tagName("option"));
						if(options!=null){
							for(WebElement option : options){
								if(!option.getAttribute("value").equalsIgnoreCase("department")){
									ListvalueOfdropdowList.add(option.getText());
								}
							}
						}else{
							bodyText = "<h3 style=\"color:red\">Cannot loading data in dropdowlist in  " + ps.getMainview_diary_url() +" </h3>";
							WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText = "<h3 style=\"color:red\">Cannot loading data in dropdowlist in  " + ps.getMainview_diary_url() +" </h3>";
						WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					/********* end checking dropdowlist *******/
					
					/***** begin checking case work function *******/
					Thread.sleep(10000);
					WebElement caseWork = driver.findElement(By.id("DiaryArea"));
					String notifi = caseWork.getText();
					if(notifi.contains("There was a communication error:")){
						bodyText = "<h3 style=\"color:red\">Cannot loading case work of "+ acc.getUsername_admin()+" in " + ps.getMainview_diary_url() +" </h3>";
						WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
						/*** check function popup of table diary_table ****/
						bodyText ="<h3>Test function main diary table diary </h3><br>";
						WriteLogFile.logger.info("Test function main diary dropdow list at top");
						
						boolean displayTable = false;
						for(int i = 0; i < ListvalueOfdropdowList.size() ; i ++ ){
							if(displayTable){
								break;
							}
							WebElement dropdowList = driver.findElement(By.id("departmentList"));
							List<WebElement> options = dropdowList.findElements(By.tagName("option"));
							for(WebElement option : options){
							    if(option.getText().equalsIgnoreCase(ListvalueOfdropdowList.get(i))){
							        option.click();
							        Thread.sleep(10000);
							    	WebElement tableCase = caseWork.findElement(By.id("diary_table"));
									if(tableCase.isDisplayed()){
										displayTable = true;
										int TableRowCount = selenium.getXpathCount("//table[@id='diary_table']/tbody/tr").intValue();
										if(TableRowCount > 0 ){
											/** check when click to button checkPriority ****/
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
												}
											}else{
												bodyText = "<h3 style=\"color:red\">The popup is not display when click to the button priority  in " + ps.getMainview_diary_url() +" </h3>";
												WriteLogFile.logger.info("The popup is not display when click  to the button priority  in" + ps.getMainview_diary_url());
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
											
											/*** end check when click button checkPriority ****/
											
											/**** check popup when click to td of table ****/
											Thread.sleep(1000);
											selenium.click("xpath=//tr[@id='diary_row_id0' and @class='DiaryTraffic3']/td[2][@class='DiaryCell DiaryRow']");
											Thread.sleep(1000);
											WebElement popupClickTd = driver.findElement(By.id("casenoForm"));
											if(popupClickTd.isDisplayed()){
												WebElement table = popupClickTd.findElement(By.className("caseno_form_table"));
												if(table.isDisplayed()){
													boolean checkTitle = true;
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[1]/td[1]/b").equalsIgnoreCase("Username:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[2]/td[1]/b").equalsIgnoreCase("Business Group:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[3]/td[1]/b").equalsIgnoreCase("Case Number:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[4]/td[1]/b").equalsIgnoreCase("Current Status:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[5]/td[1]/b").equalsIgnoreCase("Date case was created:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[6]/td[1]/b").equalsIgnoreCase("Disclosure:")){
														checkTitle = false;
													}
													if(!selenium.getText("xpath=//div[@id='casenoFormMid']/table[@class='caseno_form_table']/tbody/tr[7]/td[1]/b").equalsIgnoreCase("Case Notes:")){
														checkTitle = false;
													}
													 if(!checkTitle){
														bodyText = "<h3 style=\"color:red\">The title of popup is not display right when click to the table case work in " + ps.getMainview_diary_url() +" </h3>";
														WriteLogFile.logger.info("The title of popup is not display right when click to the table case work in " + ps.getMainview_diary_url());
														SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
													 }
													boolean checkData = true;
													if(selenium.getText("caseno_username") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_bgroup") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_case_number") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_current_status") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_date_create") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_disclosure") == null){
														checkData = false;
													}
													if(selenium.getText("caseno_case_notes") == null){
														checkData = false;
													}
													if(!checkData){
														bodyText = "<h3 style=\"color:red\">The data of popup is not display right when click to the table case work in " + ps.getMainview_diary_url() +" </h3>";
														WriteLogFile.logger.info("The data of popup is not display right when click to the table case work in " + ps.getMainview_diary_url());
														SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
													}
												}else{
													bodyText = "<h3 style=\"color:red\">The popup is not display when click to the td of table case work in " + ps.getMainview_diary_url() +" </h3>";
													WriteLogFile.logger.info("The popup is not display when click to the table case work in " + ps.getMainview_diary_url());
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}
											}else{
												bodyText = "<h3 style=\"color:red\">The popup is not display when click to the table case work in " + ps.getMainview_diary_url() +" </h3>";
												WriteLogFile.logger.info("The popup is not display when click to the table case work in " + ps.getMainview_diary_url());
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
										}
									}
							       
							    }
							}
						}
					
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getMainview_diary_url()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				
				/*********end checking diary main view function is still alive **********/
			}
			driver.quit();
			
			
		}
		
		
	}
	
}

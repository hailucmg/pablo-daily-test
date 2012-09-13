package com.bp.pablo.selenium.dailytest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;
import com.bp.pablo.selenium.util.SendMailSSL;
import com.bp.pablo.selenium.util.WriteLogFile;
import com.thoughtworks.selenium.Selenium;

// TODO: Auto-generated Javadoc
/**
 * The Class PabloDailyTest.
 */
public class PabloDailyTest {

	/**
	 * Run test.
	 *
	 * @param ps the ps
	 * @param acc the acc
	 * @param driver the driver
	 * @param selenium the selenium
	 * @throws InterruptedException the interrupted exception
	 */
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
								/*if(!option.getAttribute("value").equalsIgnoreCase("department")){*/
									ListvalueOfdropdowList.add(option.getText());
								/*}*/
							}
							bodyText = "<h3>Test function main diary dropdow list at top passed</h3><br>";
							WriteLogFile.logger.info("Test function main diary dropdow list at top passed");
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
						bodyText = "<h3 style=\"color:red\">Cannot loading case work of "+ acc.getUsername_admin()+" in " + ps.getMainview_diary_url() +" at the time : "+ System.currentTimeMillis() +"</h3>";
						WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
						/*** check function popup of table diary_table ****/
						bodyText ="<h3>Test function main diary table diary </h3><br>";
						WriteLogFile.logger.info("Test function main diary table diary");
						
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
							        Thread.sleep(20000);
							        WebElement tableCase;
							        try {
							        	tableCase = caseWork.findElement(By.id("diary_table"));	
									} catch (Exception e) {
										String note = caseWork.getText();
										if(note.contains("There was a communication error:")){
											bodyText = "<h3 style=\"color:red\">Cannot loading case work of "+option.getText() +" in " + ps.getMainview_diary_url() +" at the time : "+ System.currentTimeMillis() +"</h3>";
											WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
										continue;
									}
									if(tableCase.isDisplayed()){
										displayTable = true;
										int TableRowCount = selenium.getXpathCount("//table[@id='diary_table']/tbody/tr").intValue();
										if(TableRowCount > 0 ){
											/** check when click to button checkPriority ****/
											bodyText ="<h3>Test function main diary table diary click to button check priority</h3><br>";
											WriteLogFile.logger.info("Test function main diary table diary click to button check priority");
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
													bodyText ="<h3>Test function main diary table diary click to button check priority passed</h3><br>";
													WriteLogFile.logger.info("Test function main diary table diary click to button check priority passed");
												}
											}else{
												bodyText = "<h3 style=\"color:red\">The popup is not display when click to the button priority  in " + ps.getMainview_diary_url() +" </h3>";
												WriteLogFile.logger.info("The popup is not display when click  to the button priority  in" + ps.getMainview_diary_url());
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
											
											/*** end check when click button checkPriority ****/
											
											/**** check popup when click to td of table ****/
											bodyText ="<h3>Test function main diary table diary click to random td in table</h3><br>";
											WriteLogFile.logger.info("Test function main diary table diary click to random td in table");
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
													
													if(checkTitle == true && checkData == true){
														bodyText ="<h3>Test function main diary table diary click to random td in table passed</h3><br>";
														WriteLogFile.logger.info("Test function main diary table diary click to random td in table passeds");
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
				
				/******* start checking Diary configuration: *****/
				selenium.open(ps.getDiary_configuration());
				Thread.sleep(2000);
				if(selenium.isTextPresent("Diary configuration")){
					bodyText ="<h3>Go to diary configuration passeed</h3><br>";
					WriteLogFile.logger.info("Go to diary configuration passeed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getDiary_configuration()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getDiary_configuration());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				/***** start check function of unmapped user ****/
				try {
					bodyText ="<h3>Go to diary configuration check unMapped table</h3><br>";
					WriteLogFile.logger.info("Go to diary configuration check unMapped table");
					WebElement tableUnmap = driver.findElement(By.id("usermapping"));
					if(tableUnmap.isDisplayed()){
						int TableRowCount = selenium.getXpathCount("//table[@id='usermapping']/tbody/tr").intValue();
						if(TableRowCount > 1){
							selenium.click("xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr[1]/td[2]/a[@class='buttonMap goButton']");
							Thread.sleep(1000);
							try {
								WebElement popup = driver.findElement(By.id("popupContact"));
								if(popup.isDisplayed()){
									bodyText ="<h3>Go to diary configuration check unMapped table and click to button usermapping passed</h3><br>";
									WriteLogFile.logger.info("Go to diary configuration check unMapped table and click to button usermapping passed");
									selenium.click("popupContactClose");
									Thread.sleep(1000);
								}else{
									bodyText = "<h3 style=\"color:red\">Can't load  popup when click to button usermapping in :"+ps.getDiary_configuration()+"</h3>";
									WriteLogFile.logger.info("Can't load  popup when click to button usermapping in :"+ps.getDiary_configuration());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">Can't load  popup when click to button usermapping in :"+ps.getDiary_configuration()+"</h3>";
								WriteLogFile.logger.info("Can't load  popup when click to button usermapping in :"+ps.getDiary_configuration());
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
							
						}
					}else{
						bodyText = "<h3 style=\"color:red\">Can't load  Unmapped User table in :"+ps.getDiary_configuration()+"</h3>";
						WriteLogFile.logger.info("Can't load  Unmapped User table in :"+ps.getDiary_configuration());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">Can't load  Unmapped User table in :"+ps.getDiary_configuration()+"</h3>";
					WriteLogFile.logger.info("Can't load  Unmapped User table in :"+ps.getDiary_configuration());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				/***** end check function of unmapped user ****/
				
				/***** start check function of mapped user ****/
				
				try {
					bodyText ="<h3>Go to diary configuration check Mapped table</h3><br>";
					WriteLogFile.logger.info("Go to diary configuration check Mapped table");
					WebElement tableMappedUser = driver.findElement(By.id("usermapped"));
					if(tableMappedUser.isDisplayed()){
						int TableRowCount = selenium.getXpathCount("//table[@id='usermapped']/tbody/tr").intValue();
						if(TableRowCount > 1){
							selenium.click("xpath=//table[@id='usermapped' and @class='tablesorter']/tbody/tr[1]/td[3][@class='blank']/a[ @class='goButton edit']");
							Thread.sleep(1000);
							try {
								WebElement popup = driver.findElement(By.id("popupContact"));
								if(popup.isDisplayed()){
									bodyText ="<h3>Go to diary configuration check  Mapped table and  click to button edit  passed</h3><br>";
									WriteLogFile.logger.info("Go to diary configuration check unMapped table and click to button edit  passed");
								}else{
									bodyText = "<h3 style=\"color:red\">Can't load  popup when click to button edit of Mapped table user in :"+ps.getDiary_configuration()+"</h3>";
									WriteLogFile.logger.info("Can't load  popup when click to button edit of Mapped table user in :"+ps.getDiary_configuration());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">Can't load  popup when click to button edit of Mapped table user in :"+ps.getDiary_configuration()+"</h3>";
								WriteLogFile.logger.info("Can't load  popup when click to button edit of Mapped table user in :"+ps.getDiary_configuration());
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}
					}else{
						bodyText = "<h3 style=\"color:red\">Can't load  mapped User table in :"+ps.getDiary_configuration()+"</h3>";
						WriteLogFile.logger.info("Can't load mapped User table in :"+ps.getDiary_configuration());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">Can't load  mapped User table in :"+ps.getDiary_configuration()+"</h3>";
					WriteLogFile.logger.info("Can't load mapped User table in :"+ps.getDiary_configuration());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/******** end check function of mapped user ********/
				
				/******* end checking Diary configuration ********/
				
				
				/******* start checking Diary color configuration ****/
				selenium.open(ps.getDiary_color_config());
				Thread.sleep(3000);
				if(selenium.isTextPresent("Colours")){
					bodyText = "<h3>Go to diary colour configuration passeed</h3><br>";
					WriteLogFile.logger.info("Go to diary color configuration passeed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getDiary_color_config()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getDiary_color_config());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/**** start checking table top ********/
				try {
					WebElement tableTop = driver.findElement(By.id("tableCasecode"));
					if(tableTop.isDisplayed()){
						int TableRowCount = selenium.getXpathCount("//table[@id='tableCasecode']/tbody/tr").intValue();
						if(TableRowCount == 3){
							try {
								bodyText = "<h3>Go to diary colour configuration the table in top have 3 <tr> passeed</h3><br>";
								WriteLogFile.logger.info("Go to diary color configuration the table in top have 3 <tr>  passeed");
								WebElement buttonEditOfMouseOver = tableTop.findElement(By.xpath("table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[1]/td[7][@class='blank']/a[@class='goButton editColor']"));
								if(buttonEditOfMouseOver.isDisplayed()){
									bodyText = "<h3 style=\"color:red\">THe button edit is display in the mouseover in :"+ps.getDiary_color_config()+"</h3>";
									WriteLogFile.logger.info("THe button edit is display in the mouseover in:"+ps.getDiary_color_config());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}else{
									bodyText = "<h3>Go to diary colour configuration check the button edit is not display in the mouseover passeed</h3><br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is not display in the mouseover passeed");
								}
							} catch (Exception e) {
								bodyText = "<h3>Go to diary colour configuration check the button edit is not display in the mouseover passeed</h3><br>";
								WriteLogFile.logger.info("Go to diary color configuration check the button edit is not display in the mouseover passeed");
							}
							
							try {
								WebElement buttonEditOfDisclosuer = tableTop.findElement(By.xpath("//table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[2]/td[7][@class='blank']/a[@class='goButton editColor']"));
								if(buttonEditOfDisclosuer.isDisplayed()){
									bodyText = "<h3>Go to diary colour configuration check the button edit is display in the disclosure passeed</h3><br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the disclosure passeed");
								}else{
									bodyText = "<h3 style=\"color:red\">THe button edit is not display in the disclosure in :"+ps.getDiary_color_config()+"</h3>";
									WriteLogFile.logger.info("THe button edit is not display in the disclosure in:"+ps.getDiary_color_config());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">THe button edit is not display in the disclosure in :"+ps.getDiary_color_config()+"</h3>";
								WriteLogFile.logger.info("THe button edit is not display in the disclosure in:"+ps.getDiary_color_config());
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
							
							try {
								WebElement buttonEditOfpayroll = tableTop.findElement(By.xpath("//table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[3]/td[7][@class='blank']/a[@class='goButton editColor']"));
								if(buttonEditOfpayroll.isDisplayed()){
									bodyText = "<h3>Go to diary colour configuration check the button edit is display in the payroll passeed</h3><br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the payroll passeed");
								}else{
									bodyText = "<h3 style=\"color:red\">THe button edit is not display in the payroll in :"+ps.getDiary_color_config()+"</h3>";
									WriteLogFile.logger.info("THe button edit is not display in the payroll in:"+ps.getDiary_color_config());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">THe button edit is not display in the payroll in :"+ps.getDiary_color_config()+"</h3>";
								WriteLogFile.logger.info("THe button edit is not display in the payroll in:"+ps.getDiary_color_config());
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
							
						}else{
							bodyText = "<h3 style=\"color:red\">The table top have more than 3 tr in :"+ps.getDiary_color_config()+"</h3>";
							WriteLogFile.logger.info("The table top have more than 3 tr in :"+ps.getDiary_color_config());
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					}else{
						bodyText = "<h3 style=\"color:red\">Can't load  table location top is not display in :"+ps.getDiary_color_config()+"</h3>";
						WriteLogFile.logger.info("Can't load  table  location top is not display in :"+ps.getDiary_color_config());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">Can't load  table location top in :"+ps.getDiary_color_config()+"</h3>";
					WriteLogFile.logger.info("Can't load  table  location top in :"+ps.getDiary_color_config());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/**** end checking table top *******/
				
				
				/******* start checking table bottom *******/
				try {
					WebElement tableBottom = driver.findElement(By.id("tableCasecode1"));
					if(tableBottom.isDisplayed()){
						try {
							
							WebElement buttAdd = tableBottom.findElement(By.xpath("//table[@id='tableCasecode1' and @class='tablesorter']/thead/tr/th[8]/a[@id='addBtn' and @class='goButton addColor']"));
							if(buttAdd.isDisplayed()){
								bodyText = "<h3>Go to diary colour configuration check the button add is display in the table bottom passeed</h3><br>";
								WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the table bottom passeed");
							}else{
								bodyText = "<h3 style=\"color:red\">THe button add is not display in the table bottom in :"+ps.getDiary_color_config()+"</h3>";
								WriteLogFile.logger.info("THe button add is not display in the table bottom in: "+ps.getDiary_color_config());
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						} catch (Exception e) {
							bodyText = "<h3 style=\"color:red\">THe button add is not display in the table bottom in :"+ps.getDiary_color_config()+"</h3>";
							WriteLogFile.logger.info("THe button add is not display in the table bottom in: "+ps.getDiary_color_config());
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					}else{
						bodyText = "<h3 style=\"color:red\">Can't load  table location bottom in :"+ps.getDiary_color_config()+"</h3>";
						WriteLogFile.logger.info("Can't load  table  location bottom in :"+ps.getDiary_color_config());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">Can't load  table location bottom in :"+ps.getDiary_color_config()+"</h3>";
					WriteLogFile.logger.info("Can't load  table  location bottom in :"+ps.getDiary_color_config());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				
				
				/******* end checking table bottom *******/
				
				/******* end checking Diary color configuration ****/
				
				/****** Start checking Diary_summary *****/
				
				selenium.open(ps.getDiary_summary());
				Thread.sleep(2000);
				if(selenium.isTextPresent("Diary summary")){
					bodyText = "<h3>Go to diary summary passeed</h3><br>";
					WriteLogFile.logger.info("Go to diary summary passeed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getDiary_summary()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getDiary_summary());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				try {
					String lastUpdated = selenium.getText("xpath=//div[@id='content']/div[@id='lastUpdated']");
					if(lastUpdated.trim().length() == 0 || lastUpdated == null){
						bodyText = "<h3 style=\"color:red\">The display of last updated null in "+ps.getDiary_summary()+ " </h3>";
						WriteLogFile.logger.info("The display of last updated is null in :" +ps.getDiary_summary());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}else{
						bodyText = "<h3>Go to diary summary check last updated passeed</h3><br>";
						WriteLogFile.logger.info("Go to diary summary check last updated passeed");
					}
					
					String totalCase = selenium.getText("xpath=//div[@id='content']/div[@id='summary_content']/div[@id='headingContent']/ul[@id='summary_value_title']/li[1][@class='summary_total_case']");
					if(totalCase.equalsIgnoreCase("Total Cases")){
						bodyText = "<h3>Go to diary summary check total cases passeed</h3><br>";
						WriteLogFile.logger.info("Go to diary summary check total cases passeed");
					}else{
						bodyText = "<h3 style=\"color:red\">The display of last updated"+ps.getDiary_summary()+ " is not right</h3>";
						WriteLogFile.logger.info("The display of last updated in :" +ps.getDiary_summary() +" is not right");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					String slaStatus = selenium.getText("xpath=//ul[@id='summary_value_title']/li[2][@class='summary_title_li']/ul[@class='summary_sla_status']/li[1][@class='summary_title_top summary_title']/span");
					System.out.println(slaStatus.trim());
					if(slaStatus.trim().equalsIgnoreCase("SLA status")){
						bodyText = "<h3>Go to diary summary check SLA status  passeed</h3><br>";
						WriteLogFile.logger.info("Go to diary summary check SLA status passeed");
					}else{
						bodyText = "<h3 style=\"color:red\">The display of SLA status "+ps.getDiary_summary()+ " is not right</h3>";
						WriteLogFile.logger.info("The display of SLA status in :" +ps.getDiary_summary() +" is not right");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					String workFlow = selenium.getText("xpath=//ul[@id='summary_value_title']/li[3][@class='summary_title_li']/ul[@class='summary_workflow_status']/li[1][@class='summary_title_top summary_title']/span");
					System.out.println(workFlow.trim());
					if(workFlow.equalsIgnoreCase("Workflow status")){
						bodyText = "<h3>Go to diary summary check Workflow status  passeed</h3><br>";
						WriteLogFile.logger.info("Go to diary summary check Workflow status passeed");
					}else{
						bodyText = "<h3 style=\"color:red\">The display of Workflow status "+ps.getDiary_summary()+ " is not right</h3>";
						WriteLogFile.logger.info("The display of  Workflow  status in :" +ps.getDiary_summary() +" is not right");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
					}
					
					String deparment = selenium.getText("xpath=//div[@id='departmentTitle' and @class='summary_team_title round_border summary_team_bg']/div[1][@class='summary_team_title_des']/a");
					if(deparment.equalsIgnoreCase("Department")){
						bodyText = "<h3>Go to diary summary check Department passeed</h3><br>";
						WriteLogFile.logger.info("Go to diary summary Department  passeed");
					}else{
						bodyText = "<h3 style=\"color:red\">The display of Department in " +ps.getDiary_summary()+ " is not right</h3>";
						WriteLogFile.logger.info("The display of Department in :" +ps.getDiary_summary() +" is not right");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">The display of "+ps.getDiary_summary()+ " is not right</h3>";
					WriteLogFile.logger.info("The display of :" +ps.getDiary_summary() +" is not right");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/****** End checking Diary_summary *****/
				
				
				/***** start checking Team management ********/
					selenium.open(ps.getTeam_allocation_url());
					Thread.sleep(2000);
					if(selenium.isTextPresent("Team management")){
						bodyText = "<h3>Go to Team management passeed</h3><br>";
						WriteLogFile.logger.info("Go to Team management passeed");
					}else{
						bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getTeam_allocation_url()+"</h3>";
						WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getTeam_allocation_url());
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					try {
						
						WebElement tableAllocation = driver.findElement(By.className("allocation_table_team_allocation"));
						if(tableAllocation.isDisplayed()){
							bodyText = "<h3>Go to Team management check table passeed</h3><br>";
							WriteLogFile.logger.info("Go to Team management check table passeed");
							int TableRowCount = selenium.getXpathCount("//table[@class='allocation_table_team_allocation']/tbody/tr").intValue();
							if(TableRowCount > 1){
								ArrayList<String> listUser = new ArrayList<String>();
								for(int i = 2 ; i < TableRowCount+1 ; i++){
									WebElement user = driver.findElement(By.xpath("//div[@id='team_allocation']/table[@class='allocation_table_team_allocation']/tbody/tr["+i+"][@class='allocation_user_row']/td[1]/div/a"));
									listUser.add(user.getAttribute("title"));
								}
								if(listUser.size() > 0){
									boolean checkDupplicate = false;
									String userDupplicate = "";
									for(int j = 0; j < listUser.size();j++){
											if(j == listUser.size() - 1){
												break;
											}
											if(listUser.get(j).equalsIgnoreCase(listUser.get(j+1))){
												userDupplicate = listUser.get(j);
												checkDupplicate = true;
												break;
											}
									}
									if(checkDupplicate){
										bodyText = "<h3 style=\"color:red\">Dupplicated user "+  userDupplicate + " in :</h3>"  +ps.getTeam_allocation_url();
										WriteLogFile.logger.info("Dupplicated user "+  userDupplicate + " in :"  +ps.getTeam_allocation_url());
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}else{
										bodyText = "<h3>Go to Team management check dupplicated user passeed</h3><br>";
										WriteLogFile.logger.info("Go to Team management check dupplicated user passeed");
									}
								}
							}
						}else{
							bodyText = "<h3 style=\"color:red\">The display of tableAllocation "+ps.getTeam_allocation_url()+ " is not right</h3>";
							WriteLogFile.logger.info("The display of tableAllocation: " +ps.getTeam_allocation_url() +" is not right");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText = "<h3 style=\"color:red\">The display of tableAllocation is missing in :</h3> "+ps.getTeam_allocation_url()+ " ";
						WriteLogFile.logger.info("The display of tableAllocation is missing in: " + ps.getTeam_allocation_url() +" ");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
				/***** end checking Team management ********/
				
					
				/******* start checking payroll page ******/
				selenium.open(ps.getPayroll_url());
				Thread.sleep(1000);
				if(selenium.isTextPresent("Payroll dates")){
					bodyText = "<h3>Go to Payroll passeed</h3><br>";
					WriteLogFile.logger.info("Go to Payroll passeed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getPayroll_url()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getPayroll_url());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				try {
					WebElement tablePayroll = driver.findElement(By.id("table_bank"));
					if(tablePayroll.isDisplayed()){
						try {
							WebElement buttonAdd = tablePayroll.findElement(By.xpath("//td[@id='addRow' and @class='blank']/a[@class='goButton']"));
							if(buttonAdd.isDisplayed()){
								buttonAdd.click();
								Thread.sleep(1000);
								try {
									WebElement popupAdd = driver.findElement(By.id("searchFormAdd"));
									if(popupAdd.isDisplayed()){
										WebElement dropdowDateMonth = popupAdd.findElement(By.id("dateMonth"));
										List<WebElement> options = dropdowDateMonth.findElements(By.tagName("option"));
										 int YearCurrent = Calendar.getInstance().get(Calendar.YEAR);
										 String pastYear = Integer.toString(YearCurrent -1 );
										 System.out.println(pastYear);
										 String NextfurtureYear = Integer.toString(YearCurrent+2);
										 System.out.println(NextfurtureYear);
										 boolean checkYear = true;
										 for(WebElement option : options){
											 String year = option.getText().split("-")[1];
											 System.out.println(year);
											 if(year.equalsIgnoreCase(pastYear) || year.equalsIgnoreCase(NextfurtureYear)){
												 checkYear = false;
												 break;
											 }
										 }
										 if(checkYear){
											 	bodyText = "<h3>Go to Payroll check dropdowlist in popup when click add button passeed</h3><br>";
												WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
										 }else{
												bodyText = "<h3 style=\"color:red\">The display of dropdown when click add button is wrong<h3>";
												WriteLogFile.logger.info("The display of  dropdown when  add button is wrong");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										 }
									}else{
										bodyText = "<h3 style=\"color:red\">The display of popup when click add button is missing<h3>";
										WriteLogFile.logger.info("The display of  popup when  add button is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
									}
								} catch (Exception e) {
									bodyText = "<h3 style=\"color:red\">The display of popup when click add button is missing<h3>";
									WriteLogFile.logger.info("The display of  popup when  add button is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
								}
								
								
								
							}else{
								bodyText = "<h3 style=\"color:red\">The display of add button is missing<h3>";
								WriteLogFile.logger.info("The display of add button is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
							}
						} catch (Exception e) {
							bodyText = "<h3 style=\"color:red\">The display of add button is missing<h3>";
							WriteLogFile.logger.info("The display of add button is missing");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
						
						int TableRowCount = selenium.getXpathCount("//table[@id='table_bank']/tbody/tr").intValue();
						System.out.println(TableRowCount);
						if(TableRowCount == 2){
							String onlyPayroll = selenium.getText("xpath=//table[@id='table_bank']/tbody/tr[2]/td[2]");
							System.out.println("date" + onlyPayroll);
							DateFormat formatter =  new SimpleDateFormat("dd-MM-yy");
							Date date1 = new Date();
							String datetest = formatter.format(date1);
							Date dateCur = (Date)formatter.parse(datetest);
							Date date2 = (Date)formatter.parse(onlyPayroll.trim());
							if(date2.after(dateCur)){
								try {
									WebElement buttonEdit = driver.findElement(By.xpath("//table[@id='table_bank']/tbody/tr[2]/td[3]/a"));
									if(buttonEdit.isDisplayed()){
										buttonEdit.click();
										Thread.sleep(1000);
										try {
											WebElement popupEdit = driver.findElement(By.id("searchFormEdit"));
											if(popupEdit.isDisplayed()){
												bodyText = "<h3>Go to Payroll check dropdowlist in popup when click edit button passeed</h3><br>";
												WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click edit button passeed");
											}else{
												bodyText = "<h3 style=\"color:red\">The display of popup when click edit button is missing<h3>";
												WriteLogFile.logger.info("The display of  popup when  edit button is missing");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
											}
										} catch (Exception e) {
											bodyText = "<h3 style=\"color:red\">The display of popup when click edit button is missing<h3>";
											WriteLogFile.logger.info("The display of  popup when  edit button is missing");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										}
									}else{
										bodyText = "<h3 style=\"color:red\">The display of  edit button in payroll is missing<h3>";
										WriteLogFile.logger.info("The display of  edit button in payroll is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");				
									}
								} catch (Exception e) {
									bodyText = "<h3 style=\"color:red\">The display of  edit button in payroll is missing<h3>";
									WriteLogFile.logger.info("The display of  edit button in payroll is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");			
								}
								
							}else{
								bodyText = "<h3>Go to Payroll check  popup when click edit button because the payroll date is the past date not show the edit button passeed</h3><br>";
								WriteLogFile.logger.info("Go to Payroll check  popup when click edit button because the payroll date is the past date not show the edit button passeed");	
							}
						}else if(TableRowCount > 2){
							try {
								WebElement buttonEdit = driver.findElement(By.xpath("//table[@id='table_bank']/tbody/tr[3]/td[3]/a"));
								if(buttonEdit.isDisplayed()){
									buttonEdit.click();
									Thread.sleep(1000);
									try {
										WebElement popupEdit = driver.findElement(By.id("searchFormEdit"));
										if(popupEdit.isDisplayed()){
											bodyText = "<h3>Go to Payroll check dropdowlist in popup when click edit button passeed</h3><br>";
											WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
										}else{
											bodyText = "<h3 style=\"color:red\">The display of popup when click edit button in payroll is missing<h3>";
											WriteLogFile.logger.info("The display of  popup when  edit button in payroll is missing");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										}
									} catch (Exception e) {
										bodyText = "<h3 style=\"color:red\">The display of popup when click edit button in payroll is missing<h3>";
										WriteLogFile.logger.info("The display of  popup when  edit button in payroll is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
									}
								}else{
									bodyText = "<h3 style=\"color:red\">The display of edit button  is missing<h3>";
									WriteLogFile.logger.info("The display of  edit button is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");		
								}
							} catch (Exception e) {
								e.printStackTrace();
								bodyText = "<h3 style=\"color:red\">The display of edit button in payroll  is missing<h3>";
								WriteLogFile.logger.info("The display of  edit button in payroll is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
							}
							
						}else{
							bodyText = "<h3>Go to Payroll check dropdowlist in popup when click edit button passeed</h3><br>";
							WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
						}
					}else{
						bodyText = "<h3 style=\"color:red\">The display of tablePayroll is missing<h3>";
						WriteLogFile.logger.info("The display of tablePayroll is missing");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					e.printStackTrace();
					bodyText = "<h3 style=\"color:red\">The display of tablePayroll is missing<h3>";
					WriteLogFile.logger.info("The display of tablePayroll is missing");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}	
					
					
				/******** end checking payroll page *******/	
				
				/******** start checking bank holiday page *******/
				selenium.open(ps.getBank_holiday_url());
				Thread.sleep(1000);
				if(selenium.isTextPresent("Bank holidays")){
					bodyText = "<h3>Go to Bank holiday passeed</h3><br>";
					WriteLogFile.logger.info("Go to Bank holiday passeed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website "+ps.getBank_holiday_url()+"</h3>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getBank_holiday_url());
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				try {
					WebElement tableBank = driver.findElement(By.id("table_bank"));
					if(tableBank.isDisplayed()){
						try {
							WebElement buttonAdd = tableBank.findElement(By.xpath("//td[@id='addRow' and @class='blank']/a[@class='goButton']"));
							if(buttonAdd.isDisplayed()){
								buttonAdd.click();
								Thread.sleep(1000);
								try {
									WebElement popupAdd = driver.findElement(By.id("searchFormAdd"));
									if(popupAdd.isDisplayed()){
										try {
											WebElement submitButton = popupAdd.findElement(By.id("submitButton"));
											if(submitButton.isDisplayed()){
												bodyText = "<h3>Go to Bank holiday test function Add button is show popup passeed</h3><br>";
												WriteLogFile.logger.info("Go to Bank holiday test function Add button is show popup passeed");
											}else{
												bodyText = "<h3 style=\"color:red\">The display of submit button in popup when click Add button in bank holiday  is missing<h3>";
												WriteLogFile.logger.info("The display of submit button in popup when click Add button in bank holiday  is missing");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
											}
										} catch (Exception e) {
											bodyText = "<h3 style=\"color:red\">The display of submit button in popup when click Add button in bank holiday  is missing<h3>";
											WriteLogFile.logger.info("The display of submit button in popup when click Add button in bank holiday  is missing");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										}
										
										
									}else{
										bodyText = "<h3 style=\"color:red\">The display of popup when click add button in bank holiday is missing<h3>";
										WriteLogFile.logger.info("The display of  popup when  add button in bank holiday is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
									}
								} catch (Exception e) {
									bodyText = "<h3 style=\"color:red\">The display of popup when click add button in bank holiday is missing<h3>";
									WriteLogFile.logger.info("The display of  popup when  add button in bank holiday is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
								
								
							}else{
								bodyText = "<h3 style=\"color:red\">The display of add button in bank holiday  is missing<h3>";
								WriteLogFile.logger.info("The display of  add button in bank holiday is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
							}
						} catch (Exception e) {
							bodyText = "<h3 style=\"color:red\">The display of add button in bank holiday  is missing<h3>";
							WriteLogFile.logger.info("The display of  add button in bank holiday is missing");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
						}
						
						int TableRowCount = selenium.getXpathCount("//table[@id='table_bank']/tbody/tr").intValue();
						if(TableRowCount > 1){
							try {
								WebElement buttonEdit = driver.findElement(By.xpath("//div[@id='table_info']/table[@id='table_bank']/tbody/tr[2]/td[3][@class='blank']/a[@class='goButton']"));
								if(buttonEdit.isDisplayed()){
									buttonEdit.click();
									Thread.sleep(1000);
									try {
										WebElement popupEdit = driver.findElement(By.id("searchFormEdit"));
										if(popupEdit.isDisplayed()){
											try {
												WebElement buttonUpdate = driver.findElement(By.xpath("//div[@id='searchFormDiv']/table/tbody/tr[3]/td[@id='checktd' and @class='blank']/a"));
												if(buttonUpdate.isDisplayed()){
													bodyText = "<h3>Go to Bank holiday all funciton passed</h3><br>";
													WriteLogFile.logger.info("Go to Bank holiday all function is passed");
												}else{
													System.out.println("gogo");
													bodyText = "<h3 style=\"color:red\">The display of update button in popop when click EDIT button in bank holiday  is missing<h3>";
													WriteLogFile.logger.info("The display of update button in popop when click EDIT button in bank holiday  is missing");
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}
											} catch (Exception e) {
												bodyText = "<h3 style=\"color:red\">The display of update button in popop when click EDIT button in bank holiday  is missing<h3>";
												WriteLogFile.logger.info("The display of update button in popop when click EDIT button in bank holiday  is missing");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
										}else{
											bodyText = "<h3 style=\"color:red\">The display of popup when click eidt button in bank holiday is missing<h3>";
											WriteLogFile.logger.info("The display of  popup when  edit button in bank holiday is missing");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										}
									} catch (Exception e) {
										bodyText = "<h3 style=\"color:red\">The display of popup when click eidt button in bank holiday is missing<h3>";
										WriteLogFile.logger.info("The display of  popup when  edit button in bank holiday is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
									
								}else{
									bodyText = "<h3 style=\"color:red\">The display of edit button in bank holiday  is missing<h3>";
									WriteLogFile.logger.info("The display of  edit button in bank holiday is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">The display of edit button in bank holiday  is missing<h3>";
								WriteLogFile.logger.info("The display of  edit button in bank holiday is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}else{
							bodyText = "<h3>Go to Bank holiday there was no bank holidays in table so we can't check the function edit ! Other function is passed</h3><br>";
							WriteLogFile.logger.info("Go to Bank holiday there was no bank holidays in table so we can't check the function edit ! Other function is passed");
						}
					}else{
						bodyText = "<h3 style=\"color:red\">The display of table bank holiday is missing<h3>";
						WriteLogFile.logger.info("The display of table bank holiday is missing");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">The display of table bank holiday is missing<h3>";
					WriteLogFile.logger.info("The display of table bank holiday is missing");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				
				/******** end checking bank holiday page *******/
				
				
				/****** start checking administrator dropbox *****/
				
				selenium.open(ps.getDropbox_administrator());
				
				Thread.sleep(2000);
				String dropboxRandom = null;
				if(selenium.isTextPresent("Drop box administration")){
					bodyText = "<h3>Go to dropbox administrator passed</h3><br>";
					WriteLogFile.logger.info("<h3>Go to dropbox administrator passed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website :"+ ps.getDropbox_administrator() + "<h3>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_administrator() + "<h3>");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				try {
					WebElement tableAdmin = driver.findElement(By.id("dropbox"));
					if(tableAdmin.isDisplayed()){
						try {
							WebElement buttonAddDropbox = tableAdmin.findElement(By.id("btnAdd"));
							if(buttonAddDropbox.isDisplayed()){
								buttonAddDropbox.click();
								Thread.sleep(1000);
								try {
									WebElement popupAddDropbox = driver.findElement(By.id("addDropUser"));
									if(popupAddDropbox.isDisplayed()){
										int TableRowCount = selenium.getXpathCount("//table[@id='tblAddDropbox']/tbody/tr").intValue();
										if(TableRowCount >= 5){
											try {
												WebElement selectDuration = driver.findElement(By.id("duration_days"));
												List<WebElement> options = selectDuration.findElements(By.tagName("option"));
												if(options.isEmpty() || options.size() == 0){
													bodyText = "<h3 style=\"color:red\">The select duration in add dropbox popup is null <h3>";
													WriteLogFile.logger.info("The select durations in add dropbox popup is null");
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}else{
													selectDuration.click();
													bodyText = "<h3>Go to dropbox administrator check select duration passed</h3><br>";
													WriteLogFile.logger.info("<h3>Go to dropbox administrator passed");
												}
											} catch (Exception e) {
												bodyText = "<h3 style=\"color:red\">The select duration in add dropbox popup is null <h3>";
												WriteLogFile.logger.info("The select durations in add dropbox popup is null");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
											try {
												WebElement selectOwner = driver.findElement(By.id("slOwner"));
												List<WebElement> options = selectOwner.findElements(By.tagName("option"));
												if(options.isEmpty() || options.size() == 0){
													bodyText = "<h3 style=\"color:red\">The select owner in add dropbox popup is null <h3>";
													WriteLogFile.logger.info("The select owner in add dropbox popup is null");
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}else{
													selectOwner.click();
													Thread.sleep(1000);
													buttonAddDropbox.click();
													Thread.sleep(1000);
													bodyText = "<h3>Go to dropbox administrator check select owner passed</h3><br>";
													WriteLogFile.logger.info("<h3>Go to dropbox administrator passed");
												}
											} catch (Exception e) {
												bodyText = "<h3 style=\"color:red\">The select owner in add dropbox popup is null <h3>";
												WriteLogFile.logger.info("The select owner in add dropbox popup is null");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
											
											
										}else{
											bodyText = "<h3 style=\"color:red\">The display of table in popup add dropbox when click Add button in dropbox administrator is wrong<h3>";
											WriteLogFile.logger.info("The display of table in popup add dropbox when click Add button in dropbox administrator is wrong");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
									}else{
										bodyText = "<h3 style=\"color:red\">The display of popup add dropbox when click Add button in dropbox administrator is missing<h3>";
										WriteLogFile.logger.info("The display of popup add dropbox when click Add button in dropbox administrator is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
								} catch (Exception e) {
									bodyText = "<h3 style=\"color:red\">The display of popup add dropbox when click Add button in dropbox administrator is missing<h3>";
									WriteLogFile.logger.info("The display of popup add dropbox when click Add button in dropbox administrator is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							}else{
								bodyText = "<h3 style=\"color:red\">The button add dropbox in dropbox administrator is missing<h3>";
								WriteLogFile.logger.info("The button add  dropbox in dropbox administrator is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						} catch (Exception e) {
							bodyText = "<h3 style=\"color:red\">The button add dropbox in dropbox administrator is missing<h3>";
							WriteLogFile.logger.info("The button add  dropbox in dropbox administrator is missing");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
						
						int TableRowCount = selenium.getXpathCount("//table[@id='dropbox']/tbody/tr").intValue();
						if(TableRowCount > 0){
							try {
								dropboxRandom = selenium.getText("xpath=//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[2]");
								WebElement buttonAddUser = driver.findElement(By.xpath("//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[5][@class='blank']/input[@class='dropboxInfo']"));
								if(buttonAddUser.isDisplayed()){
									buttonAddUser.click();
									Thread.sleep(1000);
									try {
										WebElement popupAddUser = driver.findElement(By.id("editDropUser"));
										if(popupAddUser.isDisplayed()){
											try {
												WebElement btnAddUser = popupAddUser.findElement(By.id("btnAddUser"));
												if(btnAddUser.isDisplayed()){
													btnAddUser.click();
													Thread.sleep(2000);
													WebElement viewDropUser = driver.findElement(By.id("viewDropUser"));
													if(viewDropUser.isDisplayed()){
														selenium.type("listMail", "lan.ta@c-mg.com");
														selenium.click("btnCheckMail");
														Thread.sleep(5000);
														WebElement tableShow = driver.findElement(By.id("MailUsertbl"));
														if(tableShow.isDisplayed()){
															bodyText = "<h3>Go to dropbox administrator all funciton passed</h3><br>";
															WriteLogFile.logger.info("Go to dropbox administrator all function is passed");
														}else{
															bodyText = "<h3 style=\"color:red\">The table when click btnCheckMail in dropbox administrator is missing<h3>";
															WriteLogFile.logger.info("The table when click btnCheckMail in dropbox administrator is missing");
															SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
														}
													}else{
														bodyText = "<h3 style=\"color:red\">The popup when click btnAddUser in dropbox administrator is missing<h3>";
														WriteLogFile.logger.info("The popup when click btnAddUser in dropbox administrator is missing");
														SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
													}
												}else{
													bodyText = "<h3 style=\"color:red\">The button btnAddUser  in dropbox administrator is missing<h3>";
													WriteLogFile.logger.info("The button btnAddUser in dropbox administrator is missing");
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}
											} catch (Exception e) {
												bodyText = "<h3 style=\"color:red\">There are some error in function add user to dropbox<h3>";
												WriteLogFile.logger.info("There are some error in function add user to dropbox");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
										}else{
											bodyText = "<h3 style=\"color:red\">The display of popup add user when click Add user to dropbox in dropbox administrator is missing<h3>";
											WriteLogFile.logger.info("The display of popup add user when click Add user to dropbox in dropbox administrator is missing");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
									} catch (Exception e) {
										bodyText = "<h3 style=\"color:red\">The display of popup add user when click Add user to dropbox in dropbox administrator is missing<h3>";
										WriteLogFile.logger.info("The display of popup add user when click Add user to dropbox in dropbox administrator is missing");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
								}else{
									bodyText = "<h3 style=\"color:red\">The button add user to dropbox in dropbox administrator is missing<h3>";
									WriteLogFile.logger.info("The button add user in dropbox administrator is missing");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">The button add user to dropbox in dropbox administrator is missing<h3>";
								WriteLogFile.logger.info("The button add user in dropbox administrator is missing");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}else{
							bodyText = "<h3>Go to dropbox administrator can't not check function add user because in this moment there are no dropbox!Other function is passed</h3><br>";
							WriteLogFile.logger.info("<h3>Go to dropbox administrator can't not check function add user because in this moment there are no dropbox!Other function is passed");
						}
						
					}else{
						bodyText = "<h3 style=\"color:red\">The table in dropbox administrator is missing<h3>";
						WriteLogFile.logger.info("The table in dropbox administrator is missing");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">The table in dropbox administrator is missing<h3>";
					WriteLogFile.logger.info("The table in dropbox administrator is missing");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				
				
				/****** end checking administrator dropbox *****/
				
				
				/****** start checking dropboxes ********/
				selenium.open(ps.getDropbox_url());
				Thread.sleep(1000);
				if(selenium.isTextPresent("Dropboxes")){
					bodyText = "<h3>Go to dropbox download page passed</h3><br>";
					WriteLogFile.logger.info("<h3>Go to dropbox download page passed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website :"+ ps.getDropbox_url() + "<h3>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_url() + "<h3>");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				if(dropboxRandom == null || dropboxRandom.trim().length() == 0){
					String notifi = selenium.getText("xpath=//div[@id='content']/div");
					if(notifi.equalsIgnoreCase("No dropbox found.")){
						bodyText = "<h3>Go to dropbox download page passed,there is no dropbox so we can't check main function</h3><br>";
						WriteLogFile.logger.info("<h3>Go to dropbox download page passed,there is no dropbox so we can't check main function");
					}else{
						bodyText = "<h3 style=\"color:red\">There are some error(when no dropbox found) in " +ps.getDropbox_url();
						WriteLogFile.logger.info("There are some error(when no dropbox found) in :"+ ps.getDropbox_url() + "<h3>");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				}else{
					try {
						WebElement listOfDroboxes = driver.findElement(By.id("dropbox_list_sl"));
						if(listOfDroboxes.isDisplayed()){
							boolean checkExistDropbox = false;
							List<WebElement> options = listOfDroboxes.findElements(By.tagName("option"));
							if(options.size() > 0){
								for(WebElement opt : options){
									if(opt.getText().equalsIgnoreCase(dropboxRandom)){
										checkExistDropbox = true;
									}
								}
								if(!checkExistDropbox){
									bodyText = "<h3 style=\"color:red\">Data in the dropdowlist of dropbox is not equal with dropbox administrator in " +ps.getDropbox_url();
									WriteLogFile.logger.info("Data in the dropdowlist of dropbox is not equal with dropbox administrator  in :"+ ps.getDropbox_url() + "<h3>");
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}else{
									bodyText = "<h3>Go to dropbox download page check dropdowlist passed</h3><br>";
									WriteLogFile.logger.info("<h3>Go to dropbox download page check dropdowlist passed");
								}
							}else{
								bodyText = "<h3 style=\"color:red\">Can not find any data in the dropdowlist of dropbox in " +ps.getDropbox_url();
								WriteLogFile.logger.info("Can not find any data in the dropdowlist of dropbox in :"+ ps.getDropbox_url() + "<h3>");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}else{
							bodyText = "<h3 style=\"color:red\">Can not find the dropdowlist of dropbox in " +ps.getDropbox_url();
							WriteLogFile.logger.info("Can not find the dropdowlist of dropbox in :"+ ps.getDropbox_url() + "<h3>");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText = "<h3 style=\"color:red\">Can not find the dropdowlist of dropbox in " +ps.getDropbox_url();
						WriteLogFile.logger.info("Can not find the dropdowlist of dropbox in :"+ ps.getDropbox_url() + "<h3>");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					try {
						WebElement DisplayUpload = driver.findElement(By.id("file_drop_zone"));
						if(DisplayUpload.isDisplayed()){
							try {
								WebElement buttonUpload = DisplayUpload.findElement(By.id("file_input_drop"));
								String type = buttonUpload.getAttribute("type");
								if(type.equalsIgnoreCase("file")){
									bodyText = "<h3>Go to dropbox download page check button upload passed</h3><br>";
									WriteLogFile.logger.info("<h3>Go to dropbox download page check button upload passed");
								}else{
									bodyText = "<h3 style=\"color:red\">In side The upload file have some error like the button of upload is not type = file : " +ps.getDropbox_url();
									WriteLogFile.logger.info("In side The upload file have some error like the button of upload is not type = file : " +ps.getDropbox_url());
									SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText = "<h3 style=\"color:red\">In side The upload file have some error : " +ps.getDropbox_url();
								WriteLogFile.logger.info("Inside The upload file have some error  :"+ ps.getDropbox_url() + "<h3>");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}else{
							bodyText = "<h3 style=\"color:red\">Can not find the location of upload file  : " +ps.getDropbox_url();
							WriteLogFile.logger.info("Can not find the location of upload file  :"+ ps.getDropbox_url() + "<h3>");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
						}
					} catch (Exception e) {
						bodyText = "<h3 style=\"color:red\">The upload file have some error : " +ps.getDropbox_url();
						WriteLogFile.logger.info("The upload file have some error  :"+ ps.getDropbox_url() + "<h3>");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					try {
						WebElement tableDownload = driver.findElement(By.id("dropbox"));
						if(tableDownload.isDisplayed()){
							int TableRowCount = selenium.getXpathCount("//table[@id='dropbox']/tbody/tr").intValue();
							if(TableRowCount > 0){
								String filetext = selenium.getText("xpath=//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[2]");
								if(filetext.equalsIgnoreCase("No file found.")){
									bodyText = "<h3>Go to dropbox download page check table download passed</h3><br>";
									WriteLogFile.logger.info("<h3>Go to dropbox download page check table download passed");
								}else{
									try {
										WebElement inforButton = driver.findElement(By.xpath("//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[1]/input[1][@class='dropbox_info']"));
										if(inforButton.isDisplayed()){
											inforButton.click();
											Thread.sleep(2000);
											try {
												WebElement popupInfor = driver.findElement(By.id("dropbox_file_info"));
												if(popupInfor.isDisplayed()){
													int rowInsde = selenium.getXpathCount("//div[@id='dropbox_file_info']/div[2][@class='dropbox_file_info_mid']/table/tbody/tr").intValue();
													if(rowInsde == 3){
														bodyText = "<h3>Go to dropbox download page, the popup infor is passed</h3><br>";
														WriteLogFile.logger.info("<h3>Go to dropbox download page, the popup infor is passed");
													}else{
														bodyText = "<h3 style=\"color:red\">The table of infor popup have " + rowInsde +" <tr> : " +ps.getDropbox_url();
														WriteLogFile.logger.info("The table of infor popup have " + rowInsde +" <tr> : " +ps.getDropbox_url());
														SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
													}
												}else{
													bodyText = "<h3 style=\"color:red\">Can not find the popup infor when click to the button infor in table download : " +ps.getDropbox_url();
													WriteLogFile.logger.info("Can not find the popup infor when click to the button infor in table download :"+ ps.getDropbox_url() + "<h3>");
													SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}
											} catch (Exception e) {
												bodyText = "<h3 style=\"color:red\">Can not find the popup infor when click to the button infor in table download : " +ps.getDropbox_url();
												WriteLogFile.logger.info("Can not find the popup infor when click to the button infor in table download :"+ ps.getDropbox_url() + "<h3>");
												SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
											}
										}else{
											bodyText = "<h3 style=\"color:red\">Can not find the button infor in table download : " +ps.getDropbox_url();
											WriteLogFile.logger.info("Can not find the button infor in table download  :"+ ps.getDropbox_url() + "<h3>");
											SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
									} catch (Exception e) {
										bodyText = "<h3 style=\"color:red\">Can not find the button infor in table download : " +ps.getDropbox_url();
										WriteLogFile.logger.info("Can not find the button infor in table download  :"+ ps.getDropbox_url() + "<h3>");
										SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
									
								}
							}
						}else{
							bodyText = "<h3 style=\"color:red\">Can not find the table of download : " +ps.getDropbox_url();
							WriteLogFile.logger.info("Can not find the table of download  :"+ ps.getDropbox_url() + "<h3>");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText = "<h3 style=\"color:red\">Can not find the table of download : " +ps.getDropbox_url();
						WriteLogFile.logger.info("Can not find the table of download  :"+ ps.getDropbox_url() + "<h3>");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
					
				}
				
				/****** end checking dropboxes ********/
				
				
				/******* start checking dropbox configuration *******/
				selenium.open(ps.getDropbox_configuration());
				Thread.sleep(1000);
				if(selenium.isTextPresent("Dropbox configuration")){
					bodyText = "<h3>Go to dropbox configuration page passed</h3><br>";
					WriteLogFile.logger.info("<h3>Go to dropbox configuration page passed");
				}else{
					bodyText = "<h3 style=\"color:red\">Cannot open website :"+ ps.getDropbox_configuration() + "<h3>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_configuration() + "<h3>");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				try {
					WebElement table = driver.findElement(By.xpath("//div[@id='dropbox_configuration']/div[1][@class='dropbox_configuration_left_content']/table"));
					if(table.isDisplayed()){
						int tableRow = selenium.getXpathCount("//div[@id='dropbox_configuration']/div[1][@class='dropbox_configuration_left_content']/table/tbody/tr").intValue();
						if(tableRow == 5){
							String defaultValue = selenium.getText("xpath=//span[@id='defaultFileExpiryVal' and @class='view_value']");
							System.out.println(defaultValue);
							if(defaultValue.equalsIgnoreCase("90 days")){
								bodyText = "<h3>Go to dropbox configuration page the value is 90 passed</h3><br>";
								WriteLogFile.logger.info("<h3>Go to dropbox configuration the value is 90 page passed");
							}else{
								bodyText = "<h3 style=\"color:red\">the value default is not equal with 90 in table configuration: " +ps.getDropbox_configuration();
								WriteLogFile.logger.info("the value default is not equal with 90 in table configuration:"+ ps.getDropbox_configuration() + "<h3>");
								SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						}else{
							bodyText = "<h3 style=\"color:red\">the table configuration is not display right: " +ps.getDropbox_configuration();
							WriteLogFile.logger.info(" the table configuration is not display right  :"+ ps.getDropbox_configuration() + "<h3>");
							SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					}else{
						bodyText = "<h3 style=\"color:red\">Can not find the table configuration : " +ps.getDropbox_configuration();
						WriteLogFile.logger.info("Can not find the table configuration  :"+ ps.getDropbox_configuration() + "<h3>");
						SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText = "<h3 style=\"color:red\">Can not find the table configuration : " +ps.getDropbox_configuration();
					WriteLogFile.logger.info("Can not find the table configuration  :"+ ps.getDropbox_configuration() + "<h3>");
					SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				
				/******* end checking dropbox configuration *******/
				
				
				
			}
			driver.quit();
			
			
		}
		
		
	}
	
}

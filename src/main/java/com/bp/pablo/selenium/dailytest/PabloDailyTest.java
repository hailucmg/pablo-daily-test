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
			boolean continueCheck = true;
//			selenium.open(ps.getMain_url());
//			selenium.setTimeout("200000");
//			if(selenium.isTextPresent("Access to Pablo")){
//				 //bodyText =" Beginning of automation test for Pablo: "+ ps.getMain_url()+" </h3><br>";
//				 WriteLogFile.logger.info("Beginning of automation test for Pablo");
//				 continueCheck = true;
//			}else{
//				bodyText = " Server was broken. Cannot open website "+ps.getMain_url()+"</h3>";
//				WriteLogFile.logger.info("Server was broken. Can not open website");
//				SendMailSSL.sendMailCMG(bodyText, "Pablo server broken");
//			}
//			/********* end checking server is still alive **********/
			
			if(continueCheck){
				
				/*********checking login function is still alive **********/
				bodyText+=" <h3>Go to Login</h3><br>";
				selenium.open(ps.getLogin_url());
				Thread.sleep(1000);
				selenium.type("_request_username", acc.getUsername_admin());
				selenium.type("_request_password", acc.getPassword_admin());
				selenium.click("check_term");
				selenium.click("doauth");
				Thread.sleep(2000);
				selenium.waitForPageToLoad("2000");
				try {
					
					WebElement logout = driver.findElement(By.xpath("//div[@id='footernav']/span[2][@class='fakelink']/a"));
					String attr = logout.getText();
					if(attr.equalsIgnoreCase("logout")){
						 bodyText +="Test function login with user :"+acc.getUsername_admin()+" : PASSED<br>";
						 WriteLogFile.logger.info("Test function login passed");
					}else{
						bodyText+="<h2>The Link of logout is not equal with 'logout'</h2><br>";
					}
				} catch (Exception e) {
					bodyText+="Test function login with user :"+acc.getUsername_admin()+": FAILED<br>";
				}
				 try {
					 bodyText+="<h3>Go to Home Page</h3><br>";
					 /*********checking homepage function is still alive **********/
					selenium.open(ps.getHome_url());
					Thread.sleep(2000);
					WebElement payrollNextFurture = driver.findElement(By.xpath("//div[@id='block-1' and @class='block']/h1[@class='draghandle']"));
					String payrollTitle = payrollNextFurture.getText();
					if(!payrollTitle.equals("Next payroll cut-off")){
						bodyText +="Test Next payroll cutoff table: If payroll cutoff is in the future, it should be shown in homepage: FAILED <br>";
					}else{
						 bodyText +="Test Next payroll cutoff table: If payroll cutoff is in the future, it should be shown in homepage: PASSED <br>";
						 WriteLogFile.logger.info("Test function homepage passed");
					}
				} catch (Exception e) {
					bodyText +="Test Next payroll cutoff table: If payroll cutoff is in the future, it should be shown in homepage: FAILED <br>";
				}
				/********* end checking login function is still alive **********/
				
				
				
				/********* checking diary main view function is still alive **********/
				bodyText+=" <br><h3>Go to Diary center main view</h3><br>";
				selenium.open(ps.getMainview_diary_url());
				Thread.sleep(10000);
				if(selenium.isTextPresent("Diary centre")){
					 bodyText +="Loading Diary center: PASSED<br>";
					 WriteLogFile.logger.info("Test function main diary");
					/**** checking dropdowlist is working well *****/
					List<String> ListvalueOfdropdowList = new ArrayList<String>();
					try {
						//bodyText =" Test function main diary dropdow list at top </h3><br>";
						WriteLogFile.logger.info("Test function main diary dropdow list at top");
						WebElement dropdowList = driver.findElement(By.id("departmentList"));
						List<WebElement> options = dropdowList.findElements(By.tagName("option"));
						if(options!=null){
							for(WebElement option : options){
								if(!option.getAttribute("value").equalsIgnoreCase("department")){
									ListvalueOfdropdowList.add(option.getText());
								}
							}
							bodyText += "Test function dropdow list at top : PASSED<br>";
							WriteLogFile.logger.info("Test function main diary dropdow list at top passed");
						}else{
							bodyText += "Test function dropdow list at top : FAILED <br>";
							WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
						}
					} catch (Exception e) {
						bodyText += "Test function dropdow list at top : FAILED<br>";
						WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
					}
					/********* end checking dropdowlist *******/
					
					/***** begin checking case work function *******/
					Thread.sleep(10000);
					WebElement caseWork = driver.findElement(By.id("DiaryArea"));
					String notifi = caseWork.getText();
					if(notifi.contains("There was a communication error:")){
						bodyText += "Cannot loading case work of "+ acc.getUsername_admin()+" in " + ps.getMainview_diary_url() +" at the time : "+ System.currentTimeMillis() +"<br>";
						WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
					}
						/*** check function popup of table diary_table ****/
						//bodyText =" Test function main diary table diary </h3><br>";
						WriteLogFile.logger.info("Test function main diary table diary");
						boolean displayTable = false;
						System.out.println(ListvalueOfdropdowList.size());
						for(int i = 0; i < ListvalueOfdropdowList.size() ; i ++ ){
							if(displayTable){
								break;
							}
							WebElement dropdowList = driver.findElement(By.id("departmentList"));
							List<WebElement> options = dropdowList.findElements(By.tagName("option"));
							for(WebElement option : options){
								System.out.println(option);
							    if(option.getText().equalsIgnoreCase(ListvalueOfdropdowList.get(i))){
							        option.click();
							        Thread.sleep(40000);
							        WebElement tableCase = null;
							        try {
							        	tableCase = driver.findElement(By.id("diary_table"));
									} catch (Exception e) {
										String note = caseWork.getText();
										if(note.contains("There was a communication error:")){
											bodyText += "Cannot loading case work of "+option.getText() +" in " + ps.getMainview_diary_url() +" at the time : "+ System.currentTimeMillis() +"<br>";
											WriteLogFile.logger.info("Cannot loading case work in " + ps.getMainview_diary_url());
										}else{
											bodyText +="Change to the next item in dropdown list : PASSED </h3><br>";
										}
										continue;
									}
									if(tableCase.isDisplayed()){
										displayTable = true;
										int TableRowCount = selenium.getXpathCount("//table[@id='diary_table']/tbody/tr").intValue();
										if(TableRowCount > 0 ){
											/** check when click to button checkPriority ****/
											//bodyText =" Test function main diary table diary click to button check priority</h3><br>";
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
													bodyText += "Click on arrow: popup should be shown and the Reallocated list is not empty : FAILED<br>";
													WriteLogFile.logger.info(">The dropdown list is empty when click to the button Reallocate To in " + ps.getMainview_diary_url());
													
												}else{
													bodyText +="Click on arrow: popup should be shown and the Reallocated list is not empty : PASSED<br>";
													WriteLogFile.logger.info("Test function main diary table diary click to button check priority passed");
												}
											}else{
												bodyText += "Click on arrow : popup should be shown and the Reallocated list is not empty : FAILED<br>";
												WriteLogFile.logger.info("The popup is not display when click  to the button priority  in" + ps.getMainview_diary_url());
											}
											
											/*** end check when click button checkPriority ****/
											
											/**** check popup when click to td of table ****/
											//bodyText =" Test function main diary table diary click to random td in table</h3><br>";
											WriteLogFile.logger.info("Test function main diary table diary click to random td in table");
											Thread.sleep(1000);
											try {
												selenium.click("xpath=//tr[@id='diary_row_id0']/td[2][@class='DiaryCell DiaryRow']");
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
															bodyText += " Click on the row: Popup should be shown with correct text : FAILED<br>";
															WriteLogFile.logger.info("The title of popup is not display right when click to the table case work in " + ps.getMainview_diary_url());
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
															bodyText += "Click on the row: Popup should be shown with correct text : FAILED<br>";
															WriteLogFile.logger.info("The data of popup is not display right when click to the table case work in " + ps.getMainview_diary_url());
														}
														
														if(checkTitle == true && checkData == true){
															bodyText +=" Click on the row: Popup should be shown with correct text : PASSED <br>";
															WriteLogFile.logger.info("Test function main diary table diary click to random td in table passeds");
														}
														
													}else{
														bodyText += "Click on the row: Popup should be shown with correct text : FAILED <br>";
														WriteLogFile.logger.info("The popup is not display when click to the table case work in " + ps.getMainview_diary_url());
													}
												}else{
													bodyText += " Click on the row: Popup should be shown with correct text : FAILED<br>";
													WriteLogFile.logger.info("The popup is not display when click to the table case work in " + ps.getMainview_diary_url());
													//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
												}
											} catch (Exception e) {
												bodyText += "Click on the row: Popup should be shown with correct text : FAILED<br>";
												WriteLogFile.logger.info("Cannot open a first row in table main diary");
											}
										
										}
									}
							    }
							}
						}
					
				}else{
					bodyText += "Loading Diary center:FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website");
					
				}
				/*********end checking diary main view function is still alive **********/
				
				/******* start checking Usermapping*****/
				bodyText +=" <br><h3>Go to Usermapping</h3><br>";
				selenium.open(ps.getUsermapping_url());
				Thread.sleep(2000);
				if(selenium.isTextPresent("User mapping")){
					bodyText +="Load usermapping with text Administrator Mapping User  : PASSED <br>";
					WriteLogFile.logger.info("Go to diary configuration passeed");
				}else{
					bodyText += "Load usermapping with text Administrator Mapping User  :  FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getUsermapping_url());
				}
				/***** start check function of unmapped user ****/
				try {
					//bodyText =" Go to diary configuration check unMapped table</h3><br>";
					WriteLogFile.logger.info("Go to Administrator Mapping User check unMapped table");
					WebElement tableUnmap = driver.findElement(By.id("usermapping"));
					if(tableUnmap.isDisplayed()){
						int TableRowCount = selenium.getXpathCount("//table[@id='usermapping']/tbody/tr").intValue();
						if(TableRowCount > 1){
							//start checking open cms user not null
							for(int i = 1 ; i < TableRowCount+1;i++){
								try {
									String xpathTemp = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+i+"]/td[1]";
									String temp = selenium.getText(xpathTemp);
									if(temp.equalsIgnoreCase("")){
										bodyText += "Load usermapping with text opencms Mapping User  :  FAILED in td :"+i+"<br>";
									}
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
							//check BP user
							for(int j = 1; j < TableRowCount+1;j++){
								try {
									String xpathEditMappingBPUser = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[2]/input[@class='BtnDropboxEdit edit']";
									String xpathMappingBPUser = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[2]/input[@class='BtnDropboxLink buttonMap']";
									boolean check = false;
									WebElement buttonEditMapping = driver.findElement(By.xpath(xpathEditMappingBPUser));
									if(buttonEditMapping.isDisplayed()){
										check = true;
										buttonEditMapping.click();
										WebElement popup = driver.findElement(By.id("popupContact"));
										if(popup.isDisplayed()){
											bodyText +="Go to usermapping page click to button usermapping BPUSER : PASSED <br>";
										}else{
											bodyText +="Go to usermapping page click to button usermapping BPUSER : Fail(popup not display) <br>";
										}
									}
									if(!check){
										WebElement buttonMapping = driver.findElement(By.xpath(xpathMappingBPUser));
										if(buttonMapping.isDisplayed()){
											check = true;
											buttonMapping.click();
											WebElement popup = driver.findElement(By.id("popupContact"));
											if(popup.isDisplayed()){
												bodyText +="Go to usermapping page click to button edit mapping BPUSER : PASSED <br>";
											}else{
												bodyText +="Go to usermapping page click to button edit mapping BPUSER : Fail(popup not display)<br>";
											}

										}
									}
									if(!check){
										bodyText +="Go to usermapping page can not find button usermapping or edit mapping in row : " + j +" of column BP user <br>";
									}
									
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
							//check aviary user
							for(int j = 1; j < TableRowCount+1;j++){
								try {
									String xpathEditMappingAviary = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[3]/input[@class='BtnDropboxEdit editAviary']";
									String xpathMappingAviary = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[3]/input[@class='BtnDropboxLink buttonMapAviary']";
									boolean check = false;
									WebElement buttonEditMapping = driver.findElement(By.xpath(xpathEditMappingAviary));
									if(buttonEditMapping.isDisplayed()){
										check = true;
										buttonEditMapping.click();
										WebElement popup = driver.findElement(By.id("popupContact"));
										if(popup.isDisplayed()){
											bodyText +="Go to usermapping page click to button usermapping Aviary : PASSED <br>";
										}else{
											bodyText +="Go to usermapping page click to button usermapping Aviary : Fail(popup not display) <br>";
										}
									}
									if(!check){
										WebElement buttonMapping = driver.findElement(By.xpath(xpathMappingAviary));
										if(buttonMapping.isDisplayed()){
											check = true;
											buttonMapping.click();
											WebElement popup = driver.findElement(By.id("popupContact"));
											if(popup.isDisplayed()){
												bodyText +="Go to usermapping page click to button edit mapping Aviary : PASSED <br>";
											}else{
												bodyText +="Go to usermapping page click to button edit mapping Aviary : Fail(popup not display)<br>";
											}

										}
									}
									if(!check){
										bodyText +="Go to usermapping page can not find button usermapping or edit mapping in row : " + j +"  of column aviary<br>";
									}
									
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
							//checking pensionline user
							for(int j = 1; j < TableRowCount+1;j++){
								try {
									String xpathEditMappingPLUser = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[4]/input[@class='BtnDropboxEdit editPL']";
									String xpathMappingBPPLUser = "xpath=//table[@id='usermapping' and @class='tablesorter']/tbody/tr["+j+"]/td[4]/input[@class='BtnDropboxLink buttonMapPL']";
									boolean check = false;
									WebElement buttonEditMapping = driver.findElement(By.xpath(xpathEditMappingPLUser));
									if(buttonEditMapping.isDisplayed()){
										check = true;
										buttonEditMapping.click();
										WebElement popup = driver.findElement(By.id("popupContact"));
										if(popup.isDisplayed()){
											bodyText +="Go to usermapping page click to button usermapping PL user : PASSED <br>";
										}else{
											bodyText +="Go to usermapping page click to button usermapping PL user : Fail(popup not display) <br>";
										}
									}
									if(!check){
										WebElement buttonMapping = driver.findElement(By.xpath(xpathMappingBPPLUser));
										if(buttonMapping.isDisplayed()){
											check = true;
											buttonMapping.click();
											WebElement popup = driver.findElement(By.id("popupContact"));
											if(popup.isDisplayed()){
												bodyText +="Go to usermapping page click to button edit mapping PL user : PASSED <br>";
											}else{
												bodyText +="Go to usermapping page click to button edit mapping PL user : Fail(popup not display)<br>";
											}

										}
									}
									if(!check){
										bodyText +="Go to usermapping page can not find button usermapping or edit mapping in row : " + j +" of column PL user <br>";
									}
									
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
							
						}
					}else{
						bodyText += "Go to usermapping page check table Administrator and click to button usermapping : FAILED<br>";
						WriteLogFile.logger.info("Can't load  Unmapped User table in :"+ps.getUsermapping_url());
					}
					
				} catch (Exception e) {
					bodyText += " Can't load  Unmapped User table in :"+ ps.getUsermapping_url() +"<br>";
					WriteLogFile.logger.info("Can't load  Unmapped User table in :"+ps.getUsermapping_url());
				}
				
				
				/******* end checking mapping *******/
				
				
				/******* start checking Diary color configuration ****/
				bodyText +=" <br><h3>Diary color configuration</h3><br>";
				selenium.open(ps.getDiary_color_config());
				Thread.sleep(3000);
				if(selenium.isTextPresent("Colours")){
					bodyText += "<br> Go to diary colour configuration : PASSED<br>";
					WriteLogFile.logger.info("Go to diary color configuration passeed");
				}else{
					bodyText += " Go to diary colour configuration : FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getDiary_color_config());
					//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/**** start checking table top ********/
				try {
					WebElement tableTop = driver.findElement(By.id("tableCasecode"));
					if(tableTop.isDisplayed()){
						int TableRowCount = selenium.getXpathCount("//table[@id='tableCasecode']/tbody/tr").intValue();
						if(TableRowCount == 3){
							try {
								//bodyText = " Go to diary colour configuration the table in top have 3 <tr> passeed</h3><br>";
								WriteLogFile.logger.info("Go to diary color configuration the table in top have 3 <tr>  passeed");
								WebElement buttonEditOfMouseOver = tableTop.findElement(By.xpath("table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[1]/td[7][@class='blank']/input[@class='BtnDropboxEdit editColor']"));
								if(buttonEditOfMouseOver.isDisplayed()){
									bodyText += " Go to diary colour configuration check the button edit is not display in the mouseover : FAILED<br>";
									WriteLogFile.logger.info("THe button edit is display in the mouseover in:"+ps.getDiary_color_config());
									//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}else{
									bodyText += " Go to diary colour configuration check the button edit is not display in the mouseover : PASSED<br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is not display in the mouseover passeed");
								}
							} catch (Exception e) {
								bodyText += " Go to diary colour configuration check the button edit is not display in the mouseover : PASSED<br>";
								WriteLogFile.logger.info("Go to diary color configuration check the button edit is not display in the mouseover passeed");
							}
							
							try {
								WebElement buttonEditOfDisclosuer = tableTop.findElement(By.xpath("//table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[2]/td[7][@class='blank']/input[@class='BtnDropboxEdit editColor']"));
								if(buttonEditOfDisclosuer.isDisplayed()){
									bodyText += " Go to diary colour configuration check the button edit is display in the disclosure : PASSED<br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the disclosure passeed");
								}else{
									bodyText += " Go to diary colour configuration check the button edit is display in the disclosure : FAILED<br>";
									WriteLogFile.logger.info("THe button edit is not display in the disclosure in:"+ps.getDiary_color_config());
									//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText += " Go to diary colour configuration check the button edit is display in the disclosure : FAILED<br>";
								WriteLogFile.logger.info("THe button edit is not display in the disclosure in:"+ps.getDiary_color_config());
								//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
							
							try {
								WebElement buttonEditOfpayroll = tableTop.findElement(By.xpath("//table[@id='tableCasecode' and @class='tablesorter']/tbody/tr[3]/td[7][@class='blank']/input[@class='BtnDropboxEdit editColor']"));
								if(buttonEditOfpayroll.isDisplayed()){
									bodyText += " Go to diary colour configuration check the button edit is display in the payroll : PASSED<br>";
									WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the payroll passeed");
								}else{
									bodyText += " Go to diary colour configuration check the button edit is display in the payroll : FAILED<br>";
									WriteLogFile.logger.info("THe button edit is not display in the payroll in:"+ps.getDiary_color_config());
									//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							} catch (Exception e) {
								bodyText += " Go to diary colour configuration check the button edit is display in the payroll : FAILED<br>";
								WriteLogFile.logger.info("THe button edit is not display in the payroll in:"+ps.getDiary_color_config());
								//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
							
						}else{
							bodyText += " The first table: It should have 3 rows: FAILED</h3>";
							WriteLogFile.logger.info("The table top have more than 3 tr in :"+ps.getDiary_color_config());
							//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					}else{
						bodyText += " Loading page with 2 tables : FAILED</h3><br>";
						WriteLogFile.logger.info("Can't load  table  location top is not display in :"+ps.getDiary_color_config());
						//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText += " Loading page with 2 tables : FAILED</h3><br>";
					WriteLogFile.logger.info("Can't load  table  location top in :"+ps.getDiary_color_config());
					//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/**** end checking table top *******/
				
				
				/******* start checking table bottom *******/
				try {
					WebElement tableBottom = driver.findElement(By.id("tableCasecode1"));
					if(tableBottom.isDisplayed()){
						try {
							
							WebElement buttAdd = tableBottom.findElement(By.xpath("//table[@id='tableCasecode1' and @class='tablesorter']/thead/tr/th[8]/input[@id='addBtn' and @class='BtnDropboxAdd addColor']"));
							if(buttAdd.isDisplayed()){
								bodyText += " Go to diary colour configuration check the button add is display in the table bottom : PASSED<br>";
								WriteLogFile.logger.info("Go to diary color configuration check the button edit is display in the table bottom passeed");
							}else{
								bodyText += " Go to diary colour configuration check the button add is display in the table bottom : FAILED<br>";
								WriteLogFile.logger.info("THe button add is not display in the table bottom in: "+ps.getDiary_color_config());
							}
						} catch (Exception e) {
							bodyText += " Go to diary colour configuration check the button add is display in the table bottom : FAILED<br>";
							WriteLogFile.logger.info("THe button add is not display in the table bottom in: "+ps.getDiary_color_config());
						}
					}else{
						bodyText += " Loading page with 2 tables : FAILED<br>";
						WriteLogFile.logger.info("Can't load  table  location bottom in :"+ps.getDiary_color_config());
					}
				} catch (Exception e) {
					bodyText += " Loading page with 2 tables : FAILED</h3>";
					WriteLogFile.logger.info("Can't load  table  location bottom in :"+ps.getDiary_color_config());
				}
				
				/******* end checking table bottom *******/
				
				/******* end checking Diary color configuration ****/
				
				/****** Start checking Diary_summary *****/
				bodyText +=" <br><h3>Go To Diary_summary</h3><br>";
				selenium.open(ps.getDiary_summary());
				Thread.sleep(2000);
				if(selenium.isTextPresent("Diary summary")){
					bodyText += "<br> Go to diary summary : PASSED<br>";
					WriteLogFile.logger.info("Go to diary summary passeed");
				}else{
					bodyText += "<br> Go to diary summary : FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getDiary_summary());
				}
				
				try {
					String lastUpdated = selenium.getText("xpath=//div[@id='content']/div[@id='lastUpdated']");
					if(lastUpdated.trim().length() == 0 || lastUpdated == null){
						bodyText += " Go to diary summary check last updated : FAILED<br>";
						WriteLogFile.logger.info("The display of last updated is null in :" +ps.getDiary_summary());
						//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}else{
						bodyText += " Go to diary summary check last updated : PASSED<br>";
						WriteLogFile.logger.info("Go to diary summary check last updated passeed");
					}
					
					String totalCase = selenium.getText("xpath=//div[@id='content']/div[@id='summary_content']/div[@id='headingContent']/ul[@id='summary_value_title']/li[1][@class='summary_total_case']");
					if(totalCase.equalsIgnoreCase("Total Cases")){
						bodyText += " Go to diary summary check total cases : PASSED<br>";
						WriteLogFile.logger.info("Go to diary summary check total cases passeed");
					}else{
						bodyText += " Go to diary summary check total cases : FAILED<br>";
						WriteLogFile.logger.info("The display of last updated in :" +ps.getDiary_summary() +" is not right");
					}
					
					String slaStatus = selenium.getText("xpath=//ul[@id='summary_value_title']/li[2][@class='summary_title_li']/ul[@class='summary_sla_status']/li[1][@class='summary_title_top summary_title']/span");
					if(slaStatus.trim().equalsIgnoreCase("SLA status")){
						bodyText += " Go to diary summary check SLA status  : PASSED<br>";
						WriteLogFile.logger.info("Go to diary summary check SLA status passeed");
					}else{
						bodyText += " Go to diary summary check SLA status  : FAILED<br>";
						WriteLogFile.logger.info("The display of SLA status in :" +ps.getDiary_summary() +" is not right");
					}
					
					String workFlow = selenium.getText("xpath=//ul[@id='summary_value_title']/li[3][@class='summary_title_li']/ul[@class='summary_workflow_status']/li[1][@class='summary_title_top summary_title']/span");
					if(workFlow.equalsIgnoreCase("Workflow status")){
						bodyText += " Go to diary summary check Workflow status : PASSED<br>";
						WriteLogFile.logger.info("Go to diary summary check Workflow status passeed");
					}else{
						bodyText += " Go to diary summary check Workflow status : FAILED<br>";
						WriteLogFile.logger.info("The display of  Workflow  status in :" +ps.getDiary_summary() +" is not right");
					}
					
					String deparment = selenium.getText("xpath=//div[@id='departmentTitle' and @class='summary_team_title round_border summary_team_bg']/div[1][@class='summary_team_title_des']/a");
					if(deparment.equalsIgnoreCase("Department")){
						bodyText += " Show entry department : PASSED<br>";
						WriteLogFile.logger.info("Show entry department : PASSED");
					}else{
						bodyText += " Show entry department : FAILED<br>";
						WriteLogFile.logger.info("The display of Department in :" +ps.getDiary_summary() +" is not right");
						//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
					}
				} catch (Exception e) {
					bodyText += " Show information summary : FAILED<br>";
					WriteLogFile.logger.info("The display of :" +ps.getDiary_summary() +" is not right");
					//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
				}
				
				/****** End checking Diary_summary *****/
				
				
				/***** start checking Team management ********/
				bodyText +=" <br><h3>Go To Team management</h3><br>";
					selenium.open(ps.getTeam_allocation_url());
					Thread.sleep(20000);
					if(selenium.isTextPresent("Team management")){
						bodyText += "<br> Loading page with Team management text : PASSED<br>";
						WriteLogFile.logger.info("Go to Team management passeed");
					}else{
						bodyText += " Loading page with Team management text : FAILED<br>";
						WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getTeam_allocation_url());
					}
					try {
						
						WebElement tableAllocation = driver.findElement(By.className("allocation_table_team_allocation"));
						if(tableAllocation.isDisplayed()){
							bodyText += " Go to Team management check table : PASSED<br>";
							WriteLogFile.logger.info("Go to Team management check table : PASSED");
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
										bodyText += " Dupplicated user "+  userDupplicate + " in :</h3>"  + ps.getTeam_allocation_url()+"<br>";
										WriteLogFile.logger.info("Dupplicated user "+  userDupplicate + " in :"  +ps.getTeam_allocation_url());
										//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}else{
										bodyText += " Go to Team management check dupplicated user : PASSED<br>";
										WriteLogFile.logger.info("Go to Team management check dupplicated user passeed");
									}
								}
							}
						}else{
							bodyText += " Go to Team management check table : FAILED</h3><br>";
							WriteLogFile.logger.info("The display of tableAllocation: " +ps.getTeam_allocation_url() +" is not right");
							//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText += " Go to Team management check table : FAILED <br>";
						WriteLogFile.logger.info("The display of tableAllocation is missing in: " + ps.getTeam_allocation_url() +" ");
						//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
					
				/***** end checking Team management ********/
				
					
				/******* start checking payroll page ******/
				bodyText +=" <br><h3>Go To Payroll page</h3><br>";
				selenium.open(ps.getPayroll_url());
				Thread.sleep(3000);
				if(selenium.isTextPresent("Payroll dates")){
					bodyText += "<br> Go to Payroll : PASSED<br>";
					WriteLogFile.logger.info("Go to Payroll passeed");
				}else{
					bodyText += " Go to Payroll : FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getPayroll_url());
				}
				try {
					WebElement tablePayroll = driver.findElement(By.id("table_bank"));
					if(tablePayroll.isDisplayed()){
						try {
							WebElement buttonAdd = tablePayroll.findElement(By.xpath("//table[@id='table_bank' and @class='tablesorter']/thead/tr/th[3][@class='header']/input[@id='addRow' and @class='BtnDropboxAdd addDropbox']"));
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
										 String NextfurtureYear = Integer.toString(YearCurrent+2);
										 boolean checkYear = true;
										 for(WebElement option : options){
											 String year = option.getText().split("-")[1];
											 if(year.equalsIgnoreCase(pastYear) || year.equalsIgnoreCase(NextfurtureYear)){
												 checkYear = false;
												 break;
											 }
										 }
										 if(checkYear){
											 	bodyText += " Go to Payroll check dropdowlist in popup when click add button : PASSED<br>";
												bodyText +="The first month in dropdown list of Month field is not < current month and not > same month of the next year : PASSED<br>";
											 	WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
										 }else{
												bodyText += " the first month in dropdown list of Month field is not < current month and not > same month of the next year : FAILED <br>";
												WriteLogFile.logger.info("The display of  dropdown when  add button is wrong");
										 }
									}else{
										bodyText += " Popup should be shown :FAILED <br>";
										WriteLogFile.logger.info("The display of  popup when  add button is missing");
										//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
									}
								} catch (Exception e) {
									bodyText += " Popup should be shown :FAILED <br>";
									WriteLogFile.logger.info("The display of  popup when  add button is missing");
								}
							}else{
								bodyText += " Click on Add button: FAILED <br>";
								WriteLogFile.logger.info("The display of add button is missing");
							}
						} catch (Exception e) {
							bodyText += " Click on Add button: FAILED <br>";
							WriteLogFile.logger.info("The display of add button is missing");
						}
						WebElement closePopup = driver.findElement(By.id("searchCloseAdd"));
						closePopup.click();
						Thread.sleep(5000);
						int TableRowCount = selenium.getXpathCount("//table[@id='table_bank']/tbody/tr").intValue();
						if(TableRowCount >= 1){
							try {
								WebElement buttonEdit = driver.findElement(By.xpath("//table[@id='table_bank' and @class='tablesorter']/tbody[@id='table_tbody']/tr[1]/td[3][@class='blank']/input[1][@class='BtnDropboxEdit editDropbox']"));
								if(buttonEdit.isDisplayed()){
									buttonEdit.click();
									Thread.sleep(5000);
									try {
										WebElement popupEdit = driver.findElement(By.id("searchFormEdit"));
										if(popupEdit.isDisplayed()){
											bodyText += " Edit pop-up of an entry if there is one : PASSED <br>";
											WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
										}else{
											bodyText += " Edit pop-up of an entry if there is one : FAILED <br>";
											WriteLogFile.logger.info("The display of  popup when  edit button in payroll is missing");
											//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");	
										}
									} catch (Exception e) {
										bodyText += " Edit pop-up of an entry if there is one : FAILED <br>";
										WriteLogFile.logger.info("The display of  popup when  edit button in payroll is missing");
									}
								}else{
									bodyText += " Edit pop-up of an entry if there is one : FAILED <br>";
									WriteLogFile.logger.info("The display of  edit button is missing");
								}
							} catch (Exception e) {
								e.printStackTrace();
								bodyText += " Edit pop-up of an entry if there is one : FAILED  <br>";
								WriteLogFile.logger.info("The display of  edit button in payroll is missing");
							}
							
						}else{
							//bodyText = " Go to Payroll check dropdowlist in popup when click edit button passeed</h3><br>";
							WriteLogFile.logger.info("Go to Payroll check dropdowlist in popup when click add button passeed");
						}
					}else{
						bodyText += " Loading page with shown table : FAILED <br>";
						WriteLogFile.logger.info("The display of tablePayroll is missing");
						//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
					}
				} catch (Exception e) {
					bodyText += " Loading page with shown table : FAILED <br>";
					WriteLogFile.logger.info("The display of tablePayroll is missing");
				}	
					
					
				/******** end checking payroll page *******/	
				
				/******** start checking bank holiday page *******/
				bodyText +="<br><h3>Go To bank holiday page</h3><br>";
				selenium.open(ps.getBank_holiday_url());
				selenium.waitForPageToLoad("2000");
				Thread.sleep(15000);
				if(selenium.isTextPresent("Bank holidays")){
					bodyText += " Go to Bank holiday : PASSED<br>";
					WriteLogFile.logger.info("Go to Bank holiday passeed");
				}else{
					bodyText += " Go to Bank holiday : FAILED<br>";
					WriteLogFile.logger.info("Server was broken. Can not open website :" +ps.getBank_holiday_url());
				}
				try {
					WebElement tableBank = driver.findElement(By.id("table_bank"));
					if(tableBank.isDisplayed()){
						try {
							WebElement buttonAdd = tableBank.findElement(By.xpath("//table[@id='table_bank' and @class='tablesorter']/thead/tr/th[3][@class='header']/input[@id='addRow' and @class='BtnDropboxAdd addDropbox']"));
							if(buttonAdd.isDisplayed()){
								buttonAdd.click();
								Thread.sleep(1000);
								try {
									WebElement popupAdd = driver.findElement(By.id("searchFormAdd"));
									if(popupAdd.isDisplayed()){
										try {
											WebElement submitButton = popupAdd.findElement(By.id("btnAddDropBox"));
											if(submitButton.isDisplayed()){
												bodyText += " Click on Add button: popup should be shown PASSED<br>";
												WriteLogFile.logger.info("Go to Bank holiday test function Add button is show popup passeed");
											}else{
												bodyText += " Click on Add button: popup should be shown : FAILED <br>";
												WriteLogFile.logger.info("The display of submit button in popup when click Add button in bank holiday  is missing");
												
											}
										} catch (Exception e) {
											bodyText += " Click on Add button: popup should be shown : FAILED <br>";
											WriteLogFile.logger.info("The display of submit button in popup when click Add button in bank holiday  is missing");
										}
										
										
									}else{
										bodyText += " Click on Add button: popup should be shown : FAILED <br>";
										WriteLogFile.logger.info("The display of  popup when  add button in bank holiday is missing");
										
									}
								} catch (Exception e) {
									bodyText += " Click on Add button: popup should be shown : FAILED <br>";
									WriteLogFile.logger.info("The display of  popup when  add button in bank holiday is missing");
								}
							}else{
								bodyText += " Click on Add button: popup should be shown : FAILED <br>";
								WriteLogFile.logger.info("The display of  add button in bank holiday is missing");
							}
						} catch (Exception e) {
							bodyText += " Click on Add button: popup should be shown : FAILED  <br>";
							WriteLogFile.logger.info("The display of  add button in bank holiday is missing");
						}
						WebElement closePopup = driver.findElement(By.id("searchCloseAdd"));
						closePopup.click();
						Thread.sleep(3000);
						int TableRowCount = selenium.getXpathCount("//table[@id='table_bank']/tbody/tr").intValue();
						if(TableRowCount >= 1){
							try {
								WebElement buttonEdit = driver.findElement(By.xpath("//table[@id='table_bank' and @class='tablesorter']/tbody[@id='table_tbody']/tr[1]/td[3][@class='blank']/input[1][@class='BtnDropboxEdit editDropbox']"));
								if(buttonEdit.isDisplayed()){
									buttonEdit.click();
									Thread.sleep(5000);
									try {
										WebElement popupEdit = driver.findElement(By.id("searchFormEdit"));
										if(popupEdit.isDisplayed()){
											try {
												WebElement buttonUpdate = driver.findElement(By.xpath("//table/tbody/tr[3]/td[3]/input[@id='editButton' and @class='BtnDropboxEdit saveDropbox']"));
												if(buttonUpdate.isDisplayed()){
													bodyText += " Click on Edit button: popup should be shown : PASSED <br>";
													WriteLogFile.logger.info("Go to Bank holiday all function is passed");
												}else{
													bodyText += " Click on Edit button: popup should be shown : FAILED <br>";
													WriteLogFile.logger.info("The display of update button in popop when click EDIT button in bank holiday  is missing");
												}
											} catch (Exception e) {
												bodyText += " Click on Edit button: popup should be shown : FAILED  <br>";
												WriteLogFile.logger.info("The display of update button in popop when click EDIT button in bank holiday  is missing");
											}
										}else{
											bodyText += " Click on Edit button: popup should be shown : FAILED  <br>";
											WriteLogFile.logger.info("The display of  popup when  edit button in bank holiday is missing");
										}
									} catch (Exception e) {
										bodyText += " Click on Edit button: popup should be shown : FAILED <br>";
										WriteLogFile.logger.info("The display of  popup when  edit button in bank holiday is missing");
									}
									
								}else{
									bodyText += " Click on Edit button: popup should be shown : FAILED <br>";
									WriteLogFile.logger.info("The display of  edit button in bank holiday is missing");
								}
							} catch (Exception e) {
								bodyText += " Click on Edit button: popup should be shown : FAILED <br>";
								WriteLogFile.logger.info("The display of  edit button in bank holiday is missing");
							}
						}else{
							bodyText += " Go to Bank holiday there was no bank holidays in table so we can't check the function edit ! Other function is passed <br>";
							WriteLogFile.logger.info("Go to Bank holiday there was no bank holidays in table so we can't check the function edit ! Other function is passed");
						}
					}else{
						bodyText += " Click on ADD button: popup should be shown : FAILED <br>";
						WriteLogFile.logger.info("The display of table bank holiday is missing");
					}
				} catch (Exception e) {
					bodyText += " Click on ADD button: popup should be shown : FAILED <br>";
					WriteLogFile.logger.info("The display of table bank holiday is missing");
				}
				
				
				/******** end checking bank holiday page *******/
				
				
				/****** start checking administrator dropbox *****/
				bodyText +="<br><h3>Go To dropbox administrator</h3><br>";
				selenium.open(ps.getDropbox_administrator());
				selenium.waitForPageToLoad("2000");
				Thread.sleep(2000);
				String dropboxRandom = null;
				if(selenium.isTextPresent("Drop box administration")){
					bodyText += " Go to dropbox administrator : PASSED<br>";
					WriteLogFile.logger.info(" Go to dropbox administrator passed");
				}else{
					bodyText += " Go to dropbox administrator : FAILED  <br>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_administrator() + " ");
				}
				try {												  
					WebElement tableAdmin = driver.findElement(By.id("dropboxtblconfig"));
					if(tableAdmin.isDisplayed()){
						bodyText+="  Loading page with table : PASSED<br>";
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
											bodyText+=" there is enough 5 fields : PASSED<br>";
											try {
												WebElement selectDuration = driver.findElement(By.id("duration_days"));
												List<WebElement> options = selectDuration.findElements(By.tagName("option"));
												if(options.isEmpty() || options.size() == 0){
													bodyText += " The select duration in add dropbox popup have data : FAILED  <br>";
													WriteLogFile.logger.info("The select durations in add dropbox popup is null");
												}else{
													//selectDuration.click();
													bodyText += " The select duration in add dropbox popup have data : PASSED <br>";
													WriteLogFile.logger.info(" Go to dropbox administrator passed");
												}
											} catch (Exception e) {
												bodyText += " The select duration in add dropbox popup have data : FAILED  <br>";
												WriteLogFile.logger.info("The select durations in add dropbox popup is null");
											}
											try {
												WebElement selectOwner = driver.findElement(By.id("slOwner"));
												List<WebElement> options = selectOwner.findElements(By.tagName("option"));
												if(options.isEmpty() || options.size() == 0){
													bodyText += " The select owner in add dropbox popup  have data : FAILED  <br>";
													WriteLogFile.logger.info("The select owner in add dropbox popup is null");
												}else{
													//selectOwner.click();
													Thread.sleep(1000);
													buttonAddDropbox.click();
													Thread.sleep(1000);
													bodyText += " The select owner in add dropbox popup  have data : PASSED <br>";
													WriteLogFile.logger.info(" Go to dropbox administrator passed");
												}
											} catch (Exception e) {
												bodyText += " The select owner in add dropbox popup  have data : FAILED <br>";
												WriteLogFile.logger.info("The select owner in add dropbox popup is null");
											}
											
											
										}else{
											bodyText += " Click on Add button:there is enough 5 fields : FAILED  <br>";
											WriteLogFile.logger.info("The display of table in popup add dropbox when click Add button in dropbox administrator is wrong");
											//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
									}else{
										bodyText += " Click on Add button: popup will display : FAILED  <br>";
										WriteLogFile.logger.info("The display of popup add dropbox when click Add button in dropbox administrator is missing");
										//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
									}
								} catch (Exception e) {
									bodyText += "  Click on Add button: popup will display : FAILED  <br>";
									WriteLogFile.logger.info("The display of popup add dropbox when click Add button in dropbox administrator is missing");
									//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
								}
							}else{
								bodyText += " Add button will display : FAILED <br>";
								WriteLogFile.logger.info("The button add  dropbox in dropbox administrator is missing");
								//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
							}
						} catch (Exception e) {
							bodyText += " Add button will display : FAILED  <br>";
							WriteLogFile.logger.info("The button add  dropbox in dropbox administrator is missing");
						}
						
						int TableRowCount = selenium.getXpathCount("//table[@id='dropboxtblconfig']/tbody/tr").intValue();
						if(TableRowCount > 1){
							int checkAdd = 0;
							try {
								try {
									for(int i = 1; i < TableRowCount + 1 ; i ++ ){
										String xpath = "xpath=//table[@id='dropboxtblconfig' and @class='tablesorter']/tbody/tr["+i+"]/td[2]";
										dropboxRandom = selenium.getText(xpath);
										System.out.println(dropboxRandom);
										if(!dropboxRandom.equalsIgnoreCase("fakerow")){
											checkAdd = i;
											break;
										}else{
											dropboxRandom = null;
											continue;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								String xpathButtonAdd ="//table[@id='dropboxtblconfig' and @class='tablesorter']/tbody/tr["+checkAdd+"]/td[6][@class='blank']/input[@class='dropboxInfo']";
								WebElement buttonAddUser = driver.findElement(By.xpath(xpathButtonAdd));
								if(buttonAddUser.isDisplayed()){
									buttonAddUser.click();
									Thread.sleep(1000);
									try {
										WebElement popupAddUser = driver.findElement(By.id("editDropUser"));
										if(popupAddUser.isDisplayed()){
											bodyText+= " Click on Info button: popup should be shown : PASSED <br>";
											try {
												WebElement btnAddUser = popupAddUser.findElement(By.id("btnAddUser"));
												if(btnAddUser.isDisplayed()){
													bodyText += " The button add new user to dropbox is display : PASSED</h3><br>";
													btnAddUser.click();
													Thread.sleep(2000);
													WebElement viewDropUser = driver.findElement(By.id("viewDropUser"));
													if(viewDropUser.isDisplayed()){
														bodyText += "  Click to button add new user to dropbox ==> the popup should be show : PASSED <br>";
														selenium.type("listMail", "lan.ta@c-mg.com");
														selenium.click("btnCheckMail");
														Thread.sleep(10000);
														WebElement tableShow = driver.findElement(By.id("MailUsertbl"));
														if(tableShow.isDisplayed()){
															bodyText += " select add new user button: check email address of third paries user : PASSED</h3><br>";
															bodyText += " select valid submit : PASSED<br>";
															bodyText += "  it should have report summary : PASSED<br>";
															WriteLogFile.logger.info("Go to dropbox administrator all function is passed");
														}else{
															bodyText += " t should have report summary : FAILED  <br>";
															WriteLogFile.logger.info("The table when click btnCheckMail in dropbox administrator is missing");
														}
													}else{
														bodyText += "   Click to button add new user to dropbox ==> the popup should be show : FAILED <br>";
														WriteLogFile.logger.info("The popup when click btnAddUser in dropbox administrator is missing");
													}
												}else{
													bodyText += " The button add new user to dropbox is display : FAILED  <br>";
													WriteLogFile.logger.info("The button btnAddUser in dropbox administrator is missing");
												}
											} catch (Exception e) {
												bodyText += " select add new user button: check email address of third paries user : FAILED  <br>";
												bodyText += " select valid submit : FAILED  <br>";
												bodyText += " it should have report summary : FAILED  <br>";
												WriteLogFile.logger.info("There are some error in function add user to dropbox");
											}
										}else{
											bodyText += " Click on Info button: popup should be shown : FAILED  <br>";
											WriteLogFile.logger.info("The display of popup add user when click Add user to dropbox in dropbox administrator is missing");
										}
									} catch (Exception e) {
										bodyText += " Click on Info button: popup should be shown : FAILED  <br>";
										WriteLogFile.logger.info("The display of popup add user when click Add user to dropbox in dropbox administrator is missing");
									}
								}else{
									bodyText += " The button show information in dropbox administrator is display : FAILED  <br>";
									WriteLogFile.logger.info("The button add user in dropbox administrator is missing");
								}
							} catch (Exception e) {
								bodyText += " The button show information in dropbox administrator is display : FAILED  <br>";
								WriteLogFile.logger.info("The button add user in dropbox administrator is missing");
							}
						}else{
							bodyText += " Go to dropbox administrator can't not check function add user because in this moment there are no dropbox!Other function is passed</h3><br>";
							WriteLogFile.logger.info(" Go to dropbox administrator can't not check function add user because in this moment there are no dropbox!Other function is passed");
						}
						
					}else{
						bodyText += " Loading page with table : FAILED  <br>";
						WriteLogFile.logger.info("The table in dropbox administrator is missing");
					}
				} catch (Exception e) {
					bodyText += " Loading page with table : FAILED <br>";
					WriteLogFile.logger.info("The table in dropbox administrator is missing");
				}
				
				
				
				/****** end checking administrator dropbox *****/
				
				
				/****** start checking dropboxes ********/
				bodyText +=" <br><h3>Go To dropboxes page</h3><br>";
				selenium.open(ps.getDropbox_url());
				Thread.sleep(5000);
				if(selenium.isTextPresent("Dropboxes")){
					bodyText +=" Go to dropbox download page : PASSED <br>";
					WriteLogFile.logger.info(" Go to dropbox download page passed");
				}else{
					bodyText += " Go to dropbox download page : FAILED  <br>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_url() + " ");
				}
				if(dropboxRandom == null || dropboxRandom.trim().length() == 0){
					String notifi = selenium.getText("xpath=//div[@id='content']/div");
					if(notifi.equalsIgnoreCase("No dropbox found.")){
						bodyText += "  There is not dropdown in Dropbox administrator page: no dropbox is shown: PASSED <br>";
						WriteLogFile.logger.info(" Go to dropbox download page passed,there is no dropbox so we can't check main function");
					}else{
						bodyText += " There is not dropdown in Dropbox administrator page: no dropbox is shown: FAILED<br>";
						WriteLogFile.logger.info("There are some error(when no dropbox found) in :"+ ps.getDropbox_url() + " ");
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
									bodyText += " There is dropdown in Dropbox administrator page: Dropbox list should be shown : FAILED <br>";
									WriteLogFile.logger.info("Data in the dropdowlist of dropbox is not equal with dropbox administrator  in :"+ ps.getDropbox_url() + " ");
								}else{
									bodyText += " There is dropdown in Dropbox administrator page: Dropbox list should be shown : PASSED<br>";
									WriteLogFile.logger.info(" Go to dropbox download page check dropdowlist passed");
								}
							}else{
								bodyText += " Can not find any data in the dropdowlist of dropbox in " +ps.getDropbox_url()+"<br>";
								WriteLogFile.logger.info("Can not find any data in the dropdowlist of dropbox in :"+ ps.getDropbox_url() + " ");
							}
						}else{
							bodyText += " There is dropdown in Dropbox administrator page: Dropbox list should be shown : FAILED <br>";
							WriteLogFile.logger.info("Can not find the dropdowlist of dropbox in :"+ ps.getDropbox_url() + " ");
							//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
						}
					} catch (Exception e) {
						bodyText += " There is dropdown in Dropbox administrator page: Dropbox list should be shown : FAILED <br>";
						WriteLogFile.logger.info("Can not find the dropdowlist of dropbox in :"+ ps.getDropbox_url() + " ");
					}
					
					try {
						WebElement DisplayUpload = driver.findElement(By.id("file_drop_zone"));
						if(DisplayUpload.isDisplayed()){
							try {
								WebElement buttonUpload = DisplayUpload.findElement(By.id("file_input_drop"));
								String type = buttonUpload.getAttribute("type");
								if(type.equalsIgnoreCase("file")){
									bodyText += "  Check the display of upload button : PASSED</h3><br>";
									WriteLogFile.logger.info(" Go to dropbox download page check button upload passed");
								}else{
									bodyText += "  Check the display of upload button : FAILED <br>";
									WriteLogFile.logger.info("In side The upload file have some error like the button of upload is not type = file : " +ps.getDropbox_url());
								}
							} catch (Exception e) {
								bodyText += "  Check the dispaly of upload button : FAILED<br>";
								WriteLogFile.logger.info("Inside The upload file have some error  :"+ ps.getDropbox_url() + " ");
							}
						}else{
							bodyText += "  Check the dispaly of upload button : FAILED <br>";
							WriteLogFile.logger.info("Can not find the location of upload file  :"+ ps.getDropbox_url() + " ");
						}
					} catch (Exception e) {
						bodyText += " Check the dispaly of upload button : FAILED <br>";
						WriteLogFile.logger.info("The upload file have some error  :"+ ps.getDropbox_url() + " ");
					}
					
					try {
						WebElement tableDownload = driver.findElement(By.id("dropbox"));
						if(tableDownload.isDisplayed()){
							int TableRowCount = selenium.getXpathCount("//table[@id='dropbox']/tbody/tr").intValue();
							if(TableRowCount > 0){
								String filetext = selenium.getText("xpath=//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[2]");
								if(filetext.equalsIgnoreCase("No file found")){
									bodyText += " Go to dropbox download page check table download : PASSED <br>";
									WriteLogFile.logger.info(" Go to dropbox download page check table download passed");
								}else{
									try {
										WebElement inforButton = driver.findElement(By.xpath("//table[@id='dropbox' and @class='tablesorter']/tbody/tr[1]/td[1]/input[1][@class='dropbox_info']"));
										if(inforButton.isDisplayed()){
											inforButton.click();
											Thread.sleep(2000);
											try {
												WebElement popupInfor = driver.findElement(By.id("dropbox_file_info"));
												if(popupInfor.isDisplayed()){
													bodyText += " If there is data: click on Info button: PASSED<br>";
													int rowInsde = selenium.getXpathCount("//div[@id='dropbox_file_info']/div[2][@class='dropbox_file_info_mid']/table/tbody/tr").intValue();
													if(rowInsde == 3){
														bodyText += " 3 fields should be shown in the popup : PASSED <br>";
														WriteLogFile.logger.info(" Go to dropbox download page, the popup infor is passed");
													}else{
														bodyText += " 3 fields should be shown in the popup : FAILED <br>";
														WriteLogFile.logger.info("The table of infor popup have " + rowInsde +" <tr> : " +ps.getDropbox_url());
													}
												}else{
													bodyText += " If there is data: click on Info button: FAILED <br>";
													WriteLogFile.logger.info("Can not find the popup infor when click to the button infor in table download :"+ ps.getDropbox_url() + " ");
												}
											} catch (Exception e) {
												bodyText += " If there is data: click on Info button: FAILED <br>";
												WriteLogFile.logger.info("Can not find the popup infor when click to the button infor in table download :"+ ps.getDropbox_url() + " ");
											}
										}else{
											bodyText += " Find the button infor in table download : FAILED <br>";
											WriteLogFile.logger.info("Can not find the button infor in table download  :"+ ps.getDropbox_url() + " ");
											//SendMailSSL.sendMailCMG(bodyText, "Pablo server have a problem");
										}
									} catch (Exception e) {
										bodyText += "  Find the button infor in table download : FAILED <br>";
										WriteLogFile.logger.info("Can not find the button infor in table download  :"+ ps.getDropbox_url() + " ");
									}
									
								}
							}
						}else{
							bodyText += " Find the table of download : FAILED <br>";
							WriteLogFile.logger.info("Can not find the table of download  :"+ ps.getDropbox_url() + " ");
						}
					} catch (Exception e) {
						bodyText += " Find the table of download : FAILED <br>";
						WriteLogFile.logger.info("Can not find the table of download  :"+ ps.getDropbox_url() + " ");
					}
					
					
				}
				
				/****** end checking dropboxes ********/
				
				
				/******* start checking dropbox configuration *******/
				bodyText +="<br><h3>Go To  dropbox configuration</h3><br>";
				selenium.open(ps.getDropbox_configuration());
				Thread.sleep(5000);
				if(selenium.isTextPresent("Dropbox configuration")){
					bodyText += " Go to dropbox configuration page : PASSED <br>";
					WriteLogFile.logger.info(" Go to dropbox configuration page passed");
				}else{
					bodyText += " Go to dropbox configuration page : FAILED  <br>";
					WriteLogFile.logger.info("Cannot open website :"+ ps.getDropbox_configuration() + " ");
				}
				try {
					WebElement table = driver.findElement(By.xpath("//div[@id='dropbox_configuration']/div[1][@class='dropbox_configuration_left_content']/table"));
					if(table.isDisplayed()){
						int tableRow = selenium.getXpathCount("//div[@id='dropbox_configuration']/div[1][@class='dropbox_configuration_left_content']/table/tbody/tr").intValue();
						if(tableRow == 5){
							bodyText += " Table with 5 fields and correct data : PASSED <br>";
							String defaultValue = selenium.getText("xpath=//span[@id='defaultFileExpiryVal' and @class='view_value']");
							if(defaultValue.equalsIgnoreCase("30 days")){
								bodyText += " Go to dropbox configuration page the value is 90 passed <br>";
								WriteLogFile.logger.info(" Go to dropbox configuration the value is 30, page passed");
							}else{
								bodyText += " the value default is not equal with 30 in table configuration: " +ps.getDropbox_configuration()+"<br>";
								WriteLogFile.logger.info("the value Default file expiry in days("+defaultValue+") is not equal with 30 days in table configuration:"+ ps.getDropbox_configuration() + " ");
							}
							//check default value for other
							String defaultNonceLink = selenium.getText("xpath=//span[@id='nonceLinkDurationVal' and @class='view_value']");
							if(defaultNonceLink.equalsIgnoreCase("5 days")){
								WriteLogFile.logger.info(" Go to dropbox configuration the value default noncelink duration  is 5, page passed");
							}else{
								bodyText += " the value default nonce link duration("+defaultNonceLink+") is not equal with 5  in table configuration: " +ps.getDropbox_configuration()+"<br>";
								WriteLogFile.logger.info("the value default nonce link duration("+defaultNonceLink+") is not equal with 5  in table configuration: " +ps.getDropbox_configuration());
							}
							String defaultDayOfInnactityvity = selenium.getText("xpath=//span[@id='maximumDayOfInactivityVal' and @class='view_value']");
							if(defaultDayOfInnactityvity.equalsIgnoreCase("90 days")){
								WriteLogFile.logger.info(" Go to dropbox configuration the value Days of inactivity before users need to revalidate their accounts is 90, page passed");
							}else{
								bodyText += " the value Days of inactivity before users need to revalidate their accounts("+defaultDayOfInnactityvity+") is not equal with 90  in table configuration: " +ps.getDropbox_configuration()+"<br>";
								WriteLogFile.logger.info("the value default Days of inactivity before users need to revalidate their accounts("+defaultDayOfInnactityvity+")  is not equal with 90  in table configuration: " +ps.getDropbox_configuration());
							}
							String defaulDaypiros = selenium.getText("xpath=//span[@id='revalidDatePromptDayVal' and @class='view_value']");
							if(defaulDaypiros.equalsIgnoreCase("14, 7 & 1 days")){
								WriteLogFile.logger.info("Defaul day priors is passed");
							}else{
								bodyText += " the value Days prior to a user's re-validation date to send an email reminder("+defaulDaypiros+") is not equal with 14, 7 & 1 days  in table configuration: " +ps.getDropbox_configuration()+"<br>";
								WriteLogFile.logger.info("Days prior to a user's re-validation date to send an email reminder("+defaulDaypiros+") is not equal with 14, 7 & 1 days  in table configuration: " +ps.getDropbox_configuration());
							}
							
							String defaulDayDurationDropbox = selenium.getText("dropboxDurationOptionVal");
							if(defaulDayDurationDropbox.equalsIgnoreCase("1, 7 ,28, 90, 180, *")){
								WriteLogFile.logger.info("passed default dayduration of dropbox");
							}else{
								bodyText += " the value Default duration of a Dropbox in days("+defaulDayDurationDropbox+") is not equal with 1, 7 ,28, 90, 180, * in table configuration: " +ps.getDropbox_configuration()+"<br>";
								WriteLogFile.logger.info("Default duration of a Dropbox in days("+defaulDayDurationDropbox+")  is not equal with 1, 7 ,28, 90, 180, * in table configuration: " +ps.getDropbox_configuration());
							}
						}else{
							bodyText += " Table with 5 fields and correct data : FAILED <br>";
							WriteLogFile.logger.info(" the table configuration is not display right  :"+ ps.getDropbox_configuration() + " ");
						}
					}else{
						bodyText += " Table with 5 fields and correct data : FAILED <br>";
						WriteLogFile.logger.info("Can not find the table configuration  :"+ ps.getDropbox_configuration() + " ");
					}
				} catch (Exception e) {
					bodyText += " Table with 5 fields and correct data : FAILED <br>";
					WriteLogFile.logger.info("Can not find the table configuration  :"+ ps.getDropbox_configuration() + " ");
				}
				
				
				/******* end checking dropbox configuration *******/
					
					/******* start checking module session & password *****/
				
					/**start checking datasource configuration **/
						bodyText +="<br><h3>Go to datasource configuration</h3><br>";
						selenium.open(ps.getDatasource_config_url());
						selenium.waitForPageToLoad("5000");
						Thread.sleep(1000);			
						if(selenium.isTextPresent("Data source management")){
							bodyText += "Loading page with text Data source management : PASSED <br>";
							WriteLogFile.logger.info(" Go to datasource configuration page passed");
						}else{
							bodyText += "Loading page with text Data source management : FAILED<br>";
							WriteLogFile.logger.info("Cannot open website :"+ ps.getDatasource_config_url() + " ");
						}
						
						try {
							WebElement tableConfig = driver.findElement(By.id("datasourcetbl"));
							if(tableConfig.isDisplayed()){
								bodyText +="Table is show in page : PASSED<br>";
								try {
									WebElement bttAdd = tableConfig.findElement(By.id("btnAdd"));
									if(bttAdd.isDisplayed()){
										bodyText +="Button Add datasource is show in page : PASSED<br>";
										bttAdd.click();
										Thread.sleep(1000);
										try {
											WebElement popupAdd = driver.findElement(By.id("AddDSDiv"));
											if(popupAdd.isDisplayed()){
												bodyText +="Click Button Add datasource: the popup is show in page : PASSED<br>";
												try {
													boolean checkSystemlist = true;
													WebElement systemList = popupAdd.findElement(By.id("system_name"));
													if(systemList.isDisplayed()){
														List<WebElement> options = systemList.findElements(By.tagName("option"));
														if(options.size() > 0){
															for(WebElement option : options){
																if(option.getText().length() == 0 || option.getText() == ""){
																	checkSystemlist = false;
																	bodyText+="Check the dropdowlist Systems : FAILED<br> ";
																	WriteLogFile.logger.info("The dropdow list system have a null option in  popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
																}
															}
															if(checkSystemlist){
																bodyText+="Check the dropdowlist Systems : PASSED <br> ";
																WriteLogFile.logger.info("The dropdow list system in popup when click button add datasource is passed :"+ ps.getDatasource_config_url() + " ");
															}
														}else{
															bodyText+="Check the dropdowlist Systems : FAILED <br>";
															WriteLogFile.logger.info("The dropdow list system is null popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
														}
													}else{
														bodyText+="Check the dropdowlist Systems : FAILED<br>";
														WriteLogFile.logger.info("The dropdow list system in  popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
													}
													
													WebElement isOperation = popupAdd.findElement(By.id("operationalYes"));
													if(!isOperation.isSelected()){
														bodyText+="The radio button Operation is selected default : FAILED <br>";
														WriteLogFile.logger.info("The operation radio button default is not Yes in  popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
													}else{
														bodyText+="The radio button Operation is selected default : PASSED <br>";
														WriteLogFile.logger.info("The operation radio button default Yes in popup when click button add datasource is passed :"+ ps.getDatasource_config_url() + " ");
													}
													
													WebElement instance_type = popupAdd.findElement(By.id("instance_type"));
													List<WebElement> options = instance_type.findElements(By.tagName("option"));
													if(options.size()> 0 ){
														boolean checkOption= true;
														for(WebElement option : options){
															if(option.getText().trim().length() == 0){
																checkOption = false;
																bodyText+=" Check the dropdow list instance type : FAILED <br>";
																WriteLogFile.logger.info("The dropdow list instance_type have a null option in  popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
															}
														}
														if(checkOption){
															bodyText+=" Check the dropdow list instance type : PASSED <br>";
															WriteLogFile.logger.info("The dropdow list instance_type in popup when click button add datasource is passed :"+ ps.getDatasource_config_url() + " ");
														}
													}else{
														bodyText+=" Check the dropdow list instance type : FAILED ";
														WriteLogFile.logger.info("The dropdow list instance_type is null popup when click button add datasource is  invisiable :"+ ps.getDatasource_config_url() + " ");
													} 
													
													WebElement bttSubmit= popupAdd.findElement(By.id("BtnAddSource"));
													bttSubmit.click();
													Thread.sleep(1000);
													if(popupAdd.isDisplayed()){
														bttAdd.click();
														Thread.sleep(1000);
														bodyText+="Check submit button when we don't fill any data: the popup won't hide : PASSED <br>";
														bodyText+="------------------------------<br>";
													}else{
														bodyText+="Check submit button when we don't fill any data: the popup won't hide : FAILED <br>";
													}
													
												} catch (Exception e) {
													bodyText += "Click Button Add datasource: the popup is show in page : FAILED <br> ";
													WriteLogFile.logger.info("Some information in  popup when click button add datasource is missing or not visiable :"+ ps.getDatasource_config_url() + " ");
												}
											}else{
												bodyText += " Click Button Add datasource: the popup is show in page : FAILED <br>";
												WriteLogFile.logger.info("the popup didn't load when click button add datasource in  :"+ ps.getDatasource_config_url() + " ");
											}
											
										} catch (Exception e) {
											e.printStackTrace();
											bodyText += "Click Button Add datasource: the popup is show in page : FAILED <br> ";
											WriteLogFile.logger.info("the popup didn't load when click button add datasource in  :"+ ps.getDatasource_config_url() + " ");
										}
									}else{
										bodyText += "Button Add datasource is show in page : FAILED <br>";
										WriteLogFile.logger.info("the button Add datasource in table of datasource configuration is invisiable in :"+ ps.getDatasource_config_url() + " ");
									}
								} catch (Exception e) {
									e.printStackTrace();
									bodyText += " Button Add datasource is show in page : FAILED <br>";
									WriteLogFile.logger.info("Can not find the button Add datasource in table of datasource configuration :"+ ps.getDatasource_config_url() + " ");
								}
								try {
									int TableRowCount = selenium.getXpathCount("//table[@id='datasourcetbl']/tbody/tr").intValue();
									WriteLogFile.logger.info("Table row : " + TableRowCount);
									if(TableRowCount > 0){
										for(int i = 1 ; i <= TableRowCount;i++){
											try {
												Thread.sleep(5000);
												WebElement bttEdit = driver.findElement(By.xpath("//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[11]/input[@id='btnEditDataSource' and @class='BtnDsEdit editDs']"));
												WriteLogFile.logger.info("row : " + i);
												if(bttEdit.isDisplayed()){
													bttEdit.click();
													Thread.sleep(1000);
													try {
														WebElement popup = driver.findElement(By.id("AddDSDiv"));
														if(popup.isDisplayed()){
															Thread.sleep(1000);
															bodyText += "Click to edit button of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]/")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") => pop up show : PASSED<br>";
															String system = selenium.getSelectedLabel("system_name");
															String environment = selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]");
															String xpath = "xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[5]";
															try {
																String server = selenium.getText(xpath);
																System.out.println(server);
															} catch (Exception e) {
																e.printStackTrace();
															}
															
															WebElement radio = popup.findElement(By.id("operationalYes"));
															String operation = "NO";
															if(radio.isSelected()){
																operation = "YES";
															}
															String instance_type = selenium.getSelectedValue("instance_type");
															try {
																WebElement bttTest = popup.findElement(By.id("BtnTestSource"));
																WebElement bttCancel  = popup.findElement(By.id("BtnCancelSource"));
																if(bttTest.isDisplayed()){
																	bodyText += "button test in popup edit of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : PASSED<br>";
																	WriteLogFile.logger.info("button test in popup edit of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : PASSED<br>");
																	bttTest.click();
																	Thread.sleep(20000);
																		String TestResult = selenium.getText("result");
																			if(TestResult.equalsIgnoreCase("Connected successfully!")){
																				try {
																					bttCancel.click();
																					bodyText+="Testing connection is connected successfully with information : <br>";
																					bodyText+="System : "+ system +"<br>";
																					bodyText+="Environment : " +environment +"<br>";
																					bodyText+="Operational : " + operation +"<br>";
																					bodyText+="Instance type : " + instance_type +"<br>";
																					bodyText+="------------------------------<br>";
																				} catch (Exception e) {
																					bodyText+="Testing connection is connected fail with information : <br>";
																					bodyText+="System : "+ system +"<br>";
																					bodyText+="Environment : " +environment +"<br>";
																					bodyText+="Operational : " + operation +"<br>";
																					bodyText+="Instance type : " + instance_type +"<br>";
																					bodyText+="------------------------------<br>";
																					bttEdit.click();
																				}
																			}else{
																				try {
																					bodyText+="Testing connection is connected fail with information : <br>";
																					bodyText+="System : "+ system +"<br>";
																					bodyText+="Environment : " +environment +"<br>";
																					bodyText+="Operational : " + operation +"<br>";
																					bodyText+="Instance type : " + instance_type +"<br>";
																					bodyText+="------------------------------<br>";
																					bttCancel.click();
																				} catch (Exception e) {
																					bodyText+="Testing connection is connected fail with information : <br>";
																					bodyText+="System : "+ system +"<br>";
																					bodyText+="Environment : " +environment +"<br>";
																					bodyText+="Operational : " + operation +"<br>";
																					bodyText+="Instance type : " + instance_type +"<br>";
																					bodyText+="------------------------------<br>";
																					bttEdit.click();
																				}
																				
																			}
																}else{
																	bodyText += "button test in popup edit of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>";
																	continue;
																}
															} catch (Exception e) {
																e.printStackTrace();
																bodyText += "button test in popup edit of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>";
																continue;
															}
														}else{
															bodyText += "Click to edit button of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") => pop up show : FAILED<br>";
															continue;
														}
													} catch (Exception e) {
														e.printStackTrace();
														bodyText += "Click to edit button of datasource :"+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") => pop up show : FAILED<br>";
														continue;
													}
												}else{
													bodyText+="Can not testing Datasource "+selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]/")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>";
													WriteLogFile.logger.info("Can not testing Datasource "+selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]/")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>");
													continue;
												}
											} catch (Exception e) {
												e.printStackTrace();
												bodyText+="Can not testing Datasource "+selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]/")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>";
												WriteLogFile.logger.info("Can not testing Datasource "+selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[2]/")+"("+ selenium.getText("xpath=//table[@id='datasourcetbl' and @class='tablesorter']/tbody/tr["+i+"]/td[3]") +") : FAILED<br>");
												continue;
											}
											
										}
										
									}else{
										bodyText+="There is no datasource for testing<br>";
									}
								} catch (Exception e) {
									WriteLogFile.logger.info("error " + e.getMessage());
									bodyText += "Table is show in page : FAILED <br>";
								}
																
								
							}else{
								bodyText += "Table is show in page : FAILED <br>";
								WriteLogFile.logger.info("the table of datasource configuration is invisiable in :"+ ps.getDatasource_config_url() + " ");
							}
						} catch (Exception e) {
							bodyText += " Table is show in page : FAILED <br>";
							WriteLogFile.logger.info("Can not find the table of datasource configuration in :"+ ps.getDatasource_config_url() + " ");
						}
					/**end checking datasource configuration **/
				
				
				
				
					/******* end checking module session & password *****/
					SendMailSSL.sendMailCMG(bodyText, ps.getSystem_test() +"("+ ps.getMain_url()+")" + " automation test result ");
			}
			
			driver.quit();
			
			
		}
		
		
	}
	
}

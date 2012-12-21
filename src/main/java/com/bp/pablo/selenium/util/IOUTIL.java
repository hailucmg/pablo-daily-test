package com.bp.pablo.selenium.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bp.pablo.element.MailNotification;
import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;


public class IOUTIL {
	public static final String BASE_PATH = "C:\\AutomationTest\\Pablo";
	public static final String ACCOUNT_PROPERTIES = "account.properties";
	public static final String SITES_PROPERTIES = "site.properties";
	public static final String MAILNOTIFICATION = "mail.properties";
	/**
	 * Load account test.
	 *
	 * @return the test account
	 */
	public static TestAccount loadAccountTest() {

		try {
			File cfgFolder = new File(BASE_PATH + "\\cfg");
			if (!cfgFolder.exists() || !cfgFolder.isDirectory()) {
				cfgFolder.mkdirs();
			}
			File cfgFile = new File(BASE_PATH + "\\cfg\\" + ACCOUNT_PROPERTIES);
			
			if (!cfgFile.exists() || !cfgFile.isFile()) {
				InputStream is =  IOUTIL.class.getResourceAsStream("/" + ACCOUNT_PROPERTIES);
				FileOutputStream fos = new FileOutputStream(cfgFile);
				int n = 0;
				byte[] b = new byte[1024];
				while ((n=is.read(b)) != -1) {
					fos.write(b, 0, n);
				}
				fos.flush();
				fos.close();
			}
			cfgFile = new File(BASE_PATH + "\\cfg\\" + ACCOUNT_PROPERTIES);
			TestAccount acc = new TestAccount();
			Properties pro = new Properties();
			pro.load(new FileInputStream(cfgFile));
			String username_admin = pro.getProperty("username_admin");
			String password_admin = pro.getProperty("password_admin");
			String username_normal = pro.getProperty("username_normal");
			String password_normal = pro.getProperty("password_normal");
			acc.setUsername_admin(username_admin);
			acc.setPassword_admin(password_admin);
			acc.setUsername_normal(username_normal);
			acc.setPassword_normal(password_normal);
			return acc;
		} catch (Exception ex) {
			WriteLogFile.logger.info(ex.getMessage());
			return null;
		}
	}

	/**
	 * Load all url.
	 *
	 * @return the pablo site
	 */
	public static PabloSite loadAllUrl() {
		try {
			File cfgFolder = new File(BASE_PATH + "\\cfg");
			if (!cfgFolder.exists() || !cfgFolder.isDirectory()) {
				cfgFolder.mkdirs();
			}
			File cfgFile = new File(BASE_PATH + "\\cfg\\" + SITES_PROPERTIES);
			if (!cfgFile.exists() || !cfgFile.isFile()) {
				InputStream is =  IOUTIL.class.getResourceAsStream("/" + SITES_PROPERTIES);
				FileOutputStream fos = new FileOutputStream(cfgFile);
				int n = 0;
				byte[] b = new byte[1024];
				while ((n=is.read(b)) != -1) {
					fos.write(b, 0, n);
				}
				fos.flush();
				fos.close();
				is.close();
			}
			cfgFile = new File(BASE_PATH + "\\cfg\\" + SITES_PROPERTIES);
			PabloSite ps = new PabloSite();
			Properties pro = new Properties();			
			pro.load(new FileInputStream(cfgFile));
			ps.setSystem_test(pro.getProperty("System_test"));
			ps.setMain_url(pro.getProperty("main_url"));
			ps.setLogin_url(pro.getProperty("login_url"));
			ps.setDropbox_url(pro.getProperty("dropbox_url"));
			ps.setMainview_diary_url(pro.getProperty("diary_center"));
			ps.setHome_url(pro.getProperty("home_url"));
			ps.setUsermapping_url(pro.getProperty("usermapping"));
			ps.setDiary_color_config(pro.getProperty("diary_color_config"));
			ps.setDiary_summary(pro.getProperty("diary_summary"));
			ps.setTeam_allocation_url(pro.getProperty("team_allocation_url"));
			ps.setPayroll_url(pro.getProperty("payroll_url"));
			ps.setBank_holiday_url(pro.getProperty("bank_holiday_url"));
			ps.setDropbox_url(pro.getProperty("dropbox_url"));
			ps.setDatasource_config_url(pro.getProperty("datasource_configuration"));
			ps.setDropbox_administrator(pro
					.getProperty("dropbox_administrator"));
			ps.setDropbox_configuration(pro
					.getProperty("dropbox_configuration"));
			return ps;
		} catch (Exception ex) {
			WriteLogFile.logger.info(ex.getMessage());
			return null;
		}
	}
	
	
	public static MailNotification loadAllAddressMail(){
		try {
			File cfgFolder = new File(BASE_PATH + "\\cfg");
			if (!cfgFolder.exists() || !cfgFolder.isDirectory()) {
				cfgFolder.mkdirs();
			}
			File cfgFile = new File(BASE_PATH + "\\cfg\\" + MAILNOTIFICATION);
			if (!cfgFile.exists() || !cfgFile.isFile()) {
				InputStream is =  IOUTIL.class.getResourceAsStream("/" + MAILNOTIFICATION);
				FileOutputStream fos = new FileOutputStream(cfgFile);
				int n = 0;
				byte[] b = new byte[1024];
				while ((n=is.read(b)) != -1) {
					fos.write(b, 0, n);
				}
				fos.flush();
				fos.close();
				is.close();
			}
			cfgFile = new File(BASE_PATH + "\\cfg\\" + MAILNOTIFICATION);
			Properties pro = new Properties();			
			pro.load(new FileInputStream(cfgFile));
			MailNotification mailNotifi = new MailNotification();
			String notifiAddress = pro.getProperty("send_to");
			List<String> sendTo = null;
			if(notifiAddress!=null && notifiAddress.length() > 0){
				String[] adds = notifiAddress.split(",");
				if(adds.length > 0 && adds!=null){
					sendTo = new ArrayList<String>();
					for(String add :  adds){
						if(add.length()> 0 && add.contains("@c-mg.com")){
							sendTo.add(add);
						}
					}
				}
			}
			mailNotifi.setSendto(sendTo);
			return mailNotifi;
		} catch (Exception e) {
			WriteLogFile.logger.info(e.getMessage());
			return null;
		}
	}
	
	
}

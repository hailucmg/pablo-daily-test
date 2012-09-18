package com.bp.pablo.selenium.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.bp.pablo.element.DatabaseInfor;
import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;


public class IOUTIL {
	public static final String ACCOUNT_PROPERTIES = "account.properties";
	public static final String SITES_PROPERTIES = "site.properties";
	public static final String AVIARY_PROPERTIES = "aviaryconnection.properties";
	/**
	 * Load account test.
	 *
	 * @return the test account
	 */
	public static TestAccount loadAccountTest() {

		try {
			TestAccount acc = new TestAccount();
			Properties pro = new Properties();
			pro.load(IOUTIL.class.getResourceAsStream("/" + ACCOUNT_PROPERTIES));
			String username_admin = pro.getProperty("username_admin");
			String password_admin = pro.getProperty("password_admin");
			String username_normal = pro.getProperty("username_normal");
			String password_normal = pro.getProperty("password_normal");
			acc.setUsername_admin(username_admin);
			acc.setPassword_admin(password_admin);
			acc.setUsername_normal(username_normal);
			acc.setPassword_normal(password_normal);
			return acc;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e2) {
			e2.printStackTrace();
			SendMailSSL.sendMailCMG("<h3>account.properties is null!</h3>",
					"File account.properties is null");
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
			PabloSite ps = new PabloSite();
			Properties pro = new Properties();			
			pro.load(IOUTIL.class.getResourceAsStream("/" + SITES_PROPERTIES));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			SendMailSSL.sendMailCMG("<h3>pablo_site.properties is null!</h3>",
					"file site.properties is null");
			return null;
		}
	}
	
	public static DatabaseInfor loadInfor(String type){
		DatabaseInfor dbInfor = new DatabaseInfor();
		Properties pro = new Properties();
		if(type.equalsIgnoreCase("aviary")){
			try {
			pro.load(IOUTIL.class.getResourceAsStream("/" + AVIARY_PROPERTIES));
			} catch (IOException e) {
				SendMailSSL.sendMailCMG("<h3>aviaryconnection.properties is null!</h3>","file aviaryconnection.properties is null");
				return null;
			}
		}
		dbInfor.setSystem(pro.getProperty("system"));
		dbInfor.setEnvironment(pro.getProperty("environment"));
		dbInfor.setOperation(pro.getProperty("operation"));
		dbInfor.setServer(pro.getProperty("server"));
		dbInfor.setInstance_name(pro.getProperty("instance_name"));
		dbInfor.setInstance_type(pro.getProperty("instance_type"));
		dbInfor.setUsername(pro.getProperty("username"));
		dbInfor.setPassword(pro.getProperty("password"));
		return null;
	}
}

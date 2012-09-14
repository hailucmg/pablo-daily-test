package com.bp.pablo.selenium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;

public class IOUTIL {
	public static final String ACCOUNT_PROPERTIES = "account.properties";
	public static final String SITES_PROPERTIES = "site.properties";

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
			ps.setDiary_configuration(pro.getProperty("diary_configuration"));
			ps.setDiary_color_config(pro.getProperty("diary_color_config"));
			ps.setDiary_summary(pro.getProperty("diary_summary"));
			ps.setTeam_allocation_url(pro.getProperty("team_allocation_url"));
			ps.setPayroll_url(pro.getProperty("payroll_url"));
			ps.setBank_holiday_url(pro.getProperty("bank_holiday_url"));
			ps.setDropbox_url(pro.getProperty("dropbox_url"));
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
}

package com.bp.pablo.selenium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.bp.pablo.element.PabloSite;
import com.bp.pablo.element.TestAccount;

public class IOUTIL {
	
	public static TestAccount loadAccountTest(){
		 File f = new File("account.properties");
		 if(f.exists()){
			 try {
				 TestAccount acc = new TestAccount();
				 Properties pro = new Properties();
				 FileInputStream in = new FileInputStream(f);
				 pro.load(in);
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
				return null;
			}
		 }else{
			 SendMailSSL.sendMailCMG("<h3>account.properties is not exist!</h3>", "Missing file to run system");
			 return null;
		 }
		 
	}
	
	public static PabloSite loadAllUrl(){
		File f = new File("pablo_site.properties");
		if(f.exists()){
			try {
				PabloSite ps = new PabloSite();
				Properties pro = new Properties();
				FileInputStream in;
				in = new FileInputStream(f);
				pro.load(in);
				ps.setMain_url(pro.getProperty("main_url"));
				ps.setLogin_url(pro.getProperty("login_url"));
				return ps;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			SendMailSSL.sendMailCMG("<h3>pablo_site.properties is not exist!</h3>", "Missing file to run system");
			return null;
		}
	}
	
	public static void main(String arg[]){
		/*TestAccount test = loadAccountTest();
		if(test!=null){
			System.out.println(test.getUsername());
			System.out.println(test.getPassword());
		}else{
			System.out.println("sai cmnr");
		}*/
		/*PabloSite ps = loadAllUrl();
		if(ps!=null){
			System.out.println(ps.getMain_url());
			System.out.println(ps.getLogin_url());
		}else{
			System.out.println("sai cmnr");
		}*/
	}
	
}

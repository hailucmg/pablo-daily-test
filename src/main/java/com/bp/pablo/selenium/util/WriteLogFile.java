package com.bp.pablo.selenium.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
/**
 * @author "lan.ta"
 *
 */
public class WriteLogFile {
	public static Logger logger;
	public static final String LOG_NAME = "SeleniumLog.log";
	static {
	    try {
	      File logFolder = new File(IOUTIL.BASE_PATH + "\\log");
	      if (!logFolder.exists() || !logFolder.isDirectory()) {
	    	  logFolder.mkdirs();
	      }
	      boolean append = true;
	      FileHandler fh = new FileHandler(IOUTIL.BASE_PATH + "\\log\\" + LOG_NAME,append);
	      fh.setFormatter(new Formatter() {
	         public String format(LogRecord rec) {
	            StringBuffer buf = new StringBuffer(1000);
	            buf.append(new java.util.Date());
	            buf.append(' ');
	            buf.append(rec.getLevel());
	            buf.append(' ');
	            buf.append(formatMessage(rec));
	            buf.append('\n');
	            buf.append('\n');
	            return buf.toString();
	            }
	          });
	      logger = Logger.getLogger("TestLog");
	      logger.addHandler(fh);
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}

package com.bp.pablo.selenium.util;

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

	static {
	    try {
	      boolean append = true;
	      FileHandler fh = new FileHandler("C:\\SeleniumLog.log",append);
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

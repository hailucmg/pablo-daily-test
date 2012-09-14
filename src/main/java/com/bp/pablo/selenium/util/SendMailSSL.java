/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package com.bp.pablo.selenium.util;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author "lan.ta"
 *
 */
public class SendMailSSL {

    /**
     * @param args
     * @throws javax.mail.MessagingException
     * @throws
     * @throws AddressException
     */
	
    public static void sendMailCMG(String body, String subject)
       {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "lan.ta@c-mg.com", "19092011");
                    }
                });
            MimeMessage message = new MimeMessage(session);
            try {
				message.setFrom(new InternetAddress("lan.ta@c-mg.com"));
				message.setRecipients(Message.RecipientType.TO,
			    InternetAddress.parse("hai.lu@c-mg.com"));
			    message.setSubject(subject);
			    message.setContent(body, "text/html");
			    Transport.send(message);
			} catch (AddressException e) {
				try {
					WriteLogFile.logger.info("Can not send mail");
					throw e;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} catch (javax.mail.MessagingException e) {
				try {
					WriteLogFile.logger.info("Can not send mail");
					throw e;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
           
        
    }

}

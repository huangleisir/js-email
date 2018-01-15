package com.jst.email.common.util;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jst.email.config.ConfigSetting;
import com.jst.prodution.email.serviceBean.MailBean;


public class SendMail {
	private static final Logger log = LoggerFactory.getLogger(SendMail.class);
	public static String toChinese(String text) {
		try {
			text = MimeUtility.encodeText(new String(text.getBytes(), "GB2312"), "GB2312", "B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public static boolean sendMail(MailBean mb) {
		log.info("发邮件入参--："+JSON.toJSONString(mb));
		String host = mb.getHost();
		final String username = ConfigSetting.getProperty("emailFrom");
		final String password = ConfigSetting.getProperty("password");
		String from =ConfigSetting.getProperty("emailFrom");
		String to = mb.getTo();//收件人
		String subject = mb.getSubject();
		String content = mb.getContent();
		String fileName = mb.getFilename();
		Vector<String> file = mb.getFile();
         File sendFile  = mb.getSendFile();
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.exmail.qq.com"); // 设置SMTP的主机
		props.put("mail.smtp.auth", "true"); // 需要经过验证

		Session session = Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);

			Multipart mp = new MimeMultipart();
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setText(content);
			mp.addBodyPart(mbpContent);

			/* 往邮件中添加附件 */
			if (file != null) {
				Enumeration<String> efile = file.elements();
				while (efile.hasMoreElements()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					fileName = efile.nextElement().toString();
					FileDataSource fds = new FileDataSource(fileName);
					mbpFile.setDataHandler(new DataHandler(fds));
					mbpFile.setFileName(toChinese(fds.getName()));
					mp.addBodyPart(mbpFile);
				}
			}
			msg.setContent(mp);
			msg.setSentDate(new Date());
			Transport.send(msg);
		} catch (MessagingException me) {
			me.printStackTrace();
			return false;
		}
		return true;
	}

}
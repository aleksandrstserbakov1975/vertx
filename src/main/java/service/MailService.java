package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import validator.MailValidator;
import enums.MessageEnum;
import enums.StatusEnum;
import model.Mail;
import model.Message;

public class MailService {

	protected static final String CONF_FILE_NAME = "conf.properties";
	protected static final String UTF8 = "UTF-8";
	
	public static class Config {
		public static final String from = "mail.from";
		public static final String replyTo = "mail.replyTo";
		public static final String password = "mail.password";
		public static final String smtpHost = "mail.smtp.host";
		public static final String smtpPort = "mail.smtp.port";
		public static final String smtpAuth = "mail.smtp.auth";
		public static final String smtpStartTLSEnable = "mail.smtp.starttls.enable";
	}
			
	public Message send(Mail mail) {
		Message outputMessage = validate(mail);
		if (isOk(outputMessage)) {
			try {
				sendEmail(mail);
				handleSuccess(outputMessage);
			} catch (Exception e) {
				e.printStackTrace();
				outputMessage = handleException(e, outputMessage);
			}
		}
		return outputMessage;
	}

	protected boolean isOk(Message message){
		return StringUtils.equals(StatusEnum.success.name(), message.getStatus());		
	}
	
	protected Message handleSuccess(Message message){
		message.setStatus(StatusEnum.success.name());
		message.addMessage(MessageEnum.success.getText());
		return message;
	}
	
	protected Message handleException(Exception e, Message errorMessage) {
		errorMessage.setStatus(StatusEnum.error.name());
		errorMessage.addMessage(MessageEnum.error.getText());
		errorMessage.setStackTrace(ExceptionUtils.getStackTrace(e));
		return errorMessage;
	}

	protected Message validate(Mail mail) {
		MailValidator validator = createValidator();
		return validator.validate(mail);
	}

	protected MailValidator createValidator(){
		return new MailValidator();
	}
	
	protected Properties loadConfig() throws Exception {
		Properties props = new Properties();
		InputStream is = new FileInputStream(new File(CONF_FILE_NAME));
		props.load(is);
		return props;
	}
	
	protected void sendEmail(Mail mail) throws Exception {
		MimeMessage mimeMessage = composeMessage(mail);
		sendMimeMessage(mimeMessage);
	}
	
	protected MimeMessage composeMessage(Mail mail) throws Exception {
		Properties config = loadConfig();
		
		final String fromEmail = config.getProperty(Config.from);
		final String password = config.getProperty(Config.password);

		final String toEmail = mail.getTo();

		Properties props = new Properties();
		if (config.containsKey(Config.smtpHost))
			props.put("mail.smtp.host", config.getProperty(Config.smtpHost)); // SMTP Host
		if (config.containsKey(Config.smtpPort))
			props.put("mail.smtp.port", config.getProperty(Config.smtpPort)); // TLS Port
		if (config.containsKey(Config.smtpAuth))
			props.put("mail.smtp.auth", config.getProperty(Config.smtpAuth)); // enable authentication
		if (config.containsKey(Config.smtpStartTLSEnable))
			props.put("mail.smtp.starttls.enable", config.getProperty(Config.smtpStartTLSEnable)); // enable STARTTLS

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = BooleanUtils.toBoolean(config.getProperty(Config.smtpAuth)) ? Session.getInstance(props, auth) : Session.getInstance(props);

		MimeMessage msg = new MimeMessage(session);
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress(fromEmail));
		msg.setReplyTo(InternetAddress.parse(config.getProperty(Config.replyTo), false));
		msg.setSubject(mail.getSubject(), UTF8);
		msg.setText(mail.getBody(), UTF8);
		msg.setSentDate(new Date());
		msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		return msg;
	}
	
	protected void sendMimeMessage(MimeMessage mimeMessage) throws MessagingException {
		Transport.send(mimeMessage);
	}
}

package service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailServiceTestable extends service.MailService {

	@Override
	protected void sendMimeMessage(MimeMessage mimeMessage) throws MessagingException {
		// do not send in real
	}
	
}

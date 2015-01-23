package service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import model.Mail;
import model.Message;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import service.MailService.Config;
import enums.MessageEnum;
import enums.StatusEnum;

public class MailServiceTest {

	private MailService mailService;
	private Properties config = new Properties();
	private Mail mail;
	Message successMessage;
	Message errorMessage;
	
	@Before
    public void init() throws Exception{
       mailService = spy(MailServiceTestable.class);
       config.setProperty(Config.from, "test.from");
       config.setProperty(Config.replyTo, "test.replyTo");
       config.setProperty(Config.smtpHost, "test.host");
       
       doReturn(config).when(mailService).loadConfig();
       
		mail = new Mail();
		mail.setTo("a@hot.com");
		mail.setSubject("test");
		mail.setBody("test");
       
		successMessage = new Message();
		successMessage.setStatus(StatusEnum.success.name());

		errorMessage = new Message();
		errorMessage.setStatus(StatusEnum.error.name());
		errorMessage.addMessage("Validation error");
    }
	
	@Test
	public void testValidEmailMessage(){
		doReturn(successMessage).when(mailService).validate(any(Mail.class));
		
		Message message = mailService.send(mail);
		
		Assert.assertEquals(StatusEnum.success.name(), message.getStatus());
	}

	@Test
	public void testInvalidEmailMessage(){
		doReturn(errorMessage).when(mailService).validate(any(Mail.class));
		
		Message message = mailService.send(mail);
		
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals("Validation error", message.getMessages().get(0));
	}

	@Test
	public void testExceptionOnEmailSend() throws Exception {
		doReturn(successMessage).when(mailService).validate(any(Mail.class));
		doThrow(new MessagingException()).when(mailService).sendMimeMessage(any(MimeMessage.class));
		
		Message message = mailService.send(mail);
		
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals(MessageEnum.error.getText(), message.getMessages().get(0));
	}
	
}

package validator;

import static org.mockito.Mockito.spy;
import junit.framework.Assert;
import model.Mail;
import model.Message;

import org.junit.Before;
import org.junit.Test;

import enums.StatusEnum;

public class MailValidatorTest {

	MailValidator validator = spy(MailValidator.class);
	Mail mail;
	
	@Before
    public void init() throws Exception{
       mail = new Mail();
       mail.setTo("a@hot.ee");
       mail.setBody("test");
       mail.setSubject("test");
	}
       
	@Test
	public void testValid(){
		Message message = validator.validate(mail);
		
		Assert.assertEquals(0, message.getMessages().size());
		Assert.assertEquals(StatusEnum.success.name(), message.getStatus());
	}

	@Test
	public void testInvalidTo(){
		mail.setTo("blablabla");
		Message message = validator.validate(mail);
		
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
	}

	@Test
	public void testEmptyTo(){
		mail.setTo(null);
		Message message = validator.validate(mail);
		
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
	}

	@Test
	public void testInvalidSubject(){
		mail.setSubject(null);
		Message message = validator.validate(mail);
		
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
	}

	@Test
	public void testInvalidTooLongSubject(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < 50; i++) sb.append("bla");
		
		mail.setSubject(sb.toString());
		Message message = validator.validate(mail);
		
		Assert.assertEquals(1, message.getMessages().size());
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
	}
	
	
	@Test
	public void testInvalidAll(){
		mail.setSubject(null);
		mail.setTo(null);
		mail.setBody(null);
		Message message = validator.validate(mail);
		
		Assert.assertEquals(3, message.getMessages().size());
		Assert.assertEquals(StatusEnum.error.name(), message.getStatus());
	}
	
}

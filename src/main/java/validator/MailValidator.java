package validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import enums.StatusEnum;
import model.Mail;
import model.Message;

public class MailValidator {

	private static final int MAX_LENGTH_SUBJECT = 128;
	
	public Message validate(Mail mail){
		Message message = new Message();
		message.setStatus(StatusEnum.success.name());
		validateTo(mail, message);
		validateSubject(mail, message);
		validateBody(mail, message);
		return message;
	}
	
	protected Message validateTo(Mail mail, Message message){
		String to = mail.getTo();
		if (StringUtils.isEmpty(to)){
			addErrorMessage("Field 'To:' is empty.", message);
		} else if (! EmailValidator.getInstance().isValid(to) ){
			addErrorMessage("Field 'To:' contains invalid aadress.", message);
		}
		return message;
	}

	protected Message validateSubject(Mail mail, Message message){
		String subject = mail.getSubject();
		if (StringUtils.isEmpty(subject)){
			addErrorMessage("Field 'Subject:' is empty.", message);
		} else if (StringUtils.length(subject) > MAX_LENGTH_SUBJECT){
			addErrorMessage("Field's 'Subject:' max length is 128 symbols, currently is " + StringUtils.length(subject) + ".", message);
		}		
		return message;
	}
	
	protected Message validateBody(Mail mail, Message message){
		String body = mail.getBody();
		if (StringUtils.isEmpty(body)){
			addErrorMessage("Field 'Body:' is empty.", message);
		}		
		return message;
	}
	
	private void addErrorMessage(String messageText, Message message){
		message.addMessage(messageText);
		message.setStatus(StatusEnum.error.name());
	}
	
}

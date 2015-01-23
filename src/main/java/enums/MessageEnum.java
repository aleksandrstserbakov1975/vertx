package enums;

public enum MessageEnum {
	error("Your email could not be sent, please try again later."),
	success("You email was sent successfully!");
	
	private String text;
	
	MessageEnum(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
}

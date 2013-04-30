package application;

import java.io.Serializable;

public class Serialized_Message implements Serializable{
    private String to;
    private String from;
    private String msgText;
    //other necessary properties

    // a default message that is used when there is no paramiters given
    public Serialized_Message(){
       this("broadcast", "server" ,"default message");
    }

    // The constructor for the given message
    public Serialized_Message(String to, String from, String msgText) {
    	setTo(to);
    	setFrom(from);
        setMsgText(msgText);
	}
    
    public static Serialized_Message encodeMessage(String raw_message){
    	String[] parts = raw_message.split(":");
    	if(parts.length == 1){
    		return new Serialized_Message("broadcast", "default", parts[0].trim());
    	}else if(parts.length == 2){
    		return new Serialized_Message(parts[0].trim(), "default", parts[1].trim());
    	}else if(parts.length == 3){
    		return new Serialized_Message(parts[0].trim(), parts[1].trim(), parts[2].trim());
    	}
    	return new Serialized_Message();
    }

	private void setFrom(String from) {
		this.from = from;
		
	}

	private void setMsgText(String msgText) {
		this.msgText = msgText;
		
	}

	private void setTo(String to) {
		this.to = to;
		
	}

	public String getMessage(){
		return msgText;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getFrom(){
		return from;
	}

	public static void displayMessage(Serialized_Message from_server) {
		System.out.println("From: " + from_server.from + "\n\r" + 
							"Message: " + from_server.msgText);
		
	}
}
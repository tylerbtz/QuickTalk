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
    public Serialized_Message(String id, String from, String msgText) {
    	setTo(to);
    	setFrom(from);
        setMsgText(msgText);
	}
    
    public static Serialized_Message encodeMessage(String raw_message){
    	
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
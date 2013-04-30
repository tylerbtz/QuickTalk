package networking;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import application.Routing_Table;
import application.Serialized_Message;

public class Network_Client extends Thread{

	private static PrintWriter out = null;
	private int socket_number;
	private String ip_address;
	private final BlockingQueue<Serialized_Message> listen;
	private final BlockingQueue<Serialized_Message> talk;
	private static String name;
	
	public Network_Client(int i, String j, LinkedBlockingQueue<Serialized_Message> listen_q,
			LinkedBlockingQueue<Serialized_Message> talk_q, String name_q) {
		setSocketNumber(i);
		setIpAddress(j);
		this.listen = listen_q;
		this.talk = talk_q;
		this.name = name_q;
	}
	
	private void setIpAddress(String string) {
		this.ip_address = string;
		
	}
	private void setSocketNumber(int number) {
		this.socket_number = number;

	}
	public void run(){

		Socket echoSocket = null;

		BufferedReader in = null;

		try {
			echoSocket = new Socket(this.ip_address, this.socket_number);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Could not connect. Unknown server!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: server.");
			System.exit(1);
		}
		
		String inputLine = null;
		try {
			while(!in.ready()){
				System.out.println("Waiting for connect");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			if(in.readLine().equals("Connect")){
				out.println(name);
				System.out.println("here");
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		while (true) {
			try {
				if (in.ready()) {
					inputLine = in.readLine();
					System.out.println("message recieved");
					// Once a line is read in from a client create a serialized message 
					talk.add(creteMessage(inputLine));
					inputLine = null;
				
				}

				while (!listen.isEmpty()) {
					Serialized_Message outputLine = null;
					try {
						//Receive a message from the server main.
						outputLine = listen.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					out.println(sendMessage(outputLine));
					System.out.println("Message sent");
					while (outputLine != null) {
						outputLine = null;
					}

				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * try { out.close(); in.close(); stdIn.close(); echoSocket.close(); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

	

	/*
	 * Creates a message to send in the form of a line
	 */
	private String sendMessage(Serialized_Message outputLine) {
		String message = outputLine.getTo() +" : "+outputLine.getFrom()+" : "+outputLine.getMessage();
		System.out.println("Sending message: " + message);
		return message;
	}

	/*
	 * Creates a message from a read line from a client.  
	 * The line format:  <name of recipient | group | broadcast> : <name of sender> : Message 
	 */
	private Serialized_Message creteMessage(String inputLine) {
		String[] parsed = inputLine.split("\\:");
		Serialized_Message message = new Serialized_Message(parsed[0].trim(),parsed[1].trim(),parsed[2].trim());
		return message;
	}
	

}

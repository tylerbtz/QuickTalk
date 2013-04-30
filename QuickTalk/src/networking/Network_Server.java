package networking;

import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;

import application.Routing_Table;
import application.Serialized_Message;

//import controller.Base;

public class Network_Server extends Thread {
	private int socket_number;
	private final BlockingQueue<Serialized_Message> listen;
	private final BlockingQueue<Serialized_Message> talk;

	public Network_Server(int i, LinkedBlockingQueue<Serialized_Message> listen_q,
			LinkedBlockingQueue<Serialized_Message> talk_q) {
		setSocketNumber(i);
		this.listen = listen_q;
		this.talk = talk_q;
	}

	public void setSocketNumber(int number) {
		this.socket_number = number;

	}

	public void run() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(socket_number);
			System.out.println("Socket started: " + socket_number);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + socket_number);
			System.exit(1);
			
		}


		Socket clientSocket = null;

		try {
			clientSocket = serverSocket.accept();
			System.out.println("New connection established");

		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}

		PrintWriter out = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String inputLine;
		Serialized_Message outputLine = null;

		out.println("Connect");
		try {
			while(!in.ready()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in.ready()) {
				
				String message = in.readLine();
				System.out.println("Message from client: " + message);
				Routing_Table.addListing(message,this.socket_number);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while (true) {
			try {

				if (in.ready()) {
					inputLine = in.readLine();
					
					System.out.println("Message reciever from a client "+ inputLine);
					// Once a line is read in from a client create a serialized message 
					talk.add(creteMessage(inputLine));
					inputLine = null;
				
				}

				while (!listen.isEmpty()) {
					try {
						
						//Receive a message from the server main.
						outputLine = listen.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(Routing_Table.existOnThisSocket(this.socket_number,outputLine.getTo())){
						out.println(sendMessage(outputLine));
					}
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
		 * out.close(); try { in.close(); clientSocket.close();
		 * serverSocket.close(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}
	
	/*
	 * Creates a message to send in the form of a line
	 */
	private String sendMessage(Serialized_Message outputLine) {
		
		return outputLine.getTo()+" : "+outputLine.getFrom()+" : "+outputLine.getMessage();
	}

	/*
	 * Creates a message from a read line from a client.  
	 * The line format:  <name of recipient | group | broadcast> : <name of sender> : Message 
	 */
	private Serialized_Message creteMessage(String inputLine) {
		String[] parsed = inputLine.split(":");
		Serialized_Message message = new Serialized_Message(parsed[0].trim(),parsed[1].trim(),parsed[2].trim());
		System.out.println("new Message to " + message.getTo());
		return message;
	}
}

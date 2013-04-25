package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import networking.Network_Client;
import networking.Network_Server;

public class Main {

	private static int socket_start = 0;
	private static int socket_stop = 0;
	private static LinkedBlockingQueue<Serialized_Message> listen;
	private static LinkedBlockingQueue<Serialized_Message> talk;
	
	/*
	 * Main function that controls how the program is to run.
	 * @param args An array of the passed in arguments from the command line call
	 * @return
	 */
	public static void main(String[] args) {
		
		//Check to see that at least one argument is passed in.
		if (args.length > 0) {
			
			//check to see if the first argument is server.  If it is start the server
			if (args[0].equals("server")) {
				//There should be three arguments to start a server
				if(args.length == 3){
					Main.startServer(args);
				}else{
					Main.printUsage();
				}
			} 
			
			//If it isnt a server check to see if it is a client.  If it is start a client
			else if (args[0].equals("client")) {
				//clients should be four arguments in length.
				if(args.length == 4){
					Main.startClient(args);
				} else{
					Main.printUsage();
				}
			}
			//If it is not a server or client then return the usage to the user
			else {
				Main.printUsage();
			}
		} else {
			//If no arguments are passed in let the user know how to use the program 
			Main.printUsage();
		}
	}

	/*
	 * Prints the application usage to the user with a system out
	 */
	private static void printUsage() {
		System.out.println("Error! Uses are: \n"
				+ "QuickTalk 'server' <starting port> <ending port>\n"
				+ "QuickTalk 'client' <ip address> <port> <user_name>");	
	}

	private static void startClient(String[] args) {
		listen = new LinkedBlockingQueue<Serialized_Message>();
		talk = new LinkedBlockingQueue<Serialized_Message>();
		String client_name = args[3];		
		Thread client_thread = new Network_Client(Integer.parseInt(args[2]),args[1],talk,listen,client_name);
		
		client_thread.start();
		
		
		Serialized_Message from_server = null;
		
		//Create a buffered reader for messages to be typed on the command line
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		
		while (true) {
			
			try{
				if(stdIn.ready()){
					sendMessage(Serialized_Message.encodeMessage(stdIn.readLine()));
					
				}
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while (!listen.isEmpty()) {
				try {
					System.out
							.println("Server Main -> Recieved a message from a client.");
					from_server = listen.take();
					
					//Check if the message is for this client
					//if it is display the message
					if(from_server.getTo().equals(client_name)){
						Serialized_Message.displayMessage(from_server);
					}
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}



	private static void startServer(String[] args) {
		List<Thread> threads = new ArrayList<Thread>();
		
		
		
		// Send server started message and start an instance of the base
		System.out.println("Server Main --> Server started");

		Main.setSocketStart(Integer.parseInt(args[1]));
		Main.setSocketStop(Integer.parseInt(args[2]));
		
		if(Routing_Table.routingList == null){
			Routing_Table.routingList = new ClientInfo[socket_stop - socket_start + 1];
			Routing_Table.setSocketStart(Main.socket_start);
			Routing_Table.setSocketStop(Main.socket_stop);
			System.out.println("Server Main --> Server started");
		}
		
		listen = new LinkedBlockingQueue<Serialized_Message>();
		talk = new LinkedBlockingQueue<Serialized_Message>();
				
		
		
		for (int i = Main.socket_start; i <= Main.socket_stop; i++) {
			threads.add(new Network_Server(i, listen, talk));
			
		}
		for (int j = 0; j < threads.size(); j++) {
			threads.get(j).start();

		}
		Serialized_Message from_client = null;
		

		while (true) {
			while (!listen.isEmpty()) {
				try {
					System.out
							.println("Server Main -> Recieved a message from a client.");
					from_client = listen.take();
					sendMessage(from_client);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Send a message to the attached threads
	 */
	private static void sendMessage(Serialized_Message from_client) {
		talk.add(from_client);
		
	}

	/*
	 * Set the starting value for the servers sockets.
	 */
	public static void setSocketStart(int number) {
		Main.socket_start = number;
	}

	/*
	 * set the stopping value for the servers sockets
	 */
	public static void setSocketStop(int number) {
		Main.socket_stop = number;

	}

}

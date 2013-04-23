package networking;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
	
		while(!in.equals("connect")){
			out.println(name);
		}
		

		/*
		 * try { out.close(); in.close(); stdIn.close(); echoSocket.close(); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

	

	private static void sendMessage(String message) {
		out.println(message);

	}

}

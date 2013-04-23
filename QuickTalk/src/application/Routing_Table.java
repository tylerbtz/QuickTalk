package application;

import java.util.LinkedList;

//To be completed by ron.  Methods must be static
public class Routing_Table {
	RClient[] clientlist;//this is a list of the clients and their addresses
	
	public Routing_Table(int start, int end){
		int size = end-start+1;
		clientlist = new RClient[120];
	}

	//needs to check a table to see if the to is on the socket number
	public static boolean existOnThisSocket(int socket_number, String to) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void addListing(String readLine, int socket_number) {
		// TODO Auto-generated method stub
		
	}



}

class RClient {
	private int socket_number;
	private String client_name;
	 
	public RClient(int thesocket_number, String theName){
		socket_number = thesocket_number;
		client_name = theName;
	}
	
}
package application;

import java.util.LinkedList;

import networking.Network_Client;

//To be completed by ron.  Methods must be static
public class Routing_Table {

	private static int socket_start = 0;
	private static int socket_stop = 0;

	public static ClientInfo[] routingList;

	// needs to check a table to see if the to is on the socket number
	public static boolean existOnThisSocket(int socket_number, String to) {
		if (socket_number >= Routing_Table.socket_start
				&& socket_number <= Routing_Table.socket_stop) {
			if (Routing_Table.routingList[socket_number
					- Routing_Table.socket_start].equals(to)) {
				return true;

			}
		}
		return false;
	}

	public static int getSocketNumber(String readLine) {
		for (int x = Routing_Table.socket_start; x <= Routing_Table.socket_stop; x++) {
			if (Routing_Table.routingList[x	- Routing_Table.socket_start].equals(readLine)) {
				return x;

			}
		}
		return -1;
	}
	
	public static String getReadLine(int socket_number){
		
		if (socket_number >= Routing_Table.socket_start
				&& socket_number <= Routing_Table.socket_stop) {
			return Routing_Table.routingList[socket_number - Routing_Table.socket_start].name;
		}
		return null;
		
	}

	public static void addListing(String readLine, int socket_number) {
		Routing_Table.routingList[socket_number - Routing_Table.socket_start] = new ClientInfo(
				readLine);
	}

	public static void setSocketStart(int number) {
		Routing_Table.socket_start = number;
	}
/////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////
	/*
	 * set the stopping value for the servers sockets
	 */
	public static void setSocketStop(int number) {
		Routing_Table.socket_stop = number;

	}

}

class ClientInfo {
	public String name;

	ClientInfo(String thename) {
		name = thename;

	}
}
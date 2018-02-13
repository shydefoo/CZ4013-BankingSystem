package main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import services.CreateAccountService;

public class Application {
	public static void main(String[] args){
		String serverIpAddress = "localhost";
		int serverPortNumber = 8000;
		Console console = new Console(new Scanner(System.in));
				
		try {
			Client client = new Client(serverIpAddress, serverPortNumber);
			client.addService(0, new CreateAccountService());
			client.execute(0, console);
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

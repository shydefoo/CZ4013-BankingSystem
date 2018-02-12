package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePackerClass;

public class CreateAccountService extends Service {
	
	
	public CreateAccountService(){}
	
	/***
	 * Account creation: 
	 * Required details - 1) Name (string), 2) Password (6 digit integer), 3) Currency type (string?), 4) Initial Balance (Double)
	 * @throws IOException 
	 */
	@Override
	public void executeRequest(Console console, Client client) throws IOException {
		Console.println("---------------------Account creation---------------------------------");
		String name = console.askForString("Enter your name:");
		int pin = console.askForInteger("Input your 6 digit pin-number:");
		String currency = console.askForString("Specify currency type:");
		double init_balance = console.askForDouble("Enter initial balance:");
		int message_id = client.getMessage_id();
		BytePackerClass packer = new BytePackerClass.Builder()
								.setProperty("ServiceId", client.CREATE_ACCOUNT)
								.setProperty("messageId", message_id)
								.setProperty("Name", name)
								.setProperty("Pin", pin)
								.setProperty("Currency",currency)
								.setProperty("Balance", init_balance)
								.build();
		client.send(packer);
		
		
	}

}

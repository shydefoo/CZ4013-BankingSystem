package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public class CreateAccountService extends Service {

	protected final static String NAME = "Name";
	protected final static String PIN = "Pin";
	protected final static String CURRENCY = "Currency";
	protected final static String BALANCE = "Balance";
	
	public CreateAccountService(){
		super(null);
	}
	
	/***
	 * Account creation: 
	 * Required details - 1) Name (string), 2) Password (6 digit integer), 3) Currency type (string), 4) Initial Balance (Double)
	 * @throws IOException 
	 */
	@Override
	public void executeRequest(Console console, Client client) throws IOException {
		Console.println("---------------------Account creation---------------------------------");
		String name = console.askForString("Enter your name:");
		int pin = console.askForInteger("Input your 6 digit pin-number:");
		String currency = console.askForString("Specify currency type:");
		double init_balance = console.askForDouble("Enter initial balance:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		BytePacker packer = new BytePacker.Builder()
								.setProperty(Service.SERVICE_ID, new OneByteInt(Client.CREATE_ACCOUNT))
								.setProperty(Service.MESSAGE_ID, message_id)
								.setProperty(NAME, name)
								.setProperty(PIN, pin)
								.setProperty(CURRENCY,currency)
								.setProperty(BALANCE, init_balance)
								.build();
		client.send(packer);
		
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		/*Check if reply status is 0. 0 means success. */
		if(checkStatus(unpackedMsg)){ 
			
			String reply = unpackedMsg.getString(Service.REPLY);
			Console.println(reply);
		}
		else{
			Console.println("Account create failed");
		}
	}
	
	@Override
	public String ServiceName() {
		return "Create Account";
	}

}

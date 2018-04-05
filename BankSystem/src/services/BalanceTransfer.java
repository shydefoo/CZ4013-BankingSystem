package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
/**
 * This class creates Balance Transfer requests
 * @author Shide
 *
 */
public class BalanceTransfer extends Service {
	/**
	 * Class constructor for BalanceTransfer
	 * 
	 */
	public BalanceTransfer() {
		super(null);
	}
	/***
	 * Balance Transfer (non-idempotent operation): 
	 * Required details - 1) Name (string), 2) Account Number (5 digit integer), 3) Password (6 digit integer), 4) Account Number of Recipient (5 digit integer), 5) amount (double)
	 * @throws IOException 
	 */
	public void executeRequest(Console console, Client client) throws IOException{
		Console.println("---------------------Balance Transfer---------------------------------");
		String name = console.askForString("Enter your name:");
		int accNum = console.askForInteger("Enter your Account Number:");
		int pin = console.askForInteger("Input your 6 digit pin-number:");
		int receiver = console.askForInteger("Enter Account Number of Recipient:");
		double amount = console.askForDouble("Enter amount to transfer:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		
		BytePacker packer = new BytePacker.Builder()
								.setProperty("ServiceId", new OneByteInt(Client.TRANSFER_BALANCE))
								.setProperty("messageId", message_id)
								.setProperty("Name", name)
								.setProperty("accNum", accNum)
								.setProperty("Pin", pin)
								.setProperty("receiver", receiver)
								.setProperty("amount", amount)
								.build();
		client.send(packer);
		
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		if(checkStatus(unpackedMsg)){
			String reply = unpackedMsg.getString(Service.REPLY);
			Console.println(reply);	
		}
		else{
			Console.println("Transfer failed");
		}
	}
	
	@Override
	public String ServiceName() {
		return "Transfer Funds";
	}
}

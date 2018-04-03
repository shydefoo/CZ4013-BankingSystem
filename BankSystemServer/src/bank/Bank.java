package bank;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import main.Console;

public class Bank {
	public static HashMap<Integer, Account> AllTheAccounts;
	public Bank(){
		AllTheAccounts = new HashMap<>();
	}
	
	
	public static int createAccount(String accOwner, int pin, String accCurrency, double accBalance){
		//Generate account number. For now...Use size of hash map. Account number should be an integer. 
		int accNum = -1;
		int min = 1000;
		int max = 9999;
		do{
			accNum = ThreadLocalRandom.current().nextInt(min, max);
		}while(AllTheAccounts.get(accNum)!=null);
		//int accNum = AllTheAccounts.size()+1;
		Account newAcc = new Account.Builder()
						.setAccOwner(accOwner)
						.setAccPin(pin)
						.setAccNumber(accNum)
						.setAccCurrency(accCurrency)
						.setAccBalance(accBalance)
						.build();
		AllTheAccounts.put(accNum, newAcc);
		Console.debug("Account created, acc num: " + accNum);
		return accNum;
	}
	
	public static double checkBalance(int accNum, int pin){
		double balance = 0;
		Account acc = AllTheAccounts.get(accNum);
		if(acc!=null){
			if(acc.getAccPin() == pin){
				balance = acc.getAccBalance();
			}
			else balance = -2; //balance == -2 : invalid pin
		}
		else{
			balance = -1; //balance == -1 : account does not exist
		}
		return balance;
	}
	
	public static double updateBalance(String accOwner, int accNum, int pin, int choice, double amount){
		//Account temp = AllTheAccounts.get(accNum);
		System.out.println("choice " + choice);
		//Check to see if account exists
		if(AllTheAccounts.get(accNum)==null)return -1;
		//Check to see if pin number matches record
		if(AllTheAccounts.get(accNum).getAccPin() != pin)return -2;
		
		//choice = 1 means user wants to deposit money
		if(choice == 1){
			Account temp = AllTheAccounts.get(accNum);
			temp.setAccBalance(temp.getAccBalance() + amount);		
		}
		//withdraw money
		else if(choice==0){
			Account temp = AllTheAccounts.get(accNum);
			//Check to see if account has enough money to withdraw
			if(temp.getAccBalance() > amount){
				temp.setAccBalance(temp.getAccBalance() - amount);
				Console.debug("Account's balance is " + temp.getAccBalance());
			}		
			else {
				Console.debug("Account's balance is not enough. " + temp.getAccBalance());
				return -3;
			}
		}
		else return -4; //Invalid choice	
		
		System.out.println(AllTheAccounts.get(accNum).getAccBalance());		
		return AllTheAccounts.get(accNum).getAccBalance();
	}
	
	public static int closeAccount(String accOwner, int accNum, int pin){
		Account temp = AllTheAccounts.get(accNum);
		if(temp!=null){
			Console.debug("Not null");
			if(temp.getAccPin()==pin){
				AllTheAccounts.remove(accNum);
				return 1;
			}
			else{
				return -2;
			}
		}
		else{
			Console.debug("hmmmm");
			return -1;
		}
	}
	public static double transferBalance(String accOwner, int accNum, int receiver, int pin, double amount){
		
		Account senderAcc, receiverAcc;

		if(AllTheAccounts.get(accNum) == null || AllTheAccounts.get(receiver) == null) {
			return -1;
		}
		
		senderAcc = AllTheAccounts.get(accNum);
		receiverAcc = AllTheAccounts.get(receiver);
		
		if(senderAcc.getAccPin() != pin) {
			return -2; //incorrect pin
		}
		
		
		if(senderAcc.getAccBalance() > amount) {
			senderAcc.setAccBalance(senderAcc.getAccBalance() - amount);
			receiverAcc.setAccBalance(receiverAcc.getAccBalance() + amount);
			System.out.println(AllTheAccounts.get(accNum).getAccBalance());
			System.out.println(AllTheAccounts.get(receiver).getAccBalance());
			return senderAcc.getAccBalance();
		}
		else{
			return -3; //insufficient funds
		}
				
		
	}
	
}

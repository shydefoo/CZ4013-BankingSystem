package bank;

import java.util.HashMap;

public class Bank {
	public static HashMap<Integer, Account> AllTheAccounts;
	public Bank(){
		AllTheAccounts = new HashMap<>();
	}
	
	
	public static int createAccount(String accOwner, int pin, String accCurrency, double accBalance){
		//Generate account number. For now...Use size of hash map. Account number should be an integer. 
		
		int accNum = AllTheAccounts.size()+1;
		Account newAcc = new Account.Builder()
						.setAccOwner(accOwner)
						.setAccPin(pin)
						.setAccNumber(accNum)
						.setAccCurrency(accCurrency)
						.setAccBalance(accBalance)
						.build();
		AllTheAccounts.put(accNum, newAcc);
		System.out.println(accNum);
		return accNum;
	}
	
	
}

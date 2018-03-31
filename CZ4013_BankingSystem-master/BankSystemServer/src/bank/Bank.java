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
	
	public static double updateBalance(String accOwner, int accNum, int pin, String accCurrency, double amount, int choice){
		//choice = 1 means user wants to deposit money
		Account temp;
		if(choice == 1)
		{
			temp = AllTheAccounts.get(accNum);
			temp.setAccBalance(temp.getAccBalance() + amount);
			
		}
		//withdraw money
		else
		{
			temp = AllTheAccounts.get(accNum);
			if(temp.getAccBalance() > amount) {
				temp.setAccBalance(temp.getAccBalance() - amount);
				System.out.println("Account's balance is " + temp.getAccBalance());
			}
				
			else
				System.out.println("Account's balance is not enough " + temp.getAccBalance());
		}
		System.out.println(AllTheAccounts.get(accNum).getAccBalance());
		return temp.getAccBalance();
	}
}

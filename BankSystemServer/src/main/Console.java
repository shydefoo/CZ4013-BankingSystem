package main;
import java.util.Scanner;

/**
 * This class is in charged of printing results on the console.
 * @author Shide
 *
 */
public class Console {
	private Scanner scanner;
	static boolean debug_info = true;
	
	/**
	 * Class constructor of Console 
	 * @param scanner
	 */
	public Console(Scanner scanner) {
		this.scanner = scanner;
	}

	public static void println(String str) {
		System.out.println(str);
	}

	public static void debug(String str) {
		if (debug_info)
			System.out.println(str);
	}

	/**
	 * Prompts the user to enter an integer of choice
	 * @param question
	 *            A string of question to ask user for input
	 * @return user input
	 */
	public int askForInteger(String question) {
		System.out.println(question);
		return askForInteger();
	}

	/**
	 * Scans next input in the console as an integer
	 *
	 * @return Integer if user key in correctly, error message otherwise
	 */
	public int askForInteger() {
		while (true) {
			try {
				return Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException ignored) {
			}
		}
	}

	/**
	 * Prompts user to input answer through a question
	 * @param question - question to prompt user with
	 * @return Double unless input is not a number
	 */
	public double askForDouble(String question) {
		System.out.println(question);
		return askForDouble();
	}
	
	/**
	 * Scans the next input from the user as a double 
	 * @return
	 */
	public double askForDouble() {
		while (true) {
			try {
				return Double.parseDouble(scanner.nextLine());
			} catch (NumberFormatException ignored) {
			}
		}
	}
	/**
	 * Scans the next input as a string
	 * @param question
	 * @return
	 */
	public String askForString(String question) {
		System.out.println(question);
		return scanner.nextLine();
	}
	
	/**
	 * Method to get integer input from user, within a specific range of choices
	 * @param min - smallest option number
	 * @param max - largest option number
	 * @param question - Question to user
	 * @return - input from user
	 */
	public int askForInteger(int min, int max, String question){
		System.out.println(question);
		int choice = min;
		while(true){
			try{
				choice = Integer.parseInt(scanner.nextLine());
				if(choice >= min && choice <=max){
					return choice;
				}
				else{
					System.out.println("Invalid Choice");
					System.out.println(question);
				}
			}catch(NumberFormatException ignored){}
		}
	}
	
	/**
	 * Method to get double input from user, within a specific range of choices
	 * @param min - smallest option number
	 * @param max - largest option number
	 * @param question - Question to user
	 * @return - double input from user
	 */
	public double askForDouble(double min, double max, String question){
		System.out.println(question);
		double choice = min;
		while(true){
			try{
				choice = Double.parseDouble(scanner.nextLine());
				if(choice >= min && choice <=max){
					return choice;
				}
				else{
					System.out.println("Invalid Choice");
					System.out.println(question);
				}
			}catch(NumberFormatException ignored){}
		}
	}
	
	
}

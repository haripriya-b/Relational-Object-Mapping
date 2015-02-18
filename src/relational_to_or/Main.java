package relational_to_or;

import java.util.*;

public class Main {
	
	public static Scanner sc = new Scanner(System.in);
	public static DAO_Factory dao_Factory;
	
	public static void main(String args[]) {
		String username = "", password = "", dbname="";
		String url = "jdbc:mysql://localhost/";
		try {
			System.out.println("Welcome to our Relational to OO Mapping System!");
			System.out.println("Please enter the details:");
			System.out.println("Username:");
			username=sc.nextLine();
			System.out.println("Password:");
			password=sc.nextLine();
			System.out.println("Database name:");
			dbname=sc.nextLine();
			dao_Factory = new DAO_Factory(url+dbname, username, password);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

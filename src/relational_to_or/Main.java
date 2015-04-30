package relational_to_or;

import java.util.*;

/*
 * Reverse-Engineering:
 * 
 * Given an existing database in mysql, this programs detects the various object-oriented features and maps 
 * from relational design to Object-Oriented design. 
 * 
 * After the mapping files are created, to get the POJOS, create a new console configuration by :
 * right click the package -> new -> other -> Hibernate Console Configuration 
 * Create a new configuration from:
 * Run -> Hibernate Code Generation -> Hibernate Code Generation Configuration 
 * 
 * The XML/POJOS created are of the same format as those used in Hibernate.
 * 
 * Team:
 * Anisha Nazareth 	   IMT2012005
 * Anusha P S 		   IMT2012007
 * Haripriya Bendapudi IMT2012018
 */

public class Main {
	
	public static Scanner sc = new Scanner(System.in);
	public static DAO_Factory dao_Factory;
	
	public static void main(String args[]) {
		String username = "", password = "", dbname="";
		try {
			// UI + Input
			System.out.println("Welcome to our Relational to OO Mapping System!");
			System.out.println("Please enter the details:");
			System.out.println("Username:");
			username=sc.nextLine();
			System.out.println("Password:");
			password=sc.nextLine();
			System.out.println("Database name:");
			dbname=sc.nextLine();
			
			
			// Instantiate a DAO_Factory and create a connection
			dao_Factory = new DAO_Factory(dbname, username, password);
			dao_Factory.activateConnection();
			ReverseEnggDAO rdao = dao_Factory.getReverseEnggDAO();
			
			
			// Get names of all the classes in the database
			ArrayList<String> names = rdao.getClassNames();
			for(int i=0; i<names.size(); i++) {
				System.out.println(names.get(i));
			}
			
			// Generate a list of file names for Hibernate Mapping Files for each of the classes in the database
			ArrayList<String> fileNames = new ArrayList<String>();
			for (int i=0;i<names.size();i++) {
				fileNames.add(names.get(i).toLowerCase() + ".hbm.xml");
				//System.out.println(fileNames.get(i));
			}
			
			// Get all the relations between the classes in the database
			rdao.getAllRelations();
			
			// Create classes containing their attributes, pks 
			ArrayList<Class_Details> classes = rdao.getClasses();
			
			// Add all the relations of a class to its Class_Relation object.
			ArrayList<Class_Relation> class_Relations = rdao.getClassRelations();
			for(int i=0; i<class_Relations.size(); i++) {
				System.out.println(class_Relations.get(i).toString());
			}
			
			// Generate the Hibernate Mapping Files.
			XMLWriter file;
			for (int i=0;i<names.size();i++) {
				file = new XMLWriter(classes.get(i),fileNames.get(i),class_Relations);
				file.createXML();
			}
			
			dao_Factory.deactivateConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

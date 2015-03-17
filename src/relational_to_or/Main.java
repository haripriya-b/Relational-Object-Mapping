package relational_to_or;

import java.util.*;

public class Main {
	
	public static Scanner sc = new Scanner(System.in);
	public static DAO_Factory dao_Factory;
	
	public static void main(String args[]) {
		String username = "", password = "", dbname="";
		try {
			System.out.println("Welcome to our Relational to OO Mapping System!");
			System.out.println("Please enter the details:");
			System.out.println("Username:");
			username=sc.nextLine();
			System.out.println("Password:");
			password=sc.nextLine();
			System.out.println("Database name:");
			dbname=sc.nextLine();
			dao_Factory = new DAO_Factory(dbname, username, password);
			dao_Factory.activateConnection();
			ReverseEnggDAO rdao = dao_Factory.getReverseEnggDAO();
			ArrayList<String> names = rdao.getClassNames();
			for(int i=0; i<names.size(); i++) {
				System.out.println(names.get(i));
			}
			
			ArrayList<String> fileNames = new ArrayList<String>();
			for (int i=0;i<names.size();i++) {
				fileNames.add(names.get(i)+".hbm.xml");
				//System.out.println(fileNames.get(i));
			}
			ArrayList<Attribute> a = rdao.getPrimaryKeys("applicant");
			for(int i=0; i<a.size(); i++) {
				a.get(i).print();
			}
			
			ArrayList<Class_Details> classes = rdao.getClasses(names);
			
			ArrayList<Attribute> a1 = rdao.getAttributes("applicant");
			for(int i=0;i<a1.size();i++){
				a1.get(i).print();
			}
			
			XMLWriter configFiles = new XMLWriter();
			configFiles.setFiles(fileNames);
			try {
				configFiles.saveConfig(classes);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
			dao_Factory.deactivateConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

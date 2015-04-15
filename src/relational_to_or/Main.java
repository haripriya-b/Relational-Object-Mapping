package relational_to_or;

import java.io.ObjectInputStream.GetField;
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
				fileNames.add(names.get(i).toLowerCase()+".hbm.xml");
				//System.out.println(fileNames.get(i));
			}
			
			/*System.out.println("Primary Keys:");
			
			ArrayList<Attribute> a = rdao.getPrimaryKeys("PROFESSOR_COURSE");
			for(int i=0; i<a.size(); i++) {
				a.get(i).print();
			}
			
			ArrayList<Class_Details> classes = rdao.getClasses(names);
			
			System.out.println("Attributes");
			
			ArrayList<Attribute> a1 = rdao.getAttributes("PROFESSOR_COURSE");
			for(int i=0;i<a1.size();i++){
				a1.get(i).print();
			}*/
			
			rdao.getAllRelations();
			
			ArrayList<Class_Details> classes = rdao.getClasses();
			
			XMLWriter file;
			for (int i=0;i<names.size();i++) {
				file = new XMLWriter(classes.get(i),fileNames.get(i));
				file.createXML();
			}
			
			
			ArrayList<Class_Relation> class_Relations = rdao.getClassRelations();
			for(int i=0; i<class_Relations.size(); i++) {
				System.out.println(class_Relations.get(i).toString());
			}
			
			dao_Factory.deactivateConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

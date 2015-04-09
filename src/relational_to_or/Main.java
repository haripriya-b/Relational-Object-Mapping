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
			
			XMLWriter file;
			for (int i=0;i<names.size();i++) {
				file = new XMLWriter(classes.get(i),fileNames.get(i));
				file.createXML();
			}
			
			rdao.getAllConstraints();
			rdao.findManyToManyRelations(classes);
			rdao.findInheritance();
			rdao.findComposition();
			rdao.findOnetoOne();
			rdao.findOneToMany();
			
			for(int i=0; i<rdao.getConstraints().size(); i++) {
				System.out.println("Table name = "+rdao.getConstraints().get(i).getTableName().getName()+
						"Referenced table name = "+rdao.getConstraints().get(i).getReferencedTableName().getName()+
						"Column name = "+rdao.getConstraints().get(i).getColumnName().getName()+
						"Relation Type = "+rdao.getConstraints().get(i).getType());
			}
			
			dao_Factory.deactivateConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

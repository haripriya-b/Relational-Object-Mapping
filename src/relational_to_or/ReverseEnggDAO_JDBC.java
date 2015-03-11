package relational_to_or;

import java.util.*;
import java.sql.*;

public class ReverseEnggDAO_JDBC implements ReverseEnggDAO {
	
	Connection dbconnection;
	String dbname;

	public ReverseEnggDAO_JDBC(Connection dbconnection2, String dbname) {
		this.dbconnection = dbconnection2;
		this.dbname = dbname;
	}

	@Override
	public ArrayList<String> getClassNames() {
		
		return null;
	}

	@Override
	public ArrayList<Attribute> getAttributes(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Attribute> getPrimaryKeys(String className) {
		  HashMap m1 = new HashMap(); 
	      m1.put("int", "integer");
	      m1.put("bigint", "long");
	      m1.put("smallint", "short");
	      m1.put("float", "float");
	      m1.put("double", "double");
	      m1.put("numeric", "big_decimal");
	      m1.put("char", "character");
	      m1.put("varchar","string");
	      m1.put("tinyint","byte");
	      m1.put("bit","boolean");
	      m1.put("date","date");
	      m1.put("time","time");
	      m1.put("timestamp","timestamp");
	      m1.put("varbinary","binary");
	      m1.put("blob","blob");
	      m1.put("clob","clob");      
		String sql;
		Statement stmt_primarykey1 = null;
		ArrayList<Attribute> list_of_pks= null;
		try{
			stmt_primarykey1 = dbconnection.createStatement();
			sql = "		select column_name, data_type, character_maximum_length from information_schema.columns where table_schema=" + dbname + "and table_name=" + className + " and column_key='PRI';";
			ResultSet rs_primarykey1 = stmt_primarykey1.executeQuery(sql);
																																																																																																																																																																																			
			//Extract data from result set
			while(rs_primarykey1.next()){
				String column_name = rs_primarykey1.getString("column_name");
				String column_type = rs_primarykey1.getString("data_type");
				int size = rs_primarykey1.getInt("character_maximum_length");
				String data_type = m1.get(column_type);
				Attribute temp_key = new Attribute(column_name,data_type,size,true,true);
				list_of_pks.add(temp_key);
				//Retrieve by column name
				// Add exception handling here if more than one row is returned
			}
			
			
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		// Add exception handling when there is no matching record
		return list_of_pks;
		return null;
		return null;
	}

	@Override
	public ArrayList<Class_Details> getClasses(ArrayList<String> classNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForeignKeyDetails getFKs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<RelatedTables> getOneToOneAssociations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<RelatedTables> getOneToManyAssociation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<RelatedTables> getManyToManyAssociation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<InheritedTables> getInheritace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<RelatedTables> getComposition() {
		// TODO Auto-generated method stub
		return null;
	}

}

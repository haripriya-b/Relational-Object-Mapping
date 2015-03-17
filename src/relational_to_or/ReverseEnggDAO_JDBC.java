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
		String sql;
		Statement stmt = null;
		ArrayList<String> names = new ArrayList<String>();
		String name = "";
		try{
			stmt = dbconnection.createStatement();
			sql = "select table_name from tables where table_schema=\""+dbname+"\"";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				name = rs.getString("table_name");
				names.add(name);
			}
		}
		catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return names;
	}

	@Override
	public ArrayList<Attribute> getAttributes(String className) {
		
		HashMap<String,String> m1 = new HashMap<String,String>();
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
		Statement stmt_attributes = null;
		ArrayList<Attribute> attributes= new ArrayList<Attribute>();
		try {
			
			stmt_attributes = dbconnection.createStatement();
			sql = "select column_name name, data_type type, "
					+ "character_maximum_length size, is_nullable, "
					+ "constraint_type from information_schema.columns "
					+ "LEFT JOIN (select * from information_schema.table_constraints"
					+ " where table_schema='" + dbname + "' and table_name='" 
					+ className + "' and constraint_type='UNIQUE') as tc "
					+ "ON column_name=tc.constraint_name "
					+ "where columns.table_schema='" + dbname + "' and "
					+ "columns.table_name='" + className + "' and column_key!='PRI'";
			
			ResultSet rs = stmt_attributes.executeQuery(sql);
			
			while(rs.next()) {
				String column_name = rs.getString("name");
				String column_type = rs.getString("type");
				int size = rs.getInt("size");
				String data_type = m1.get(column_type);
				String is_nullable = rs.getString("is_nullable");
				boolean is_null = true;
				if (is_nullable.equals("NO"))
					is_null = false;
				String unique = rs.getString("constraint_type");
				boolean is_unique = true;
				if (unique == null)
						is_unique = false;
				Attribute temp_key = new Attribute(column_name,data_type,size,is_null,is_unique);
				attributes.add(temp_key); 
			}
			
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		return attributes;
	}

	@Override
	public ArrayList<Attribute> getPrimaryKeys(String className) {
		HashMap<String,String> m1 = new HashMap<String,String>();
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
		ArrayList<Attribute> list_of_pks= new ArrayList<Attribute>();
		try{
			stmt_primarykey1 = dbconnection.createStatement();
			sql = "select column_name, data_type, character_maximum_length from ";
			sql+="information_schema.columns where table_schema=\"" + dbname + "\" and ";
			sql+="table_name=\"" + className + "\" and column_key='PRI'";
			ResultSet rs_primarykey1 = stmt_primarykey1.executeQuery(sql);
			
			while(rs_primarykey1.next()){
				String column_name = rs_primarykey1.getString("column_name");
				String column_type = rs_primarykey1.getString("data_type");
				int size = rs_primarykey1.getInt("character_maximum_length");
				String data_type = m1.get(column_type);
				Attribute temp_key = new Attribute(column_name,data_type,size,false,true);
				list_of_pks.add(temp_key);
			}
			
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return list_of_pks;
	}

	@Override
	public ArrayList<Class_Details> getClasses(ArrayList<String> classNames) {
		ArrayList<Class_Details> classes = new ArrayList<Class_Details>();
		for(int i=0; i<classNames.size(); i++) {
			Class_Details c = new Class_Details();
			c.setName(classNames.get(i));
			c.setAttributes(getAttributes(classNames.get(i)));
			c.setPrimaryKeys(getPrimaryKeys(classNames.get(i)));
			classes.add(c);
		}
		return classes;
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
package relational_to_or;

import java.util.*;
import java.sql.*;
import org.apache.commons.lang.StringUtils;

public class ReverseEnggDAO_JDBC implements ReverseEnggDAO {
	
	Connection dbconnection;
	String dbname;
	ArrayList<Referential_Constraint> constraints;
	ArrayList<Class_Details> classes;

	public ArrayList<Class_Details> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Class_Details> classes) {
		this.classes = classes;
	}

	public ArrayList<Referential_Constraint> getConstraints() {
		return constraints;
	}

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
	public void generateClasses(ArrayList<String> classNames) {
		classes = new ArrayList<Class_Details>();
		for(int i=0; i<classNames.size(); i++) {
			Class_Details c = new Class_Details();
			c.setName(classNames.get(i));
			c.setAttributes(getAttributes(classNames.get(i)));
			c.setPrimaryKeys(getPrimaryKeys(classNames.get(i)));
			classes.add(c);
		}
	}

	

	@Override
	public Class_Details getClassbyName(String name) {
			for(int i=0; i<classes.size(); i++)
			{
				if( classes.get(i).getName().equals(name) )
					return classes.get(i);
			}
			return null;
	}
	

	@Override
	public Attribute getAttributebyName(String name, Class_Details class1) {
		for(int i=0; i<class1.getAttributes().size(); i++)
		{
			if( class1.getAttributes().get(i).getName().equals(name) )
				return class1.getAttributes().get(i);
		}
		for(int i=0; i<class1.getPrimaryKeys().size(); i++)
		{
			if( class1.getPrimaryKeys().get(i).getName().equals(name) )
				return class1.getPrimaryKeys().get(i);
		}
		return null;
	}

	@Override
	public void getAllConstraints() {
		String sql;
		Statement stmt_referentialconstraint1 = null;
		ArrayList<Referential_Constraint> list_of_rcs= new ArrayList<Referential_Constraint>();
		try{
			stmt_referentialconstraint1 = dbconnection.createStatement();
			sql = "select information_schema.key_column_usage.table_name, column_name," +
					" information_schema.key_column_usage.referenced_table_name, delete_rule" +
					" from information_schema.key_column_usage inner join information_schema.referential_constraints on" +
					" information_schema.referential_constraints.constraint_name = information_schema.key_column_usage.constraint_name" +
					" where information_schema.key_column_usage.table_schema=\"" + dbname + "\"";
			ResultSet rs_referentialconstraint1 = stmt_referentialconstraint1.executeQuery(sql);
			
			while(rs_referentialconstraint1.next()){
				String table_name = rs_referentialconstraint1.getString("key_column_usage.table_name");
				String column_name = rs_referentialconstraint1.getString("key_column_usage.column_name");
				String referenced_table = rs_referentialconstraint1.getString("key_column_usage.referenced_table_name");
				String delete_type = rs_referentialconstraint1.getString("referential_constraints.delete_rule");
				ArrayList<String> class_names = getClassNames();
				generateClasses(class_names);
				Class_Details tbl1 = getClassbyName(table_name);
				Class_Details tbl2 = getClassbyName(referenced_table);
				Attribute column = getAttributebyName(column_name,tbl1);
				boolean delete_rule;
				if(delete_type.equals("CASCADE"))
					delete_rule = true;
				else
					delete_rule = false;
				Referential_Constraint temp_constraint = new Referential_Constraint(tbl1,column,tbl2,delete_rule);
				list_of_rcs.add(temp_constraint);
			}
			
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		this.constraints = list_of_rcs;
	}

	@Override
	public ArrayList<ManyToMany> findManyToManyRelations() {
		ArrayList<ManyToMany> relations = new ArrayList<ManyToMany>();
		for(int i =0; i<classes.size(); i++) {
			if(classes.get(i).getPrimaryKeys().size()==2) {
				ManyToMany new_rel = new ManyToMany();
				Class_Details table1 = new Class_Details();
				Class_Details table2 = new Class_Details();
				boolean table1set = false;
				for(int j=0; j<constraints.size(); j++) {
					if(StringUtils.equals(constraints.get(j).getTableName().getName(),classes.get(i).getName())) {
						constraints.get(j).setType(Relation_Type.MANY_TO_MANY);
						if(!table1set) {
							table1.setName(constraints.get(j).getReferencedTableName().getName());
							table1.setPrimaryKeys(constraints.get(j).getReferencedTableName().getPrimaryKeys());
							table1.setAttributes(constraints.get(j).getReferencedTableName().getAttributes());
						}
						else {
							table2.setName(constraints.get(j).getReferencedTableName().getName());
							table2.setPrimaryKeys(constraints.get(j).getReferencedTableName().getPrimaryKeys());
							table2.setAttributes(constraints.get(j).getReferencedTableName().getAttributes());
						}
					}
				}
				new_rel.setTable1(table1);
				new_rel.setTable2(table2);
				relations.add(new_rel);
			}
		}
		return relations;
	}

	@Override
	public void findInheritance() {
		for(int i=0; i<this.constraints.size();i++) {
			if(this.constraints.get(i).isOnDeleteCascade() && this.constraints.get(i).getType()==null) {
				if(this.constraints.get(i).getTableName().getPrimaryKeys().get(0).equals(this.constraints.get(i).getColumnName())) {
					this.constraints.get(i).setType(Relation_Type.INHERITANCE);
				}
			}
		}
	}

	@Override
	public void findComposition() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).isOnDeleteCascade() && this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.COMPOSITION);
			}
		}
	}

	@Override
	public void findOnetoOne() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).getColumnName().isUnique() && this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.ONE_TO_ONE);
			}
		}
	}

	@Override
	public void findOneToMany() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.MANY_TO_ONE);
			}
		}
	}
	
	@Override
	public void getAllRelations() {
		
		getAllConstraints();
		findManyToManyRelations();
		findInheritance();
		findComposition();
		findOnetoOne();
		findOneToMany();
		
	}


}

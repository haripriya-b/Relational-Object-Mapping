package relational_to_or;

import java.util.*;
import java.sql.*;

import org.apache.commons.lang.StringUtils;

public class ReverseEnggDAO_JDBC implements ReverseEnggDAO {
	// Attributes
	Connection dbconnection;
	String dbname;
	// Details of all the constraints in the database 
	ArrayList<Referential_Constraint> constraints;
	// Details(name, attributes, pks) of all the classes in the database
	ArrayList<Class_Details> classes;
	// Details of all the relations(two classes involved, type of relation, ) between the 
	// various classes in the database
	ArrayList<Class_Relation> class_Relations = new ArrayList<>();
	

	//Getters
	public ArrayList<Class_Details> getClasses() {
		return classes;
	}

	public ArrayList<Class_Relation> getClassRelations() {
		return class_Relations;
	}

	public ArrayList<Referential_Constraint> getConstraints() {
		return constraints;
	}
	
	// Constructor 
	public ReverseEnggDAO_JDBC(Connection dbconnection2, String dbname) {
		this.dbconnection = dbconnection2;
		this.dbname = dbname;
	}

	@Override
	// Given a className, returns all the relations in which that particular class participates
	public Class_Relation getClassRelationbyName(String className) {
		for(int i=0; i<class_Relations.size(); i++) {
			if(class_Relations.get(i).getClass_Details().getName().equals(className)) {
				return class_Relations.get(i);
			}
		}
		return null;
	}

	@Override
	// Gets a list of names of all the classes/tables in the database
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
	// Gets a list of all the attributes(other than the primary keys) of a give class/table 
	// in a format accepted by hibernate
	public ArrayList<Attribute> getAttributes(String className) {
		// A map between the data-types as stored in mysql and their corresponding counter-parts in hibernate
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
			
			// An sql join statement to get the all the attributes except the pks.
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
	// Gets all the primary keys of a class and related data.
	public ArrayList<Attribute> getPrimaryKeys(String className) {
		// A map between the data-types as stored in mysql and their corresponding counter-parts in hibernate
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
			// An sql statement to get the pks and related data.
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
	// Given the names of the various classes in the database, it returns a list of Class_details containing the
	// name of the class, list of its attribute and pks.
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
	// Given the name of the class, returns the details(Class_Details) of that particular class
	public Class_Details getClassbyName(String name) {
			for(int i=0; i<classes.size(); i++)
			{
				if( classes.get(i).getName().equals(name) )
					return classes.get(i);
			}
			return null;
	}
	

	@Override
	// Given the names of the class and the required attribute(even pk), it searches through the list of 
	// attributes and pks and returns the details of that particular attribute
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
	// Queries the information_schema and gets all the possible relations between various classes adds 
	// the relation to the list of Referential_Constraints
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
	// If the primary key is a composite key, it detects and sets the type of the relation to MANY_TO_MANY
	public void findManyToManyRelations() {
		for(int i =0; i<classes.size(); i++) {
			if(classes.get(i).getPrimaryKeys().size()==2 && classes.get(i).getAttributes().size()==0) {
				for(int j=0; j<constraints.size(); j++) {
					if(StringUtils.equals(constraints.get(j).getTable().getName(),classes.get(i).getName())) {
						constraints.get(j).setType(Relation_Type.MANY_TO_MANY);
					}
				}
			}
		}
	}

	@Override
	// If OnDeleteCascade is true and the primary key is a foreign key reference to the other table the type is set to INHERITANCE
	public void findInheritance() {
		for(int i=0; i<this.constraints.size();i++) {
			if(this.constraints.get(i).isOnDeleteCascade() && this.constraints.get(i).getType()==null) {
				if(this.constraints.get(i).getTable().getPrimaryKeys().get(0).equals(this.constraints.get(i).getColumn())) {
					this.constraints.get(i).setType(Relation_Type.INHERITANCE);
				}
			}
		}
	}

	@Override
	// If onDeleteCascade is true and the type of the relation is not yet set, it is set to COMPOSITION 
	public void findComposition() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).isOnDeleteCascade() && this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.COMPOSITION);
			}
		}
	}

	@Override
	// If there is a foreign key reference to a column and the foreign key column is also set as UNIQUE, the relation type is set to ONE_TO_ONE
	public void findOnetoOne() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).getColumn().isUnique() && this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.ONE_TO_ONE);
			}
		}
	}

	@Override
	// All the rest of the relations whose types are not yet set are set to MANY_TO_MANY
	public void findOneToMany() {
		for(int i=0; i<this.constraints.size(); i++) {
			if(this.constraints.get(i).getType()==null) {
				this.constraints.get(i).setType(Relation_Type.MANY_TO_ONE);
			}
		}
	}
	
	@Override
	// Gets all the constraints, sets the type of relation and add the relation to the set of relation in
	// each of the class that participates in the relation.
	public void getAllRelations() {
		
		// Gets all the possible relations and detects and sets the type of the relation
		getAllConstraints();
		findManyToManyRelations();
		findInheritance();
		findComposition();
		findOnetoOne();
		findOneToMany();
		
		for(int i=0; i<classes.size(); i++) {
			Class_Relation cr = new Class_Relation();
			cr.setClass_Details(classes.get(i));
			class_Relations.add(cr);
		}
		
		// Goes through all the relations and adds them to each of the class that participates in the relation
		for(int i = 0; i<constraints.size(); i++) {
			if(constraints.get(i).getType()==Relation_Type.ONE_TO_ONE) {
				getClassRelationbyName(constraints.get(i).getTable().getName()).addRelation(constraints.get(i));
				Referential_Constraint newConstraint = new Referential_Constraint();
				newConstraint.setTable(constraints.get(i).getReferencedTable());
				newConstraint.setReferencedTable(constraints.get(i).getTable());
				newConstraint.setColumn(constraints.get(i).getColumn());
				newConstraint.setOnDeleteCascade(constraints.get(i).isOnDeleteCascade());
				newConstraint.setType(constraints.get(i).getType());
				newConstraint.setInverse(true);
				getClassRelationbyName(constraints.get(i).getReferencedTable().getName()).addRelation(newConstraint);
			}
			else if (constraints.get(i).getType()==Relation_Type.MANY_TO_ONE) {
				getClassRelationbyName(constraints.get(i).getTable().getName()).addRelation(constraints.get(i));
				Referential_Constraint newConstraint = new Referential_Constraint();
				newConstraint.setTable(constraints.get(i).getReferencedTable());
				newConstraint.setReferencedTable(constraints.get(i).getTable());
				newConstraint.setColumn(constraints.get(i).getColumn());
				newConstraint.setOnDeleteCascade(constraints.get(i).isOnDeleteCascade());
				newConstraint.setType(constraints.get(i).getType());
				newConstraint.setInverse(true);
				getClassRelationbyName(constraints.get(i).getReferencedTable().getName()).addRelation(newConstraint);
			}
			else if (constraints.get(i).getType()==Relation_Type.MANY_TO_MANY) {
				for(int j=i+1; j<constraints.size(); j++) {
					if(constraints.get(i).getTable().getName().equals(
							constraints.get(j).getTable().getName())) {
						getClassRelationbyName(constraints.get(i).getTable().getName()).addRelation(constraints.get(i));
						constraints.get(j).setInverse(true);
						getClassRelationbyName(constraints.get(j).getTable().getName()).addRelation(constraints.get(j));
						Referential_Constraint newConstraint1 = new Referential_Constraint();
						newConstraint1.setTable(constraints.get(i).getReferencedTable());
						newConstraint1.setReferencedTable(constraints.get(i).getTable());
						newConstraint1.setColumn(constraints.get(i).getColumn());
						newConstraint1.setOnDeleteCascade(constraints.get(i).isOnDeleteCascade());
						newConstraint1.setType(constraints.get(i).getType());
						newConstraint1.setInverse(constraints.get(i).isInverse());
						getClassRelationbyName(constraints.get(i).getReferencedTable().getName()).addRelation(newConstraint1);
						Referential_Constraint newConstraint2 = new Referential_Constraint();
						newConstraint2.setTable(constraints.get(j).getReferencedTable());
						newConstraint2.setReferencedTable(constraints.get(j).getTable());
						newConstraint2.setColumn(constraints.get(j).getColumn());
						newConstraint2.setOnDeleteCascade(constraints.get(j).isOnDeleteCascade());
						newConstraint2.setType(constraints.get(j).getType());
						newConstraint2.setInverse(constraints.get(j).isInverse());
						getClassRelationbyName(constraints.get(j).getReferencedTable().getName()).addRelation(newConstraint2);
					}
				}
			}
			else if (constraints.get(i).getType()==Relation_Type.COMPOSITION) {
				getClassRelationbyName(constraints.get(i).getTable().getName()).addRelation(constraints.get(i));
				Referential_Constraint newConstraint = new Referential_Constraint();
				newConstraint.setTable(constraints.get(i).getReferencedTable());
				newConstraint.setReferencedTable(constraints.get(i).getTable());
				newConstraint.setColumn(constraints.get(i).getColumn());
				newConstraint.setOnDeleteCascade(constraints.get(i).isOnDeleteCascade());
				newConstraint.setType(constraints.get(i).getType());
				newConstraint.setInverse(true);
				getClassRelationbyName(constraints.get(i).getReferencedTable().getName()).addRelation(newConstraint);
			}
			else if(constraints.get(i).getType()==Relation_Type.INHERITANCE) {
				Referential_Constraint newConstraint = new Referential_Constraint();
				newConstraint.setTable(constraints.get(i).getReferencedTable());
				newConstraint.setReferencedTable(constraints.get(i).getTable());
				newConstraint.setColumn(constraints.get(i).getColumn());
				newConstraint.setOnDeleteCascade(constraints.get(i).isOnDeleteCascade());
				newConstraint.setType(constraints.get(i).getType());
				newConstraint.setInverse(constraints.get(i).isInverse());
				getClassRelationbyName(constraints.get(i).getReferencedTable().getName()).addRelation(newConstraint);
			}
		}
	}
}

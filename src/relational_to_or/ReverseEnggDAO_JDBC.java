package relational_to_or;

import java.util.ArrayList;
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
		// TODO Auto-generated method stub
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

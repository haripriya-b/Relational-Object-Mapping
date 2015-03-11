package relational_to_or;

import java.util.ArrayList;

public class ReverseEnggDAO_JDBC implements ReverseEnggDAO {

	@Override
	public ArrayList<String> getClassNames() {
		// TODO Auto-generated method stub
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

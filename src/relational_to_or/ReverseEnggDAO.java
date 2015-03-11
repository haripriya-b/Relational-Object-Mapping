package relational_to_or;

import java.util.ArrayList;
import java.util.Map;

public interface ReverseEnggDAO {
	 ArrayList<String> getClassNames();
	 ArrayList<Attribute> getAttributes(String className);
	 ArrayList<Attribute> getPrimaryKeys(String className);
	 ArrayList<Class_Details> getClasses(ArrayList<String> classNames);
	 ForeignKeyDetails getFKs();
	 ArrayList<RelatedTables> getOneToOneAssociations();
	 ArrayList<RelatedTables> getOneToManyAssociation();
	 ArrayList<RelatedTables> getManyToManyAssociation();
	 ArrayList<InheritedTables> getInheritace();
	 ArrayList<RelatedTables> getComposition();
}

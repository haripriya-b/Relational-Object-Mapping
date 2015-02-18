package relational_to_or;

import java.util.ArrayList;
import java.util.Map;

public interface ReverseEnggDAO {
	 ArrayList<String> getObjects();
	 Map<String, String> getAttributes(String className);
	 Map<String, String> getPrimaryKeys();
	 ForeignKeyDetails getFKs();
	 ArrayList<RelatedTables> getOneToOneAssociations();
	 ArrayList<RelatedTables> getOneToManyAssociation();
	 ArrayList<RelatedTables> getManyToManyAssociation();
	 ArrayList<InheritedTables> getInheritace();
	 ArrayList<RelatedTables> getComposition();

}

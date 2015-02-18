package relational_to_or;

import java.util.ArrayList;
import java.util.Map;

public interface ReverseEnggDAO {
	public ArrayList<String> getObjects();
	public Map<String, String> getAttributes(String className);
	public Map<String, String> getPrimaryKeys();
	public ForeignKeyDetails getFKs();
	ArrayList<RelatedTables> getOneToOneAssociations();
	ArrayList<RelatedTables> getOneToManyAssociation();
	ArrayList<RelatedTables> getManyToManyAssociation();
	ArrayList<InheritedTables> getInheritace();
	ArrayList<RelatedTables> getComposition();

}

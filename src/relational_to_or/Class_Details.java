package relational_to_or;

import java.util.ArrayList;

public class Class_Details {
		
	String name;
	ArrayList<Attribute> primaryKeys;
	ArrayList<Attribute> attributes;
	ArrayList<Relation> relations;
	
	public ArrayList<Relation> getRelations() {
		return relations;
	}
	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Attribute> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(ArrayList<Attribute> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	
		



	
	
	
}

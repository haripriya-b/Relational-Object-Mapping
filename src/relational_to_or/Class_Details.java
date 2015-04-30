package relational_to_or;

import java.util.ArrayList;

// Stores all the attributes and primary keys of a class.

public class Class_Details {
	// Attributes
	String name;
	ArrayList<Attribute> primaryKeys;
	ArrayList<Attribute> attributes;

	// Getters and Setters
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

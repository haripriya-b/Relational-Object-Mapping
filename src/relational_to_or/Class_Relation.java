package relational_to_or;

import java.util.ArrayList;

// Stores the details of the various relations that the class participates in


public class Class_Relation {
	// Attributes
	Class_Details class_Details;
	ArrayList<Referential_Constraint> relations = new ArrayList<>();
	
	// Getters and Setters. 
	public Class_Details getClass_Details() {
		return class_Details;
	}
	public void setClass_Details(Class_Details class_Details) {
		this.class_Details = class_Details;
	}
	public ArrayList<Referential_Constraint> getRelations() {
		return relations;
	}
	public void setRelations(ArrayList<Referential_Constraint> relations) {
		this.relations = relations;
	}
	
	// Adds a relation to the set of relations of the class
	public void addRelation(Referential_Constraint relation) {
		relations.add(relation);
	}
	
	@Override
	public String toString() {
		String r = "";
		for(int i =0; i<relations.size(); i++) {
			r = r+relations.get(i).toString()+" ";
		}
		return "Class_Relation [class_Details=" + class_Details.getName()
				+ ", relations=" + r + "]";
	}
	
}

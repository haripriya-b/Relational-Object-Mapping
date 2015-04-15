package relational_to_or;

import java.util.ArrayList;

public class Class_Relation {
	Class_Details class_Details;
	ArrayList<Referential_Constraint> relations = new ArrayList<>();
	
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
	public void addRelation(Referential_Constraint relation) {
		relations.add(relation);
	}
}

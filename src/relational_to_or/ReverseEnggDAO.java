package relational_to_or;

import java.util.ArrayList;

public interface ReverseEnggDAO {
	// Getters
	ArrayList<Referential_Constraint> getConstraints();
	ArrayList<Class_Details> getClasses();
	ArrayList<Class_Relation> getClassRelations();
	 
	// Given a className, returns all the relations in which that particular class participates
	Class_Relation getClassRelationbyName(String className);
	 
	// Gets a list of names of all the classes/tables in the database
	ArrayList<String> getClassNames();
	 
	// Gets a list of all the attributes(other than the primary keys) of a give class/table 
	// in a format accepted by hibernate
	ArrayList<Attribute> getAttributes(String className);
	
	// Gets all the primary keys of a class and related data.
	ArrayList<Attribute> getPrimaryKeys(String className);
	
	// Given the names of the various classes in the database, it returns a list of Class_details containing the
	// name of the class, list of its attribute and pks.
	void generateClasses(ArrayList<String> classNames);
	
	// Given the name of the class, returns the details(Class_Details) of that particular class
	Class_Details getClassbyName(String name);

	// Given the names of the class and the required attribute(even pk), it searches through the list of 
	// attributes and pks and returns the details of that particular attribute
	Attribute getAttributebyName(String name, Class_Details class1);
	
	// Queries the information_schema and gets all possible relations between the various classes of the 
	// database
	void getAllConstraints();
	
	// Detects and sets MANY_TO_MANY relations
	void findManyToManyRelations();
	
	// Detects and sets INHERITANCE
	void findInheritance();
	
	// Detects and sets COMPOSITION
	void findComposition();
	
	// Detects and sets ONE_TO_ONE
	void findOnetoOne();
	
	// Detects and sets ONE_TO_MANY
	void findOneToMany();
	
	// Gets all the constraints, sets the type of relation and add the relation to the set of relation in
	// each of the class that participates in the relation.
	void getAllRelations();
	 
}

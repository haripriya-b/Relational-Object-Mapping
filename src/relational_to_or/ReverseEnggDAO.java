package relational_to_or;

import java.util.ArrayList;

public interface ReverseEnggDAO {
	 ArrayList<Referential_Constraint> getConstraints();
	 ArrayList<Class_Details> getClasses();
	 ArrayList<Class_Relation> getClassRelations();
	 Class_Relation getClassRelationbyName(String className);
	 ArrayList<String> getClassNames();
	 ArrayList<Attribute> getAttributes(String className);
	 ArrayList<Attribute> getPrimaryKeys(String className);
	 void generateClasses(ArrayList<String> classNames);
	 Class_Details getClassbyName(String name);
	 Attribute getAttributebyName(String name, Class_Details class1);
	 void getAllConstraints();
	 ArrayList<ManyToMany> findManyToManyRelations();
	 void findInheritance();
	 void findComposition();
	 void findOnetoOne();
	 void findOneToMany();
	 void getAllRelations();
	 
}

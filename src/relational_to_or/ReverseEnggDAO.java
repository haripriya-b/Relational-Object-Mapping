package relational_to_or;

import java.util.ArrayList;
import java.util.Map;

public interface ReverseEnggDAO {
	 ArrayList<String> getClassNames();
	 ArrayList<Attribute> getAttributes(String className);
	 ArrayList<Attribute> getPrimaryKeys(String className);
	 ArrayList<Class_Details> getClasses(ArrayList<String> classNames);
	 Class_Details getClassbyName(String name, ArrayList<Class_Details> classes);
	 Attribute getAttributebyName(String name, Class_Details class1);
	 void getAllConstraints();
	 ArrayList<ManyToMany> findManyToManyRelations(ArrayList<Class_Details> classes);
	 void findInheritance();
	 void findComposition();
	 void findOnetoOne();
	 void findOneToMany();
}

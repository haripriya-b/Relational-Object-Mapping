package relational_to_or;

import java.util.ArrayList;
import java.util.Map;

public interface ReverseEnggDAO {
	 ArrayList<String> getClassNames();
	 ArrayList<Attribute> getAttributes(String className);
	 ArrayList<Attribute> getPrimaryKeys(String className);
	 ArrayList<Class_Details> getClasses(ArrayList<String> classNames);
	 ArrayList<Referential_Constraint> getAllConstraints();
	 ArrayList<ManyToMany> getManyToManyRelations(ArrayList<Referential_Constraint> constraints, ArrayList<Class_Details> classes);
	 ArrayList<Referential_Constraint> findManyToManyRelations(ArrayList<Referential_Constraint> constraints, ArrayList<Class_Details> classes);
	 ArrayList<Referential_Constraint> findInheritance(ArrayList<Referential_Constraint> constraints);
	 ArrayList<Referential_Constraint> findComposition(ArrayList<Referential_Constraint> constraints);
	 ArrayList<Referential_Constraint> findOnetoOne(ArrayList<Referential_Constraint> constraints);
	 ArrayList<Referential_Constraint> findOneToMany(ArrayList<Referential_Constraint> constraints);
}

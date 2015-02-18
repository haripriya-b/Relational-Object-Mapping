package relational_to_or;

import java.util.ArrayList;

public class InheritedTables {
	String superClass;
	ArrayList<String> subClasses;
	
	public InheritedTables() {}
	
	public String getSuperClass() {
		return superClass;
	}
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}
	public ArrayList<String> getSubClasses() {
		return subClasses;
	}
	public void setSubClasses(ArrayList<String> subClasses) {
		this.subClasses = subClasses;
	}
	
	

}

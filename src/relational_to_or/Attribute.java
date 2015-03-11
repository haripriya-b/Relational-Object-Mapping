package relational_to_or;

public class Attribute {
	
	String name;
	String type;
	int size;
	boolean notnull;
	boolean unique;
	
	
	public Attribute(String name, String type, int size, boolean notnull,
			boolean unique) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.notnull = notnull;
		this.unique = unique;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isNotnull() {
		return notnull;
	}
	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public void print() {
		System.out.println("Attribute [name=" + name + ", type=" + type + ", size=" + size
				+ ", notnull=" + notnull + ", unique=" + unique + "]");
	}
	
	

}

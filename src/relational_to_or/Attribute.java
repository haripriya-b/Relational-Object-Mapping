package relational_to_or;

public class Attribute {
	
	String name;
	String type;
	int size;
	boolean is_nullable;
	boolean unique;
	
	
	public Attribute(String name, String type, int size, boolean notnull,
			boolean unique) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.is_nullable = notnull;
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
		return is_nullable;
	}
	public void setNotnull(boolean notnull) {
		this.is_nullable = notnull;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public void print() {
		System.out.println("Attribute [name=" + name + ", type=" + type + ", size=" + size
				+ ", notnull=" + is_nullable + ", unique=" + unique + "]");
	}
	
	

}

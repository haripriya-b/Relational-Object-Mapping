package relational_to_or;

public class ManyToMany {
	Class_Details table1;
	Class_Details table2;

	public ManyToMany(){
	}
	public ManyToMany(Class_Details table1, Class_Details table2) {
		this.table1 = table1;
		this.table2 = table2;
	}
	public Class_Details getTable1() {
		return table1;
	}
	public void setTable1(Class_Details table1) {
		this.table1 = table1;
	}
	public Class_Details getTable2() {
		return table2;
	}
	public void setTable2(Class_Details table2) {
		this.table2 = table2;
	}
}

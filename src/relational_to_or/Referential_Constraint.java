package relational_to_or;

public class Referential_Constraint {
		Class_Details table;
		Attribute column;
		Class_Details referencedTable;
		boolean onDeleteCascade;
		boolean inverse = false;
		Relation_Type type;
		
		public Referential_Constraint(){}
		public Referential_Constraint(Class_Details table,
				Attribute column, Class_Details referencedTable,
				boolean onDeleteCascade) {
			this.table = table;
			this.column = column;
			this.referencedTable = referencedTable;
			this.onDeleteCascade = onDeleteCascade;
		}
		public Class_Details getTable() {
			return table;
		}
		public void setTable(Class_Details table) {
			this.table = table;
		}
		public Attribute getColumn() {
			return column;
		}
		public void setColumn(Attribute column) {
			this.column = column;
		}
		public Class_Details getReferencedTable() {
			return referencedTable;
		}
		public void setReferencedTable(Class_Details referencedTable) {
			this.referencedTable = referencedTable;
		}
		public boolean isOnDeleteCascade() {
			return onDeleteCascade;
		}
		public void setOnDeleteCascade(boolean onDeleteCascade) {
			this.onDeleteCascade = onDeleteCascade;
		}
		public boolean isInverse() {
			return inverse;
		}
		public void setInverse(boolean inverse) {
			this.inverse = inverse;
		}
		public Relation_Type getType() {
			return type;
		}
		public void setType(Relation_Type type) {
			this.type = type;
		}
		@Override
		public String toString() {
			return "Referential_Constraint [table=" + table.getName() + ", column="
					+ column.getName() + ", referencedTable=" + referencedTable.getName()
					+ ", onDeleteCascade=" + onDeleteCascade + ", inverse="
					+ inverse + ", type=" + type + "]";
		}
		
		
}

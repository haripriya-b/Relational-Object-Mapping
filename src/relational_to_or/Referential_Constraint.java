package relational_to_or;

public class Referential_Constraint {
		Class_Details tableName;
		Attribute columnName;
		Class_Details referencedTableName;
		boolean onDeleteCascade;
		Relation_Type type;
		
		public Referential_Constraint(Class_Details tableName,
				Attribute columnName, Class_Details referencedTableName,
				boolean onDeleteCascade, Relation_Type type) {
			this.tableName = tableName;
			this.columnName = columnName;
			this.referencedTableName = referencedTableName;
			this.onDeleteCascade = onDeleteCascade;
			this.type = type;
		}
		public Class_Details getTableName() {
			return tableName;
		}
		public void setTableName(Class_Details tableName) {
			this.tableName = tableName;
		}
		public Attribute getColumnName() {
			return columnName;
		}
		public void setColumnName(Attribute columnName) {
			this.columnName = columnName;
		}
		public Class_Details getReferencedTableName() {
			return referencedTableName;
		}
		public void setReferencedTableName(Class_Details referencedTableName) {
			this.referencedTableName = referencedTableName;
		}
		public boolean isOnDeleteCascade() {
			return onDeleteCascade;
		}
		public void setOnDeleteCascade(boolean onDeleteCascade) {
			this.onDeleteCascade = onDeleteCascade;
		}
		public Relation_Type getType() {
			return type;
		}
		public void setType(Relation_Type type) {
			this.type = type;
		}
}

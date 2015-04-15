package relational_to_or;

public class Relation {
	String related_class;
	String fk_column;
	Relation_Type relation_type;
	
	public Relation() {
	}
	public Relation(String related_class, String fk_column,
			Relation_Type relation_type) {
		this.related_class = related_class;
		this.fk_column = fk_column;
		this.relation_type = relation_type;
	}
	public String getRelated_class() {
		return related_class;
	}
	public void setRelated_class(String related_class) {
		this.related_class = related_class;
	}
	public String getFk_column() {
		return fk_column;
	}
	public void setFk_column(String fk_column) {
		this.fk_column = fk_column;
	}
	public Relation_Type getRelation_type() {
		return relation_type;
	}
	public void setRelation_type(Relation_Type relation_type) {
		this.relation_type = relation_type;
	}
}

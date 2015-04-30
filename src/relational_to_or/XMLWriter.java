package relational_to_or;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

// Using DOM API, it creates a tree for a given class and using this tree, generates the XML-Mapping file for
// a given class.
public class XMLWriter {
	// Attribute
	Class_Details c;
	String file_name;
	Document doc;
	ArrayList<Class_Relation> class_Relations;
	
	// Constructor
	public XMLWriter(Class_Details c, String file_name, ArrayList<Class_Relation> class_Relations) {
		this.c = c;
		this.file_name = file_name;
		this.class_Relations = class_Relations;
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		this.doc = doc;
	}
	
	// Constructor
	public XMLWriter (Class_Details c, ArrayList<Class_Relation> class_Relations, Document doc) {
		this.c = c;
		this.class_Relations = class_Relations;
		this.doc = doc;
	}
	
	// Creates an XML Document
	public void createXML() {
		createTree();
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("hibernate-mapping",
				    "-//Hibernate/Hibernate Mapping DTD//EN",
				    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/home/haripriya/workspace/ReverseEngg/src/reverse", file_name).getPath());
			transformer.transform(source, result);
		}
		catch(TransformerException tfe) {
			tfe.printStackTrace();
		}		
	}
	
	// Creates a DOM tree with root as hibernate-mapping and the class as the child of the root.
	// All pks, attributes and relations of the class are added as children of the class. 
	private void createTree() {
		// creating root element
		Element rootElement = doc.createElement("hibernate-mapping");
		doc.appendChild(rootElement);
		
		//creating class element and its attributes
		Element classElement = doc.createElement("class");
		rootElement.appendChild(classElement);
		String refTable = c.getName().substring(0, 1).toUpperCase() + c.getName().substring(1, c.getName().length()).toLowerCase();
		classElement.setAttribute("name", refTable);
		classElement.setAttribute("table", c.getName());
		
		addPrimaryKeys(classElement);
		addAttributes(classElement);
		addRelationships(classElement);
	}
	
	// Sets the attributes of primary keys and adds them as children of a class in the DOM tree.  
	private void addPrimaryKeys(Element classElement) {
		ArrayList <Attribute> primaryKeys = c.getPrimaryKeys();
		//creating id element for pks
		if(primaryKeys.size()==1) {
			Element idElement = doc.createElement("id");
			classElement.appendChild(idElement);
			idElement.setAttribute("name", primaryKeys.get(0).getName().toLowerCase());
			idElement.setAttribute("column", primaryKeys.get(0).getName());
			idElement.setAttribute("type", primaryKeys.get(0).getType());
	    }
	    else {
	    	Element idElement = doc.createElement("composite-id");
			classElement.appendChild(idElement);
			for(int i=0; i<primaryKeys.size(); i++) {
				Element keyElement = doc.createElement("key-property");
				idElement.appendChild(keyElement);
			    keyElement.setAttribute("name", primaryKeys.get(i).getName().toLowerCase());
				keyElement.setAttribute("column", primaryKeys.get(i).getName());
				keyElement.setAttribute("type", primaryKeys.get(i).getType());
		    }
	    }
	}
	
	// Sets the details of Attributes of a class and adds them as children of the class in the DOM tree 
	private void addAttributes(Element classElement) {
		ArrayList <Attribute> attributes = c.getAttributes();
		//creating property element for each attribute
		for(int i=0; i<attributes.size(); i++) {
			Element propElement = doc.createElement("property");
			classElement.appendChild(propElement);
			propElement.setAttribute("name", attributes.get(i).getName().toLowerCase());
			propElement.setAttribute("column", attributes.get(i).getName());
			propElement.setAttribute("type", attributes.get(i).getType());
			propElement.setAttribute("unique", Boolean.toString(attributes.get(i).isUnique()));
			propElement.setAttribute("not-null", Boolean.toString(!attributes.get(i).isNullable()));
		}
	}
	
	// Adds the various relations of a class as its children in the DOM tree
	private void addRelationships(Element classElement) {
		Class_Relation rel = new Class_Relation();
		for(int i=0;i<class_Relations.size();i++) {
			if (class_Relations.get(i).getClass_Details().getName() == c.getName())
				rel = class_Relations.get(i);
		}
		
		ArrayList<Referential_Constraint> relations = rel.getRelations();
		for (int i=0;i<relations.size();i++) {
			Referential_Constraint relation = relations.get(i);
			
			// If the relation is ONE_TO_ONE, add it as many-to-many with unique=true in class which has 
			// inverse=true and as one-to-one to the other class. 
			if (relation.getType() == Relation_Type.ONE_TO_ONE) {
				
				if(relation.isInverse() == true) {
					Element relElement = doc.createElement("many-to-one");
					classElement.appendChild(relElement);
					relElement.setAttribute("name", relation.getReferencedTable().getName().toLowerCase());
					relElement.setAttribute("unique", "true");
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
					relElement.setAttribute("not-null", "true");
					relElement.setAttribute("column", relation.getColumn().getName());
				}else {
					Element relElement = doc.createElement("one-to-one");
					classElement.appendChild(relElement);
					relElement.setAttribute("name", relation.getReferencedTable().getName().toLowerCase());
					relElement.setAttribute("property-ref", relation.getTable().getName().toLowerCase());
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
				}
					
				
			// If the relation in MANY_TO_ONE, add it as many-to-one to the side having isInverse=false and as 
			// one-to-many with inverse=true to the other side.
			}else if (relation.getType() == Relation_Type.MANY_TO_ONE) {
				if (relation.isInverse() == false) {
					Element relElement = doc.createElement("many-to-one");
					classElement.appendChild(relElement);
					relElement.setAttribute("name", relation.getReferencedTable().getName().toLowerCase());
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
					relElement.setAttribute("column", relation.getColumn().getName());
					//relElement.setAttribute("not-null", "true");
					
				}else if (relation.isInverse() == true) {
					Element setElement = doc.createElement("set");
					classElement.appendChild(setElement);
					setElement.setAttribute("name" , relation.getReferencedTable().getName().toLowerCase());
					setElement.setAttribute("inverse", "true");
					//setElement.setAttribute("cascade", "all");
					
					Element keyElement = doc.createElement("key");
					setElement.appendChild(keyElement);
					keyElement.setAttribute("column", relation.getColumn().getName());
					//keyElement.setAttribute("not-null", "true");
					
					Element relElement = doc.createElement("one-to-many");
					setElement.appendChild(relElement);
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
				}
				
			// If the relation is MANY_TO__MANY and the class is not the joined class of the two classes in the 
			// many-to-many relation, find the other class in the many-to-many relation using the joined class
			// and add it as the many-to-many relation.
			}else if (relation.getType() == Relation_Type.MANY_TO_MANY) {
				
				if(relation.getTable().getPrimaryKeys().size() != 2) {
					Class_Relation joinTable = null;
					Class_Details refTable = null;
					for(int j=0;j<class_Relations.size();j++) {
						if (class_Relations.get(j).getClass_Details().getName().equals(relation.getReferencedTable().getName().toString()))
							joinTable = class_Relations.get(j);
					}
					
					if (joinTable.getRelations().get(0).getReferencedTable().getName().equals(relation.getTable().getName())) {
						refTable = joinTable.getRelations().get(1).getReferencedTable();
					}else {
						refTable = joinTable.getRelations().get(0).getReferencedTable();
					}
					
					Element setElement = doc.createElement("set");
					classElement.appendChild(setElement);
					setElement.setAttribute("name", refTable.getName().toLowerCase());
					setElement.setAttribute("table", joinTable.getClass_Details().getName());
					
					if(relation.isInverse() == true) 
						setElement.setAttribute("inverse", "true");
					
					Element keyElement = doc.createElement("key");
					setElement.appendChild(keyElement);
					keyElement.setAttribute("column", relation.getTable().getPrimaryKeys().get(0).getName());
					
					Element relElement = doc.createElement("many-to-many");
					setElement.appendChild(relElement);
					relElement.setAttribute("column", refTable.getPrimaryKeys().get(0).getName());
					String ref = refTable.getName().substring(0, 1).toUpperCase() + refTable.getName().substring(1, refTable.getName().length()).toLowerCase();
					relElement.setAttribute("class", ref);
					
				}
			// If the relation is INHERITANCE, the sub class along with its properties is added as a
			// joined-subclass to the superclass.
			}else if (relation.getType() == Relation_Type.INHERITANCE) {
				
				Class_Relation subClass = new Class_Relation();
				for (int j=0;j<class_Relations.size();j++) {
					if(relation.getReferencedTable().getName().equals(class_Relations.get(j).getClass_Details().getName())) 
						subClass = class_Relations.get(j);
				}
				
				XMLWriter sub = new XMLWriter(subClass.getClass_Details(), class_Relations,this.doc);
				
				Element subClassElement = doc.createElement("joined-subclass");
				classElement.appendChild(subClassElement);
				subClassElement.setAttribute("name", subClass.getClass_Details().getName().toLowerCase());
				subClassElement.setAttribute("table", subClass.getClass_Details().getName());
				
				Element keyElement = doc.createElement("key");
				subClassElement.appendChild(keyElement);
				keyElement.setAttribute("column", subClass.getClass_Details().getPrimaryKeys().get(0).getName());
				
				sub.addAttributes(subClassElement);
				sub.addRelationships(subClassElement);
				
			// If the relation is COMPOSITION, it is added as a many-to-one relation with the cascade set to all.
			}else if (relation.getType() == Relation_Type.COMPOSITION) {
				if (relation.isInverse() == false) {
					Element relElement = doc.createElement("many-to-one");
					classElement.appendChild(relElement);
					relElement.setAttribute("name", relation.getReferencedTable().getName().toLowerCase());
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
					relElement.setAttribute("column", relation.getColumn().getName());
					relElement.setAttribute("not-null", "true");
					
				}else if (relation.isInverse() == true) {
					Element setElement = doc.createElement("set");
					classElement.appendChild(setElement);
					setElement.setAttribute("name" , relation.getReferencedTable().getName().toLowerCase());
					setElement.setAttribute("inverse", "true");
					setElement.setAttribute("cascade", "all");
					
					Element keyElement = doc.createElement("key");
					setElement.appendChild(keyElement);
					keyElement.setAttribute("column", relation.getColumn().getName());
					keyElement.setAttribute("not-null", "true");
					
					Element relElement = doc.createElement("one-to-many");
					setElement.appendChild(relElement);
					String refTable = relation.getReferencedTable().getName().substring(0, 1).toUpperCase() + relation.getReferencedTable().getName().substring(1, relation.getReferencedTable().getName().length()).toLowerCase();
					relElement.setAttribute("class", refTable);
				}
			}
		}
	}
}


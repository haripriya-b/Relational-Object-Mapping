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

public class XMLWriter {
	Class_Details c;
	String file_name;
	Document doc;
	
	public XMLWriter(Class_Details c, String file_name) {
		this.c = c;
		this.file_name = file_name;
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
			StreamResult result = new StreamResult(new File(file_name));
			transformer.transform(source, result);
		}
		catch(TransformerException tfe) {
			tfe.printStackTrace();
		}		
	}
	
	private void createTree() {
		// creating root element
		Element rootElement = doc.createElement("hibernate-mapping");
		doc.appendChild(rootElement);
		
		//creating class element and its attributes
		Element classElement = doc.createElement("class");
		rootElement.appendChild(classElement);
		classElement.setAttribute("name", c.getName());
		classElement.setAttribute("column", c.getName());
		
		addPrimaryKeys(classElement);
		addAttributes(classElement);
	}
	
	private void addPrimaryKeys(Element classElement) {
		ArrayList <Attribute> primaryKeys = c.getPrimaryKeys();
		//creating id element for pks
		if(primaryKeys.size()==1) {
			Element idElement = doc.createElement("id");
			classElement.appendChild(idElement);
			idElement.setAttribute("name", primaryKeys.get(0).getName());
			idElement.setAttribute("column", primaryKeys.get(0).getName());
			idElement.setAttribute("type", primaryKeys.get(0).getType());
	    }
	    else {
	    	Element idElement = doc.createElement("composite-id");
			classElement.appendChild(idElement);
			for(int i=0; i<primaryKeys.size(); i++) {
				Element keyElement = doc.createElement("key-property");
				idElement.appendChild(keyElement);
			    keyElement.setAttribute("name", primaryKeys.get(i).getName());
				keyElement.setAttribute("column", primaryKeys.get(i).getName());
				keyElement.setAttribute("type", primaryKeys.get(i).getType());
		    }
	    }
	}
	
	private void addAttributes(Element classElement) {
		ArrayList <Attribute> attributes = c.getAttributes();
		//creating property element for each attribute
		for(int i=0; i<attributes.size(); i++) {
			Element propElement = doc.createElement("property");
			classElement.appendChild(propElement);
			propElement.setAttribute("name", attributes.get(i).getName());
			propElement.setAttribute("column", attributes.get(i).getName());
			propElement.setAttribute("type", attributes.get(i).getType());
			propElement.setAttribute("unique", Boolean.toString(attributes.get(i).isUnique()));
			propElement.setAttribute("not-null", Boolean.toString(!attributes.get(i).isNullable()));
		}
	}
}


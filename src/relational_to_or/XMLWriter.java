package relational_to_or;

import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLWriter {
	private ArrayList<String> configFiles;
	
	public void setFiles (ArrayList<String> configFiles) {
		this.configFiles = configFiles;
	}
	
	public void saveConfig(ArrayList<Class_Details> classes) throws Exception {
		
		//create XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		int i;
		for(i=0;i<configFiles.size();i++) {
			//Create XMLEventWriter
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(configFiles.get(i)));
			
			//Create EventFactory
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent open = eventFactory.createDTD("<");
			XMLEvent close = eventFactory.createDTD(">");
						
			//Create and write start tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			eventWriter.add(end);
			
			//Create and write !DOCTYPE
			eventWriter.add(open);
			eventWriter.add(eventFactory.createCharacters( "!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD//EN\"\"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\""));
			eventWriter.add(close);
			eventWriter.add(end);
			
			//Create hibernate-mapping open tag
			StartElement hibernateStartElement = eventFactory.createStartElement("", "", "hibernate-mapping");
			eventWriter.add(hibernateStartElement);
			eventWriter.add(end);
			
			//Create the class tag
			StartElement classStartElement = eventFactory.createStartElement("", "", "class");
			eventWriter.add(classStartElement);
			eventWriter.add(eventFactory.createAttribute("name", classes.get(i).getName()));
			eventWriter.add(eventFactory.createAttribute("table",classes.get(i).getName()));
			eventWriter.add(end);	
			
			//Write the different nodes
			ArrayList <Attribute> pks = classes.get(i).getPrimaryKeys();
			addPrimaryKeys(eventWriter, pks);
			
			ArrayList <Attribute> attributes = classes.get(i).getAttributes();
			for(int j=0; j<attributes.size(); j++) {
				addProperty(eventWriter, attributes.get(j));
			}
			
			
			// Create hibernate-mapping close tag
			eventWriter.add(eventFactory.createEndElement("", "", "hibernate-mapping"));
			eventWriter.add(end);
			
			//Create and write end tag
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
		}
	}
	
	private void addPrimaryKeys(XMLEventWriter eventWriter, ArrayList <Attribute> primaryKeys) throws XMLStreamException {

	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    XMLEvent tab = eventFactory.createDTD("\t");
	    
	    if(primaryKeys.size()==1) {
	    	// create Start node
		    StartElement sElement = eventFactory.createStartElement("", "", "id");
		    eventWriter.add(tab);
		    eventWriter.add(sElement);
		    
	    	// adding attributes to the node
		    eventWriter.add(eventFactory.createAttribute("name", primaryKeys.get(0).getName()));
		    eventWriter.add(eventFactory.createAttribute("column", primaryKeys.get(0).getName()));
		    eventWriter.add(eventFactory.createAttribute("type", primaryKeys.get(0).getType()));
	    
		    // create End node
		    EndElement eElement = eventFactory.createEndElement("", "", "id");
		    eventWriter.add(eElement);
		    eventWriter.add(end);

	    }
	    else {
	    	// create Start node
		    StartElement sElement = eventFactory.createStartElement("", "", "composite-id");
		    eventWriter.add(tab);
		    eventWriter.add(sElement);
		    
	    	// adding attributes to the node
		    for(int i=0; i<primaryKeys.size(); i++) {
			    sElement = eventFactory.createStartElement("", "", "key-property");
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    eventWriter.add(eventFactory.createAttribute("name", primaryKeys.get(i).getName()));
			    eventWriter.add(eventFactory.createAttribute("column", primaryKeys.get(i).getName()));
			    eventWriter.add(eventFactory.createAttribute("type", primaryKeys.get(i).getType()));
			    EndElement eElement = eventFactory.createEndElement("", "", "key-property");
			    eventWriter.add(eElement);
			    eventWriter.add(end);
		    }
		    
		    // create End node
		    EndElement eElement = eventFactory.createEndElement("", "", "composite-id");
		    eventWriter.add(eElement);
		    eventWriter.add(end);
	    }
	}
	
	private void addProperty(XMLEventWriter eventWriter, Attribute attribute) throws XMLStreamException {

	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    XMLEvent tab = eventFactory.createDTD("\t");
	    
	    // create Start node
	    StartElement sElement = eventFactory.createStartElement("", "", "property");
	    eventWriter.add(tab);
	    eventWriter.add(sElement);
	    
	    // adding attributes to the node
	    eventWriter.add(eventFactory.createAttribute("name", attribute.getName()));
	    eventWriter.add(eventFactory.createAttribute("column", attribute.getName()));
	    eventWriter.add(eventFactory.createAttribute("type", attribute.getType()));
	    eventWriter.add(eventFactory.createAttribute("unique", Boolean.toString(attribute.isUnique())));
	    eventWriter.add(eventFactory.createAttribute("not-null", Boolean.toString(!attribute.isNullable())));
	    
	    // create End node
	    EndElement eElement = eventFactory.createEndElement("", "", "property");
	    eventWriter.add(eElement);
	    eventWriter.add(end);

	  }
}

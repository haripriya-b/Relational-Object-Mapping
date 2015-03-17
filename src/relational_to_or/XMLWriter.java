package relational_to_or;

import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;



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
			
			//Create class close tag
			eventWriter.add(eventFactory.createEndElement("", "", "class"));
			eventWriter.add(end);
			
			// Create hibernate-mapping close tag
			eventWriter.add(eventFactory.createEndElement("", "", "hibernate-mapping"));
			eventWriter.add(end);
			
			//Create and write end tag
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
		}
	}
}

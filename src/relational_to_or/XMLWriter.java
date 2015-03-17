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
			
			//Create and write start tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			
			//Create hibernate-mapping open tag
			StartElement hibernateStartElement = eventFactory.createStartElement("", "", "hibernate-mapping");
			eventWriter.add(hibernateStartElement);
			eventWriter.add(end);
			
			//Create the class tag
			
			
			
			//Write the different nodes
			
			
			
			// Create hibernate-mapping close tag
			eventWriter.add(eventFactory.createEndElement("", "", "hibernate-mapping"));
			eventWriter.add(end);
			
			//Create and write end tag
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
		}
	}
}

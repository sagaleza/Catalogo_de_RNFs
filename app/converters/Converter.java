package converters;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import models.system.NFRCatalog;

import org.xml.sax.SAXException;

import services.ontology.OntologyService;


/**
 * Abstract Converter class for multiple models
 * Each specialized converter will need an specialized OntologyService as a component to work
 * @author rveleda
 *
 */
public abstract class Converter<T extends OntologyService> {
	
	private Class<T> clazzService;
	
	private T ontologyService = null;
	
	public Converter(Class<T> clazz) {
		this.clazzService = clazz;
	}

	protected T getOntologyService() {
    	if (ontologyService == null) {
    		try {
				ontologyService = clazzService.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
    	}
    	
        return ontologyService;
	}
	
	public void convert(File targetFile, NFRCatalog newCat) throws ParserConfigurationException, IOException, SAXException {
		this.loadFile(targetFile);
		
		
		// This should be initialized at the very beginning
		//this.getOntologyService().getOntology().loadRemoteModel();

		this.parse(newCat);
		
		this.getOntologyService().getOntology().updateRemoteModel();
		
		this.getOntologyService().getOntology().print();
	}
	
	// TODO Handle these exceptions
	protected abstract void loadFile(File targetFile) throws ParserConfigurationException, IOException, SAXException;
	
	protected abstract void parse(NFRCatalog newCat);
	
	protected abstract void persistSystemInformation(NFRCatalog cat);

}

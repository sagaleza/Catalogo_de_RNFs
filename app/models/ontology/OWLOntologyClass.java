package models.ontology;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Represents a rough version of class from a given OWL Ontology
 * @author rveleda
 *
 */
public abstract class OWLOntologyClass {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = WordUtils.capitalize(name);
		
		name = name.replace(' ', '-');
		name = name.replace('[', '-');
		name = name.replace(']', '-');

		this.name = name;
	}

}

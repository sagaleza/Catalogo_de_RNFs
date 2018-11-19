package models.ontology.ndr;

import models.ontology.OWLOntologyClass;


/**
 * Represents NFRType class from the NDR Ontology
 * @author rveleda
 *
 */
public class NFRType extends OWLOntologyClass {
	
	// TODO Verify inversed relationships
	// Is there a need to implement them here?
	
	public NFRType(String name) {
		this.setName("NFR_" + name);
	}

}

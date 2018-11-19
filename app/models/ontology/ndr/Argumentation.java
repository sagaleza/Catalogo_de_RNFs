package models.ontology.ndr;

import models.ontology.OWLOntologyClass;

/**
 * Represents Argumentation class form the NDR Ontology
 * @author rveleda
 *
 */
public abstract class Argumentation extends OWLOntologyClass {
	
	private Contribution argumentationKind;

	public Contribution getArgumentationKind() {
		return argumentationKind;
	}

	public void setArgumentationKind(Contribution argumentationKind) {
		this.argumentationKind = argumentationKind;
	}


}

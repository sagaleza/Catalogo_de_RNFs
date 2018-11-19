package models.ontology.ndr;

import models.ontology.OWLOntologyClass;


/**
 * Represents an Interdepedency from the NDR Ontology
 * @author rveleda
 *
 */
public abstract class Interdependency extends OWLOntologyClass {
	
	private Contribution contributionKind;

	public Contribution getContributionKind() {
		return contributionKind;
	}

	public void setContributionKind(Contribution contributionKind) {
		this.contributionKind = contributionKind;
	}
	
	@Override
	public void setName(String name) {
		super.setName(name + "_Interdependency");
	}

}

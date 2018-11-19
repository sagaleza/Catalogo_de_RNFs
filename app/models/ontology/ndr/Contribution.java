package models.ontology.ndr;

import models.ontology.OWLOntologyClass;

/**
 * Represents a Contribution class from the NDR Ontology
 * @author rveleda
 *
 */
public class Contribution extends OWLOntologyClass {

	public Contribution(String name) {
		this.setName(name);
	}

	private enum ContributionValue {
		BREAK,
		HURT,
		UNKNOWN,
		HELP,
		MAKE,
		SOMEPLUS,
		SOMEMINUS,
		AND,
		OR,
		EQUAL;
		
		@Override
		public String toString() {
			switch (this) {
				case BREAK:
					return "Break";
				case HURT: 
					return "Hurt";//
				case UNKNOWN:
					return "Unknown";
				case HELP:
					return "Help";//
				case MAKE:
					return "Make";//
				case SOMEPLUS:
					return "Some+";
				case SOMEMINUS:
					return "Some-";
				case AND:
					return "And";
				case OR:
					return "Or";//
				case EQUAL:
					return "Equal";
				default:
					return "";
			}
		};
	};
	
	@Override
	public void setName(String name) {
		if ("help".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.HELP.toString());
		} else if ("hurt".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.HURT.toString());
		} else if ("make".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.MAKE.toString());
		} else if ("or".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.OR.toString());
		} else if ("unknown".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.UNKNOWN.toString());
		} else if ("some+".equalsIgnoreCase(name) || "SomePlus".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.SOMEPLUS.toString());
		} else if ("some-".equalsIgnoreCase(name) || "SomeMinus".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.SOMEMINUS.toString());
		} else if ("and".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.AND.toString());
		} else if ("break".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.BREAK.toString());
		} else if ("equal".equalsIgnoreCase(name)) {
			super.setName(ContributionValue.EQUAL.toString());
		}
	}
	
}

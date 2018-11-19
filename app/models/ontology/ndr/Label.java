package models.ontology.ndr;

import models.ontology.OWLOntologyClass;

/**
 * Represents the Label class from the NDR Ontology
 * @author rveleda
 *
 */
public class Label extends OWLOntologyClass {

	public Label(String name) {
		this.setName(name);
	}

	private enum LabelValue {
		DENIED,
		WEAKLY_DENIED,
		UNDECIDED,
		WEAKLY_SATISFIED,
		SATISFICED,
		CONFLICT;
		
		@Override
		public String toString() {
			switch (this) {
				case DENIED:
					return "Denied";
				case WEAKLY_DENIED: 
					return "Weakly Denied";
				case UNDECIDED:
					return "Undecided";
				case WEAKLY_SATISFIED:
					return "Weakly satisfied";
				case SATISFICED:
					return "Satisficed";
				case CONFLICT:
					return "Conflict";
				default:
					return "";
			}
		};
	};
	
	@Override
	public void setName(String name) {
		if ("PartiallySatisfied".equalsIgnoreCase(name)) {
			super.setName(LabelValue.WEAKLY_SATISFIED.toString());
		} else if ("Satisfied".equalsIgnoreCase(name)) {
			super.setName(LabelValue.SATISFICED.toString());
		} else if ("Conflict".equalsIgnoreCase(name)) {
			super.setName(LabelValue.CONFLICT.toString());
		} else if ("Denied".equalsIgnoreCase(name)) {
			super.setName(LabelValue.DENIED.toString());
		} else if ("Unknown".equalsIgnoreCase(name)) {
			super.setName(LabelValue.UNDECIDED.toString());
		} else if ("PartiallyDenied".equalsIgnoreCase(name)) {
			super.setName(LabelValue.WEAKLY_DENIED.toString());
		}
	}
	
	
	
}

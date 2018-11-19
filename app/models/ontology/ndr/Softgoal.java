package models.ontology.ndr;

import models.ontology.OWLOntologyClass;

/**
 * Represents a Softgoal from the NDR Ontology
 * @author rveleda
 *
 */
public abstract class Softgoal extends OWLOntologyClass {
	
	private NFRType type;
	
	private String topic;
	
	private boolean priority;
	
	private Label label;

	public NFRType getType() {
		return type;
	}

	public void setType(NFRType type) {
		this.type = type;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
}

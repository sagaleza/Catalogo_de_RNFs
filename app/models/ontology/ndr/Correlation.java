package models.ontology.ndr;

/**
 * Represents a Correlation from the NDR Ontology
 * @author rveleda
 *
 */
public class Correlation extends ImplicitInterdependency {
	
	private Softgoal correlationHead;
	
	private Softgoal correlationTail;

	public Softgoal getCorrelationHead() {
		return correlationHead;
	}

	public void setCorrelationHead(Softgoal correlationHead) {
		this.correlationHead = correlationHead;
	}

	public Softgoal getCorrelationTail() {
		return correlationTail;
	}

	public void setCorrelationTail(Softgoal correlationTail) {
		this.correlationTail = correlationTail;
	}
	
	@Override
	public void setName(String name) {
		super.setName("Correlation_" + name);
	}

}

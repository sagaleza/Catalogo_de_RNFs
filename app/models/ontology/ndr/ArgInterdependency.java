package models.ontology.ndr;

/**
 * Represents ArgInterdependency class from the NDR Ontology
 * @author rveleda
 *
 */
public class ArgInterdependency extends Argumentation {
	
	private Interdependency argRefinementHead;
	
	private Claim argRefinementTail;
	
	public ArgInterdependency(String name) {
		super.setName("ArgInterdependency_" + name);
	}

	public Interdependency getArgRefinementHead() {
		return argRefinementHead;
	}

	public void setArgRefinementHead(Interdependency argRefinementHead) {
		this.argRefinementHead = argRefinementHead;
	}

	public Claim getArgRefinementTail() {
		return argRefinementTail;
	}

	public void setArgRefinementTail(Claim argRefinementTail) {
		this.argRefinementTail = argRefinementTail;
	}
	
	@Override
	public void setName(String name) {
		super.setName("ArgInterdependency_" + name);
	}

}

package models.ontology.ndr;


/**
 * Represents a NFRDecomposition from the NDR Ontology
 * @author rveleda
 *
 */
public class NFRDecomposition extends ExplicitInterdependency {
	
	private NFRSoftgoal nfrDecTail;
	
	private NFRSoftgoal nfrDecHead;

	public NFRSoftgoal getNfrDecTail() {
		return nfrDecTail;
	}

	public void setNfrDecTail(NFRSoftgoal nfrDecTail) {
		this.nfrDecTail = nfrDecTail;
	}

	public NFRSoftgoal getNfrDecHead() {
		return nfrDecHead;
	}

	public void setNfrDecHead(NFRSoftgoal nfrDecHead) {
		this.nfrDecHead = nfrDecHead;
	}
	
	@Override
	public void setName(String name) {
		super.setName("NFRDecomposition_" + name);
	}
}

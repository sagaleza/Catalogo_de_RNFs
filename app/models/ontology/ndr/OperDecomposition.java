package models.ontology.ndr;

/**
 * Represents an OperDecomposition from the NDR Ontology
 * @author rveleda
 *
 */
public class OperDecomposition extends Decomposition {
	
	private OperSoftgoal operDecHead;
	
	private OperSoftgoal operDecTail;

	public OperSoftgoal getOperDecHead() {
		return operDecHead;
	}

	public void setOperDecHead(OperSoftgoal operDecHead) {
		this.operDecHead = operDecHead;
	}

	public OperSoftgoal getOperDecTail() {
		return operDecTail;
	}

	public void setOperDecTail(OperSoftgoal operDecTail) {
		this.operDecTail = operDecTail;
	}
	
	@Override
	public void setName(String name) {
		super.setName("OperDecomposition_" + name);
	}
}

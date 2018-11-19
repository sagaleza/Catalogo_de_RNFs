package models.ontology.ndr;

/**
 * Represents an Operationalization from the NDR Ontology
 * @author rveleda
 *
 */
public class Operationalization extends ExplicitInterdependency {
	
	private NFRSoftgoal operationalizationHead;
	
	private OperSoftgoal operationalizationTail;

	public NFRSoftgoal getOperationalizationHead() {
		return operationalizationHead;
	}

	public void setOperationalizationHead(NFRSoftgoal operationalizationHead) {
		this.operationalizationHead = operationalizationHead;
	}

	public OperSoftgoal getOperationalizationTail() {
		return operationalizationTail;
	}

	public void setOperationalizationTail(OperSoftgoal operationalizationTail) {
		this.operationalizationTail = operationalizationTail;
	}
	
	@Override
	public void setName(String name) {
		super.setName("Operationalization_" + name);
	}

}

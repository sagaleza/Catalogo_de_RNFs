package models.ontology.ndr;

/**
 * Represents the ArgNFRSoftgoal class from the NDR Ontology
 * @author rveleda
 *
 */
public class ArgNFRSoftgoal extends Argumentation {
	
	private Softgoal argSoftgoalHead;
	
	private Claim argSoftgoalTail;
	
	public ArgNFRSoftgoal(String name) {
		super.setName("ArgSoftgoal_" + name);
	}

	public Softgoal getArgSoftgoalHead() {
		return argSoftgoalHead;
	}

	public void setArgSoftgoalHead(Softgoal argSoftgoalHead) {
		this.argSoftgoalHead = argSoftgoalHead;
	}

	public Claim getArgSoftgoalTail() {
		return argSoftgoalTail;
	}

	public void setArgSoftgoalTail(Claim argSoftgoalTail) {
		this.argSoftgoalTail = argSoftgoalTail;
	}

}

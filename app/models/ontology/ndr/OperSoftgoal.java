package models.ontology.ndr;

/**
 * Represents an Operationalization Softgoal from the NDR Ontology
 * Note: Operationalization (NFR Framework) = Task (i* Framework)
 * @author rveleda
 *
 */
public class OperSoftgoal extends Softgoal {
	
	// Implement further specilization
	
	public OperSoftgoal() {}
	
	public OperSoftgoal(String name) {
		super.setName(name);
	}

}

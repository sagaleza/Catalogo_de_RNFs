package converters.xmi;

/**
 * Enum class that represents the possible stereotypes in a XMI file.
 * @author rveleda
 *
 */
public enum XMIStereotypes {
	
	NFR_SOFTGOAL("NFRSoftgoal"),
	OPERATIONALIZING_SOFTGOAL("OperationalizingSoftgoal"),
	CLAIM_SOFTGOAL("ClaimSoftgoal"),
	AND("And"),
	OR("Or"),
	EQUAL("Equal"),
	SOME_PLUS("SomePlus"),
	SOME_MINUS("SomeMinus"),
	MAKE("Make"),
	HELP("Help"),
	HURT("Hurt"),
	BREAK("Break");
	
	private final String type;
	
	private XMIStereotypes(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

}

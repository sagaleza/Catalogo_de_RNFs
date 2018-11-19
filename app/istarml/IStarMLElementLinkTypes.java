package istarml;


/**
 *  Represents the possible element links (contribution, decomposition, etc...) 
 *  from the iStarML specification
 * @author rveleda
 *
 */
public enum IStarMLElementLinkTypes {
	
	SOFTGOAL("softgoal"),
	TASK("task"),
	GOAL("goal"), // Not currently supported by the NDR Ontology
	RESOURCE("resource"); // Not currently supported by the NDR Ontology
	
	private final String type;
	
	private IStarMLElementLinkTypes(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

}

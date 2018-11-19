package istarml;

/**
 * Represents the possible element types for an element in the iStarML specification
 * @author rveleda
 *
 */
public enum IStarMLElementTypes {
	
	SOFTGOAL("softgoal"),
	TASK("task"),
	GOAL("goal"), // Not currently supported by the NDR Ontology
	RESOURCE("resource"); // Not currently supported by the NDR Ontology
	
	private final String type;
	
	private IStarMLElementTypes(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
	
}

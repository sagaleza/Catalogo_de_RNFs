package models.graph;

/**
 * Represents a interdependency in a digraph from the Graphviz drawing framework
 * @author rveleda
 *
 */
public class GraphInterdependency {

	private GraphNode headNode;
	
	private GraphNode tailNode;
	
	private boolean correlation;
	
	private String label;

	public GraphInterdependency(GraphNode headNode, GraphNode tailNode, String label, boolean correlation) {
		this.headNode = headNode;
		this.tailNode = tailNode;
		this.label = label;
		this.correlation = correlation;
	}

	public boolean isCorrelation() {
		return correlation;
	}

	public void setCorrelation(boolean correlation) {
		this.correlation = correlation;
	}

	public GraphNode getHeadNode() {
		return headNode;
	}

	public void setHeadNode(GraphNode headNode) {
		this.headNode = headNode;
	}

	public GraphNode getTailNode() {
		return tailNode;
	}

	public void setTailNode(GraphNode tailNode) {
		this.tailNode = tailNode;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		String s = "\"" + this.cleanElementName(this.getHeadNode().getName()) + "\" -> \"" + this.cleanElementName(this.getTailNode().getName()) + "\"";
		
		s += "[";
		s += correlation ? "style=dashed," : "dir=back,";
		s += "fontsize=25,penwidth=1.0,label=\"" + label + "\",";
		s += "color=" + this.defineColor(label);
		s += "]";
		
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		GraphInterdependency wannaBe = (GraphInterdependency) obj;
		
		return this.getHeadNode().getName().equalsIgnoreCase(wannaBe.getHeadNode().getName())
				&& this.getTailNode().getName().equalsIgnoreCase(wannaBe.getTailNode().getName());
	}
	
	@Override
	public int hashCode() {
		return this.getHeadNode().getName().hashCode() + this.getTailNode().getName().hashCode();
	}
	
	private String defineColor(String label) {
		if (label.equalsIgnoreCase("hurt") || label.equalsIgnoreCase("some-") || label.equalsIgnoreCase("break")) {
			return "red";
		} else if (label.equalsIgnoreCase("help") || label.equalsIgnoreCase("some+") || label.equalsIgnoreCase("make")) {
			return "green";
		}
		
		return "black";
	}
	
	private String cleanElementName(String elementName) {
		elementName = elementName.replaceAll("-", "_");
		elementName = elementName.replaceAll(">", "_");
		elementName = elementName.replaceAll("<", "_");
		
		return elementName;
	}
	
}

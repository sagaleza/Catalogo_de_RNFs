package models.graph;



/**
 * Represents a node inside a digrah from the GraphViz drawing framework.
 * @author rveleda
 *
 */
public class GraphNode {
	
	private String name;
	
	private String type;
	
	private boolean highlighted;
	
	public GraphNode(String name, String type) {
		this.name = name.replaceAll("-", " ");
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	@Override
	public String toString() {
		// TODO Fix the absolute path
		// TODO Generate constants for types
		
		if (this.type.equalsIgnoreCase("NFRSoftgoal")) {
			//return "\"" + this.cleanElementName(this.name) + "\" [" + (this.isHighlighted() ? "fontcolor=blue," : "") + "label=<<br/><br/><br/>"+this.name+">,fontsize=30,shape=none,image=\"/Users/rveleda/Development/york/workspace/NDRToolWeb/conf/images/NFRSoftgoal_new2.png\"]";
			return "\"" + this.cleanElementName(this.name) + "\" [" + (this.isHighlighted() ? "fontcolor=blue," : "") + "label=<<br/><br/><br/>"+this.name+">,fontsize=20,shape=none,image=\"/home/ubuntu/app/ndrtoolweb-1.0-SNAPSHOT/conf/images/NFRSoftgoal_new.png\"]";
		} else if (this.type.equalsIgnoreCase("OperSoftgoal")) {
			//return "\"" + this.cleanElementName(this.name) + "\" [" + (this.isHighlighted() ? "fontcolor=blue," : "") + "label=<<br/><br/><br/>"+this.name+">,fontsize=30,shape=none,image=\"/Users/rveleda/Development/york/workspace/NDRToolWeb/conf/images/OperSoftgoal_new2.png\"]";
			return "\"" + this.cleanElementName(this.name) + "\" [" + (this.isHighlighted() ? "fontcolor=blue," : "") + "label=<<br/><br/><br/>"+this.name+">,fontsize=20,shape=none,image=\"/home/ubuntu/app/ndrtoolweb-1.0-SNAPSHOT/conf/images/OperSoftgoal_new.png\"]";
		}
		
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {
		GraphNode wannaBe = (GraphNode) obj;
		return this.getName().equalsIgnoreCase(wannaBe.getName());
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	private String cleanElementName(String elementName) {
		elementName = elementName.replaceAll("-", "_");
		elementName = elementName.replaceAll(">", "_");
		elementName = elementName.replaceAll("<", "_");
		
		return elementName;
	}

}

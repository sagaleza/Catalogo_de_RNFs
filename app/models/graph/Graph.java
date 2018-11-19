package models.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a digraph from the GraphViz drawing framework
 * @author rveleda
 *
 */
public class Graph {

	private Set<GraphInterdependency> relationships;
	
	private Set<GraphNode> nodes;
	
	private String name;
	
	public Graph(String name) {
		this.relationships = new HashSet<GraphInterdependency>();
		this.nodes = new HashSet<GraphNode>();
		this.name = name;
	}

	public Set<GraphInterdependency> getRelationships() {
		return relationships;
	}

	public Set<GraphNode> getNodes() {
		return nodes;
	}

	public String getName() {
		return name;
	}
	
	public boolean isEmpty() {
		return nodes.size() == 0;
	}

}

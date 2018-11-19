package services.graph;

import java.io.File;

import models.graph.Graph;
import models.graph.GraphInterdependency;
import models.graph.GraphNode;

/**
 * Represents the service for drawing the Graph through the GraphViz API
 * @author rveleda
 *
 */
public class GraphServiceImpl implements GraphService {
	
	private static GraphServiceImpl _instance = null;
	
	public static synchronized GraphServiceImpl getInstance() {
    	if (_instance == null)
    		_instance = new GraphServiceImpl();
    	
        return _instance;
    }

	@Override
	public boolean generateDrawing(Graph graph) {
		if (graph.isEmpty())
			return false;
		
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		//gv.addln("A [shape=none, image=\"/Users/rveleda/Development/york/workspace/NDRToolWeb/images/cloud-md.png\"]");
		//gv.addln("A -> B[color=red,penwidth=3.0,label=\"Hurt\"];");
		
		gv.addln("graph [ranksep=\"3.5\", nodesep=\"0.5\"];");
		
		for (GraphNode node : graph.getNodes()) {
			gv.addln(node.toString());
		}
		
		for (GraphInterdependency relationship : graph.getRelationships()) {
			gv.addln(relationship.toString());
		}
		
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());

		//	gv.increaseDpi();
		//gv.increaseDpi();// 106 dpi
		
		gv.decreaseDpi();
		gv.decreaseDpi();
//		gv.decreaseDpi();
//		gv.decreaseDpi();
//		gv.decreaseDpi();
//		gv.decreaseDpi();
//		gv.decreaseDpi();

		String type = "png";
		//      String type = "dot";
		//      String type = "fig";    // open with xfig
		//      String type = "pdf";
		//      String type = "ps";
		//      String type = "svg";    // open with inkscape
		//      String type = "png";
		//      String type = "plain";
		
		String repesentationType= "dot";
		//		String repesentationType= "neato";
		//		String repesentationType= "fdp";
		//		String repesentationType= "sfdp";
		// 		String repesentationType= "twopi";
		// 		String repesentationType= "circo";
		
		File out = new File("/tmp/" + graph.getName() + "." + type);   // Linux
		//      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
		
		return true;
	}
}

package jena;

import java.io.File;

import services.graph.GraphViz;

public class Proba
{
	public static void main(String[] args)
	{
		Proba p = new Proba();
		p.start();
//		p.start2();
	}

	/**
	 * Construct a DOT graph in memory, convert it
	 * to image and store the image in the file system.
	 */
	private void start()
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("A [shape=none, image=\"/Users/rveleda/Development/york/workspace/NDRToolWeb/images/NFRSoftgoal.png\"]");
		gv.addln("B -> A[color=red,penwidth=3.0,label=\"Hurt\"];");
		gv.addln("A -> C[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("A -> D[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("A -> E[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("A -> F[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("A -> G[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("A -> H[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R1[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R2[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R3[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R4[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R5[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("C -> R6[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q1[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q2[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q3[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q4[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q5[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("D -> Q6[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ1[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ2[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ3[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ4[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ5[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("E -> QQ6[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("Q3 -> E[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("B -> Q1[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("QQ1 -> D[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln("Q1 -> QQ1[color=green,penwidth=3.0,label=\"Help\"];");
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());

	//	gv.increaseDpi();
		//gv.increaseDpi();// 106 dpi

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
		
		File out = new File("test."+ type);   // Linux
		//      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}

	/**
	 * Read the DOT source from a file,
	 * convert to image and store the image in the file system.
	 */
	private void start2()
	{
		String dir = "/home/jabba/Dropbox/git.projects/laszlo.own/graphviz-java-api";     // Linux
		String input = dir + "/sample/simple.dot";
		//	   String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";    // Windows

		GraphViz gv = new GraphViz();
		gv.readSource(input);
		System.out.println(gv.getDotSource());

		String type = "gif";
		//    String type = "dot";
		//    String type = "fig";    // open with xfig
		//    String type = "pdf";
		//    String type = "ps";
		//    String type = "svg";    // open with inkscape
		//    String type = "png";
		//      String type = "plain";
		
		
		String repesentationType= "dot";
		//		String repesentationType= "neato";
		//		String repesentationType= "fdp";
		//		String repesentationType= "sfdp";
		// 		String repesentationType= "twopi";
		//		String repesentationType= "circo";
		
		File out = new File("/tmp/simple." + type);   // Linux
		//	   File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple." + type);   // Windows
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}
}

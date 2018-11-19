package jena;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.graphviz.Attribute;
import org.kohsuke.graphviz.Graph;
import org.kohsuke.graphviz.Style;

public class GraphVizTest {
	
	public static void main(String[] args) {
		Graph g = new Graph();
		
		Style s = new Style();
		s.attr(Attribute.COLOR, Color.RED);
		
		try {
			g.nodeWith(s).node("a").to().node("b").generateTo(Arrays.asList("bash", "-c", "dot"),new File("test1.gif"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

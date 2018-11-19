package jena;

import java.io.File;
import java.io.FileInputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class SPARQL {
	
	public static void main(String[] args) {
		
		try {
			FileInputStream is = new FileInputStream(new File("ndr.owl"));
			
			Model model = ModelFactory.createMemModelMaker().createFreshModel();
			
			model.read(is, "http://www.ime.uerj.br/ontologies/2012/5/NDR.owl");
			
			is.close();
			
//			String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//					+ "PREFIX ndr: <http://www.ime.uerj.br/ontologies/2012/5/NDR.owl#>"
//					+ "SELECT DISTINCT ?interType ?softgoal ?softgoalLabel "
//					+ "WHERE {?interType rdf:type ndr:Correlation. ?interType ndr:correlationHead ?softgoal. "
//					+ "?softgoal rdf:type ndr:NFRSoftgoal. ?softgoal ndr:type ndr:Transparency. ?interType ndr:contributionKind ndr:Help.}";
			
//			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//					+ "PREFIX ndr: <http://www.ime.uerj.br/ontologies/2012/5/NDR.owl#>"
//					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//					+ "SELECT DISTINCT ?interlinkId ?softgoal ?contributionKind WHERE {{?interlinkId ndr:nfrDecHead ndr:Transparency. ?interlinkId ndr:nfrDecTail ?softgoal. ?interlinkId ndr:contributionKind ?contributionKind.}";
//			
			
//			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//					+ "PREFIX ndr: <http://www.ime.uerj.br/ontologies/2012/5/NDR.owl#>"
//					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//					+ "SELECT DISTINCT ?interlinkId ?softgoalParent ?softgoalSpring ?contributionKind WHERE "
//					+ "{?interlinkId rdf:type ndr:Correlation. "
//					+ "?interlinkId ndr:correlationHead ?softgoalParent. "
//					+ "?interlinkId ndr:correlationTail ?softgoalSpring. "
//					+ "?interlinkId ndr:contributionKind ?contributionKind.}";
//			
//			Query query = QueryFactory.create(queryString);
//			
//			QueryExecution qe = QueryExecutionFactory.create(query, model);
//			
//			ResultSet rs = qe.execSelect();
//			
//			ResultSetFormatter.out(System.out, rs, query);
//			
//			qe.close();
			
			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "PREFIX ndr: <http://www.ime.uerj.br/ontologies/2012/5/NDR.owl#> "
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "SELECT  ?interlinkId ?label ?head WHERE {?interlinkId rdf:type ndr:Correlation. ?interlinkId ndr:correlationTail ndr:Availability. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:correlationHead ?head}";
			
			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.sparqlService("http://10.5.7.7:3030/ndrpure/query", query);
			
			ResultSet rs = qe.execSelect();
			
			while (rs.hasNext()) {
				QuerySolution qs = rs.next();
				
				System.out.println(qs.get("head").asNode());
			}
			
			ResultSetFormatter.out(System.out, rs, query);
			
			qe.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

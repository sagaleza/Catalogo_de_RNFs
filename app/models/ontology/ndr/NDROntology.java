package models.ontology.ndr;

import org.apache.jena.ontology.OntModelSpec;

import models.ontology.OWLOntology;
/**
 * Represents the NDR Ontology presented in the ndr.owl
 * @author rveleda
 *
 */
public class NDROntology extends OWLOntology {

	public static final String NAMESPACE = "http://www.yorku.ca/itec/ontologies/2014/9/NDR.owl#";
	
	// LOCAL
	private static final String URI = "http://localhost:3030/ndrprod/data";
	private static final String QUERY_URI = "http://localhost:3030/ndrprod/query";
	
	public NDROntology() {
		super(OntModelSpec.OWL_MEM);
	}

	@Override
	public String getRemoteModelURI() {
		return URI;
	}

	@Override
	public String getRemoteModelQueryURI() {
		return QUERY_URI;
	}
	
}

package services.ontology;

import models.graph.Graph;
import models.ontology.OWLOntology;
import models.ontology.OWLOntologyClass;

public interface OntologyService {
	
	public void addIndividual(OWLOntologyClass individualClass);
	
	public OWLOntology getOntology();
	
	public void query(String element, Graph graph, String mainCatalog);
	
	public void removeIndividual(String individualName);

}

package jena;

import models.ontology.ndr.NDROntology;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.ModelCom;


public class LoadRemoteData {
	
	public static void main(String[] args) {
		
		String serviceURI = "http://10.5.7.7:3030/ds2/data";
		
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);

		ModelCom targetModel = (ModelCom) accessor.getModel();
		
		OntDocumentManager ontDocumentManagerNDR = new OntDocumentManager();

		OntModelSpec ontModelSpecNDR = new OntModelSpec(OntModelSpec.OWL_MEM);
        ontModelSpecNDR.setDocumentManager(ontDocumentManagerNDR);
        
        OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpecNDR, targetModel);
        ontModel.write( System.out, "RDF/XML-ABBREV");
        
        OntClass ontClaim = ontModel.getOntClass(NDROntology.NAMESPACE + "Claim");
        Individual claimIndividual = ontModel.createIndividual(NDROntology.NAMESPACE + formatURI("TestClaim"), ontClaim);
        claimIndividual.addLabel("TestClaim", "");
        
        accessor.putModel(ontModel);

		System.out.println("done");
	}
	
	public static String formatURI(String uri) {
        String softGoalUri;

        softGoalUri = uri;
        if (softGoalUri.contains(" ") ||
            softGoalUri.contains("[") ||
            softGoalUri.contains("]")) {
            softGoalUri = softGoalUri.replace(' ', '_');
            softGoalUri = softGoalUri.replace('[', '_');
            softGoalUri = softGoalUri.replace(']', '_');
        }

        return softGoalUri;
	}

}

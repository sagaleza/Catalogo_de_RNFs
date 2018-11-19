package models.ontology;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.ModelFactory;

import play.Logger;



/**
 * Represents an OWL Ontology
 * @author rveleda
 *
 */
public abstract class OWLOntology {
	
    private OntDocumentManager ontDocumentManagerNDR;
    private OntModelSpec ontModelSpecNDR;
    private OntModel ontModelNDR;
    private DatasetAccessor accessor;
    
    public OWLOntology(OntModelSpec ontSpec) {
        ontDocumentManagerNDR = new OntDocumentManager();

        ontModelSpecNDR = new OntModelSpec(ontSpec);
        ontModelSpecNDR.setDocumentManager(ontDocumentManagerNDR);
        
//        ontModelNDR = ModelFactory.createOntologyModel(ontModelSpecNDR, null);
//        
//        ontModelNDR.read("file:///"+ontPath);
    }
    
    public abstract String getRemoteModelURI();
    
    public abstract String getRemoteModelQueryURI();
    
    public void loadRemoteModel() {		
		accessor = DatasetAccessorFactory.createHTTP(this.getRemoteModelURI());
		ontModelNDR = ModelFactory.createOntologyModel(ontModelSpecNDR, accessor.getModel());
    }
    
    public void updateRemoteModel() {
    	if (accessor != null) {
    		accessor.putModel(ontModelNDR);
    	} else {
    		Logger.error("Cannot update remote model: DatasetAccessor is null for this Ontology class");
    	}
    }
    
    public void print() {
    	ontModelNDR.write( System.out, "RDF/XML-ABBREV");
    }

	public OntDocumentManager getOntDocumentManagerNDR() {
		return ontDocumentManagerNDR;
	}

	public void setOntDocumentManagerNDR(OntDocumentManager ontDocumentManagerNDR) {
		this.ontDocumentManagerNDR = ontDocumentManagerNDR;
	}

	public OntModelSpec getOntModelSpecNDR() {
		return ontModelSpecNDR;
	}

	public void setOntModelSpecNDR(OntModelSpec ontModelSpecNDR) {
		this.ontModelSpecNDR = ontModelSpecNDR;
	}

	public OntModel getOntModelNDR() {
		return ontModelNDR;
	}

	public void setOntModelNDR(OntModel ontModelNDR) {
		this.ontModelNDR = ontModelNDR;
	}
    
    

}

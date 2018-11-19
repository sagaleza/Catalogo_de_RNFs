package services.ontology.ndr;

import java.util.ArrayList;
import java.util.List;

import models.graph.Graph;
import models.graph.GraphInterdependency;
import models.graph.GraphNode;
import models.ontology.OWLOntologyClass;
import models.ontology.ndr.ArgInterdependency;
import models.ontology.ndr.ArgNFRSoftgoal;
import models.ontology.ndr.Claim;
import models.ontology.ndr.Correlation;
import models.ontology.ndr.NDROntology;
import models.ontology.ndr.NFRDecomposition;
import models.ontology.ndr.NFRSoftgoal;
import models.ontology.ndr.OperDecomposition;
import models.ontology.ndr.OperSoftgoal;
import models.ontology.ndr.Operationalization;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.util.iterator.ExtendedIterator;

import services.ontology.OntologyService;
import services.system.NFRCatalogServiceImpl;

/**
 * Represents the NDR Ontology service.
 * It contains methods to perform modifications and retrieve information from the ontology.
 * @author rveleda
 *
 */
public class NDROntologyServiceImpl implements OntologyService {
	
	//TODO FIX THIS
    private int seqCorrelation;
    
    private static NDROntologyServiceImpl _instance = null;
    
    private NDROntology ontology;
    
    public NDROntologyServiceImpl() {
    	this.ontology = new NDROntology();
    	
    	// Loading it at the very beginning
    	this.ontology.loadRemoteModel();
    	
    	this.ontology.print();
    }

    public static synchronized NDROntologyServiceImpl getInstance() {
    	if (_instance == null)
    		_instance = new NDROntologyServiceImpl();
    	
        return _instance;
    }

    @Override
    public NDROntology getOntology() {
		return ontology;
	}

	@Override
	public void addIndividual(OWLOntologyClass individualClass) {
		if (individualClass instanceof NFRSoftgoal) {		
			this.addNFRSoftgoalIndividual((NFRSoftgoal) individualClass);
		} else if (individualClass instanceof OperSoftgoal) {
			this.addOperSoftgoalIndividual((OperSoftgoal) individualClass);
		} else if (individualClass instanceof Claim) {
			this.addClaimIndividual((Claim) individualClass);
		} else if (individualClass instanceof NFRDecomposition) {
			this.addNFRDecomposition((NFRDecomposition) individualClass);
		} else if (individualClass instanceof Operationalization) {
			this.addOperationalization((Operationalization) individualClass);
		} else if (individualClass instanceof OperDecomposition) {
			this.addOperDecomposition((OperDecomposition) individualClass);
		} else if (individualClass instanceof ArgNFRSoftgoal) {
			this.addArgNFRSoftgoal((ArgNFRSoftgoal) individualClass);
		} else if (individualClass instanceof ArgInterdependency) {
			this.addArgInterdependency((ArgInterdependency) individualClass);
		} else if (individualClass instanceof Correlation) {
			this.addCorrelation((Correlation) individualClass);
		}
	}
	
	private void addCorrelation(Correlation correlation) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + correlation.getName());

		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("Correlation")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + correlation.getContributionKind().getName()));
		} else {
	        OntClass correlationClass = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "Correlation");
	        Individual indCorrelation = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + correlation.getName(), correlationClass);

	        indCorrelation.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "correlationHead"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + correlation.getCorrelationHead().getName()));
	        indCorrelation.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "correlationTail"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + correlation.getCorrelationTail().getName()));
	        indCorrelation.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + correlation.getContributionKind().getName()));
		}
	}
	
	private void addArgInterdependency(ArgInterdependency argInter) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + argInter.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("ArgInterdependency")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argumentationKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argInter.getArgumentationKind().getName()));
		} else {
	        OntClass operDecomposition = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "ArgInterdependency");
	        Individual indNfrDecomposition = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + argInter.getName(), operDecomposition);

	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argRefinementHead"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argInter.getArgRefinementHead().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argRefinementTail"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argInter.getArgRefinementTail().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argumentationKind"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argInter.getArgumentationKind().getName()));
		}
	}
	
	private void addArgNFRSoftgoal(ArgNFRSoftgoal argSoftgoal) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + argSoftgoal.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("ArgSoftgoal")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argumentationKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argSoftgoal.getArgumentationKind().getName()));
		} else {
	        OntClass operDecomposition = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "ArgSoftgoal");
	        Individual indNfrDecomposition = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + argSoftgoal.getName(), operDecomposition);

	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argSoftgoalHead"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argSoftgoal.getArgSoftgoalHead().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argSoftgoalTail"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argSoftgoal.getArgSoftgoalTail().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "argumentationKind"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + argSoftgoal.getArgumentationKind().getName()));
		}
	}
	
	private void addOperDecomposition(OperDecomposition operDec) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + operDec.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("OperDecomposition")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operDec.getContributionKind().getName()));
		} else {
	        OntClass operDecomposition = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "OperDecomposition");
	        Individual indNfrDecomposition = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + operDec.getName(), operDecomposition);

	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "operDecHead"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operDec.getOperDecHead().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "operDecTail"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operDec.getOperDecTail().getName()));
	        indNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operDec.getContributionKind().getName()));
		}
	}
	
	private void addOperationalization(Operationalization oper) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + oper.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("Operationalization")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + oper.getContributionKind().getName()));
		} else {
	        OntClass operationalization = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "Operationalization");
	        Individual IndOperationalization = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + oper.getName(), operationalization);

	        IndOperationalization.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "operationalizationHead"), 
	        												ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + oper.getOperationalizationHead().getName()));
	        IndOperationalization.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "operationalizationTail"), 
	        												ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + oper.getOperationalizationTail().getName()));
	        IndOperationalization.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
	        												ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + oper.getContributionKind().getName()));
		}
	}
	
	private void addNFRDecomposition(NFRDecomposition nfrDec) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + nfrDec.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("NFRDecomposition")) {
			searchInd.setPropertyValue(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
					ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + nfrDec.getContributionKind().getName()));
		} else {
	        OntClass nfrDecomposition = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "NFRDecomposition");
	        Individual IndNfrDecomposition = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + nfrDec.getName(), nfrDecomposition);

	        IndNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "nfrDecHead"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + nfrDec.getNfrDecHead().getName()));
	        
	        IndNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "nfrDecTail"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + nfrDec.getNfrDecTail().getName()));
	        
	        IndNfrDecomposition.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "contributionKind"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + nfrDec.getContributionKind().getName()));
		}
	}
	
	private void addOperSoftgoalIndividual(OperSoftgoal operSoftgoal) {
		// Verify if this OperSoftgoal exists as NFRSoftgoal
		// If it exists, it should not be downgraded to OperSoftgoal. Therefore, the creation of this element should be ignored.
		
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + operSoftgoal.getName());
		
		if (searchInd == null) {
	        OntClass nfrType = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "NFR_Type");
	        Individual indNfrType = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + operSoftgoal.getType().getName(), nfrType);
	        indNfrType.addLabel(operSoftgoal.getType().getName(), "");
			
	        OntClass ontSoftgoal = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "OperSoftgoal");
	        Individual operSoftgoalIndividual = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + operSoftgoal.getName(), ontSoftgoal);
	        operSoftgoalIndividual.addLabel(operSoftgoal.getName(), "");
	        
	        operSoftgoalIndividual.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "type"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operSoftgoal.getType().getName()));
	        
	        operSoftgoalIndividual.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "topic"), operSoftgoal.getTopic());
	        
	        if (operSoftgoal.getLabel() != null) {
	        	operSoftgoalIndividual.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "label"), 
	            		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + operSoftgoal.getLabel().getName()));
	        }
		}
	}

	private void addNFRSoftgoalIndividual(NFRSoftgoal softgoal) {
		// Verify if this Softgoal exists as OperSoftgoal
		// If it exists, Softgoal should have preference over OperSoftgoal
		
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + softgoal.getName());
		
		if (searchInd != null && searchInd.getOntClass().getLocalName().equalsIgnoreCase("OperSoftgoal")) {
			searchInd.setOntClass(ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "NFRSoftgoal"));
		} else {
	        OntClass nfrType = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "NFR_Type");
	        Individual indNfrType = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + softgoal.getType().getName(), nfrType);
	        indNfrType.addLabel(softgoal.getType().getName(), "");

	        OntClass ontSoftgoal = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "NFRSoftgoal");
	        Individual nfrSoftgoal = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + softgoal.getName(), ontSoftgoal);
	        nfrSoftgoal.addLabel(softgoal.getName(), "");

	        nfrSoftgoal.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "type"), 
	        		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + softgoal.getType().getName()));

	        nfrSoftgoal.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "topic"), softgoal.getTopic());
	        
	        if (softgoal.getLabel() != null) {
	            nfrSoftgoal.addProperty(ontology.getOntModelNDR().getProperty(NDROntology.NAMESPACE + "label"), 
	            		ontology.getOntModelNDR().createResource(NDROntology.NAMESPACE + softgoal.getLabel().getName()));
	        }
		}
	}
	
	private void addClaimIndividual(Claim claim) {
        OntClass ontClaim = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + "Claim");
        Individual claimIndividual = ontology.getOntModelNDR().createIndividual(NDROntology.NAMESPACE + claim.getName(), ontClaim);
        claimIndividual.addLabel(claim.getName(), "");
	}
	
	private void queryOperDecompositions(String targetElement, Graph graph, String mainCatalog) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ndr: <"+NDROntology.NAMESPACE+"> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?interlinkId ?label ?tail ?headtype ?tailtype "
				+ "WHERE {?interlinkId rdf:type ndr:%s. ?interlinkId ndr:%s ndr:%s. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:%s ?tail. "
				+ "?tail rdf:type ?tailtype. ndr:%s rdf:type ?headtype}";
		
		queryString = String.format(queryString, "OperDecomposition", "operDecHead", targetElement, "operDecTail", targetElement);
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.getOntology().getRemoteModelQueryURI(), QueryFactory.create(queryString));

		ResultSet rs = qe.execSelect();
		
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			
			String tailElement = qs.get("tail").asNode().getLocalName();
			String contributionKind = qs.get("label").asNode().getURI().contains("+") 
					|| qs.get("label").asNode().getURI().contains("-")
					? qs.get("label").asNode().getURI().substring(qs.get("label").asNode().getURI().indexOf("#") + 1)
					: qs.get("label").asNode().getLocalName();
					
			String headType = qs.get("headtype").asNode().getLocalName();
			String tailType = qs.get("tailtype").asNode().getLocalName();
			
            System.out.println("INTERLINK: " + qs.get("interlinkId").asNode().getLocalName());
            System.out.println("LABEL: " + contributionKind);
            System.out.println("TAIL: " + tailElement);
            System.out.println("==============================================");
            
            GraphNode headNode = new GraphNode(targetElement,headType);
            GraphNode tailNode = new GraphNode(tailElement, tailType);
            
            graph.getNodes().add(headNode);
            graph.getNodes().add(tailNode);
            graph.getRelationships().add(new GraphInterdependency(headNode, tailNode, contributionKind, false));
 
            // TODO Handle Argumentation
    		this.queryCorrelations(tailElement, graph);
    		this.queryInverseCorrelations(tailElement, graph, mainCatalog);
    		this.queryOperDecompositions(tailElement, graph, mainCatalog);
		}
		
		qe.close();
	}

	private void queryOperationalizations(String targetElement, Graph graph, String mainCatalog) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ndr: <"+NDROntology.NAMESPACE+"> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?interlinkId ?label ?tail ?headtype ?tailtype "
				+ "WHERE {?interlinkId rdf:type ndr:%s. ?interlinkId ndr:%s ndr:%s. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:%s ?tail. "
				+ "?tail rdf:type ?tailtype. ndr:%s rdf:type ?headtype}";
		
		queryString = String.format(queryString, "Operationalization", "operationalizationHead", targetElement, "operationalizationTail", targetElement);
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.getOntology().getRemoteModelQueryURI(), QueryFactory.create(queryString));

		ResultSet rs = qe.execSelect();
		
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			
			String tailElement = qs.get("tail").asNode().getLocalName();
			String contributionKind = qs.get("label").asNode().getURI().contains("+") 
											|| qs.get("label").asNode().getURI().contains("-")
											? qs.get("label").asNode().getURI().substring(qs.get("label").asNode().getURI().indexOf("#") + 1)
											: qs.get("label").asNode().getLocalName();
			
			String headType = qs.get("headtype").asNode().getLocalName();
			String tailType = qs.get("tailtype").asNode().getLocalName();
			
            System.out.println("INTERLINK: " + qs.get("interlinkId").asNode().getLocalName());
            System.out.println("LABEL: " + contributionKind);
            System.out.println("TAIL: " + tailElement);
            System.out.println("==============================================");
            
            GraphNode headNode = new GraphNode(targetElement, headType);
            GraphNode tailNode = new GraphNode(tailElement, tailType);
            
            graph.getNodes().add(headNode);
            graph.getNodes().add(tailNode);
            graph.getRelationships().add(new GraphInterdependency(headNode, tailNode, contributionKind, false));
            
            // TODO Handle Argumentation
    		this.queryCorrelations(tailElement, graph);
    		this.queryInverseCorrelations(tailElement, graph, mainCatalog);
            this.queryOperDecompositions(tailElement, graph, mainCatalog);
		}
		
		qe.close();
	}
	
	private void queryInverseCorrelations(String targetElement, Graph graph, String mainCatalog) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ndr: <"+NDROntology.NAMESPACE+"> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?interlinkId ?label ?head ?headtype ?tailtype "
				+ "WHERE {?interlinkId rdf:type ndr:%s. ?interlinkId ndr:%s ndr:%s. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:%s ?head. "
				+ "?head rdf:type ?headtype. ndr:%s rdf:type ?tailtype}";
		
		queryString = String.format(queryString, "Correlation", "correlationHead", targetElement, "correlationTail", targetElement);
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.getOntology().getRemoteModelQueryURI(), QueryFactory.create(queryString));

		ResultSet rs = qe.execSelect();
		
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			
			String headElement = qs.get("head").asNode().getLocalName();
			String contributionKind = qs.get("label").asNode().getURI().contains("+") 
					|| qs.get("label").asNode().getURI().contains("-")
					? qs.get("label").asNode().getURI().substring(qs.get("label").asNode().getURI().indexOf("#") + 1)
					: qs.get("label").asNode().getLocalName();
			
			String headType = qs.get("headtype").asNode().getLocalName();
			String tailType = qs.get("tailtype").asNode().getLocalName();
			
			boolean sameCatalog = true;
			
			if (!mainCatalog.equalsIgnoreCase("")) {
				sameCatalog = NFRCatalogServiceImpl.getInstance().isElementInCatalog(headElement, mainCatalog);
			}
			
			if (sameCatalog) {
	            System.out.println("INTERLINK: " + qs.get("interlinkId").asNode().getLocalName());
	            System.out.println("LABEL: " + contributionKind);
	            System.out.println("HEAD: " + headElement);
	            System.out.println("==============================================");
	            
	            GraphNode headNode = new GraphNode(targetElement, tailType);
	            GraphNode tailNode = new GraphNode(headElement, headType);
	            
	            graph.getNodes().add(headNode);
	            graph.getNodes().add(tailNode);
	            
	            boolean found = false;
	            
	            GraphInterdependency tmp = new GraphInterdependency(tailNode, headNode, contributionKind, true);
	            
	            for (GraphInterdependency inter : graph.getRelationships()) {
	            	if (inter.getHeadNode().getName().equalsIgnoreCase(tmp.getTailNode().getName()) 
	            			&& inter.getTailNode().getName().equalsIgnoreCase(tmp.getHeadNode().getName())) {
	            		found = true;
	            		break;
	            	}
	            }
	            
	            if (!found)
	            	graph.getRelationships().add(tmp);
			}
		}
		
		qe.close();
	}
	
	private void queryCorrelations(String targetElement, Graph graph) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ndr: <"+NDROntology.NAMESPACE+"> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?interlinkId ?label ?head ?headtype ?tailtype "
				+ "WHERE {?interlinkId rdf:type ndr:%s. ?interlinkId ndr:%s ndr:%s. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:%s ?head. "
				+ "?head rdf:type ?headtype. ndr:%s rdf:type ?tailtype}";
		
		queryString = String.format(queryString, "Correlation", "correlationTail", targetElement, "correlationHead", targetElement);
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.getOntology().getRemoteModelQueryURI(), QueryFactory.create(queryString));

		ResultSet rs = qe.execSelect();
		
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			
			String headElement = qs.get("head").asNode().getLocalName();
			String contributionKind = qs.get("label").asNode().getURI().contains("+") 
					|| qs.get("label").asNode().getURI().contains("-")
					? qs.get("label").asNode().getURI().substring(qs.get("label").asNode().getURI().indexOf("#") + 1)
					: qs.get("label").asNode().getLocalName();
			
			String headType = qs.get("headtype").asNode().getLocalName();
			String tailType = qs.get("tailtype").asNode().getLocalName();
			
            System.out.println("INTERLINK: " + qs.get("interlinkId").asNode().getLocalName());
            System.out.println("LABEL: " + contributionKind);
            System.out.println("HEAD: " + headElement);
            System.out.println("==============================================");
            
            GraphNode headNode = new GraphNode(targetElement, tailType);
            GraphNode tailNode = new GraphNode(headElement, headType);
            
            graph.getNodes().add(headNode);
            graph.getNodes().add(tailNode);
            graph.getRelationships().add(new GraphInterdependency(headNode, tailNode, contributionKind, true));
            
            this.queryCorrelations(headElement, graph);
		}
		
		qe.close();
	}

	private void queryDecompositions(String parentElement, Graph graph, String mainCatalog) {
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ndr: <"+NDROntology.NAMESPACE+"> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?interlinkId ?label ?tail ?headtype ?tailtype "
				+ "WHERE {?interlinkId rdf:type ndr:%s. ?interlinkId ndr:%s ndr:%s. ?interlinkId ndr:contributionKind ?label. ?interlinkId ndr:%s ?tail. "
				+ "?tail rdf:type ?tailtype. ndr:%s rdf:type ?headtype}";
		
		queryString = String.format(queryString, "NFRDecomposition", "nfrDecHead", parentElement, "nfrDecTail", parentElement);
		
		System.out.println(queryString);
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.getOntology().getRemoteModelQueryURI(), QueryFactory.create(queryString));

		ResultSet rs = qe.execSelect();
		
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			
			String tailElement = qs.get("tail").asNode().getLocalName();
			String contributionKind = qs.get("label").asNode().getURI().contains("+") 
					|| qs.get("label").asNode().getURI().contains("-")
					? qs.get("label").asNode().getURI().substring(qs.get("label").asNode().getURI().indexOf("#") + 1)
					: qs.get("label").asNode().getLocalName();
					
			String headType = qs.get("headtype").asNode().getLocalName();
			String tailType = qs.get("tailtype").asNode().getLocalName();
			
            System.out.println("INTERLINK: " + qs.get("interlinkId").asNode().getLocalName());
            System.out.println("LABEL: " + contributionKind);
            System.out.println("TAIL: " + tailElement);
            System.out.println("==============================================");
            
            GraphNode headNode = new GraphNode(parentElement, headType);
            GraphNode tailNode = new GraphNode(tailElement, tailType);
            
            graph.getNodes().add(headNode);
            graph.getNodes().add(tailNode);
            graph.getRelationships().add(new GraphInterdependency(headNode, tailNode, contributionKind, false));
            
            // TODO Handle Argumentation            
            this.queryCorrelations(tailElement, graph);
            this.queryInverseCorrelations(tailElement, graph, mainCatalog);
            this.queryOperationalizations(tailElement, graph, mainCatalog);
            this.queryDecompositions(tailElement, graph, mainCatalog);
		}

		qe.close();
	}
	
	//FIXME Not sure if this is the best way to handle this search... SPARQL should have something more efficient
	@SuppressWarnings("unchecked")
	private List<String> searchIndividualsFromClasses(String searchTerm, String clazz) {
		OntClass ontClasses = ontology.getOntModelNDR().getOntClass(NDROntology.NAMESPACE + clazz);
		
		ExtendedIterator<Individual> individuals = (ExtendedIterator<Individual>) ontClasses.listInstances();
		
		List<String> resultList = new ArrayList<String>();
		
		while (individuals.hasNext()) {
			Individual ind = individuals.next();

			if (StringUtils.containsIgnoreCase(ind.getLocalName(), searchTerm))
				resultList.add(ind.getLocalName());
			
		}
		
		return resultList;
	}

	public List<String> queryElements(String term) {
		List<String> resultList = this.searchIndividualsFromClasses(term, "NFRSoftgoal");
		
		resultList.addAll(this.searchIndividualsFromClasses(term, "OperSoftgoal"));
		
		return resultList;
	}

	@Override
	public void query(String element, Graph graph, String mainCatalog) {
		
		this.queryDecompositions(element, graph, mainCatalog);
		this.queryInverseCorrelations(element, graph, mainCatalog);
		this.queryCorrelations(element, graph);
		this.queryOperationalizations(element, graph, mainCatalog);
		this.queryOperDecompositions(element, graph, mainCatalog);
		
		// See if the element exists on the ontology, at least...
		//FIXME SPARQL should be used in this search instead of loading the whole remote model
		if (graph.getNodes().size() == 0) {
			Individual ind = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + element);
			
			if (ind != null)
				graph.getNodes().add(new GraphNode(element, ind.getOntClass().getLocalName()));
		}
	}

	@Override
	public void removeIndividual(String individualName) {
		Individual searchInd = ontology.getOntModelNDR().getIndividual(NDROntology.NAMESPACE + individualName);
		
		if (searchInd != null) {
			searchInd.remove();
		}
	}
}

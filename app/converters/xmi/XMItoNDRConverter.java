package converters.xmi;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.ontology.ndr.ArgInterdependency;
import models.ontology.ndr.ArgNFRSoftgoal;
import models.ontology.ndr.Claim;
import models.ontology.ndr.Contribution;
import models.ontology.ndr.Correlation;
import models.ontology.ndr.Interdependency;
import models.ontology.ndr.Label;
import models.ontology.ndr.NFRDecomposition;
import models.ontology.ndr.NFRSoftgoal;
import models.ontology.ndr.NFRType;
import models.ontology.ndr.OperDecomposition;
import models.ontology.ndr.OperSoftgoal;
import models.ontology.ndr.Operationalization;
import models.system.NFRCatalog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import services.ontology.ndr.NDROntologyServiceImpl;
import services.system.NFRCatalogServiceImpl;
import converters.Converter;

/**
 * XMI Converter class
 * It parses the XMI and sends the information towards the target Ontology service
 * @author rveleda
 *
 */
public class XMItoNDRConverter extends Converter<NDROntologyServiceImpl> {
	
	//TODO Add logger...
	
	private Document targetDocument;
	
	private static final String UML_STEREOTYPE_COMPONENT = "UML:Stereotype";
	private static final String UML_CLASS_COMPONENT = "UML:Class";
	private static final String UML_TAGGEDVALUE_COMPONENT = "UML:TaggedValue";
	private static final String UML_DEPENDENCY_COMPONENT = "UML:Dependency";
	private static final String UML_LABEL_TAG = "Label";
	private static final String UML_CONSEQUENCE_TAG = "Consequence";
	
    private static XMItoNDRConverter _instance = null;
    
    private XMItoNDRConverter(Class<NDROntologyServiceImpl> clazz) {
    	super(clazz);
    }

    public static synchronized XMItoNDRConverter getInstance() {
    	if (_instance == null)
    		_instance = new XMItoNDRConverter(NDROntologyServiceImpl.class);
    	
        return _instance;
    }
	
	@Override
	protected void loadFile(File targetFile) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(targetFile);
		
		doc.getDocumentElement().normalize();
		
		this.targetDocument = doc;
	}

	@Override
	protected void parse(NFRCatalog newCat) {
		NodeList stereotypeList = this.targetDocument.getElementsByTagName(UML_STEREOTYPE_COMPONENT);
		
		for (int i=0; i<stereotypeList.getLength(); i++) {
			Element stereotypeElement = (Element) stereotypeList.item(i);
			
			String stereotypeName = stereotypeElement.getAttribute("name");
			
			String[] extendedElements = stereotypeElement.getAttribute("extendedElement").split(" ");
			
			for (String extendedElement : extendedElements) {
				
				if (stereotypeName.equalsIgnoreCase(XMIStereotypes.NFR_SOFTGOAL.toString())) {
					String elementName = this.getElementAttributeName(UML_CLASS_COMPONENT, extendedElement).trim();
					
					NFRSoftgoal softgoal = new NFRSoftgoal(elementName);
					softgoal.setType(new NFRType(softgoal.getName()));
					softgoal.setTopic(""); // TODO When should we set the topic?
					
					String labelName = this.getLabelValue(UML_TAGGEDVALUE_COMPONENT, extendedElement);
					
					if (labelName != null) {
						softgoal.setLabel(new Label(labelName));
					}
					
					newCat.getOriginalElementNames().add(elementName);
					this.getOntologyService().addIndividual(softgoal);
				} else if (stereotypeName.equalsIgnoreCase(XMIStereotypes.OPERATIONALIZING_SOFTGOAL.toString())) {
					String elementName = this.getElementAttributeName(UML_CLASS_COMPONENT, extendedElement).trim();
					
					OperSoftgoal operSoftgoal = new OperSoftgoal(elementName);
					operSoftgoal.setType(new NFRType(operSoftgoal.getName()));
					operSoftgoal.setTopic(""); // TODO When should we set the topic?
					
					String labelName = this.getLabelValue(UML_TAGGEDVALUE_COMPONENT, extendedElement);
					
					if (labelName != null) {
						operSoftgoal.setLabel(new Label(labelName));
					}
					
					newCat.getOriginalElementNames().add(elementName);
					this.getOntologyService().addIndividual(operSoftgoal);
				} else if (stereotypeName.equalsIgnoreCase(XMIStereotypes.CLAIM_SOFTGOAL.toString())) {
					String elementName = this.getElementAttributeName(UML_CLASS_COMPONENT, extendedElement).trim();
					
					newCat.getOriginalElementNames().add(elementName);
					this.getOntologyService().addIndividual(new Claim(elementName));
				} else if (stereotypeName.equalsIgnoreCase(XMIStereotypes.MAKE.toString()) || 
						stereotypeName.equalsIgnoreCase(XMIStereotypes.SOME_PLUS.toString()) || 
						stereotypeName.equalsIgnoreCase(XMIStereotypes.HURT.toString()) || 
						stereotypeName.equalsIgnoreCase(XMIStereotypes.SOME_MINUS.toString()) || 
						stereotypeName.equalsIgnoreCase(XMIStereotypes.HELP.toString()) ||
						stereotypeName.equalsIgnoreCase(XMIStereotypes.BREAK.toString()) ||
						stereotypeName.equalsIgnoreCase(XMIStereotypes.AND.toString()) ||
						stereotypeName.equalsIgnoreCase(XMIStereotypes.OR.toString()) ||
						stereotypeName.equalsIgnoreCase(XMIStereotypes.EQUAL.toString())) {
					
					// supplier == head
					// client == tail
					
					// TODO Reduce the logic in this method
					
					String clientId = this.getElementAttributeClient(UML_DEPENDENCY_COMPONENT, extendedElement).trim();
					String supplierId = this.getElementAttributeSupplier(UML_DEPENDENCY_COMPONENT, extendedElement).trim();
					
					XMIStereotypes headElementType = null;
					XMIStereotypes tailElementType = null;
					
					String nfrSoftgoals = this.getElementAttributeExtendedElement(UML_STEREOTYPE_COMPONENT, 
																XMIStereotypes.NFR_SOFTGOAL);
					
					String operSoftgoals = this.getElementAttributeExtendedElement(UML_STEREOTYPE_COMPONENT, 
																XMIStereotypes.OPERATIONALIZING_SOFTGOAL);
					
					String claimSoftgoals = this.getElementAttributeExtendedElement(UML_STEREOTYPE_COMPONENT, 
																XMIStereotypes.CLAIM_SOFTGOAL);
					
					// CLIENT can be a dependency as well. Handle this for ArgInterdependency
					if (nfrSoftgoals != null && Arrays.asList(nfrSoftgoals.split(" ")).contains(supplierId)) {
						headElementType = XMIStereotypes.NFR_SOFTGOAL;
					} else if (operSoftgoals != null && Arrays.asList(operSoftgoals.split(" ")).contains(supplierId)) {
						headElementType = XMIStereotypes.OPERATIONALIZING_SOFTGOAL;
					} else if (claimSoftgoals != null && Arrays.asList(claimSoftgoals.split(" ")).contains(supplierId)) {
						headElementType = XMIStereotypes.CLAIM_SOFTGOAL;
					}
					
					if (nfrSoftgoals != null && Arrays.asList(nfrSoftgoals.split(" ")).contains(clientId)) {
						tailElementType = XMIStereotypes.NFR_SOFTGOAL;
					} else if (operSoftgoals != null && Arrays.asList(operSoftgoals.split(" ")).contains(clientId)) {
						tailElementType = XMIStereotypes.OPERATIONALIZING_SOFTGOAL;
					} else if (claimSoftgoals != null && Arrays.asList(claimSoftgoals.split(" ")).contains(clientId)) {
						tailElementType = XMIStereotypes.CLAIM_SOFTGOAL;
					}
					
					Contribution contributionKind = new Contribution(stereotypeName);
					
					// FIXME Temporary handling for AND or OR interdependencies
					if (contributionKind.getName().equals(XMIStereotypes.AND.toString()) || contributionKind.getName().equals(XMIStereotypes.OR.toString())) {
						contributionKind.setName(XMIStereotypes.HELP.toString());
					}
					
					// If headElementType is null AND tailElementType is CLAIM_SOFTGOAL, it is an ArgInterdependency
					if (headElementType == null && tailElementType.equals(XMIStereotypes.CLAIM_SOFTGOAL)) {
						String id = extendedElement.substring(extendedElement.indexOf(".") + 1);
						String interdependencyId = supplierId.substring(extendedElement.indexOf(".") + 1);
						
						Interdependency targetInterdependency = new Interdependency() {};
						targetInterdependency.setName(interdependencyId);
						
						ArgInterdependency argInter = new ArgInterdependency(id);
						argInter.setArgRefinementHead(targetInterdependency);
						argInter.setArgRefinementTail(new Claim(this.getElementAttributeName(UML_CLASS_COMPONENT, clientId)));
						argInter.setArgumentationKind(contributionKind);
						
						this.getOntologyService().addIndividual(argInter);
					// if headElementType is not NULL AND tailElementType is CLAIM_SOFTGOAL, it is an ArgNFRSoftgoal
					} else if (tailElementType.equals(XMIStereotypes.CLAIM_SOFTGOAL)) { 
						String id = extendedElement.substring(extendedElement.indexOf(".") + 1);
						
						ArgNFRSoftgoal argSoftgoal = new ArgNFRSoftgoal(id);
						argSoftgoal.setArgSoftgoalHead(new NFRSoftgoal(this.getElementAttributeName(UML_CLASS_COMPONENT, supplierId)));
						argSoftgoal.setArgSoftgoalTail(new Claim(this.getElementAttributeName(UML_CLASS_COMPONENT, clientId)));
						argSoftgoal.setArgumentationKind(contributionKind);
						
						this.getOntologyService().addIndividual(argSoftgoal);
					} else {
						//String id = extendedElement.substring(extendedElement.indexOf(".") + 1);
						
						String supplierName = this.getElementAttributeName(UML_CLASS_COMPONENT, supplierId);
						String clientName = this.getElementAttributeName(UML_CLASS_COMPONENT, clientId);
						
						Interdependency interdependency = this.findInterdependencyType(headElementType, tailElementType);
						
						// Is correlation interdependency or normal interdependency?
						if (isInterdependencyCorrelation(extendedElement)) {
							Correlation correlation = new Correlation();
							correlation.setContributionKind(contributionKind);
							correlation.setName(supplierName + "_" + clientName);
							
							if (interdependency instanceof NFRDecomposition) {
								correlation.setCorrelationHead(new NFRSoftgoal(supplierName));
								correlation.setCorrelationTail(new NFRSoftgoal(clientName));
							} else if (interdependency instanceof Operationalization) {
								correlation.setCorrelationHead(new NFRSoftgoal(supplierName));
								correlation.setCorrelationTail(new OperSoftgoal(clientName));
							} else if (interdependency instanceof OperDecomposition) {
								correlation.setCorrelationHead(new OperSoftgoal(supplierName));
								correlation.setCorrelationTail(new OperSoftgoal(clientName));
							}

							this.getOntologyService().addIndividual(correlation);
						} else {
							if (interdependency instanceof NFRDecomposition) {
								NFRDecomposition nfrDec = (NFRDecomposition) interdependency;
								
								nfrDec.setName(supplierName + "_" + clientName);
								nfrDec.setContributionKind(contributionKind);
								nfrDec.setNfrDecHead(new NFRSoftgoal(supplierName));
								nfrDec.setNfrDecTail(new NFRSoftgoal(clientName));
								
								this.getOntologyService().addIndividual(nfrDec);
							} else if (interdependency instanceof Operationalization) {
								Operationalization operationalization = (Operationalization) interdependency;
								operationalization.setName(supplierName + "_" + clientName);
								operationalization.setContributionKind(contributionKind);
								operationalization.setOperationalizationHead(new NFRSoftgoal(supplierName));
								operationalization.setOperationalizationTail(new OperSoftgoal(clientName));
								
								this.getOntologyService().addIndividual(operationalization);
							} else if (interdependency instanceof OperDecomposition) {
								OperDecomposition operDec = (OperDecomposition) interdependency;
								operDec.setName(supplierName + "_" + clientName);
								operDec.setContributionKind(contributionKind);
								operDec.setOperDecHead(new OperSoftgoal(supplierName));
								operDec.setOperDecTail(new OperSoftgoal(clientName));
								
								this.getOntologyService().addIndividual(operDec);
							}
						}
					}
				}
			}
		}
		// Done with parsing and ontology update
		newCat.setTimestamp(new Date().getTime());
		newCat.setVersion(1);
		newCat.setComment("StarUML XMI");
		this.persistSystemInformation(newCat);
	}
	
	private String getElementAttribute(String componentTagName, String targetAttribute, Map<String, String> conditions) {
		NodeList elementList = this.targetDocument.getElementsByTagName(componentTagName);
		
		for (int i=0; i<elementList.getLength(); i++) {
			Element element = (Element) elementList.item(i);
			
			// Only for AND condition
			boolean match = true;
			for (String key : conditions.keySet()) {
				if (!conditions.get(key).equalsIgnoreCase(element.getAttribute(key))) {
					match = false;
					break;
				}
			}
			
			if (match)
				return element.getAttribute(targetAttribute);
		}
		
		// Unlikely...
		return null;
	}
	
	private String getElementAttributeSupplier(String componentTagName, String targetElementId) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("xmi.id", targetElementId);
		
		return this.getElementAttribute(componentTagName, "supplier", conditions);
	}
	
	private String getElementAttributeClient(String componentTagName, String targetElementId) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("xmi.id", targetElementId);
		
		return this.getElementAttribute(componentTagName, "client", conditions);
	}
	
	private String getElementAttributeExtendedElement(String componentTagName, XMIStereotypes targetElementName) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("name", targetElementName.toString());
		
		return this.getElementAttribute(componentTagName, "extendedElement", conditions);
	}
	
	private String getElementAttributeExtendedElement(String componentTagName, XMIStereotypes... targetElementNames) {
		StringBuffer sb = new StringBuffer();
		
		for (XMIStereotypes targetElementName : targetElementNames) {
			String result = this.getElementAttributeExtendedElement(componentTagName, targetElementName);
			
			if (result != null)
				sb.append(result).append(" ");
			
		}
		
		return sb.toString();
	}
	
	private String getElementAttributeName(String componentTagName, String targetElementId) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("xmi.id", targetElementId);
		
		return this.getElementAttribute(componentTagName, "name", conditions);
	}
	
	private String getLabelValue(String componentTagName, String targetModelElement) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("tag", UML_LABEL_TAG);
		conditions.put("modelElement", targetModelElement);
		
		return this.getElementAttribute(componentTagName, "value", conditions);
	}
	
	private boolean isInterdependencyCorrelation(String targetModelElement) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("tag", UML_CONSEQUENCE_TAG);
		conditions.put("modelElement", targetModelElement);
		
		return "yes".equalsIgnoreCase(this.getElementAttribute(UML_TAGGEDVALUE_COMPONENT, "value", conditions));
	}
	
	/**
	 * It finds the Interdependency type based on the types from the head (supplier) and tail (client) elements
	 * @param headType
	 * @param tailType
	 * @return
	 */
	private Interdependency findInterdependencyType(XMIStereotypes headType, XMIStereotypes tailType) {
		if (headType.equals(XMIStereotypes.NFR_SOFTGOAL) 
				&& tailType.equals(XMIStereotypes.NFR_SOFTGOAL)) {
			
			return new NFRDecomposition();
		} else if (headType.equals(XMIStereotypes.NFR_SOFTGOAL) 
				&& tailType.equals(XMIStereotypes.OPERATIONALIZING_SOFTGOAL)) {
			
			return new Operationalization();
		} else if (headType.equals(XMIStereotypes.OPERATIONALIZING_SOFTGOAL)
				&& tailType.equals(XMIStereotypes.OPERATIONALIZING_SOFTGOAL)) {
			
			return new OperDecomposition();
		}
		
		// TODO Throw exception
		
		return null;
	}

	@Override
	protected void persistSystemInformation(NFRCatalog cat) {
		NFRCatalogServiceImpl.getInstance().insert(cat);
	}

}

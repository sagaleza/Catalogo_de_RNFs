package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import models.graph.Graph;
import models.graph.GraphNode;
import models.system.NFRCatalog;
import models.system.User;

import org.apache.commons.lang3.text.WordUtils;
import org.xml.sax.SAXException;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import services.graph.GraphServiceImpl;
import services.ontology.ndr.NDROntologyServiceImpl;
import services.system.NFRCatalogServiceImpl;
import converters.xmi.XMItoNDRConverter;

public class Application extends Controller {
	
	private static final String WEB_TITLE = "NDR Tool v1.0 - Framework";
	
	public static class Login {
        public String username;
        public String password;
        
        public String validate() {
        	User u = User.authenticate(username, password);
        	
            if (u  == null)
                return "Invalid user or password";

            u.setTimestamp(new Date().getTime());
            
            u.save();
            
            return null;
        }
	}

    public static Result index() {
    	if (session("uuid") != null && session("username") != null) {
    		return redirect(routes.Application.main());
    	}
    	
    	response().setHeader("Cache-Control", "no-cache");
    	
    	return ok(views.html.index.render(WEB_TITLE, Form.form(Login.class)));
    }
    
    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        
        if (loginForm.hasErrors()) {
        	return badRequest(views.html.index.render(WEB_TITLE, loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            session("uuid", UUID.randomUUID().toString());

            return redirect(routes.Application.main());
        }
    }
    
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }
    
    @Security.Authenticated(Secured.class)
    public static Result main() {
    	List<NFRCatalog> catalogList = NFRCatalogServiceImpl.getInstance().loadAll();

    	//return redirect("/assets/html/graph/index.html#ndr");
    	return ok(views.html.search.render(WEB_TITLE, "", false, catalogList, null));
    }

    @Security.Authenticated(Secured.class)
    public static Result getGeneratedImage() {
    	return ok(new File("/tmp/" + session("uuid") + ".png"));
    }
    
    @Security.Authenticated(Secured.class)
    public static Result search() {
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	
    	String searchText = dynamicForm.get("searchText");
    	searchText = WordUtils.capitalize(searchText);
    	searchText = searchText.replaceAll(" ", "-");
    	
    	System.out.println("SEARCHING: " + searchText);
    
    	//return query(searchText);
    	return queryElements(searchText);
    }

    // TODO FIX Abstraction
    @Security.Authenticated(Secured.class)
    public static Result highlightResult(String nfr, String markedNfr) {
    	List<NFRCatalog> catalogList = NFRCatalogServiceImpl.getInstance().loadAll();
    	
    	Graph graph = new Graph(session("uuid"));
    	
    	String mainCatalog = "";
    	
    	for (NFRCatalog catalog : catalogList) {
    		if (catalog.getName().equalsIgnoreCase(nfr)) {
    			mainCatalog = catalog.getName();
    			break;
    		}
    	}
    	
    	NDROntologyServiceImpl.getInstance().query(nfr, graph, mainCatalog);
    	
    	for (GraphNode node : graph.getNodes()) {
    		if (markedNfr.equalsIgnoreCase(node.getName())) {
    			node.setHighlighted(true);
    		}
    	}
    	
		// Draw the results...
		boolean graphExists = GraphServiceImpl.getInstance().generateDrawing(graph);
		
		List<NFRCatalog> catalogOccurrences = NFRCatalogServiceImpl.getInstance().getCatalogOccurrences(nfr);
		
		List<NFRCatalog> finalList = new ArrayList<NFRCatalog>();
		
		for (NFRCatalog catalog : catalogOccurrences) {
			if (!catalog.getName().equalsIgnoreCase(nfr)) {
				finalList.add(catalog);
			}
		}
		
		catalogOccurrences = finalList;
		
		response().setHeader("Cache-Control", "no-cache");
    	
		return ok(views.html.search.render(WEB_TITLE, nfr, graphExists, catalogList, catalogOccurrences.size() == 0 ? null : catalogOccurrences));	
    }
    
    @Security.Authenticated(Secured.class)
    public static Result queryElements(String term) {
    	List<NFRCatalog> catalogList = NFRCatalogServiceImpl.getInstance().loadAll();
    	
    	List<String> resultList = NDROntologyServiceImpl.getInstance().queryElements(term);
    	
    	response().setHeader("Cache-Control", "no-cache");
    	
    	return ok(views.html.searchterm.render(WEB_TITLE, term, resultList.size() > 0, resultList.size() == 0 ? null : resultList, catalogList));	
    }

    // TODO FIX Abstraction
    @Security.Authenticated(Secured.class)
    public static Result query(String nfr) {
    	List<NFRCatalog> catalogList = NFRCatalogServiceImpl.getInstance().loadAll();
    	
    	nfr = nfr.replace(" ", "-");
    	
    	Graph graph = new Graph(session("uuid"));
    	
    	String mainCatalog = "";
    	
    	for (NFRCatalog catalog : catalogList) {
    		if (catalog.getName().equalsIgnoreCase(nfr)) {
    			mainCatalog = catalog.getName();
    			break;
    		}
    	}
    	
    	NDROntologyServiceImpl.getInstance().query(nfr, graph, mainCatalog);
    	
		// Draw the results...
		boolean graphExists = GraphServiceImpl.getInstance().generateDrawing(graph);
		
		List<NFRCatalog> catalogOccurrences = NFRCatalogServiceImpl.getInstance().getCatalogOccurrences(nfr);
		
		List<NFRCatalog> finalList = new ArrayList<NFRCatalog>();
		
		for (NFRCatalog catalog : catalogOccurrences) {
			if (!catalog.getName().equalsIgnoreCase(nfr)) {
				finalList.add(catalog);
			}
		}
		
		catalogOccurrences = finalList;
		
		response().setHeader("Cache-Control", "no-cache");
		
		return ok(views.html.search.render(WEB_TITLE, nfr, graphExists, catalogList, catalogOccurrences.size() == 0 ? null : catalogOccurrences));	
    }

    public static Result xmi(String file) {
    	try {
    		NFRCatalog cat = new NFRCatalog();
    		cat.setName(file.substring(0, 1).toUpperCase() + file.substring(1));
    		
    		file = "/Users/rveleda/Documents/York University/Master/workshop/experiment/sigs/" + file;

			XMItoNDRConverter.getInstance().convert(new File(file + ".xml"), cat);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
    	
    	return ok();
    }
    
    @Security.Authenticated(Secured.class)
    public static Result remove(String individualName) {
    	NDROntologyServiceImpl.getInstance().removeIndividual(individualName);
    	return ok("Individual removed");
    }
    
    @Security.Authenticated(Secured.class)
    public static Result admin() {
    	response().setHeader("Cache-Control", "no-cache");
    	
    	return ok(views.html.admin.render(WEB_TITLE));
    }
    
    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();

        FilePart picture = body.getFile("picture");
       
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
           
            File file = picture.getFile();
            
            fileName = fileName.substring(0, fileName.indexOf("."));
            
            System.out.println(fileName);
            System.out.println(contentType);
            
        	try {
        		NFRCatalog cat = new NFRCatalog();
        		cat.setName(fileName.substring(0, 1).toUpperCase() + fileName.substring(1));

    			XMItoNDRConverter.getInstance().convert(file, cat);
    		} catch (ParserConfigurationException | IOException | SAXException e) {
    			e.printStackTrace();
    		}
            
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }
    
//    public static Result istarml() {
//    	
//    	ccistarmlFile file = new ccistarmlFile();
//		file.loadFile("Application.istarml");
//		
//		file.xmlParser();
//		
//		ERelementList elementlist = new ERelementList(file.xmlStructure());
//		
//		elementlist.display();
//		
//		Iterator<ERelement> it = elementlist.list().iterator();
//		
//		ERelement tailElement = null;
//		
//		while (it.hasNext()) {
//			ERelement element = (ERelement) it.next();
//			
//			if (element.name.equals("ielement")) {
//				Softgoal softgoalElement = null;
//				
//				tailElement = element;
//				
//				if (element.attribute.get("type").equals(IStarMLElementTypes.SOFTGOAL.toString())) {
//					softgoalElement = new NFRSoftgoal();
//					softgoalElement.setType(new NFRType(String.valueOf(element.attribute.get("name"))));
//				} else if (element.attribute.get("type").equals(IStarMLElementTypes.TASK.toString())) {
//					softgoalElement = new OperSoftgoal();
//				}
//				
//				if (softgoalElement != null) {
//					softgoalElement.setName(String.valueOf(element.attribute.get("name")));
//					softgoalElement.setTopic("");
//					
//					if (element.attribute.get("QualitativeReasoningCombinedLabel") != null) {
//						softgoalElement.setLabel(new Label(String.valueOf(element.attribute.get("QualitativeReasoningCombinedLabel"))));
//					}
//					
//					NDROntologyServiceImpl.getInstance().addIndividual(softgoalElement);
//				} else {
//					Logger.info("Skipping element: " + element.ID);
//				}
//			}
//			
//			//{type=contribution, value=help, iref=_28V1C8eKEeCGwI-I2xY10A}
//			if (element.name.equals("ielementLink")) {
//				ERelement headElement = elementlist.getElementFromRef(String.valueOf(element.attribute.get("iref")));
//				
//				Interdependency interdependency = IStarMLtoNDRConverter.findInterdependencyType(
//						String.valueOf(headElement.attribute.get("type")), String.valueOf(tailElement.attribute.get("type")));
//				
//				Contribution contributionKind = new Contribution(String.valueOf(element.attribute.get("value")));
//				
//				String headName = String.valueOf(headElement.attribute.get("name"));
//				String tailName = String.valueOf(tailElement.attribute.get("name"));
//				
//				if (interdependency instanceof NFRDecomposition) {
//					Logger.info("Creating a NFR Decomposition...");
//					
//					NFRDecomposition nfrDec = (NFRDecomposition) interdependency;
//					nfrDec.setContributionKind(contributionKind);
//					nfrDec.setNfrDecHead(new NFRSoftgoal(headName));
//					nfrDec.setNfrDecTail(new NFRSoftgoal(tailName));
//					
//					NDROntologyServiceImpl.getInstance().addIndividual(nfrDec);
//				} else if (interdependency instanceof Operationalization) {
//					Logger.info("Creating a Operationalization...");
//					
//					Operationalization operationalization = new Operationalization();
//					operationalization.setContributionKind(contributionKind);
//					operationalization.setOperationalizationHead(new NFRSoftgoal(headName));
//					operationalization.setOperationalizationTail(new OperSoftgoal(tailName));
//					
//					NDROntologyServiceImpl.getInstance().addIndividual(operationalization);
//				}
//			}
//		}
//		
//		NDROntologyServiceImpl.getInstance().getOntology().print();
//    	
//    	return ok();
//    }

}

package jena;

import istarml.ERelement;
import istarml.ERelementList;
import istarml.ccistarmlFile;

import java.util.Iterator;

public class CCiStarML {
	
	public static void main(String[] args) {
		
		ccistarmlFile file = new ccistarmlFile();
		file.loadFile("Application.istarml");
		
		file.xmlParser();
		
		ERelementList elementlist = new ERelementList(file.xmlStructure());
		elementlist.display();
		
		Iterator<ERelement> it = elementlist.list().iterator();
		
		while (it.hasNext()) {
			ERelement element = (ERelement) it.next();

			if (element.name.equals("ielementLink")) {
				//System.out.println(elementlist.containsRef("_28V1C8eKEeCGwI-I2xY10A") ? "true that" : "boooh");
				
				System.out.println(elementlist.getElementFromRef("_28V1C8eKEeCGwI-I2xY10A").attribute.get("name"));
			}
		}
		
//		NFRType type = new NFRType();
//		type.setName("NFR_Test");
//		
//		NFRSoftgoal softgoal = new NFRSoftgoal();
//		softgoal.setName("Test");
//		softgoal.setType(type);
//		softgoal.setTopic("");
//		
//		NDROntologyServiceImpl.getInstance().addIndividual(softgoal);
//		
//		NDROntologyServiceImpl.getInstance().getOntology().print();
	}

}

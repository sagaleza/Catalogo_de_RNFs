package istarml;

import models.ontology.ndr.Interdependency;
import models.ontology.ndr.NFRDecomposition;
import models.ontology.ndr.OperDecomposition;
import models.ontology.ndr.Operationalization;

/**
 * An utility class to convert the data from iStarML to NDR individuals
 * @author rveleda
 *
 */
public class IStarMLtoNDRConverter {

	public static Interdependency findInterdependencyType(String headType, String tailType) {
		if (headType.equalsIgnoreCase(IStarMLElementTypes.SOFTGOAL.toString()) 
				&& tailType.equalsIgnoreCase(IStarMLElementTypes.SOFTGOAL.toString())) {
			
			return new NFRDecomposition();
		} else if (headType.equalsIgnoreCase(IStarMLElementTypes.SOFTGOAL.toString()) 
				&& tailType.equalsIgnoreCase(IStarMLElementTypes.TASK.toString())) {
			
			return new Operationalization();
		} else if (headType.equalsIgnoreCase(IStarMLElementTypes.TASK.toString()) 
				&& tailType.equalsIgnoreCase(IStarMLElementTypes.TASK.toString())) {
			
			return new OperDecomposition();
		}
		
		// TODO Throw exception
		
		return null;
	}
	
	
}

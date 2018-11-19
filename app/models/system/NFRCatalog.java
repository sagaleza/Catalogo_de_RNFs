package models.system;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Migrate this class to EBEAN
 * @author rveleda
 *
 */
public class NFRCatalog {
	
	private long id;
	
	private String name;
	
	private long timestamp;
	
	private String comment;
	
	private int version;
	
	private Set<String> originalElementNames;
	
	public NFRCatalog() {
		this.originalElementNames = new HashSet<String>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Set<String> getOriginalElementNames() {
		return originalElementNames;
	}

	public void setOriginalElementNames(Set<String> originalElementNames) {
		this.originalElementNames = originalElementNames;
	}
	
	public String getOriginalElementNamesPersistent() {
		StringBuffer sb = new StringBuffer();
		
		int cont = 0;
		
		for (String elementName : this.getOriginalElementNames()) {
			sb.append(elementName.replaceAll(" ", "-"));
			
			if (cont < (this.getOriginalElementNames().size()-1)) {
				sb.append(";");
			}
			
			cont++;
		}
		
		return sb.toString();
	}

}

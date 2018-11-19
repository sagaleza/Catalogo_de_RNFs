package services.system;

import java.util.List;

import models.system.NFRCatalog;

public interface NFRCatalogService {
	
	public void insert(NFRCatalog cat);
	
	public void delete(long id);
	
	public void delete(String name);
	
	public void update(NFRCatalog cat);
	
	public NFRCatalog get(long id);
	
	public NFRCatalog get(String name);
	
	public List<NFRCatalog> loadAll();
	
	public List<NFRCatalog> getCatalogOccurrences(String elementName);
	
	public boolean isElementInCatalog(String elementName, String catalogName);

}

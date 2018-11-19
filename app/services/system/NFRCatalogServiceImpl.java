package services.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.system.NFRCatalog;
import utils.MySQL;

public class NFRCatalogServiceImpl implements NFRCatalogService {
	
	private static final String TABLE_NAME = "nfr_catalog_thesis";
	
    private static NFRCatalogServiceImpl _instance = null;

    public static synchronized NFRCatalogServiceImpl getInstance() {
    	if (_instance == null)
    		_instance = new NFRCatalogServiceImpl();
    	
        return _instance;
    }

	@Override
	public void insert(NFRCatalog cat) {
		Connection dataConnection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			NFRCatalog existentCat = this.get(cat.getName());
			
			if (existentCat == null) {
				dataConnection = MySQL.getDbConnection();
				
				String query = "INSERT INTO " + TABLE_NAME + " (name, timestamp, comment, version, element_names) VALUES (?,?,?,?,?)";
				
				ps = dataConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				ps.setString(1, cat.getName());
				ps.setLong(2, cat.getTimestamp());
				ps.setString(3, cat.getComment());
				ps.setInt(4, cat.getVersion());
				ps.setString(5, cat.getOriginalElementNamesPersistent());
				
				ps.executeUpdate();
				
				rs = ps.getGeneratedKeys();
				
				if (rs.next()) {
					System.out.println("NFRCatalog #" + rs.getLong(1) + "created!");
				}
			} else {
				existentCat.setComment(cat.getComment());
				existentCat.setTimestamp(cat.getTimestamp());
				existentCat.setVersion(cat.getVersion());
				existentCat.getOriginalElementNames().addAll(cat.getOriginalElementNames());
				
				this.update(existentCat);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(rs, ps, dataConnection);
		}
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(String name) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void update(NFRCatalog cat) {
		Connection dataConnection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			dataConnection = MySQL.getDbConnection();
			
			String query = "UPDATE " + TABLE_NAME + " SET name=? , timestamp=?, comment=?, version=?, element_names=? WHERE id=?";
			
			ps = dataConnection.prepareStatement(query);
			
			ps.setString(1, cat.getName());
			ps.setLong(2, cat.getTimestamp());
			ps.setString(3, cat.getComment());
			ps.setInt(4, cat.getVersion());
			ps.setString(5, cat.getOriginalElementNamesPersistent());
			ps.setLong(6, cat.getId());
			
			ps.executeUpdate();
			
			System.out.println("NFRCatalog #" + cat.getId() + "updated!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(rs, ps, dataConnection);
		}
	}

	@Override
	public NFRCatalog get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NFRCatalog get(String name) {
		Connection dataConnection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		NFRCatalog cat = null;
		
		try {
			dataConnection = MySQL.getDbConnection();
			
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE ?";
			
			ps = dataConnection.prepareStatement(query);
			
			if (name.contains("_"))
				name = name.split("_")[0];
			
			ps.setString(1, name + "%");
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				cat = new NFRCatalog();
				cat.setId(rs.getLong("id"));
				cat.setName(rs.getString("name"));
				cat.setTimestamp(rs.getLong("timestamp"));
				cat.setComment(rs.getString("comment"));
				cat.setVersion(rs.getInt("version"));
				cat.getOriginalElementNames().addAll(Arrays.asList(rs.getString("element_names").split(";")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(rs, ps, dataConnection);
		}
		
		return cat;
	}
	
	private void closeConnections(ResultSet rs, PreparedStatement ps, Connection dataConnection) {
		try {
			if (rs != null)
				rs.close();
			
			if (ps != null)
				ps.close();
			
			if (dataConnection != null && !dataConnection.isClosed())
				dataConnection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<NFRCatalog> loadAll() {
		Connection dataConnection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NFRCatalog> catalogList = new ArrayList<NFRCatalog>();
		
		try {
			dataConnection = MySQL.getDbConnection();
			
			String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY name ASC";
			
			ps = dataConnection.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				NFRCatalog cat = new NFRCatalog();
				cat.setId(rs.getLong("id"));
				cat.setName(rs.getString("name"));
				cat.setTimestamp(rs.getLong("timestamp"));
				cat.setComment(rs.getString("comment"));
				cat.setVersion(rs.getInt("version"));
				cat.getOriginalElementNames().addAll(Arrays.asList(rs.getString("element_names").split(";")));
				
				catalogList.add(cat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(rs, ps, dataConnection);
		}
		
		return catalogList;
	}

	@Override
	public List<NFRCatalog> getCatalogOccurrences(String elementName) {
//		Connection dataConnection = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<NFRCatalog> catalogList = new ArrayList<NFRCatalog>();
//		
//		try {
//			dataConnection = MySQL.getDbConnection();
//			
//			String query = "SELECT * FROM " + TABLE_NAME + " WHERE element_names LIKE ? ORDER BY name ASC";
//			
//			ps = dataConnection.prepareStatement(query);
//			ps.setString(1, "%"+elementName+"%");
//			rs = ps.executeQuery();
//			
//			while (rs.next()) {
//				NFRCatalog cat = new NFRCatalog();
//				cat.setId(rs.getLong("id"));
//				cat.setName(rs.getString("name"));
//				cat.setTimestamp(rs.getLong("timestamp"));
//				cat.setComment(rs.getString("comment"));
//				cat.setVersion(rs.getInt("version"));
//				cat.getOriginalElementNames().addAll(Arrays.asList(rs.getString("element_names").split(";")));
//				
//				catalogList.add(cat);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeConnections(rs, ps, dataConnection);
//		}
		
		List<NFRCatalog> catalogList = new ArrayList<NFRCatalog>();
		
		for (NFRCatalog catalog : this.loadAll()) {
			for (String name : catalog.getOriginalElementNames()) {
				if (name.equalsIgnoreCase(elementName)) {
					catalogList.add(catalog);
					break;
				}
			}
		}
		
		return catalogList;
	}

	@Override
	public boolean isElementInCatalog(String elementName, String catalogName) {
		NFRCatalog catalog = this.get(catalogName);

		for (String name : catalog.getOriginalElementNames()) {
			if (name.equalsIgnoreCase(elementName)) {
				return true;
			}
		}
		
		return false;
	}
}

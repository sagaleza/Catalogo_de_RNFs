package utils;

import java.sql.Connection;

import javax.sql.DataSource;

import play.db.DB;

public class MySQL {

	static Connection dataConnection = null;

	public static Connection getDbConnection() {
		try {
			if (dataConnection == null || dataConnection.isClosed()) {
				DataSource dataSource = DB.getDataSource();
				dataConnection = dataSource.getConnection();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			dataConnection = null;
		}
		return dataConnection;
	}

}

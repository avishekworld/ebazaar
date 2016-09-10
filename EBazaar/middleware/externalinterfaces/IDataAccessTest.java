package middleware.externalinterfaces;

import java.sql.ResultSet;

import middleware.DatabaseException;

public interface IDataAccessTest {
	public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException;
}

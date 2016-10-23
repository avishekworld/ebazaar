package middleware.externalinterfaces;

import java.sql.ResultSet;

import middleware.DatabaseException;

public interface IDataAccessTest {
	public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException;
	public ResultSet runTestQuery(String query, String dbulr) throws DatabaseException;
	public Integer runTestUpdate(String query, String dbulr) throws DatabaseException;
}

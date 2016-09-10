
package middleware.externalinterfaces;

import java.sql.Connection;

import middleware.DatabaseException;

public interface IDataAccessSubsystem {
	//This starts the data access interaction and permits the use of transactions
	//The DataAccessSubsystem will keep a reference to the connection for all future uses in the current instance
	//so the connection object does not need to be managed by the client.
	public void createConnection(IDbClass dbClass) throws DatabaseException;
	public void releaseConnection(IDbClass dbClass);
	public void startTransaction() throws DatabaseException;
	public void commit() throws DatabaseException;
	public void rollback() throws DatabaseException;
    public void read() throws DatabaseException;  
    public Integer save() throws DatabaseException;
    public Integer delete() throws DatabaseException;
	public void closeAllConnections(Cleanup c);
	public Integer saveWithinTransaction(IDbClass dbClass) throws DatabaseException;
	public Integer deleteWithinTransaction(IDbClass dbClass) throws DatabaseException;
	public void atomicRead(IDbClass dbClass) throws DatabaseException;
	
		
	
}

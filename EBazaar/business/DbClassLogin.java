package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassLogin implements IDbClass {
	private static final Logger LOG = 
		Logger.getLogger(DbClassLogin.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
    private Integer custId;
    private String password;
    @SuppressWarnings("unused")
	private Login login;
    private String query;
    private IDataAccessSubsystem dataAccess;
    private boolean authenticated;
    
    public DbClassLogin(Login login){
        this.login=login;
        this.custId = login.getCustId();
        this.password = login.getPassword();
    }
    public boolean authenticate() throws DatabaseException {
    	dataAccessSS.atomicRead(this);
        return authenticated;        
    }
    
    /** Tries to locate the custId/password pair in Customer table */
    public void buildQuery() {
        query = "SELECT * FROM Customer WHERE custid = " + custId+ " AND password = '"+password+"'";
    }

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        try {
            authenticated = resultSet.next();
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }
 
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }

    public String getQuery() {
        return query;
    }
}

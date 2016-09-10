package business.productsubsystem;

import java.sql.ResultSet;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.dataaccess.DataAccessUtil;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCatalog implements IDbClass {
	
	private String query;
    private String queryType;
    private final String SAVE = "Save";
    
    public void saveNewCatalog(String name) throws DatabaseException {
    	//IMPLEMENT
    }
    
    
	public void buildQuery() throws DatabaseException {
		if(queryType.equals(SAVE)) {
			buildSaveQuery();
		}
		
	}
	void buildSaveQuery() throws DatabaseException {
		//IMPLEMENT
		query = ""; 
	}

	public String getDbUrl() {
		//IMPLEMENT
		return "";
	}

	public String getQuery() {
		//IMPLEMENT
		return "";
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		// do nothing
		
	}
	
}

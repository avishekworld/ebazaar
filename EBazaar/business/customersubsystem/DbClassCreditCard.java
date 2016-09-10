
package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import business.externalinterfaces.ICustomerProfile;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassCreditCard implements IDbClass {
    private final String READ = "Read";
    private ICustomerProfile custProfile;
    String query;
    private String queryType;
 
    public void buildQuery() {
        if(queryType.equals(READ)){
            buildReadQuery();
        }
    }
    
    void buildReadQuery() {
        //IMPLEMENT
    }
    
 
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        //IMPLEMENT
    }


    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
 
    }

 
    public String getQuery() {
        return query;
 
    }

 
}

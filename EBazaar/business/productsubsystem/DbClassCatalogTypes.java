package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;


import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;

/**
 * @author pcorazza
 * <p>
 * Class Description: 
 */
public class DbClassCatalogTypes implements IDbClass {

    private String query;
    private String queryType;
    final String GET_TYPES = "GetTypes";
    private CatalogTypes types;
    
    public CatalogTypes getCatalogTypes() throws DatabaseException {
    	//IMPLEMENT
        return new CatalogTypes();       
    }
    
    public void buildQuery() {
        if(queryType.equals(GET_TYPES)){
            buildGetTypesQuery();
        }
    }

    void buildGetTypesQuery() {
        query = "SELECT * FROM CatalogType";       
    }
    /**
     * This is activated when getting all catalog types.
     */
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        types = new CatalogTypes();
        //IMPLEMENT
        
    }

    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    }

    public String getQuery() {

        return query;
    }

}

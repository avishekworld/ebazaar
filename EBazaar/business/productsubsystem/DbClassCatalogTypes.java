package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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
	private static final Logger LOG = Logger.getLogger(DbClassCatalogTypes.class
			.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
    private String query;
    private String queryType;
    private String catalogName;
    private Integer catalogId;
    private CatalogTypes catalogTypes;
    
    private static final String GET_CATALOG_TYPES = "GetCatalogTypes";
    private static final String GET_CATALOG_ID = "GetCatalogId";
    private final String SAVE = "Save";
    
    public CatalogTypes getCatalogTypes() throws DatabaseException {
        return catalogTypes;       
    }
    
    public void buildQuery() {
        if(queryType.equals(GET_CATALOG_TYPES)){
            buildGetTypesQuery();
        }else if(queryType.equals(GET_CATALOG_ID)){
        	buildGetCatagoryIdQuery();
        }else if(queryType.equals(SAVE)){
        	buildSaveCatagoryQuery();
        }
    }

    void buildGetTypesQuery() {
        query = "SELECT * FROM CatalogType";       
    }
    
    void buildGetCatagoryIdQuery() {
        query = "SELECT catalogid FROM CatalogType where catalogname='" + catalogName+ "'";       
    }
    
    void buildSaveCatagoryQuery() {
        query = "insert into catalogtype values (null,'" + catalogName+ "')";       
    }
    
    public Integer getCatalogIdFromType(String catType) throws DatabaseException{
    	this.catalogName = catType;
    	queryType = GET_CATALOG_ID;
		dataAccessSS.atomicRead(this);
		return catalogId;
    }
    
	public List<String[]> getCatalogNames() throws DatabaseException {
		if(catalogTypes == null){
			return refreshCatalogNames();
		}
		return Collections.unmodifiableList(catalogTypes.getCatalogNames());
	}
	
	public List<String[]> refreshCatalogNames() throws DatabaseException {
		queryType = GET_CATALOG_TYPES;
		dataAccessSS.atomicRead(this);
		return Collections.unmodifiableList(catalogTypes.getCatalogNames());
	}
	
	public void saveNewCatalog(String catalogName) throws DatabaseException {
		this.catalogName = catalogName;
		queryType = SAVE;
		dataAccessSS.saveWithinTransaction(this);
	}
	
    /**
     * This is activated when getting all catalog types.
     */
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        if(queryType.equals(GET_CATALOG_TYPES)){
        	populateCatalogTypes(resultSet);
        }else if(queryType.equals(GET_CATALOG_ID)){
        	populateCatalogId(resultSet);
        }
        
    }
    
    private void populateCatalogTypes(ResultSet resultSet) throws DatabaseException{
    	catalogTypes = new CatalogTypes();
    	try {
			if(resultSet !=null){
				while (resultSet.next()) {
					int catalogId = resultSet.getInt("catalogid");
					String catalogName = resultSet.getString("catalogname");
					catalogTypes.addCatalog(catalogId, catalogName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("Unable to read catalog types");
		}
    }
    
    private void populateCatalogId(ResultSet resultSet) throws DatabaseException{
    	try {
			if(resultSet !=null){
				resultSet.first();
				catalogId = resultSet.getInt("catalogid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("Unable to get catalog id");
		}
    }

    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    }

    public String getQuery() {
        return query;
    }

}

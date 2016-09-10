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

public class DbClassQuantity implements IDbClass {
	private static final Logger LOG = 
		Logger.getLogger(DbClassQuantity.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	private Quantity quantity;
	private String productName;
	private String queryType;
    private String query;
    private final String READ = "Read";	
    private final String QUANTITY_AVAIL = "totalquantity";
    
    public DbClassQuantity() {}
    
    public void setQuantity(Quantity q) {
    	quantity=q;
    }
    /** updates quantity available field in the quantity object */
    public void readQuantityAvail(String productName) throws DatabaseException{
    	this.productName = productName;	
        queryType=READ;
        dataAccessSS.createConnection(this);
        dataAccessSS.read();	    
    }
    
    public void buildQuery() {
    	if(queryType.equals(READ)){
    		buildReadQuery();
    	}
    }
    
    private void buildReadQuery() {
    	query = "SELECT totalquantity from Product where productname='"+productName+"'";
    }
    
    public void populateEntity(ResultSet rs) throws DatabaseException {
    	if(queryType.equals(READ)){
    		populateQuantity(rs);
    	}	
    }
    
    private void populateQuantity(ResultSet rs) throws DatabaseException {
    	try {
            if(rs.next()){
                quantity.setQuantityAvailable(rs.getString(QUANTITY_AVAIL));
            }           
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }    	
    }
    
    /** Quick test */
    public static void main(String[] args){
        DbClassQuantity dbq = new DbClassQuantity();
        Quantity qty = new Quantity("20");
        dbq.setQuantity(qty);
        try {
            dbq.readQuantityAvail("Pants");
            System.out.println(qty.getQuantityAvailable());
        }
        catch(DatabaseException e){
            e.printStackTrace();
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

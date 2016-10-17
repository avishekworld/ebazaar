
package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import business.externalinterfaces.ICustomerProfile;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassCreditCard implements IDbClass {
	private static final Logger LOG = Logger.getLogger(DbClassAddress.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
    private final String READ = "Read";
    private ICustomerProfile customerProfile;
    private CreditCard defaultPayment;
    private String query;
    private String queryType;
 
    public void buildQuery() {
        if(queryType.equals(READ)){
            buildReadQuery();
        }
    }
    
    void buildReadQuery() {
    	query = "SELECT nameoncard, expdate, cardnum, cardtype "+
    	        "FROM Customer "+
    	        "WHERE custid = " + customerProfile.getCustId();
    }
    
    public void readDefaultPayment(CustomerProfile customerProfile) throws DatabaseException {
		this.customerProfile = customerProfile;
		queryType = READ;
		dataAccessSS.atomicRead(this);
	}
    
    CreditCard getDefaultPayment(){
        return defaultPayment;
    }
    
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
    	if(queryType.equals(READ)){
    		populateDefaultPayment(resultSet);
        }
    }
    
    void populateDefaultPayment(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                defaultPayment = new CreditCard(rs.getString("nameoncard"),
                                                 rs.getString("expdate"),
                                                 rs.getString("cardnum"),
                                                 rs.getString("cardtype"));
            }          
        }
        catch(SQLException e) {
        	e.printStackTrace();
            throw new DatabaseException("Unable to read defauult payment info");
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

package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;

public class DbClassCustomerProfile implements IDbClass {
	private static final Logger LOG = Logger.getLogger(DbClassCustomerProfile.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private CustomerProfile customerProfile;
	private Integer customerId;
	private String queryType;
    private String query;
    private final String READ = "Read";

	@Override
	public void buildQuery() throws DatabaseException {
		if(queryType.equals(READ)){
			buildReadCustomerProfileQuery();
        }

	}
	
	void buildReadCustomerProfileQuery() {
		query = "SELECT custid, fname, lname "+
		        "FROM Customer WHERE custid = " + customerId;
    }
	
	public CustomerProfile getCustomerProfile(Integer customerId) throws DatabaseException {
		this.customerId = customerId;
		queryType = READ;
		dataAccessSS.atomicRead(this);
		return customerProfile;
	}

	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if(queryType.equals(READ)){
			populateCustomerProfile(resultSet);
        }

	}
	
	void populateCustomerProfile(ResultSet rs) throws DatabaseException {
        try {
            if(rs != null){
            	rs.first();
            	int custid = rs.getInt("custid");
            	String fname = rs.getString("fname");
            	String lname = rs.getString("lname");
            	customerProfile = new CustomerProfile(custid, fname, lname);
            }         
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
        
    }

	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	@Override
	public String getQuery() {
		return query;
	}

}

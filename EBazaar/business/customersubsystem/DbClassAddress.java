
package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;



import business.externalinterfaces.*;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;

import middleware.externalinterfaces.*;


class DbClassAddress implements IDbClass {
	private static final Logger LOG = Logger.getLogger(DbClassAddress.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
    private ICustomerProfile custProfile;
    private IAddress address;
    private List<IAddress> addressList;
    private Address defaultShipAddress;
    private Address defaultBillAddress;
    private String queryType;
    private String query;
    
    
    private final String SAVE = "Save";
    private final String READ = "Read";
    private final String READ_DEFAULT_SHIP = "ReadDefaultShip";
    private final String READ_DEFAULT_BILL = "ReadDefaultBill";
    
	//column names
    private final String STREET="street";
    private final String CITY = "city";
    private final String STATE = "state";
    private final String ZIP = "zip";
    
    public void saveAddress(ICustomerProfile custProfile) throws DatabaseException {
        this.custProfile = custProfile;
        queryType = SAVE;
        dataAccessSS.saveWithinTransaction(this);
    }
    
    void readDefaultShipAddress(ICustomerProfile custProfile) throws DatabaseException {
    	this.custProfile = custProfile;
        queryType=READ_DEFAULT_SHIP;
        dataAccessSS.atomicRead(this);       
    }
    void readDefaultBillAddress(ICustomerProfile custProfile) throws DatabaseException {
    	this.custProfile = custProfile;
        queryType=READ_DEFAULT_BILL;
        dataAccessSS.atomicRead(this);  
    }    
    void readAllAddresses(ICustomerProfile custProfile) throws DatabaseException {
    	this.custProfile = custProfile;
    	queryType = READ;
    	dataAccessSS.atomicRead(this); 
    }
    
    public void buildQuery() throws DatabaseException {
        if(queryType.equals(SAVE)){
            buildSaveNewAddrQuery();
        }
        else if(queryType.equals(READ)){
            buildReadAllAddressesQuery();
        }
        else if(queryType.equals(READ_DEFAULT_SHIP)){
            buildReadDefaultShipQuery();
        }	
        else if(queryType.equals(READ_DEFAULT_BILL)){
            buildReadDefaultBillQuery();
        }
    }
    
    void buildReadCustNameQuery() {
        query = "SELECT fname, lname " +
        "FROM Customer " +
        "WHERE custid = " + custProfile.getCustId();        
    }
	
    void buildSaveNewAddrQuery() throws DatabaseException {
        query = "INSERT into altshipaddress " +
        		"(addressid,custid,street,city,state,zip) " +
        		"VALUES(NULL," +
        				  custProfile.getCustId() + ",'" +
        				  address.getStreet1()+"','" +
        				  address.getCity() + "','" +
        				  address.getState() + "','" +
        				  address.getZip() + "')";
    }
    void buildReadAllAddressesQuery() {
    	query = "SELECT * from  AltShipAddress "+
    	        "WHERE custid = " + custProfile.getCustId();
    }
 
    void buildReadDefaultShipQuery() {
        query = "SELECT shipaddress1, shipaddress2, shipcity, shipstate, shipzipcode "+
        "FROM Customer "+
        "WHERE custid = " + custProfile.getCustId();
    }
    
    void buildReadDefaultBillQuery() {
        query = "SELECT billaddress1, billaddress2, billcity, billstate, billzipcode "+
        "FROM Customer "+
        "WHERE custid = " + custProfile.getCustId();
    }
    
    IAddress getAddress() {
        return address;
    }
    
    List<IAddress> getAddressList() {
        return addressList;
    }
    
    Address getDefaultShipAddress(){
        return defaultShipAddress;
    }
    
    Address getDefaultBillAddress() {
        return defaultBillAddress;
    }
        
    void setAddress(IAddress addr){
        address = addr;
    }
    
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    public String getQuery() {
        return query;
        
    }
    
    /* Used only when we read from the database
     */
    public void populateEntity(ResultSet rs) throws DatabaseException {
        if(queryType.equals(READ)){
            populateAddressList(rs);
        }		        		
        else if(queryType.equals(READ_DEFAULT_SHIP)){
            populateDefaultShipAddress(rs);
        }
        else if(queryType.equals(READ_DEFAULT_BILL)){
            populateDefaultBillAddress(rs);
        }
    }
    
    //implementation provided here, but you still need to implement the sql
    void populateAddressList(ResultSet rs) throws DatabaseException {
        addressList = new LinkedList<IAddress>();
        if(rs != null){
            try {
                while(rs.next()) {
                    address = new Address();
                    String str = rs.getString(STREET);
                    address.setStreet1(str);
                    address.setCity(rs.getString(CITY));
                    address.setState(rs.getString(STATE));
                    address.setZip(rs.getString(ZIP));
                    addressList.add(address);
                }                
            }
            catch(SQLException e){
            	e.printStackTrace();
                throw new DatabaseException("Unable to read all addresses");
            }         
        }       
    }
    
    void populateDefaultShipAddress(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                defaultShipAddress = new Address(rs.getString("shipaddress1"),
                                                 rs.getString("shipaddress2"),
                                                 rs.getString("shipcity"),
                                                 rs.getString("shipstate"),
                                                 rs.getString("shipzipcode"));
            }          
        }
        catch(SQLException e) {
        	e.printStackTrace();
            throw new DatabaseException("Unable to read default ship address");
        }
        
    }
    
    void populateDefaultBillAddress(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                defaultBillAddress = new Address(rs.getString("billaddress1"),
                                                 rs.getString("billaddress2"),
                                                 rs.getString("billcity"),
                                                 rs.getString("billstate"),
                                                 rs.getString("billzipcode"));
            }          
        }
        catch(SQLException e) {
        	e.printStackTrace();
            throw new DatabaseException("Unable to read default bill address");
        }
        
    }
}

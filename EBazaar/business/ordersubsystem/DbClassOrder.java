
package business.ordersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import static business.util.StringParse.*;

import java.util.List;
import java.util.logging.Logger;

import com.sun.crypto.provider.RSACipher;


import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.externalinterfaces.IShoppingCart;
import business.util.OrderUtil;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassOrder implements IDbClass {
	private static final Logger LOG = 
		Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	private String query;
    private String queryType;
    private final String GET_ORDER_ITEMS = "GetOrderItems";
    private final String GET_ORDER_IDS = "GetOrderIds";
    private final String GET_ORDER_DATA = "GetOrderData";
    private ICustomerProfile customerProfile;
    private String orderId;
    private List<String> orderIds;
    private List<IOrderItem> orderItems;
    private Order orderData;    
    
    public void buildQuery() {
        if(queryType.equals(GET_ORDER_ITEMS)){
            buildGetOrderItemsQuery();
        }
        else if(queryType.equals(GET_ORDER_IDS)){
            buildGetOrderIdsQuery();
        }
        else if(queryType.equals(GET_ORDER_DATA)){
        	buildGetOrderDataQuery();
        }
    }
    private void buildGetOrderDataQuery() {
        query = "SELECT orderdate, totalpriceamount FROM Ord WHERE orderid = '"+orderId+"'";
    }
    private void buildGetOrderIdsQuery() {
        query = "SELECT orderid FROM Ord WHERE custid = '"+customerProfile.getCustId()+"'";
    }
    private void buildGetOrderItemsQuery() {
        query = "SELECT * FROM OrderItem WHERE orderid = '"+orderId+"'";
    }
    
    public List<String> getAllOrderIds(ICustomerProfile customerProfile) throws DatabaseException {
        //implement
    	this.customerProfile=customerProfile;
    	queryType = GET_ORDER_IDS;
    	orderIds = new LinkedList<String>();
    	dataAccessSS.createConnection(this);
		dataAccessSS.read();
		return orderIds;
        
    }
    public Order getOrderData(String orderId) throws DatabaseException {
    	//implement
    	this.orderId = orderId;
    	queryType = GET_ORDER_DATA;
    	dataAccessSS.createConnection(this);
		dataAccessSS.read();
		List<IOrderItem> orderItems = getOrderItems(orderId);
		orderData.setOrderItems(orderItems);
    	return orderData;
    }
    
    public List<IOrderItem> getOrderItems(String orderId) throws DatabaseException {
    	this.orderId = orderId;
    	queryType = GET_ORDER_ITEMS;
    	dataAccessSS.createConnection(this);
		dataAccessSS.read();
        return orderItems;
    }
    
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        if(queryType.equals(GET_ORDER_ITEMS)){
            populateOrderItems(resultSet);
        }
        else if(queryType.equals(GET_ORDER_IDS)){
            populateOrderIds(resultSet);
        }
        else if(queryType.equals(GET_ORDER_DATA)){
        	populateOrderData(resultSet);
        }
    }
    
    private void populateOrderItems(ResultSet rs) throws DatabaseException {
        orderItems = new LinkedList<IOrderItem>();
        if(rs != null){
        	try {
				while(rs.next()){
					int orderid = rs.getInt("orderid");
					int productid = rs.getInt("productid");
					int quantity = rs.getInt("quantity");
					double totalprice= rs.getDouble("totalprice");
					OrderItem orderItem = new OrderItem(productid, orderid, quantity +"", totalprice + "");
					orderItems.add(orderItem);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DatabaseException(e);
			}
        }
        //implement
    }
    private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
        orderIds = new LinkedList<String>();
        if(resultSet != null){
        	try {
				while(resultSet.next()) {
				    int orderId = resultSet.getInt("orderid");
				    orderIds.add(orderId +"");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new DatabaseException(e);
			} 
        }
    }
    private void populateOrderData(ResultSet resultSet) throws DatabaseException {
    	//implement
    	if(resultSet != null){
    		try {
				resultSet.first();
				String orderdate = resultSet.getString("orderdate");
	        	double totalpriceamount = resultSet.getDouble("totalpriceamount");
	        	orderData = new Order(Integer.parseInt(orderId), orderdate, totalpriceamount +"");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DatabaseException(e);
			}
        }
    }    
 
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    public String getQuery() {
        return query;
    }
    
    
    public void setOrderId(String orderId){
        this.orderId = orderId;
        
    } 
}

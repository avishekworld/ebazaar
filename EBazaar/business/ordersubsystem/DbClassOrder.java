
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
import business.util.ShoppingCartUtil;
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
    private final String SAVE_ORDER = "SaveOrder";
    private final String SAVE_ORDER_ITEM = "SaveOrderItem";
    private final String GET_ORDER_ITEMS = "GetOrderItems";
    private final String GET_ORDER_IDS = "GetOrderIds";
    private final String GET_ORDER_DATA = "GetOrderData";
    private ICustomerProfile customerProfile;
    private IOrder order;
    private String orderId;
    private IOrderItem orderItem;
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
        else if(queryType.equals(SAVE_ORDER)){
        	buildSaveOrderQuery();
        }
        else if(queryType.equals(SAVE_ORDER_ITEM)){
        	buildSaveOrderItemQuery();
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
    private void buildSaveOrderQuery() {
    	double orderCost = Double.parseDouble(order.getTotalPrice());
    	double shippingCost = OrderUtil.getShippingCostOfOrder(order.getOrderItems());
    	double tax = OrderUtil.getTaxOfOrder(order.getOrderItems());
    	double total = orderCost + shippingCost + tax;
    	query = "insert into ord values (null," + customerProfile.getCustId() + 
    			",'" + order.getShipAddress().getStreet1()+ "'" +
    			",'" + order.getShipAddress().getStreet2()+ "'" +
    			",'" + order.getShipAddress().getCity()+ "'" +
    			",'" + order.getShipAddress().getState()+ "'" +
    			",'" + order.getShipAddress().getZip()+ "'" +
    			",'" + order.getBillAddress().getStreet1()+ "'" +
    			",'" + order.getBillAddress().getStreet2()+ "'" +
    			",'" + order.getBillAddress().getCity()+ "'" +
    			",'" + order.getBillAddress().getState()+ "'" +
    			",'" + order.getBillAddress().getZip()+ "'" +
    			",'" + order.getPaymentInfo().getNameOnCard()+ "'" +
    			",'" + order.getPaymentInfo().getExpirationDate()+ "'" +
    			",'" + order.getPaymentInfo().getCardType()+ "'" +
    			",'" + order.getPaymentInfo().getCardNum()+ "'" +
    			",'" + order.getOrderDate()+ "'" +
    			",'" + null+ "'" +
    			",'" + null+ "'" +
    			",'" + "pending"+ "'" +
    			"," + orderCost +
    			"," + shippingCost +
    			"," + tax +
    			"," + total + ")";
    }
    private void buildSaveOrderItemQuery() {
    	double shippingCost = OrderUtil.getShippingCost(orderItem);
    	double tax = OrderUtil.getTax(orderItem);
        query = "insert into orderitem values (null," + orderId +
        		"," + orderItem.getProductid() +
        		"," + orderItem.getQuantity() +
    			"," + orderItem.getTotalPrice() +
    			"," + shippingCost +
    			"," + tax + ")";
        
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
    
    public Integer submitOrder(IOrder order, ICustomerProfile customerProfile) throws DatabaseException{
    	this.customerProfile = customerProfile;
    	this.order = order;
    	queryType = SAVE_ORDER;
    	dataAccessSS.createConnection(this);
    	Integer orderId = dataAccessSS.saveWithinTransaction(this);
    	return orderId;
    }
    
    public void submitOrderItem(IOrderItem orderItem, Integer orderId) throws DatabaseException{
    	this.orderItem = orderItem;
    	this.orderId = orderId + "";
    	queryType = SAVE_ORDER_ITEM;
    	dataAccessSS.createConnection(this);
    	dataAccessSS.saveWithinTransaction(this);
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

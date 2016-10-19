
package business.shoppingcartsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleToLongFunction;
import java.util.logging.Logger;

import static business.util.StringParse.*;

import business.*;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IShoppingCart;
import business.util.OrderUtil;
import business.util.ShoppingCartUtil;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;


public class DbClassShoppingCart implements IDbClass {
	private static final Logger LOG = Logger.getLogger(DbClassShoppingCart.class
			.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
    IDataAccessSubsystem dataAccess;
    ShoppingCart cart;
    ICartItem cartItem;
    List<ICartItem> cartItemsList;
    ICustomerProfile custProfile;
    Integer cartId;
    String query;
    String queryType;
    private final String SAVE_CART = "Save";
    private final String SAVE_CART_ITEM = "SaveItem";
    final String GET_ID="GetId";
    final String GET_SAVED_ITEMS="GetSavedItems";
    
    public void buildQuery() {
        if(queryType.equals(GET_ID)){
            buildGetIdQuery();
        }
        else if(queryType.equals(GET_SAVED_ITEMS)){
            buildGetSavedItemsQuery();
        }
        else if(queryType.equals(SAVE_CART)){
        	buildSaveCartQuery();
        }
        else if(queryType.equals(SAVE_CART_ITEM)){
        	buildSaveCartItemsQuery();
        }
        
    }
    private void buildGetIdQuery(){
        query = "SELECT shopcartid "+
                "FROM ShopCartTbl "+
                "WHERE custid = "+custProfile.getCustId();
                
    }
    
    private void buildGetSavedItemsQuery() {
		query = "select * from shopcartitem where shopcartid =" + cartId;
    }
    
    private void buildSaveCartQuery(){
    	double cartTotalCost = ShoppingCartUtil.getCartTotalPrice(cart);
    	double shippingCost = OrderUtil.getShippingCost(cart.getCartItems());
    	double tax = OrderUtil.getTax(cart.getCartItems());
    	double total = cartTotalCost + shippingCost + tax;
    	query = "insert into ShopCartTbl values (null," + custProfile.getCustId() + 
    			",'" + cart.getShippingAddress().getStreet1()+ "'" +
    			",'" + cart.getShippingAddress().getStreet2()+ "'" +
    			",'" + cart.getShippingAddress().getCity()+ "'" +
    			",'" + cart.getShippingAddress().getState()+ "'" +
    			",'" + cart.getShippingAddress().getZip()+ "'" +
    			",'" + cart.getBillingAddress().getStreet1()+ "'" +
    			",'" + cart.getBillingAddress().getStreet2()+ "'" +
    			",'" + cart.getBillingAddress().getCity()+ "'" +
    			",'" + cart.getBillingAddress().getState()+ "'" +
    			",'" + cart.getBillingAddress().getZip()+ "'" +
    			",'" + cart.getPaymentInfo().getNameOnCard()+ "'" +
    			",'" + cart.getPaymentInfo().getExpirationDate()+ "'" +
    			",'" + cart.getPaymentInfo().getCardType()+ "'" +
    			",'" + cart.getPaymentInfo().getCardNum()+ "'" +
    			"," + cartTotalCost +
    			"," + shippingCost +
    			"," + tax +
    			"," + total + ")";
    }
    
    private void buildSaveCartItemsQuery(){
    	double shippingCost = OrderUtil.getShippingCost(cartItem);
    	double tax = OrderUtil.getTax(cartItem);
    	query = "insert into ShopCartItem values (null," + cart.getCartId() + 
    			"," + cartItem.getProductid() +
    			"," + cartItem.getQuantity() +
    			"," + cartItem.getTotalprice() +
    			"," + shippingCost +
    			"," + tax + ")";
    }
    
    private double getCartTotalCost(IShoppingCart cart){
    	double total = 0.0;
    	for(ICartItem item:cart.getCartItems()){
    		total = total + Double.parseDouble(item.getTotalprice());
    	}
    	return total;
    }
    
    public Integer getShoppingCartId(ICustomerProfile custProfile) throws DatabaseException {
        this.custProfile = custProfile;
        queryType = GET_ID;
        dataAccessSS.atomicRead(this);
        return cartId;
    }
    public List<ICartItem> getSavedCartItems(Integer cartId) throws DatabaseException {
        this.cartId = cartId;
        queryType = GET_SAVED_ITEMS;
        dataAccessSS.atomicRead(this);
        return cartItemsList;
    }
    public Integer saveCartDetails(ShoppingCart cart, ICustomerProfile custProf) throws DatabaseException{
    	this.cart = cart;
    	this.custProfile = custProf;
    	queryType = SAVE_CART;
    	Integer cartId = dataAccessSS.saveWithinTransaction(this);
    	return cartId;
    }
    
    public void saveCartItems(ShoppingCart cart, ICustomerProfile custProf) throws DatabaseException{
    	this.cart = cart;
    	this.custProfile = custProf;
    	for(ICartItem item:cart.getCartItems()){
    		if(!item.isAlreadySaved()){
    			this.cartItem = item;
        		queryType = SAVE_CART_ITEM;
        		dataAccessSS.saveWithinTransaction(this);
    		}
    	}
    }

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        if(queryType.equals(GET_ID)) {
            populateShopCartId(resultSet);
        }
        else if(queryType.equals(GET_SAVED_ITEMS)){
            populateCartItemsList(resultSet);
        }
        
    }
    private void populateShopCartId(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                cartId = rs.getInt("shopcartid");
            }
        }
        catch(SQLException e){
        	e.printStackTrace();
            throw new DatabaseException("Unable to get shoppingcartid");
        }   
    }
    private void populateCartItemsList(ResultSet rs) throws DatabaseException {
        cartItemsList= new LinkedList<ICartItem>();
        int count = 1;
        if(rs != null){
            try {
                while(rs.next()) {
                    int cartItemId = rs.getInt("cartitemid");
                    int productId = rs.getInt("productid");
                    int lineItemId = count;
                    int quantity = rs.getInt("quantity");
                    double totalPrice = rs.getDouble("totalprice");
                    CartItem cartItem = new CartItem(cartItemId, productId, lineItemId, quantity +"", totalPrice + "", true);
                    cartItemsList.add(cartItem);
                    count++;
                }                
            }
            catch(SQLException e){
            	e.printStackTrace();
                throw new DatabaseException("Unable to read save cart items");
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
}

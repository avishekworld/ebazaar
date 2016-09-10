
package business.shoppingcartsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List; 
import java.util.logging.Logger;

import static business.util.StringParse.*;

import business.*;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICustomerProfile;
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
    List<ICartItem> cartItemsList;
    ICustomerProfile custProfile;
    Integer cartId;
    String query;
    final String GET_ID="GetId";
    final String GET_SAVED_ITEMS="GetSavedItems";
    String queryType;
    public void buildQuery() {
        if(queryType.equals(GET_ID)){
            buildGetIdQuery();
        }
        else if(queryType.equals(GET_SAVED_ITEMS)){
            buildGetSavedItemsQuery();
        }
        
    }
    private void buildGetIdQuery(){
        query = "SELECT shopcartid "+
                "FROM ShopCartTbl "+
                "WHERE custid = "+custProfile.getCustId();
                
    }
    
    private void buildGetSavedItemsQuery() {
    	//IMPLEMENT
        query = "";
        
    }
    
    public Integer getShoppingCartId(ICustomerProfile custProfile) throws DatabaseException {
        this.custProfile = custProfile;
        queryType = GET_ID;
        dataAccessSS.atomicRead(this);
        return cartId;
    }
    public List<ICartItem> getSavedCartItems(Integer cartId) throws DatabaseException {
        //IMPLEMENT
        return new LinkedList<ICartItem>();
        
    }

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        if(queryType.equals(GET_ID)) {
            populateShopCartId(resultSet);
        }
        else if(queryType.equals(GET_SAVED_ITEMS)){
            populateCartItemsList(resultSet);
        }
        
    }
    private void populateShopCartId(ResultSet rs){
        try {
            if(rs.next()){
                cartId = rs.getInt("shopcartid");
            }
        }
        catch(SQLException e){
            //do nothing
        }   
    }
    private void populateCartItemsList(ResultSet rs) throws DatabaseException {
    	//IMPLEMENT
        ICartItem cartItem = new CartItem("name","1","10");
               
        cartItemsList= new LinkedList<ICartItem>();
        cartItemsList.add(cartItem);
          
    }

 
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }


    public String getQuery() {
        return query;
    }
}

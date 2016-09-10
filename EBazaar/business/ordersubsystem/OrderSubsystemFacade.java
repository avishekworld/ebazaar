
package business.ordersubsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.dataaccess.DataAccessUtil;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.externalinterfaces.IOrderSubsystem;
import business.externalinterfaces.IShoppingCart;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.OrderUtil;


public class OrderSubsystemFacade implements IOrderSubsystem {
	private static final Logger LOG = 
		Logger.getLogger(OrderSubsystemFacade.class.getPackage().getName());
    ICustomerProfile custProfile;
    
    public OrderSubsystemFacade(ICustomerProfile custProfile){
        this.custProfile = custProfile;
    }
        ///////////// Interface methods
 
     
    ///////////// Convenience methods internal to the Order Subsystem
    List<String> getAllOrderIds() throws DatabaseException {
        
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getAllOrderIds(custProfile);
        
    }
    List<IOrderItem> getOrderItems(String orderId) throws DatabaseException {
        //need to implement
		return new ArrayList<IOrderItem>();
    }
    
    Order getOrderData(String orderId) throws DatabaseException {
		//need to implement
    	return new Order(1,"","");
    }
    
    
    
 

        
 
}

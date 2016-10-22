
package business.ordersubsystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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


	@Override
	public List<IOrder> getOrderHistory() throws DatabaseException {
		List<IOrder> orderList = new ArrayList<IOrder>();
		DbClassOrder dbOrder = new DbClassOrder();
		List<String> orderIds = dbOrder.getAllOrderIds(custProfile);
		for(String orderId:orderIds){
			IOrder order = dbOrder.getOrderData(orderId);
			orderList.add(order);
		}
		return orderList;
	}


	@Override
	public void submitOrder(IShoppingCart shopCart) throws DatabaseException {
		List<IOrderItem> orderItemList = new ArrayList<IOrderItem>();
		for(ICartItem carItem:shopCart.getCartItems()){
			IOrderItem orderItem = OrderUtil.createOrderItemFromCartItem(carItem, null);
			orderItemList.add(orderItem);
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String orderDate = df.format(new Date());
		String totalPrice = OrderUtil.computeTotalPrice(orderItemList);
		Order order = new Order(null, orderDate, totalPrice);
		order.setShipAddress(shopCart.getShippingAddress());
		order.setBillAddress(shopCart.getBillingAddress());
		order.setCreditCard(shopCart.getPaymentInfo());
		order.setOrderItems(orderItemList);
		
		DbClassOrder dbClass = new DbClassOrder();
		Integer orderId = dbClass.submitOrder(order, custProfile);
		order.setOrderId(orderId);
		for(IOrderItem orderItem:order.getOrderItems()){
			orderItem.setOrderid(orderId);
			dbClass = new DbClassOrder();
			dbClass.submitOrderItem(orderItem, orderId);
		}
	}


	@Override
	public IOrderItem createOrderItem(Integer prodId, Integer orderId, String quantityReq, String totalPrice) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IOrder createOrder(Integer orderId, String orderDate, String totalPrice) {
		// TODO Auto-generated method stub
		return null;
	}
 
}

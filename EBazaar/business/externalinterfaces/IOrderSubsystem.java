
package business.externalinterfaces;

import java.util.List;

import middleware.DatabaseException;


public interface IOrderSubsystem {
	/** used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade */
    List<IOrder> getOrderHistory() throws DatabaseException;
	
	/** used by customer subsystem when a final order is submitted; this method extracts order and order items
	 * from the passed in shopping cart and saves to database using data access subsystem
	 */ 
    void submitOrder(IShoppingCart shopCart) throws DatabaseException;
	
	/** used whenever an order item needs to be created from outside the order subsystem */
    IOrderItem createOrderItem(Integer prodId,Integer orderId, String quantityReq, String totalPrice);
    
    /** to create an Order object from outside the subsystem */
    IOrder createOrder(Integer orderId, String orderDate, String totalPrice);
}

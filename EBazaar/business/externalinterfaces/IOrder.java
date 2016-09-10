
package business.externalinterfaces;

import java.util.List;


public interface IOrder {
    public List<IOrderItem> getOrderItems();    
	public String getOrderDate();		
	public Integer getOrderId();		
	public String getTotalPrice();
    public IAddress getShipAddress();
    public IAddress getBillAddress();
    public ICreditCard getPaymentInfo();
    
 
	
}




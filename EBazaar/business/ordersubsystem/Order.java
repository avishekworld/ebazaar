
package business.ordersubsystem;

import java.util.List;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;


class Order implements IOrder{
    private Integer orderId;
    private String orderDate;
    private String totalPrice;
    private List<IOrderItem> orderItems;
	private IAddress shipAddress;
	private IAddress billAddress;
	private ICreditCard creditCard;    	
    
    Order(Integer orderId,String orderDate,String totalPrice){
        
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }
    public void setOrderItems(List<IOrderItem> orderItems){
    	this.orderItems = orderItems;
    }
    public List<IOrderItem> getOrderItems(){
        return orderItems;
    }

	public String getOrderDate() {
		return orderDate;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

    /**
     * @return Returns the billAddress.
     */
    public IAddress getBillAddress() {
        return billAddress;
    }
    /**
     * @param billAddress The billAddress to set.
     */
    public void setBillAddress(IAddress billAddress) {
        this.billAddress = billAddress;
    }
 
    /**
     * @param creditCard The creditCard to set.
     */
    public void setCreditCard(ICreditCard creditCard) {
        this.creditCard = creditCard;
    }
    /**
     * @return Returns the shipAddress.
     */
    public IAddress getShipAddress() {
        return shipAddress;
    }
    /**
     * @param shipAddress The shipAddress to set.
     */
    public void setShipAddress(IAddress shipAddress) {
        this.shipAddress = shipAddress;
    }

    public ICreditCard getPaymentInfo() {
 
        return creditCard;
    }

}

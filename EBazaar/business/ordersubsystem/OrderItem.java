package business.ordersubsystem;

import business.externalinterfaces.IOrderItem;


public class OrderItem implements IOrderItem {
    Integer lineitemid;
    Integer productid;
    Integer orderid;
    String quantity;
    String totalPrice;
    
    /** Used for reading data from database */
    public OrderItem(Integer lineitemid, Integer productid, Integer orderid, String quantity, String totalPrice){
        this.lineitemid = lineitemid;
        this.productid = productid;
        this.orderid = orderid;
        this.quantity = quantity;
        this.totalPrice= totalPrice;
    }
    
    /** Used for creating order item to send to dbase */
    public OrderItem(Integer productid, Integer orderid, String quantity, String totalPrice){
        
        this.productid = productid;
        this.orderid = orderid;
        this.quantity = quantity;
        this.totalPrice= totalPrice;
    } 
    
    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("lineitemid: <"+lineitemid+">,");
        buf.append("productid: <"+productid+">,");
        buf.append("orderid: <"+orderid+">,");
        buf.append("quantity: <"+quantity+">,");
        buf.append("totalPrice: <"+totalPrice+">");
        return buf.toString();
    }
    public void setLineItemId(Integer lid){
        lineitemid = lid;
    }
    
    /** When submitting an order, orderid is not known initially; after order level is submitted, 
     *  orderid can be read and inserted into orderitems
     */
    public void setOrderid(Integer orderid) {
    	this.orderid = orderid;
    }
    
    public Integer getLineitemid() {
        return lineitemid;
    }
    
    public Integer getProductid() {    
        return productid;
    }
    
    public Integer getOrderid() {       
        return orderid;
    }
    public String getQuantity() {      
        return quantity;
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }
}

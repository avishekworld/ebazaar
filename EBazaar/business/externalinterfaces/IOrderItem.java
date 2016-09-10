
package business.externalinterfaces;


public interface IOrderItem {
    public Integer getLineitemid();
    public Integer getProductid();
    public Integer getOrderid();
    public String getQuantity();
    public String getTotalPrice();
    public void setOrderid(Integer orderid);
    public void setLineItemId(Integer id);


}

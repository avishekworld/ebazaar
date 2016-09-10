package business.externalinterfaces;

public interface ICartItem {
	public boolean isAlreadySaved();
	public Integer getCartid();
	public Integer getLineitemid();
	public Integer getProductid();
	public String getProductName();
	public String getQuantity();
	public String getTotalprice();
}

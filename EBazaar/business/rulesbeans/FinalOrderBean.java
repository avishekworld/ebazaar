package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import middleware.DatabaseException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IShoppingCart;
import business.util.Pair;
import business.util.ShoppingCartUtil;

public class FinalOrderBean implements DynamicBean {
	//private static Logger log = Logger.getLogger(null);
    
	private IShoppingCart shopCart;
	public FinalOrderBean(IShoppingCart sc){		
		shopCart = sc;
	}
	
	
	///////bean interface for shopping cart
	
	public IAddress getShippingAddress() {
		return shopCart.getShippingAddress();
	}
    public IAddress getBillingAddress(){
		return shopCart.getBillingAddress();
	}
    public ICreditCard getPaymentInfo(){
		return shopCart.getPaymentInfo();
	}
    public List<ICartItem> getCartItems(){
		return shopCart.getCartItems();
	}
    /** 
     * This is a collection of pairs indicating
     * the quantity requested vs quantity avail
     * for each line item in the shopping cart
     * 
     * If return value is empty, this indicates an error condition
     * @return
     */
    
    public List<Pair> getRequestedAvailableList(){
    	List<Pair> retVal = new ArrayList<Pair>();
    
    	try {
    		retVal = ShoppingCartUtil.computeRequestedAvailableList(shopCart);
    	}
    	catch(DatabaseException ex) {
    		//log.warning("Unable to read database for handling requested/avail rule");
    	}
    	return retVal;
    }
    
    
    
	
	///////////property change listener code
    private PropertyChangeSupport pcs = 
    	new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl){
	 	pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl){	
    	pcs.removePropertyChangeListener(pcl);
    }
}

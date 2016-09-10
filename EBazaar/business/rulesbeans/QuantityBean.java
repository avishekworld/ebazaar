package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import business.Quantity;
import business.externalinterfaces.DynamicBean;

public class QuantityBean implements DynamicBean{
	private Quantity quantity;
	public QuantityBean(Quantity quantity) {
		this.quantity= quantity;
	}
	
	//////////// bean interface for address
	public String getQuantityAvailable() {
		return quantity.getQuantityAvailable();
	}
 
    public String getQuantityRequested(){
        return quantity.getQuantityRequested();
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

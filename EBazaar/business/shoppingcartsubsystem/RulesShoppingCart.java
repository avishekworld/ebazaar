package business.shoppingcartsubsystem;

import java.util.HashMap;
import java.util.List;

import middleware.EBazaarException;
import business.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import business.externalinterfaces.IShoppingCart;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.PaymentBean;
import business.rulesbeans.ShopCartBean;
import business.rulesubsystem.RulesSubsystemFacade;

public class RulesShoppingCart implements IRules{
	
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;	
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesShoppingCart(IShoppingCart shoppingCart){
		bean = new ShopCartBean(shoppingCart);
		
		//don't call prepare data here -- we don't know 
		//what else might need to be done first
		//let the RulesSubsystem make that call
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.SHOPCART_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.SHOPCART_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.SHOPCART_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
		
	}
	public void runRules() throws EBazaarException, RuleException{
    	IRulesSubsystem rules = new RulesSubsystemFacade();
    	rules.runRules(this);
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}
	/* expect a list of address values, in order
	 * street, city, state ,zip
	 */
	public void populateEntities(List<String> updates){
		//do nothing
		
	}
	
	public List getUpdates() {
		//do nothing
		return null;
	}
		
}

package business.customersubsystem;

import java.util.*;
import middleware.EBazaarException;
import business.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.PaymentBean;
import business.rulesubsystem.RulesSubsystemFacade;

public class RulesPayment implements IRules {
	
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;	
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesPayment(IAddress address, ICreditCard creditCard){
		bean = new PaymentBean(address,creditCard);
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.PAYMENT_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.PAYMENT_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.PAYMENT_DEFTEMPLATE.getVal());
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

package business.customersubsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import middleware.EBazaarException;

import business.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.AddressBean;
import business.rulesubsystem.RulesSubsystemFacade;

class RulesAddress implements IRules {
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;
	private Address updatedAddress;
	private RulesConfigProperties config = new RulesConfigProperties();
	
	RulesAddress(IAddress address){
		bean = new AddressBean(address);
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.ADDRESS_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.ADDRESS_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();	
		String deftemplate = config.getProperty(RulesConfigKey.ADDRESS_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
		
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}
	
	public void runRules() throws EBazaarException, RuleException {
		IRulesSubsystem rules = new RulesSubsystemFacade();
		rules.runRules(this);		
	}
	/* expect a list of address values, in order
	 * street, city, state ,zip
	 */
	public void populateEntities(List<String> updates){
		updatedAddress = new Address(updates.get(0),
				updates.get(1),
				updates.get(2),
				updates.get(3));
		
	}
	
	public List<Address> getUpdates() {
		List<Address> retVal = new ArrayList<Address>();
		retVal.add(updatedAddress);
		return retVal;
	}
	


}

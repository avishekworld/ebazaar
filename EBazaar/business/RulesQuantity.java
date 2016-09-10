package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import middleware.EBazaarException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.QuantityBean;
import business.rulesubsystem.RulesSubsystemFacade;

public class RulesQuantity implements IRules {

	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesQuantity(Quantity quantity){
		bean = new QuantityBean(quantity);
	}
	public String getModuleName() {
		return config.getProperty(RulesConfigKey.QUANTITY_MODULE.getVal());
	}

	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.QUANTITY_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.QUANTITY_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
	}
	public void runRules() throws EBazaarException, RuleException{
    	IRulesSubsystem rules = new RulesSubsystemFacade();
    	rules.runRules(this);
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}

	@SuppressWarnings("unchecked")
	public List getUpdates() {
		// nothing to do
		return new ArrayList();
	}

	public void populateEntities(List<String> updates) {
		// nothing to do
		
	}




}

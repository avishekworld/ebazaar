package business.externalinterfaces;
import java.util.*;

import business.RuleException;

import middleware.EBazaarException;
public interface IRules  {
	String getModuleName();
	String getRulesFile();
	void prepareData();
	HashMap<String,DynamicBean> getTable();
	void runRules() throws EBazaarException, RuleException;
	void populateEntities(List<String> updates);
	//updates are placed in a List -- object types may vary
	List getUpdates();

}

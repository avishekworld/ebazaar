package business.externalinterfaces;

import middleware.EBazaarException;
import business.RuleException;

public interface IRulesSubsystem {
	public void runRules(IRules rulesIface) throws EBazaarException,RuleException;
	
}

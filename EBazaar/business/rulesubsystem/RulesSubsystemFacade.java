package business.rulesubsystem;

import java.io.*;
import java.util.List;

import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;
import business.ParseException;
import business.RuleException;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import middleware.EBazaarException;

public class RulesSubsystemFacade implements IRulesSubsystem {
	
	public void runRules(IRules rulesIface) throws EBazaarException,RuleException {
		rulesIface.prepareData();
		ReteWrapper wrapper = new ReteWrapper();
		String nameOfRulesFile = rulesIface.getRulesFile();
		File rulesFile =new File(nameOfRulesFile);
		
		//String rulesAsString = readFile(nameOfRulesFile);
		
		wrapper.setTable(rulesIface.getTable());
		wrapper.setCurrentModule(rulesIface.getModuleName());
		try {
			wrapper.setRulesAsString(rulesFile, false);
			wrapper.runRules();
			List<String> updates = wrapper.getUpdates();
			rulesIface.populateEntities(updates);
		}
		catch(IOException iox) {
			throw new EBazaarException(iox.getMessage());
		}
		catch(OperatingException ox){
			throw new EBazaarException(ox.getMessage());
		}
		catch(ValidationException vx){
			throw new RuleException(vx.getMessage());
		}
	}
	/* this is used if the rules are accessible from this project are not
	 * not encrypted -- not used in EBazaar
	 */
	String readFile(String filename) throws ParseException {
		String theString = null;
		String newline = System.getProperty("line.separator");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while( (line = reader.readLine()) != null){
				sb.append(line + newline);
			}
			theString = sb.toString();
			
		}
		catch(IOException e) {
			
			throw new ParseException(e.getMessage());
			
		}
		return theString;		
	}
	
}

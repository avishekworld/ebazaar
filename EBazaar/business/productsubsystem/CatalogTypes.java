package business.productsubsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import business.externalinterfaces.ICatalogTypes;

/**
 * @author pcorazza
 * <p>
 * Class Description: This is a bean...it just holds
 * data in memory. It is not the entity class for a catalog --
 * it is just reference data, stored in memory.
 */
public class CatalogTypes implements ICatalogTypes {
    HashMap<Integer,String> catalogIdToName = new HashMap<Integer,String>();
    HashMap<String,Integer> catalogNameToId = new HashMap<String,Integer>();
    public List<String[]> getCatalogNames() {
    	List<String[]> retVal = new ArrayList<String[]>();
    	Collection<String> vals = catalogIdToName.values();
    	for(String s : vals){
    		retVal.add(new String[]{s});
    	}
    	return retVal;
 
        
    }
    public String getCatalogName(Integer id){
        return catalogIdToName.get(id);
    }

    public void addCatalog(Integer id, String name) {
        catalogIdToName.put(id,name);
        catalogNameToId.put(name,id);
    }
    public Integer getCatalogId(String name) {
        return catalogNameToId.get(name);
        
        
    }
	
}

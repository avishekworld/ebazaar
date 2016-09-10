
package business.util;

import java.util.LinkedList;
import java.util.List;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;


public class CustomerUtil {
    public static String[] creditCardToStringArray(ICreditCard cc){
        String[] retVal = new String[4];
        retVal[0] = cc.getNameOnCard();
        retVal[1] = cc.getCardNum();
        retVal[2] = cc.getCardType();
        retVal[3] = cc.getExpirationDate();
        return retVal;
        
    }
    public static  String[] addressToStringArray(IAddress addr){
        String[] retVal = new String[4];
        retVal[0]= addr.getStreet1();
        retVal[1]= addr.getCity();
        retVal[2] = addr.getState();
        retVal[3] = addr.getZip();
        return retVal;
    }
    
    public static List<String[]> addrListToListOfArrays(List<IAddress> addresses){
        List<String[]> addressesAsArrays = new LinkedList<String[]>();
        if(addresses != null){
			for(IAddress a : addresses){
				addressesAsArrays.add(addressToStringArray(a));
			}
        }  
        return addressesAsArrays;
    }
}

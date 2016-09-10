package business.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import middleware.DatabaseException;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IShoppingCart;
import business.productsubsystem.ProductSubsystemFacade;


public class ShoppingCartUtil {

    public static List<String[]> makeDisplayableList(List<ICartItem> cartItems) {
        if(cartItems == null) return null;
        List<String[]> returnValue = new LinkedList<String[]>();
        for(ICartItem item : cartItems){
            if(!item.getQuantity().equals("0")){
                returnValue.add(createDisplay(item));
            }
        }
        return returnValue;
        
    }
    
    public static String[] createDisplay(ICartItem item) {
        if(item == null) return null;
        final int NAME=0;
        final int QUANTITY = 1;
        final int UNIT_PRICE = 2;
        final int TOTAL_PRICE = 3;
        String[] returnValue = new String[4];
        returnValue[NAME] = item.getProductName();
        String q = item.getQuantity();
        returnValue[QUANTITY] = q;
        String total =item.getTotalprice();
        returnValue[TOTAL_PRICE]=total;
        returnValue[UNIT_PRICE] = StringParse.divideDoubles(total,q);
        return returnValue;      
    }
    
    /** This is used in the FinalOrderBean to see if, in the final order, for any of the
     *  items in the cart, the quantity requested exceeds the quantity available.
     *  This method returns a list of Pairs, where each pair corresponds to a cart item
     *  and consists of a quantity requested
     *  and a quantity available 
     */
    public static List<Pair> computeRequestedAvailableList(IShoppingCart shoppingCart) throws DatabaseException{
    	//implement
    	return new ArrayList<Pair>();
    	
    }
    
    /**
     * Returns a list of string arrays, where each String array conforms to the sequence of
     * values as they occur in the CartItemsWindow table model.
     */
    public static List<String[]> cartItemsToStringArrays(List<ICartItem> items) {
		 
		List<String[]> newData = new ArrayList<String[]>();
		for(ICartItem item : items) {
			String unitPrice = StringParse.divideDoubles(item.getTotalprice(), item.getQuantity());
			newData.add(new String[]{item.getProductName(),item.getQuantity(),unitPrice,item.getTotalprice()});
					//item.getProductid(),item.getLineitemid(),item.getCartid()});
		}
		return newData;
	}

}

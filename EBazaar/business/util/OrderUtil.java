package business.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import middleware.DatabaseException;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.externalinterfaces.IOrderSubsystem;
import business.externalinterfaces.IProductFromDb;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;

public class OrderUtil { 
    public static String STANDARD_DATE_FORMAT = "MM/dd/yyyy";
    public static String todaysDateStr(){
        Date d = new Date();
        DateFormat f = new SimpleDateFormat(STANDARD_DATE_FORMAT);
        return f.format(d);       
    }
    /** 
     * convenience method that uses createOrderItemFromCartItem and
     * computeTotalPrice(List orderItems), used for quickly assessing
     * total price in preparing data for Credit Verif system.
     * 
     */
    public static String quickComputeTotalPrice(List<ICartItem> items) {
    	List<IOrderItem> list = new ArrayList<IOrderItem>();
    	for (ICartItem item : items) {
    		list.add(createOrderItemFromCartItem(item, null));
    	}
    	return computeTotalPrice(list);
    }
    public static IOrderItem createOrderItemFromCartItem(ICartItem item, Integer orderId) {
        //IMPLEMENT
        return null;        
    }
    
    public static String computeTotalPrice(List<IOrderItem> orderItems){
        String totalprice = "0";
        if(orderItems != null){
			for(IOrderItem item : orderItems){
				totalprice = StringParse.addDoubles(totalprice,item.getTotalPrice());
			}
        }
        return totalprice;
    }
    public static List<String[]> makeItemsDisplayable(List<IOrderItem> orderItems) throws DatabaseException  {
        if(orderItems == null) return new LinkedList<String[]>();
        final int NAME = 0;
        final int QUANTITY = 1;
        final int UNIT_PRICE = 2;
        final int TOTAL_PRICE = 3;
        TwoKeyHashMap<Integer,String,IProductFromDb> productTable = (new ProductSubsystemFacade()).getProductTable();
        List<String[]> returnVal = new LinkedList<String[]>();
        String[] displayableData = null;
        String totalPrice = null;
        String nextQuantity =null;
        String prodName = null;
        Integer nextProdId = null;
        IProductFromDb nextProduct = null;
		for(IOrderItem nextItem : orderItems){
            displayableData = new String[4];
            nextProdId = nextItem.getProductid();
            nextProduct = productTable.getValWithFirstKey(nextProdId);
            displayableData[NAME] = nextProduct.getProductName();
            nextQuantity = nextItem.getQuantity();
            displayableData[QUANTITY]= nextQuantity;
            totalPrice = nextItem.getTotalPrice();
            displayableData[TOTAL_PRICE] = totalPrice;
            displayableData[UNIT_PRICE]= 
                StringParse.divideDoubles(totalPrice, nextQuantity);
            returnVal.add(displayableData);
        }
        return returnVal;
        
    }
    
    /**
     * Makes order data from lower layers available to the GUI, so all values
     * are converted to Strings
     */
    public static List<String[]> extractOrderData(List<IOrder> ordersList){
        //IMPLEMENT
    	return new ArrayList<String[]>();
        
    }

}

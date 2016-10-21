package business.util;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import business.externalinterfaces.IProductFromDb;


public class ProductUtil {
	
	public static final String[] FIELD_NAMES = {"Product Name","Price Per Unit","Mfg. Date","Quantity"};
	public static final int PRODUCT_NAME_INT = 0;
	public static final int PRICE_PER_UNIT_INT = 1;
	public static final int MFG_DATE_INT = 2;
	public static final int QUANTITY_INT = 3;
	public static final String CAT_GROUP = "Catalog Group"; 
	
    public static List<String[]> extractProductNames(List<IProductFromDb> prodList){
        final int PROD_NAME = 0;   
		List<String[]> returnValue = new LinkedList<String[]>();
        String[] nextArray = null;
        final int SIZE = 1;
        for(IProductFromDb prod : prodList){
            nextArray = new String[SIZE];
            nextArray[PROD_NAME]=prod.getProductName();
            returnValue.add(nextArray);
        }
        return returnValue;
        
    }
    //this is used in the browse and select use case
    public static String[] extractProdInfoForCust(IProductFromDb product){
        final int SIZE = 4;
        final int PROD_NAME = 0;
        final int UNIT_PRICE = 1;
        final int QUANTITY = 2;
        final int DESCRIPTION = 3;
        String[] returnValue = new String[SIZE];
        if(product != null){
            returnValue[PROD_NAME]=product.getProductName();
            returnValue[UNIT_PRICE]=product.getUnitPrice();
            returnValue[QUANTITY]=product.getQuantityAvail();
            returnValue[DESCRIPTION]=product.getDescription();
            
        }
        return returnValue;
        
    }
    public static List<String[]> extractProdInfoForCust(List<IProductFromDb> list) {
    	int size = list.size();
    	ArrayList retList = new ArrayList();
    	for(IProductFromDb prod : list) {
    		retList.add(extractProdInfoForCust(prod));
    	}
    	return retList;
    	
    }
    
    //this is used in the Product manager use case
    public static String[] extractProdInfoForManager(IProductFromDb product){
    	//IMPLEMENT
    	final int SIZE = 4;
        final int PROD_NAME = 0;
        final int UNIT_PRICE = 1;
        final int MGF_DATE = 2;
        final int QUANTITY = 3;
        String[] returnValue = new String[SIZE];
        if(product != null){
            returnValue[PROD_NAME]=product.getProductName();
            returnValue[UNIT_PRICE]=product.getUnitPrice();
            returnValue[MGF_DATE]=product.getMfgDate();
            returnValue[QUANTITY]=product.getQuantityAvail();
        }
        return returnValue;
    }
    public static List<String[]> extractProductInfoForManager(List<IProductFromDb> list) {
    	int size = list.size();
    	ArrayList retList = new ArrayList();
    	for(IProductFromDb prod : list) {
    		retList.add(extractProdInfoForManager(prod));
    	}
    	return retList;
    	
    }
}

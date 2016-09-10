package business.util;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import business.externalinterfaces.IProductFromDb;


public class ProductUtil {
	
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
    //this is used in the Product manager use case
    public static String[] extractProdInfoForManager(IProductFromDb product){
    	//IMPLEMENT
    	return new String[]{"product_name", "unit_price", "mgf_date", "quantity_avail"};
        
        
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

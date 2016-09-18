/*
 * Created on Mar 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package business.productsubsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xalan.internal.lib.ExsltStrings;

import business.DbClassQuantity;
import business.Quantity;
import business.RuleException;
import business.rulesbeans.QuantityBean;
import business.rulesubsystem.RulesSubsystemFacade;
import business.util.*;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.externalinterfaces.IProductSubsystem;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IRulesSubsystem;
import middleware.DatabaseException;
import middleware.EBazaarException;
import middleware.dataaccess.DataAccessUtil;

public class ProductSubsystemFacade implements IProductSubsystem {
	final String DEFAULT_PROD_DESCRIPTION="New Product";
	CatalogTypes types;
	public static final String BOOKS = "Books";
    public static final String CLOTHES = "Clothing";
    private static String[][] catalogTypes = 
		{{BOOKS},{CLOTHES}};
	
    public TwoKeyHashMap<Integer,String,IProductFromDb> getProductTable() throws DatabaseException {
        DbClassProduct dbClass = new DbClassProduct();
        return dbClass.readProductTable();
        
    }
	public TwoKeyHashMap<Integer,String,IProductFromDb> refreshProductTable() throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
        return dbClass.refreshProductTable();		
	}
	
	public void saveNewProduct(IProductFromGui product, String catalogType) throws DatabaseException {
		//get catalogid
		Integer catalogid = getCatalogIdFromType(catalogType); 
				//invent description
		String description = DEFAULT_PROD_DESCRIPTION;
		DbClassProduct dbclass = new DbClassProduct();
		dbclass.saveNewProduct(product, catalogid,description);
		
	}
	/* reads quantity avail and stores in the Quantity argument */
	public void readQuantityAvailable(String prodName, Quantity quantity) throws DatabaseException {
		DbClassQuantity dbclass = new DbClassQuantity();
		dbclass.setQuantity(quantity);
		dbclass.readQuantityAvail(prodName);
				
	}
	public Integer getCatalogIdFromType(String catType) throws DatabaseException {
		return 0;
	}
	
	@Override
	public List<String[]> getCatalogNames() throws DatabaseException {
		return Arrays.asList(catalogTypes); 
	}
	@Override
	public List<String[]> refreshCatalogNames() throws DatabaseException {
		return getCatalogNames();
	}
	//This is needed by ComboListener in ManageProductsController. When you have implemented this,
		//you can remove comments from body of ComboListener.
	@Override
	public List<IProductFromDb> getProductList(String catType) throws DatabaseException {
		List<IProductFromDb> productList = new ArrayList<IProductFromDb>();
		if(catType.equals(BOOKS)){
			productList.add(new Product(null,"Gone with the Wind","20","12.00","10-12-2001",null,"This is a good book"));
			productList.add(new Product(null,"Messiah of Dune","100","43.00","05-10-2001",null,"This is a good book"));
			productList.add(new Product(null,"Garden of Rama","30","52.00","10-12-1991",null,"This is a good book"));
		}else{
			productList.add(new Product(null,"Pants","20","12.00","10-12-2001",null,"This is a good cloth"));
			productList.add(new Product(null,"T-Shirts","100","43.00","05-10-2001",null,"This is a good cloth"));
			productList.add(new Product(null,"Skirts","30","52.00","10-12-1991",null,"This is a good cloth"));
		}
		return productList;
	}
	@Override
	public List<IProductFromDb> refreshProductList(String catType) throws DatabaseException {
		return getProductList(catType);
	}
	@Override
	public Integer getProductIdFromName(String prodName) throws DatabaseException {
		return 0;
	}
	@Override
	public IProductFromDb getProduct(String prodName) throws DatabaseException {
		List<IProductFromDb> productList = new ArrayList<IProductFromDb>();
		productList.addAll(getProductList(BOOKS));
		productList.addAll(getProductList(CLOTHES));
		for(IProductFromDb prod:productList){
			if(prod.getProductName().equals(prodName))
				return prod;
		}
		return null;
	}
	@Override
	public IProductFromDb getProductFromId(String prodId) throws DatabaseException {
		DbClassProduct dbClass =new DbClassProduct();
		IProductFromDb product = dbClass.readProduct(Integer.parseInt(prodId));
		return product;
		//return new Product("Gone with the Wind","10-12-2001","20","12.00");
	}
	@Override
	public void saveNewCatalogName(String name) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public IProductFromGui createProduct(String name, String date, String numAvail, String unitPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	

}

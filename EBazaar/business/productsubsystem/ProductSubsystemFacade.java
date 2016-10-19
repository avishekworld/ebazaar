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
	CatalogTypes catagotyType;
	
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
		dbclass.saveNewProduct(product, catalogid, description);
		
	}
	/* reads quantity avail and stores in the Quantity argument */
	public void readQuantityAvailable(String prodName, Quantity quantity) throws DatabaseException {
		DbClassQuantity dbclass = new DbClassQuantity();
		dbclass.setQuantity(quantity);
		dbclass.readQuantityAvail(prodName);
				
	}
	public Integer getCatalogIdFromType(String catType) throws DatabaseException {
		DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
		Integer catalogId = dbClass.getCatalogIdFromType(catType);
		return catalogId;
	}
	@Override
	public Integer getProductIdFromName(String prodName) throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		Integer catalogId = dbClass.getProductIdFromName(prodName);
		return catalogId;
	}
	@Override
	public List<String[]> getCatalogNames() throws DatabaseException {
		DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
		List<String[]> cataLogName = dbClass.getCatalogNames();
		return cataLogName; 
	}
	
	@Override
	public List<String[]> refreshCatalogNames() throws DatabaseException {
		DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
		List<String[]> cataLogName = dbClass.refreshCatalogNames();
		return cataLogName; 
	}
	//This is needed by ComboListener in ManageProductsController. When you have implemented this,
		//you can remove comments from body of ComboListener.
	@Override
	public List<IProductFromDb> getProductList(String catType) throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		List<IProductFromDb> productList = dbClass.readProductList(getCatalogIdFromType(catType));
		return productList;
	}
	@Override
	public List<IProductFromDb> refreshProductList(String catType) throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		List<IProductFromDb> productList = dbClass.refreshProductList(Integer.parseInt(catType));
		return productList;
	}
	@Override
	public IProductFromDb getProduct(String prodName) throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		IProductFromDb product = dbClass.readProduct(getProductIdFromName(prodName));
		return product;
	}
	@Override
	public IProductFromDb getProductFromId(String prodId) throws DatabaseException {
		DbClassProduct dbClass =new DbClassProduct();
		IProductFromDb product = dbClass.readProduct(Integer.parseInt(prodId));
		return product;
	}
	@Override
	public void saveNewCatalogName(String catalogName) throws DatabaseException {
		DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
		dbClass.saveNewCatalog(catalogName);
	}
	@Override
	public IProductFromGui createProduct(String name, String date, String numAvail, String unitPrice) {
		return new Product("Sample Item", "10/18/2016", "08", "5.00");
	}

	

}

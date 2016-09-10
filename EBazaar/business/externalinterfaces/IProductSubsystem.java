
package business.externalinterfaces;
import java.util.List;

import business.Quantity;
import business.RuleException;
import business.util.TwoKeyHashMap;
import middleware.DatabaseException;
import middleware.EBazaarException;

public interface IProductSubsystem {

	/** retrieves a twokey hashmap that consists of all products keyed on both name and id */
	public TwoKeyHashMap<Integer,String,IProductFromDb> getProductTable() throws DatabaseException;

	/** same as getProductTable but forces a database read */
	public TwoKeyHashMap<Integer,String,IProductFromDb> refreshProductTable() throws DatabaseException;

	

	/** saves a new product obtained from user input */
	public void saveNewProduct(IProductFromGui product, String catalogType) throws DatabaseException;

	/** reads db to determine quantity available; stores in the Quantity argument */
	public void readQuantityAvailable(String prodName, Quantity quantity) throws DatabaseException;

	
}
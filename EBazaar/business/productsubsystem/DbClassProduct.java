
package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.util.TwoKeyHashMap;
import business.*;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import static business.util.StringParse.*;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.dataaccess.DataAccessUtil;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassProduct implements IDbClass {
	private static final Logger LOG = Logger.getLogger(DbClassProduct.class
			.getPackage().getName());
	private IDataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	/**
	 * The productTable matches product ID with Product object. It is static so
	 * that requests for "read product" based on product ID can be handled
	 * without extra db hits
	 */
	private static TwoKeyHashMap<Integer, String, IProductFromDb> productTable;
	private String queryType;
	private String query;
	private IProductFromDb product;
	private IProductFromGui prodFromGui;
	private List<IProductFromDb> productList;
	Integer catalogId;
	Integer productId;

	private final String LOAD_PROD_TABLE = "LoadProdTable";
	private final String READ_PRODUCT = "ReadProduct";
	private final String READ_PROD_LIST = "ReadProdList";
	

	public void buildQuery() {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			buildProdTableQuery();
		} else if (queryType.equals(READ_PRODUCT)) {
			buildReadProductQuery();
		} else if (queryType.equals(READ_PROD_LIST)) {
			buildProdListQuery();
		} 

	}

	private void buildProdTableQuery() {
		query = "SELECT * FROM product";
	}

	private void buildProdListQuery() {
		query = "SELECT * FROM Product WHERE catalogid = " + catalogId;
	}

	private void buildReadProductQuery() {
		query = "SELECT * FROM Product WHERE productid = " + productId;
	}


	public TwoKeyHashMap<Integer, String, IProductFromDb> readProductTable()
			throws DatabaseException {
		if (productTable != null) {
			return productTable.clone();
		}
		return refreshProductTable();
	}

	/**
	 * Force a database call
	 */
	public TwoKeyHashMap<Integer, String, IProductFromDb> refreshProductTable()
			throws DatabaseException {
		queryType = LOAD_PROD_TABLE;
		dataAccessSS.atomicRead(this);

		// Return a clone since productTable must not be corrupted
		return productTable.clone();
	}

	public List<IProductFromDb> readProductList(Integer catalogId)
			throws DatabaseException {
		if (productList == null) {
			return refreshProductList(catalogId);
		}
		return Collections.unmodifiableList(productList);
	}

	public List<IProductFromDb> refreshProductList(Integer catalogId)
			throws DatabaseException {
		this.catalogId = catalogId;
		queryType = READ_PROD_LIST;
		dataAccessSS.atomicRead(this);
		return productList;
	}

	public IProductFromDb readProduct(Integer productId)
			throws DatabaseException {
		if (productTable != null && productTable.isAFirstKey(productId)) {
			return productTable.getValWithFirstKey(productId);
		}
		queryType = READ_PRODUCT;
		this.productId = productId;
		dataAccessSS.createConnection(this);
		dataAccessSS.read();
		return product;
	}

	/**
	 * Database columns: productid, productname, totalquantity, priceperunit,
	 * mfgdate, catalogid, description
	 */
	public void saveNewProduct(IProductFromGui product, Integer catalogid,
			String description) throws DatabaseException {
		//IMPLEMENT
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			populateProdTable(resultSet);
		} else if (queryType.equals(READ_PRODUCT)) {
			populateProduct(resultSet);
		} else if (queryType.equals(READ_PROD_LIST)) {
			populateProdList(resultSet);
		}
	}

	private void populateProdList(ResultSet rs) throws DatabaseException {
		productList = new LinkedList<IProductFromDb>();
		/*
		try {
			IProductFromDb product = null;
			Integer prodId = null;
			String productName = null;
			String quantityAvail = null;
			String unitPrice = null;
			String mfgDate = null;
			Integer catalogId = null;
			String description = null;
			while (rs.next()) {
				prodId = rs.getInt("productid");
				productName = rs.getString("productname");
				quantityAvail = makeString(rs.getInt("totalquantity"));
				unitPrice = makeString(rs.getDouble("priceperunit"));
				mfgDate = rs.getString("mfgdate");
				catalogId = rs.getInt("catalogid");
				description = rs.getString("description");
				product = new Product(prodId, productName, quantityAvail,
						unitPrice, mfgDate, catalogId, description);
				productList.add(product);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} */
	}

	/**
	 * Internal method to ensure that product table is up to date.
	 */
	private void populateProdTable(ResultSet rs) throws DatabaseException {
		productTable = new TwoKeyHashMap<Integer, String, IProductFromDb>();
		
	}

	private void populateProduct(ResultSet rs) throws DatabaseException {
		product = new Product(1,"","","","",1,"");
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}
}

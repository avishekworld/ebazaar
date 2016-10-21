
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
	private String productName;
	private String productDescription;
	private Integer catalogId;
	private Integer productId;

	private static final String LOAD_PROD_TABLE = "LoadProdTable";
	private static final String READ_PRODUCT = "ReadProduct";
	private static final String READ_PROD_LIST = "ReadProdList";
	private static final String GET_PRODUCT_ID = "GetProductId";
	private final String SAVE = "Save";

	public void buildQuery() {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			buildProdTableQuery();
		} else if (queryType.equals(READ_PRODUCT)) {
			buildReadProductQuery();
		} else if (queryType.equals(READ_PROD_LIST)) {
			buildProdListQuery();
		} else if (queryType.equals(GET_PRODUCT_ID)) {
			buildGetProductIdQuery();
		}else if(queryType.equals(SAVE)){
        	buildSaveProductQuery();
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
	
	void buildGetProductIdQuery() {
        query = "SELECT productid FROM Product where productname='" + productName+ "'";       
    }
	
	void buildSaveProductQuery() {
        query = "insert into product values (null," + catalogId+ ",'" + 
        		prodFromGui.getProductName() + "'," +   
        		prodFromGui.getQuantityAvail() + "," +  
        		prodFromGui.getUnitPrice() + ",'" +
        		prodFromGui.getMfgDate() + "', '"+ productDescription+"')";
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
	
	public Integer getProductIdFromName(String productName) throws DatabaseException{
    	this.productName = productName;
    	queryType = GET_PRODUCT_ID;
		dataAccessSS.atomicRead(this);
		return productId;
    }

	/**
	 * Database columns: productid, productname, totalquantity, priceperunit,
	 * mfgdate, catalogid, description
	 */
	public void saveNewProduct(IProductFromGui product, Integer catalogid,
			String description) throws DatabaseException {
		prodFromGui = product;
		this.catalogId = catalogid;
		productDescription = description;
		queryType = SAVE;
		dataAccessSS.saveWithinTransaction(this);
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			populateProdTable(resultSet);
		} else if (queryType.equals(READ_PRODUCT)) {
			populateProduct(resultSet);
		} else if (queryType.equals(READ_PROD_LIST)) {
			populateProdList(resultSet);
		} else if (queryType.equals(GET_PRODUCT_ID)) {
			populateProductId(resultSet);
		}
	}

	private void populateProdList(ResultSet rs) throws DatabaseException {
		productList = new LinkedList<IProductFromDb>();
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
			throw new DatabaseException("Unable to read product list");
		} 
	}

	/**
	 * Internal method to ensure that product table is up to date.
	 */
	private void populateProdTable(ResultSet rs) throws DatabaseException {
		productTable = new TwoKeyHashMap<Integer, String, IProductFromDb>();
		if(rs != null){
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
					productTable.put(prodId, productName, product);
				}
			} catch (SQLException e) {
				throw new DatabaseException("Unable to create helper product table");
			}
		}
	}

	private void populateProduct(ResultSet rs) throws DatabaseException {
		try {
			if(rs!=null){
				rs.first();
				int id = rs.getInt("productid");
				int cid = rs.getInt("catalogid");
				String name = rs.getString("productname");
				double price = rs.getDouble("priceperunit");
				int quantity = rs.getInt("totalquantity");
				String description = rs.getString("description");
				String date = rs.getString("mfgdate");
				product = new Product(id, name, quantity+"", price+"", date, cid, description);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			product = new Product(1,"","","","",1,"");
			throw new DatabaseException("Unable to get product details");
		}
	}
	
	private void populateProductId(ResultSet rs) throws DatabaseException {
		try {
			if(rs!=null){
				rs.first();
				productId= rs.getInt("productid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("Unable to get product id");
		}
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}
}

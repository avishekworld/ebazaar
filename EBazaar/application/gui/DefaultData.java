package application.gui;
import java.util.*;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This class stores "dummy data" for the Gui. In the final project,
 *  this class should not be used, but should be replaced by appropriate
 *  accesses of the real data. <br>
 *  The class is a singleton. Only one instance is ever used. Also, no
 *  writes to the class ever occur, so it is thread-safe.
 * <p>
 * <table border="1">
 * <tr>
 * 		<th colspan="3">Change Log</th>
 * </tr>
 * <tr>
 * 		<th>Date</th> <th>Author</th> <th>Change</th>
 * </tr>
 * <tr>
 * 		<td>Oct 22, 2004</td>
 *      <td>klevi, pcorazza</td>
 *      <td>New class file</td>
 * </tr>
 * <tr>
 * 		<td>19 jan 2005</td>
 *      <td>klevi</td>
 *      <td>modified class comment</td>
 * </tr>
 * </table>
 *
 */
public class DefaultData {
	
	
	public static DefaultData instance;
	private DefaultData() {
		//private constructor; must be accessed with getInstance method
	}
	
	/**
	 * This method provides the only avenue for obtaining an instance
	 * of this singleton class.
	 * @return DefaultData
	 */
	public static DefaultData getInstance() {
		if(instance==null) {
			instance = new DefaultData();
		}
		return instance;
	}
	
	
	/////////////names
	public final static String MESSIAH_OF_DUNE = "Messiah Of Dune";	
	public final static String GONE_WITH_THE_WIND = "Gone With The Wind";
	public final static String GARDEN_OF_RAMA = "Garden of Rama";	
	public final static String PANTS = "Pants";
	public final static String TSHIRTS = "T-Shirts";
    public final static String SKIRTS = "Skirts";
    public static final String BOOKS = "Books";
    public static final String CLOTHES = "Clothing";	
    
    //field names for MaintainProductCatalog and AddEditProduct; enumerated constants used for accessing
    //fieldNames array dynamically
    public static final String[] FIELD_NAMES = {"Product Name","Price Per Unit","Mfg. Date","Quantity"};
	public static final int PRODUCT_NAME_INT = 0;
	
	public static final int PRICE_PER_UNIT_INT = 1;
	public static final int MFG_DATE_INT = 2;
	public static final int QUANTITY_INT = 3;
	public static final String CAT_GROUP = "Catalog Group";    
	
	
	//////////////CartItems default data
	private String[][] cartItemsData = 
    	{{GARDEN_OF_RAMA,"2","50","100"},
    	{PANTS,"1","25","25"},
    	{TSHIRTS,"4","15","40"}};
	//private List cartItemsList = null;
		
    	
    public List<String[]> getCartItemsData() {
    	return Arrays.asList(cartItemsData);
    }
   
    
    /////////////ProductListWindow default data

	
	/** each row of this 2D array represents a row in the books table */
	private final static String[][] DEFAULT_BOOKS = {{GONE_WITH_THE_WIND},
		                                                {MESSIAH_OF_DUNE},
		                                                {GARDEN_OF_RAMA }};
	/** Each row of this 2D array represents a row in the clothes table */		                                                
	private final static String[][] DEFAULT_CLOTHES = {{PANTS},
		                                                {TSHIRTS},
		                                                {SKIRTS}};		                                                
		
	//simulates database tables
	private static HashMap<String, String[][]> catalogWindowData = new HashMap<String, String[][]>();
	

	public List<String[]> getCatalogWindowData(String catalog) {
		return Arrays.asList(catalogWindowData.get(catalog));
	}
	
	//initialize the HashMap before an instance is created so that the data is immediately available
	static {
		catalogWindowData.put(BOOKS,DEFAULT_BOOKS);
		catalogWindowData.put(CLOTHES, DEFAULT_CLOTHES); 
	}

	
	
	//////////////////ProductDetailsWindow data
	//this data is read by ProductListWindow and inserted into 
	//the constructor of ProductDetailsWindow
    

	private final static String GONE_REVIEW = "A moving classic that tells a tale of love and a tale of courage.";
	private final static String [] GONE_PARAMS = {GONE_WITH_THE_WIND,"12.00","20",GONE_REVIEW};
	
	
	private final static String MESSIAH_REVIEW = "You saw how good Dune was. This is Part 2 of this unforgettable trilogy.";
	private final static String [] MESSIAH_PARAMS = {MESSIAH_OF_DUNE,"43.00","100",MESSIAH_REVIEW};
	
	
	private final static String GARDEN_REVIEW = "Highly acclaimed Book of Isaac Asimov. A real nail-biter.";
	private final static String [] GARDEN_PARAMS = {GARDEN_OF_RAMA,"52.00","30",GARDEN_REVIEW};
	
	
	private final static String PANTS_REVIEW = "I've seen the Grand Canyon. I've camped at Yellowstone. But nothing on earth compares to the glory of wearing these pants.";
	private final static String [] PANTS_PARAMS = {PANTS,"12.00","20",PANTS_REVIEW};
	

	private final static String T_REVIEW = "Can be worn by men or women. Always in style.";
	private final static String [] T_PARAMS = {TSHIRTS,"43.00","100",T_REVIEW};
	
	
	private final static String SKIRTS_REVIEW = "Once this brand of skirts becomes well-known, watch out!";
	private final static String [] SKIRTS_PARAMS = {SKIRTS,"52.00","30",SKIRTS_REVIEW};

	//simulates a database table
	private static HashMap<String,String[]> productDetailsData = new HashMap<String,String[]>();
	
	//initialize the HashMap before an instance is created so that the data is immediately available
	static {	
		productDetailsData.put(GONE_WITH_THE_WIND,GONE_PARAMS); 
		productDetailsData.put(MESSIAH_OF_DUNE,MESSIAH_PARAMS); 
		productDetailsData.put(GARDEN_OF_RAMA,GARDEN_PARAMS);
		productDetailsData.put(PANTS, PANTS_PARAMS);
		productDetailsData.put(TSHIRTS,T_PARAMS); 
		productDetailsData.put(SKIRTS,SKIRTS_PARAMS);
	}
	public HashMap<String,String[]> getProductDetailsData() {
		return productDetailsData;
	}
	public String[] getProductDetailsData(String type){
		return productDetailsData.get(type);
	}
	
	
	//////////////SelectOrder default data
	
	private String[][] selectOrderDefaultData = 
		{{"XB13425", "May 1, 2003", "$300.00" },
		 {"ZZ13425", "May 2, 2003", "$400.00" },	
		 {"BB13425", "May 3, 2003", "$500.00"  }};
	
	public List<String[]> getSelectOrderDefaultData() {
		return Arrays.asList(selectOrderDefaultData);
	}
	
	/////////////ViewOrderDetails default data
	private String[][] viewOrderDetailsDefaultData = 
		{{"Shirt", "25.00", "2", "50.00"},
		 {"Belt", "18.50", "1", "18.50"},	
		 {"Extreme Programming Guide", "22.00", "4", "88.00"}};
	
	public List<String[]> getViewOrderDetailsDefaultData() {
		return Arrays.asList(viewOrderDetailsDefaultData);
	}
	
	
	////////////MaintainProductCatalog data	
	
	private static String[][] prodCatalogBooks =
		{{"Gone with the Wind","12.00","10-12-2001","20"},
		 {"Messiah of Dune","43.00","05-10-2001","100"},
		 {"Garden of Rama","52.00","10-12-1991","30"}};
	private static String[][] prodCatalogClothes =
		{{"Pants","12.00","10-12-2001","20"},
		 {"T-Shirts","43.00","05-10-2001","100"},
		 {"Skirts","52.00","10-12-1991","30"}};

	private static HashMap<String,String[][]> productCatalogChoices;
	
	static {
		productCatalogChoices = new HashMap<String,String[][]>();
		productCatalogChoices.put(BOOKS,prodCatalogBooks);
		productCatalogChoices.put(CLOTHES,prodCatalogClothes);
	}
	
	public List<String[]> getProductCatalogChoices(String catalogType) {
		return Arrays.asList(productCatalogChoices.get(catalogType));
	}
	
	
	///////////MaintainCatalogTypes data
	
	private static String[][] catalogTypes = 
		{{BOOKS},{CLOTHES}};
		
	public static List<String[]> getCatalogTypes() {
		return Arrays.asList(catalogTypes);
	}
	
	
	////////////ShipAddressesWindow data
	private static String[][] shipAddresses = 
 		{{"1607 Granville Ave",
 		 "Fairfield",
 		 "IA",
 		 "52556"}};
 	public List<String[]> getShipAddresses() {
 		return Arrays.asList(shipAddresses);
 	}
 	public static int STREET_INT =0;
 	public static int CITY_INT = 1;
 	public static int STATE_INT = 2;
 	public static int ZIP_INT = 3;

	
	/////////////FinalOrder data
    private String[][] finalOrderData = 
	{{"Garden of Rama","2","50","100"},
	{"Pants","1","25","25"}};
    
    public List<String[]> getFinalOrderData() {
    	return Arrays.asList(finalOrderData);
    }
    
    
    ////////////CatalogListWindow data
    private String [][] mainCatalogData = {{DefaultData.BOOKS},
            {DefaultData.CLOTHES}};	
    
    public String[][] getMainCatalogData() {
    	return mainCatalogData;
    
    }
    
    /////////// Credit card types for payment window
    public static final String[] CARD_TYPES = {"Visa", "MasterCard", "Discover"};
    
    
}
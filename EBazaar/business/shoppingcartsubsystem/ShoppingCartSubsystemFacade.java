
package business.shoppingcartsubsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.EBazaarException;
import business.RuleException;
import business.customersubsystem.RulesPayment;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IProductSubsystem;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IShoppingCart;
import business.externalinterfaces.IShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public class ShoppingCartSubsystemFacade implements IShoppingCartSubsystem {
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
    ShoppingCart liveCart;
    ShoppingCart savedCart;
    Integer shopCartId;
    ICustomerProfile customerProfile;
    
    //interface methods
	public void setCustomerProfile(ICustomerProfile customerProfile){
		this.customerProfile=customerProfile;
	}
	
    public void retrieveSavedCart() throws DatabaseException {
        Integer shoppintCartId = getShoppingCartId();
        if(shoppintCartId != null){
            shopCartId = shoppintCartId;
            log.info("cart id: "+shopCartId);
            List<ICartItem> items = getCartItems(shopCartId);
            log.info("list of items: "+items);
            savedCart = new ShoppingCart(items);
            savedCart.setCartId(shopCartId+"");
        }
        else {
        	savedCart = new ShoppingCart();
        }
    }
    
    //supporting methods
    
    Integer getShoppingCartId() throws DatabaseException {
        DbClassShoppingCart dbClass = new DbClassShoppingCart();
        return dbClass.getShoppingCartId(customerProfile);
    }
    
    List<ICartItem> getCartItems(Integer shopCartId) throws DatabaseException {
        DbClassShoppingCart dbClass = new DbClassShoppingCart();
        return dbClass.getSavedCartItems(shopCartId);
    }

    //make it a singleton
    private static ShoppingCartSubsystemFacade instance;
    public static IShoppingCartSubsystem getInstance(){
        if(instance == null) {
            instance = new ShoppingCartSubsystemFacade();
        }
        return instance;
    }
	/**
	 * Private constructor to make the facade a singleton
	 *
	 */
    private ShoppingCartSubsystemFacade(){
    	liveCart = new ShoppingCart(new LinkedList<ICartItem>());
    }


    public void addCartItem(String itemName, String quantity, String totalPrice, Integer pos) throws DatabaseException {
        //if a saved cart has been retrieved, it will be the live cart, unless
        //user has already added items to a new cart
        if(liveCart == null){
            liveCart = new ShoppingCart(new LinkedList<ICartItem>());
        }
        
        IProductSubsystem productSubsystem = new ProductSubsystemFacade();
        Integer productId = productSubsystem.getProductIdFromName(itemName);
        CartItem item = new CartItem(null,productId, 
                liveCart.getCartItems().size(),quantity, 
                totalPrice,false);
        if(pos == null) liveCart.addItem(item);
        else liveCart.insertItem(pos, item);
    }
    
    public boolean deleteCartItem(int pos) {
    	return liveCart.deleteCartItem(pos);
    }
    
    public void clearLiveCart() {
    	liveCart.clearCart();
    }

    public boolean deleteCartItem(String itemName){
    	return liveCart.deleteCartItem(itemName);
	}

	public List<ICartItem> getLiveCartItems() {
        return liveCart.getCartItems();        
	}


	public void setShippingAddress(IAddress addr) {
        liveCart.setShipAddress(addr);
    }
 
    public void setBillingAddress(IAddress addr) {
        liveCart.setBillAddress(addr);
    }
    public void setPaymentInfo(ICreditCard cc) {
        liveCart.setPaymentInfo(cc);
        
    }

	public IShoppingCart getLiveCart() {
		return liveCart;
	}
	public void makeSavedCartLive() {
		liveCart = savedCart;		
	}
	public void saveLiveCart() throws DatabaseException {
		DbClassShoppingCart dbClass = new DbClassShoppingCart();
		if(liveCart.getCartId() == null){
			shopCartId = dbClass.saveCartDetails(liveCart, customerProfile);
			liveCart.setCartId(shopCartId + "");
		}
		dbClass.saveCartItems(liveCart, customerProfile);
		retrieveSavedCart();
		makeSavedCartLive();
	}
	
	public void runShoppingCartRules() throws RuleException, EBazaarException {
		if(liveCart == null) liveCart=new ShoppingCart();
		IRules transferObject = new RulesShoppingCart(liveCart);
		transferObject.runRules();   
	}
	public void runFinalOrderRules() throws RuleException, EBazaarException {
		IRules transferObject = new RulesFinalOrder(liveCart);
		transferObject.runRules(); 
	}


}

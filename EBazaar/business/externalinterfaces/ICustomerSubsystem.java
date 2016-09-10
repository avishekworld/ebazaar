
package business.externalinterfaces;
import java.util.List;

import business.RuleException;
import middleware.DatabaseException;
import middleware.EBazaarException;




public interface ICustomerSubsystem {
	/** Use for loading order history,
	 * default addresses, default payment info, 
	 * saved shopping cart,cust profile
	 * after login*/
    public void initializeCustomer(Integer id) throws DatabaseException;
    
    /** Use for creating an address instance from outside the subsystem */
    public IAddress createAddress(String street, String city, String state, String zip);
    
    /** Use for saving an address created by user and marked as 'new' */
    public void saveNewAddress(IAddress addr) throws DatabaseException;
    
    /** Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window */
    public List<IAddress> getAllAddresses() throws DatabaseException;
    
    /** Used to obtain this customer's order history. Used by other subsystems
     * to read current user's order history (not used during login process)*/
    public List<IOrder> getOrderHistory();
    
    
    /** Used whenever a customer name needs to be accessed */
    public ICustomerProfile getCustomerProfile();
    
    /** Used when ship/bill window is first displayed */
    public IAddress getDefaultShippingAddress();
    
    /** Used when ship/bill window is first displayed */
    public IAddress getDefaultBillingAddress();
    
    /** Used when payment window is first displayed */
    public ICreditCard getDefaultPaymentInfo();
    
    /** Used after user has decided upon an address and is proceeding to 
	 * payment window
	 */
    public void setShippingAddressInCart(IAddress addr);
    
    /** Used after user has decided upon an address and is proceeding to 
	 * payment window
	 */
    public void setBillingAddressInCart(IAddress addr);
    
    /** Used after user has filled out payment window and is proceeding to checkout*/
    public void setPaymentInfoInCart(ICreditCard cc);
    
    /** Use when a credit card instance is needed outside the subsystem */
    public ICreditCard createCreditCard(String name, String num, String type, String expDate);
    
    /** Use when user submits final order -- customer sends its shopping cart to order subsystem
	 *  and order subsystem extracts items from shopping cart and prepares order*/
    public void submitOrder() throws DatabaseException;
    
    /**
     * After an order is submitted, the list of orders cached in CustomerSubsystemFacade
     * will be out of date; this method should cause order data to be reloaded
     */
    public void refreshAfterSubmit() throws DatabaseException;
    
    /**
	 * Used whenever the shopping cart needs to be displayed
	 */
    public IShoppingCartSubsystem getShoppingCart();
    
    /**
	 * save shopping cart to database
	 */
    public void saveShoppingCart() throws DatabaseException;
		
    /**
	 *  create an IAddress based on address fields 
     */
    public IAddress createAddress(String[] addressInfo);
    
    /**
	 *  run address rules and return the cleansed address
     *  if a RuleException is thrown, this represents a validation error
     *  and the error message should be extracted and displayed
     */
    public IAddress runAddressRules(IAddress addr) throws RuleException, EBazaarException; 
    

    /**
	*  run payment rules;
     *  if a RuleException is thrown, this represents a validation error
     *  and the error message should be extracted and displayed
     */
    public void runPaymentRules(IAddress addr, ICreditCard cc) throws RuleException, EBazaarException;

    //NEW
    public void checkCreditCard() throws EBazaarException;
    
}

package business.customersubsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.EBazaarException;
import middleware.creditverifcation.CreditVerificationFacade;
import middleware.externalinterfaces.ICreditVerification;
import business.RuleException;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderSubsystem;
import business.externalinterfaces.IRules;
import business.externalinterfaces.IShoppingCart;
import business.externalinterfaces.IShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;

import business.rulesbeans.AddressBean;
import business.rulesbeans.PaymentBean;
import business.rulesbeans.QuantityBean;
import business.rulesubsystem.RulesSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.OrderUtil;

public class CustomerSubsystemFacade implements ICustomerSubsystem {
	IShoppingCartSubsystem shoppingCartSubsystem;
	IOrderSubsystem orderSubsystem;
	List<IOrder> orderHistory;
	Address defaultShipAddress;
	Address defaultBillAddress;
	CreditCard defaultPaymentInfo;
	CustomerProfile customerProfile;

	public void initializeCustomer(Integer id) throws DatabaseException {
		loadCustomerProfile(id);
		loadDefaultShipAddress();
		loadDefaultBillAddress();
		loadDefaultPaymentInfo();
		shoppingCartSubsystem = ShoppingCartSubsystemFacade.getInstance();
		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.setShippingAddress(defaultShipAddress);
		shoppingCartSubsystem.setBillingAddress(defaultBillAddress);
		shoppingCartSubsystem.setPaymentInfo(defaultPaymentInfo);
		shoppingCartSubsystem.retrieveSavedCart();
		loadOrderData();
	}

	void loadCustomerProfile(Integer custId) throws DatabaseException {
		DbClassCustomerProfile dbClass = new DbClassCustomerProfile();
		customerProfile = dbClass.getCustomerProfile(custId);
	}

	void loadDefaultShipAddress() throws DatabaseException {
		DbClassAddress dbclass = new DbClassAddress();
		dbclass.readDefaultShipAddress(customerProfile);
		defaultShipAddress = dbclass.getDefaultShipAddress();
	}

	void loadDefaultBillAddress() throws DatabaseException {
		DbClassAddress dbclass = new DbClassAddress();
		dbclass.readDefaultBillAddress(customerProfile);
		defaultBillAddress = dbclass.getDefaultBillAddress();
	}

	void loadDefaultPaymentInfo() throws DatabaseException {
		DbClassCreditCard dbclass = new DbClassCreditCard();
		dbclass.readDefaultPayment(customerProfile);
		defaultPaymentInfo = dbclass.getDefaultPayment();
	}
	
	public List<IAddress> getAllAddresses() throws DatabaseException {
		DbClassAddress dbClass = new DbClassAddress();
		dbClass.readAllAddresses(customerProfile);
		return Collections.unmodifiableList(dbClass.getAddressList());
	}
	
	public void refreshAfterSubmit() throws DatabaseException {
		loadOrderData();
	}
	
	void loadOrderData() throws DatabaseException {
		// retrieve the order history for the customer and store here
		orderSubsystem = new OrderSubsystemFacade(customerProfile);
		orderHistory = orderSubsystem.getOrderHistory();
	}

	public IAddress createAddress(String street, String city, String state,
			String zip) {
		return new Address(street, city, state, zip);
	}

	/**
	 * Return an (unmodifiable) copy of the order history.
	 * @throws DatabaseException 
	 */
	public List<IOrder> getOrderHistory() throws DatabaseException {
		//IMPLEMENT
		orderHistory = orderSubsystem.getOrderHistory();
		return Collections.unmodifiableList(orderHistory);
	}

	public void saveNewAddress(IAddress addr) throws DatabaseException {
		DbClassAddress dbClass = new DbClassAddress();
		dbClass.setAddress(addr);
		dbClass.saveAddress(customerProfile);

	}

	public ICustomerProfile getCustomerProfile() {
		return customerProfile;
	}

	public IAddress getDefaultShippingAddress() {
		return defaultShipAddress;
	}

	public IAddress getDefaultBillingAddress() {
		return defaultBillAddress;
	}

	public void setShippingAddressInCart(IAddress addr) {
		shoppingCartSubsystem.setShippingAddress(addr);

	}

	public void setBillingAddressInCart(IAddress addr) {
		shoppingCartSubsystem.setBillingAddress(addr);

	}

	public ICreditCard getDefaultPaymentInfo() {
		return defaultPaymentInfo;
	}

	public ICreditCard createCreditCard(String name, String num, String type,
			String expDate) {

		return new CreditCard(name, expDate, num, type);
	}

	public void setPaymentInfoInCart(ICreditCard cc) {
		shoppingCartSubsystem.setPaymentInfo(cc);

	}

	/**
	 * This method inserts the liveCart of customer's shopping cart subsystem
	 * into the order subsystem on its way to the database, in the form of a
	 * to-be-processed order.
	 */
	public void submitOrder() throws DatabaseException {
		//IMPLEMENT
	}

	public void saveShoppingCart() throws DatabaseException {
		shoppingCartSubsystem.saveLiveCart();
	}

	// assumes array is in the form street,city,state,zip
	public IAddress createAddress(String[] addressInfo) {
		return createAddress(addressInfo[0], addressInfo[1], addressInfo[2],
				addressInfo[3]);
	}

	public IAddress runAddressRules(IAddress addr) throws RuleException,
			EBazaarException {

		IRules transferObject = new RulesAddress(addr);
		transferObject.runRules();

		// updates are in the form of a List; 0th object is the necessary
		// Address
		Address update = (Address) transferObject.getUpdates().get(0);
		return update;
	}

	public void runPaymentRules(IAddress addr, ICreditCard cc)
			throws RuleException, EBazaarException {
		IRules transferObject = new RulesPayment(addr, cc);
		transferObject.runRules();
	}

	public IShoppingCartSubsystem getShoppingCart() {
		return shoppingCartSubsystem;
	}

	/**
	 * Customer Subsystem is responsible for obtaining all the data needed by
	 * Credit Verif system -- it does not (and should not) rely on the
	 * controller for this data.
	 */
	@Override
	public void checkCreditCard() throws EBazaarException {
		List<ICartItem> items = ShoppingCartSubsystemFacade.getInstance()
				.getLiveCartItems();
		IShoppingCart theCart = ShoppingCartSubsystemFacade.getInstance()
				.getLiveCart();
		IAddress billAddr = theCart.getBillingAddress();
		ICreditCard cc = theCart.getPaymentInfo();
		String amount = OrderUtil.quickComputeTotalPrice(items);
		ICreditVerification creditVerif = new CreditVerificationFacade();
		creditVerif.checkCreditCard(customerProfile, billAddr, cc, new Double(
				amount));
	}

}

package middleware.externalinterfaces;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import middleware.EBazaarException;

public interface ICreditVerification {
	public void checkCreditCard(ICustomerProfile custProfile, IAddress billingAddress, 
			ICreditCard creditCard, double amount) throws EBazaarException;
}

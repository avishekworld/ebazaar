package middleware.creditverifcation;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import middleware.EBazaarException;
import middleware.externalinterfaces.ICreditVerification;
import publicview.VerificationManager;
import publicview.IVerificationSystem;
import publicview.TransactionFailedException;

public class CreditVerificationFacade implements ICreditVerification {

	/**
	 * Use of "amount" here is a violation of encapsulation. Should use a
	 * command object to encapsulate all the data.
	 * @param custProfile
	 * @param billingAddress
	 * @param creditCard
	 * @param amount
	 * @throws EBazaarException
	 */
	@Override
	public void checkCreditCard(ICustomerProfile custProfile,
			IAddress billingAddress, ICreditCard creditCard, double amount)
			throws EBazaarException {
		
		IVerificationSystem verifSystem = VerificationManager.clientInterface();
		CreditVerifMediator mediator = new CreditVerifMediator();
		mediator.processCreditRequest(verifSystem, custProfile, billingAddress, creditCard, amount);

	}

}

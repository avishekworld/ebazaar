package business.externalinterfaces;

import java.util.List;


public interface IShoppingCart {
    IAddress getShippingAddress();
    IAddress getBillingAddress();
    ICreditCard getPaymentInfo();
    List<ICartItem> getCartItems();
    double getTotalPrice();
    boolean deleteCartItem(String name);
    boolean isEmpty();

}


package business.shoppingcartsubsystem;

import java.util.logging.Logger;

import middleware.DatabaseException;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.IProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;


public class CartItem implements ICartItem {
    Integer cartid;
    Integer productid;
    Integer lineitemid;
    String quantity;
    String totalprice;
	String productName;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
//	this is true if this cart item is data that has come from
    //database
    boolean alreadySaved;
    
    /** This version of constructor used when reading data from screen */
    public CartItem(String productName, 
                    String quantity,
                    String totalprice) throws DatabaseException{
        this.productName = productName;
        this.quantity = quantity;
        this.totalprice =totalprice;
        alreadySaved = false;
        IProductSubsystem prodSS= new ProductSubsystemFacade();
        //productid = prodSS.getProductIdFromName(productName);
        productid = -1;
    }
    
    /** This version of constructor used when reading from database */
    public CartItem(Integer cartid, 
                    Integer productid, 
                    Integer lineitemid, 
                    String quantity, 
                    String totalprice,
                    boolean alreadySaved) throws DatabaseException {
        this.cartid = cartid;
        this.productid= productid;
        this.lineitemid = lineitemid;
        this.quantity = quantity;
        this.totalprice =totalprice;
        this.alreadySaved = alreadySaved;
        IProductSubsystem prodSS= new ProductSubsystemFacade();
        //productName = prodSS.getProductFromId(productid).getProductName();
        productName = "??";
    }

    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("cartid = <"+cartid+">,");
        buf.append("productid = <"+productid+">,");
        buf.append("lineitemid = <"+lineitemid+">,");
        buf.append("quantity = <"+quantity+">,");
        buf.append("totalprice = <"+totalprice+">");
        buf.append("alreadySaved = <"+alreadySaved+">");
        return buf.toString();
    }
	public boolean isAlreadySaved() {
		return alreadySaved;
	}
	public Integer getCartid() {
		return cartid;
	}
	public Integer getLineitemid() {
		return lineitemid;
	}
	public Integer getProductid() {
		return productid;
	}
	public String getProductName() {
		return productName;
	}
	public String getQuantity() {
		return quantity;
	}
	public String getTotalprice() {
		return totalprice;
	}
}

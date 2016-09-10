
package business.externalinterfaces;



/**
 * @author pcorazza
 * @since Nov 16, 2004
 * Class Description:
 * 
 * 
 */
public interface IAddress {
    public String getStreet1();
    public String getStreet2();
    public String getCity();
    public String getState();
    public String getZip();
    public void setStreet1(String s);
    public void setStreet2(String s);
    public void setCity(String s);
    public void setState(String s);
    public void setZip(String s);
    
}

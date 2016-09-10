
package business.externalinterfaces;


public interface ICustomerProfile {
    public String getFirstName();
    public String getLastName();
    public Integer getCustId();
    public void setFirstName(String fn);
    public void setLastName(String ln);
    public void setCustId(Integer id);
}

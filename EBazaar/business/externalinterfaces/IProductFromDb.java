
package business.externalinterfaces;


public interface IProductFromDb {
    public Integer getCatalogId();
    public String getMfgDate();
    public Integer getProductId();
    public String getProductName();
    public String getQuantityAvail();
    public String getUnitPrice();
    public String getDescription();
}

package business.externalinterfaces;

import java.util.List;

public interface ICatalogTypes {
    @SuppressWarnings("unchecked")
	public List<String[]> getCatalogNames();
    public String getCatalogName(Integer id);
    public Integer getCatalogId(String name);
    public void addCatalog(Integer id, String name);
}

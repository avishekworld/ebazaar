package application;
/** 
 * Implemented by MaintainProductCatalog and AddEditCatalog.
 * These screens each have a combo box, and the states of these
 * combo boxes must be kept synchronized. The IComboObserver
 * aids in this -- all IComboObservers are managed at the same time,
 * whenver a value in any of these related combo boxes is selected.
 * @author pcorazza
 *
 */
public interface IComboObserver {
	public void setCatalogGroup(String s);
	public void refreshData();
}

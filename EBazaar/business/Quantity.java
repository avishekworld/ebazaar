package business;

/** Stored at the business level since it's business data.
 *  Not stored in product subsystem because contains gui data
 */
public class Quantity {
	private String quantityRequested;
	private String quantityAvailable;
	public Quantity(String quantityRequested) {
		this.quantityRequested = quantityRequested;
	}
	public String getQuantityRequested() {
		return quantityRequested;
	}
	public String getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(String q) {
		quantityAvailable = q;
	}
	
}

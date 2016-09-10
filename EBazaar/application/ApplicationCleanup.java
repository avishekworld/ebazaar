
package application;

import java.util.logging.Logger;

import middleware.externalinterfaces.Cleanup;
import business.BusinessCleanup;


public class ApplicationCleanup implements Cleanup{
    Cleanup bc;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
    public void cleanup(){
		log.info("Disposing of all open windows...");
		for(CleanupControl c : allCleanupControllers){
			c.cleanUp();
		}
        bc = new BusinessCleanup();
        bc.cleanup();
    }
	private CleanupControl[] allCleanupControllers =
		{
			BrowseAndSelectController.getInstance(),
			CheckoutController.getInstance(),
			ManageProductsController.getInstance(),
			ViewOrdersController.getInstance()
		};
	
}

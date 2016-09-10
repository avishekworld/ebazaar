
package business;

import middleware.MiddlewareCleanup;
import middleware.externalinterfaces.Cleanup;


public class BusinessCleanup implements Cleanup {
    Cleanup mc;
    
    public void cleanup() {
        mc = new MiddlewareCleanup();
        mc.cleanup();
        
    }

}

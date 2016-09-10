
package application;

import middleware.EBazaarException;

/**
 * @author pcorazza
 * @since Nov 12, 2004
 * Class Description:
 * 
 * 
 */
public class UserException extends EBazaarException {
   

	public UserException(String msg){
        super(msg);
    }
	private static final long serialVersionUID = 3690196564010546740L;

}

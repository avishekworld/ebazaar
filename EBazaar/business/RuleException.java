/*
 * Created on Nov 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package business;

import middleware.EBazaarException;

/**
 * @author pcorazza
 * @since Nov 16, 2004
 * Class Description:
 * 
 * 
 */
public class RuleException extends EBazaarException {
   

	public RuleException(String msg){
        super(msg);
    }
	private static final long serialVersionUID = 3257003267694145848L;
}

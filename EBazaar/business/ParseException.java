
package business;

import middleware.EBazaarException;

public class ParseException extends EBazaarException {
	public ParseException(String msg) {
        super(msg);
 
    }
    public ParseException(Exception e) {
        super(e);
    }
	private static final long serialVersionUID = 3689355398893482807L;
	
}

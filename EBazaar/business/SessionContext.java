
package business;

import java.util.HashMap;

import business.externalinterfaces.CustomerConstants;

/**
 * Class Description: Stores application level data
 * during execution. In particular, caches the
 * flag indicating user has logged in and caches
 * the initial customer data in the form of an
 * Customer subsystem instance.
 */
public class SessionContext {
    
    //public interface
    
    public static SessionContext getInstance() {
        return instance;
        
    }   
    public void add(Object name, Object value){
        if(context != null){
            context.put(name,value);
        }
    }
    
    public Object get(Object name){
        if(context == null){
            return null;
        }
        return context.get(name);
    }
    public void remove(Object name){
        context.remove(name);
    }
    
    
    //private 
    
    private static SessionContext instance = new SessionContext();
    private HashMap<Object,Object> context;
    private SessionContext(){
        context = new HashMap<Object,Object>();
        context.put(CustomerConstants.LOGGED_IN, Boolean.FALSE);
        
    }
}

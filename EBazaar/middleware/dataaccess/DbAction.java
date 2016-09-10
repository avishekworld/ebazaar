
package middleware.dataaccess;

import java.sql.ResultSet;

import middleware.DatabaseException;
import middleware.externalinterfaces.IDbClass;


class DbAction {
    protected String query;
    protected ResultSet resultSet;
    protected IDbClass concreteDbClass;
    protected SimpleConnectionPool pool;
    
    DbAction(IDbClass c) throws DatabaseException {
        concreteDbClass = c;
        pool = DataAccessUtil.getPool();
    }
    void performRead() throws DatabaseException {
        concreteDbClass.buildQuery();
        ResultSet resultSet = DataAccessUtil.runQuery(pool, concreteDbClass.getDbUrl(),
                                            concreteDbClass.getQuery());
        
        concreteDbClass.populateEntity(resultSet);
    }
    
    
    Integer performUpdate() throws DatabaseException {
        concreteDbClass.buildQuery();
        Integer generatedKey = DataAccessUtil.runUpdate(pool, concreteDbClass.getDbUrl(),
                				 concreteDbClass.getQuery());
   
        return generatedKey;
    }
    
    void performDelete() throws DatabaseException {
        concreteDbClass.buildQuery();
        DataAccessUtil.runUpdate(pool, concreteDbClass.getDbUrl(),
                				 concreteDbClass.getQuery());
    }

}

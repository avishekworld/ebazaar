package business;

/** stores old values and new values for an entity class
 *  used by rules
 * @author pcorazza
 *
 */
public class EntityPair {
	private Object entityNewVals;
	private Object entityOldVals;
	public EntityPair(Object entityNew,Object entityOld){
		entityNewVals = entityNew;
		entityOldVals = entityOld;
	}


	public Object getEntityNewVals() {
		return entityNewVals;
	}
	public Object getEntityOldVals() {
		return entityOldVals;
	}
}

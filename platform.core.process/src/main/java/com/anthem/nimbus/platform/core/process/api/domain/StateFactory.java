/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.nimbus.platform.core.process.api.repository.ModelRepository;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepositoryFactory;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionState;

/**
 * View is a perspective of Core. It can be used in presentation layer or can be part of web service integration.<br>
 * Relationship of View to Core is many-to-one. View is not mandated to have a core backing.<br>
 * <br>
 * Within the platform, View is associated to an user, while Core is the same across users.<br>
 * Authorization cross-cutting component ensures that access to Core by an user is valid.<br>
 * <br>
 * An user could potentially be logged into the platform with different sessions. Relationship of Session to View is one-to-one.<br>
 * In the scenario, with 2 users (UserA, UserB) with valid access to a domain-entity (id: D100) will have following relation:<br>
 * <br>
 * UserA________SessionA1_________QuadA1__________________D100	<br>
 *    |\________SessionA2_________QuadA2__________________/|	<br>
 *     \________SessionA3_________QuadA3__________________/|	<br>
 * 														   |	<br>
 * UserB________SessionB1_________QuadB1__________________/|	<br>
 * 	   \________SessionB2_________QuadB2__________________/		<br>
 * <br>
 * <br>
 * Domain state is always retrieved directly from the Repository API.<br>
 * Quad State is always obtained from User Session and persisted back to DB on explicit _save or $save <br>
 * <br>
 * <br>
 * Within a Session, each Flow for a given domain-alias can point to multiple Quad models.<br>
 * <br>
 * TODO: Flush out details based on Composite Quad implementation...............................START
 * flow_car has two pages. First page is for searching existing cars and second is to display details of any one unique car.<br>
 * <br>
 * flow_car				- has no backing core model<br>
 * flow_car/search		- has backing core model, but used for default search criteria. Not meant to be linked with persistent entity.<br>
 * flow_car/details		- has backing core model<br>
 * <br>
 * Possible URLs:<br>
 * 		/flow_car			 _____Quad1___________________[void]
 * 		/flow_car/search	 _____Quad1___________________Car[instance level of Quad1]	
 * 		/flow_car/details:100_____Quad1___________________Car[100]
 * 		/flow_car/details:200_____Quad1___________________Car[200]
 * TODO: Flush out details based on Composite Quad implementation...............................END
 * 
 * 
 * @author Soham Chakravarti
 */
public class StateFactory<V, C> {

	@Autowired ModelRepositoryFactory repFactory;
	
	public ExecutionState<V, C> resurrect(Command cmd) {
		ModelRepository rep = repFactory.get(cmd);
		
		return null;
	}
	
	
}



/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import org.springframework.web.context.request.RequestContextHolder;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;

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
 * Example:<br>
 * flow_car has two pages. First page is for searching existing cars and second is to display details of any one unique car.<br>
 * <br>
 * flow_car/_new		- creates new entity and assigns unique persistence id, if configured with {@linkplain Repo}<br>
 * flow_car:100/_get	- checks if entity exists in session, others retrieves & puts entity in session, if configured as such in {@linkplain Repo}<br>
 * flow_car/_search		- creates {@linkplain QuadModel} in transient mode and doesn't interact with session<br>
 * <br>
 * Possible URLs:<br>
 * 		/flow_car/_new		 _____Quad1___________________new Car()
 * 		/flow_car/search	 _____Quad2___________________Car[instance level of Quad1]	
 * 		/flow_car/details:100_____Quad3___________________Car[100]
 * 		/flow_car/details:200_____Quad4___________________Car[200]
 * 
 * @author Soham Chakravarti
 */
public interface ExecutionContextLoader {

	default ExecutionContext load(Command rootDomainCmd) {
		String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		return load(rootDomainCmd, sessionId);
	}
	
	public ExecutionContext load(Command rootDomainCmd, String sessionId);
	
	public void unload(ExecutionContext eCtx, String sessionId);
	
	public void clear();
}

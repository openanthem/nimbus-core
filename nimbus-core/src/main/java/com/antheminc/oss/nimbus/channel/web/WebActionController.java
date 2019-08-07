/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.channel.web;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.cmd.exec.FileImportGateway;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.support.LoggingLevelService;

/**
 * STEPS to follow with examples. <br>
 * 
 * <p>EXAMPLES:</p>
 * Ex 1:  /anthem/icr/member/_search - POST operation which contains criteria <br>
 * Ex 2:  /anthem/icr/member/{id} - GET operation which has member id as path variable <br>
 * Ex 3:  /anthem/acmp/member/{id}/_info - GET operation which contains member id as path variable, but also contains an action 'info' to return less data <br>
 * Ex 4:  /anthem/acmp/um/case/{cid}/member/address/{aid} - GET operation to return one address from the collection <br>
 * Ex 5:  /anthem/acmp/um/case/{cid}/member/address		- POST operation to create new address <br>
 * Ex 6:  /anthem/icr/um/case/{cid}/member/address/{aid}?version=n - PUT operation to update a given address. This would be a complete model update <br>
 * Ex 7:  /anthem/icr/um/case/{cid}/member/address/{aid}?version=n - PATCH operation to update a given address. This would be a partial model update <br>
 * Ex 8:  /anthem/icr/member/_search$validate - POST operation which contains criteria which will be validated and response provided per field <br>
 * Ex 9:  /anthem/hrs/member/_search$config	 - GET operation that returns static meta data created for 'member/_search' process <br>
 * Ex 10: /anthem/hrs/um/case/{cid}/member/address$validate - POST operation that contains address field populated which will be validated at each field level <br>
 * Ex 11: /anthem/hrs/address$validate - POST operation
 * <br>
 * <br>
 * <p>STEPS:</p>
 * 1. Inject a list of available domain-aliases to consider
 * 2. Inject a list of available actions to consider
 * 3. Inject any rule to explicitly allow or prohibit a certain combination of: client-code + app-code + /p + domain-alias + action + behavior + RequestMethod.Type
 * 4. Inject patterns to follow<ol>
 * 	<li>/{app}/{domain-alias}/{id}<ul>
 * 		<li>a. domain-alias would be identified from injected list</li>
 * 		<li>b. {id} would be unrecognized and considered as refId for identified domain-alias</li>
 * 		<li>c. If the HTTP method is GET, then action would be "_get"</li>
 * 		<li>d. If the HTTP method is POST, then action would be "_create" and any RequestBody JSON object would be used</li>
 * 		<li>e. For DELETE - '_delete', PUT - '_replace', PATCH - '_update'. All these operations require mandatory 'version' parameter</li></ul></li>
 * 
 * 	<li>/{app}/{domain-alias-A}/{domain-alias-B}/{B_id}/.../{domain-alias-N}/{N_id}<ul>
 * 		<li>a. Detect all domain-aliases from the url and corresponding refId, if available</li>
 * 		<li>b. Follow same steps as above in '/{domain-alias}/{id}'</li></ul></li>
 * 
 * 	<li>/{app}/{domain-alias}/{_action}  -OR-  /{app}/{domain=alias}/{refId}/{_action}<ul>
 * 		<li>a. domain-alias would be identified from injected list</li>
 * 		<li>b. action would be identified from injected list</li>
 * 		<li>c. Follow same steps as above for recurring domain-alias identification</li></ul></li>
 * 
 * 	<li>/{app}/{domain-alias}/{_action}.{$behavior} OR /{app}/{domain-alias}{$behavior}<ul>
 * 		<li></li></ul></li></ol>
 * 
 * 5. Action can appear only once and at the end of the url but prior to Behavior, if any
 * 6. Action if explicitly provided, overrides inferred action. Ex: /{app}/{domain-alias}/{id}/_delete GET : would be treated as '_delete' action and not '_get' inferred from GET methods
 * 7. GET/_get and DELETE/_delete only accepts {refId} as argument and not any RequestBody
 * 8. Behavior can appear only once at the end or url prefixed by '.' Ex: .../../../_search$config OR ../../_search$validate
 * 			
 * @author Soham Chakravarti
 *
 */
@RestController
//@EnableResourceServer
public class WebActionController {
	
	public static final String URI_PATTERN_P = "/{clientCode}/**/p";
	public static final String URI_PATTERN_P_OPEN = URI_PATTERN_P + "/**";

	private static final Set<Action> notifyActionsToMatch = EnumSet.of(Action._replace, Action._update);
	
	@Autowired WebCommandDispatcher dispatcher;
	
	@Autowired ExecutionContextLoader ctxLoader;
	
	@Autowired WebCommandBuilder builder;
	
	@RequestMapping(value=URI_PATTERN_P+"/clear", produces="application/json", method=RequestMethod.GET)
	public void clear() {
		ctxLoader.clear();
	}
	
	/**
	 * Usage Example: /client/org/p/loglevel?level=debug&package=com.antheminc
	 * 
	 * @param level
	 * @param packageName
	 * @return
	 */
	@RequestMapping(value=URI_PATTERN_P+"/loglevel", produces="application/json", method=RequestMethod.GET)
	public Output<String> updateLogLevel(@RequestParam(value="level") String level, @RequestParam(value="package") String packageName) {
		return LoggingLevelService.setLoggingLevel(level, packageName);
	}
	
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.GET)
	public Object handleGet(HttpServletRequest req, @RequestParam(required=false) String a) {
		return handleInternal(req, RequestMethod.GET, null, a);
	}
	
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.DELETE)
	public Object handleDelete(HttpServletRequest req, @RequestParam String v) {
		return handleInternal(req, RequestMethod.DELETE, v, null);
	}
	
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.POST)
	public Object handlePost(HttpServletRequest req, @RequestBody String json) { 
		return handleInternal(req, RequestMethod.POST, null, json);
	}
	
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.PUT)
	public Object handlePut(HttpServletRequest req, @RequestParam String v, @RequestBody String json) {  
		return handleInternal(req, RequestMethod.PUT, v, json);
	}
	
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.PATCH)
	public Object handlePatch(HttpServletRequest req, @RequestParam String v, @RequestBody String json) {  
		return handleInternal(req, RequestMethod.PATCH, v, json);
	}
	
	@RequestMapping(value=URI_PATTERN_P+"/event/notify", produces="application/json", method=RequestMethod.POST)
	public Object handleEventNotify(HttpServletRequest req, @RequestBody ModelEvent<String> event) {
		Object obj = dispatcher.handle(req, event);
		filterInputParamFromOutput(obj);
		Holder<Object> output = new Holder<>(obj);
		return output;
	}
	
	private void filterInputParamFromOutput(Object obj) {
		if(obj instanceof MultiOutput) {
			MultiOutput multiOp = (MultiOutput) obj;
			List<Output<?>> outputs = multiOp.getOutputs();
			if(CollectionUtils.isEmpty(outputs))
				return;
			
			Command cmd = CommandBuilder.withUri(multiOp.getInputCommandUri()).getCommand();
			String inputParamPath = cmd.getAbsoluteDomainAlias();
			
			outputs.removeIf(o -> isRemove(inputParamPath, o));
		}
	}

	private boolean isRemove(String inputParamPath, Output<?> o) {
		if(o instanceof MultiOutput) {
			filterInputParamFromOutput(o);
		}
		else if(o.getValue() instanceof Param) {
			Param<?> param = (Param<?>) o.getValue();
			if(inputParamPath.equals(param.getPath()) && notifyActionsToMatch.contains(o.getAction())) {
				
				if(param.hasContextStateChanged())
					return false;
				
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = {"/login/*"}, method = RequestMethod.GET)
	public ResponseEntity<?> login(){
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	protected Object handleInternal(HttpServletRequest req, RequestMethod httpMethod, String v, String json) {
		Object obj = dispatcher.handle(req, json);
		Holder<Object> output = new Holder<>(obj);
		return output;
	}
}

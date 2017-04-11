/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiExpressionBuilder {

	public static final String PLATFORM_FUNCTION_START="(";
	public static final String PLATFORM_FUNCTION_END=")";
	public static final String PLATFORM_FUNCTION_ARG_SEPARATOR=",";
	public static final String EXPRESSION_BEAN="platformExpressionHandler";
	public static final String EXPRESSION_COMMAND_MESSAGE="processGatewayContext.processEngineContext.commandMsg";
	public static final String EXPRESSION_EXECUTION="execution";
	public static final String EXPRESSION_HELPER_PREFIX="defaultExpressionHelper";
	public static final String PLATFORM_FUNCTION_PREFIX="_";
	public static final String PLATFORM_URI_RESOLVE_PREFIX = 
			EXPRESSION_HELPER_PREFIX+".getResolvedUri("
			+EXPRESSION_COMMAND_MESSAGE+",";
	
	private Stack<Function> functionStack = new Stack<Function>();
	private StringBuilder runningExpression = new StringBuilder();
	private Map<String,String> functionToBeanMap;
	private String derivedExpression;
	private static List<String> platformDefaultArguments;
	
	static{
		platformDefaultArguments = new ArrayList<String>();
		platformDefaultArguments.add(EXPRESSION_COMMAND_MESSAGE);
		platformDefaultArguments.add(EXPRESSION_EXECUTION);		
	}
	
	/**
	 * 
	 * @param expression
	 */
	public ActivitiExpressionBuilder(String expression, Map<String,String> functionToBeanMap){
		this.functionToBeanMap = (functionToBeanMap == null)? new HashMap<String,String>():functionToBeanMap;
		char[] expressionCharArray = expression.toCharArray();
		for(char c : expressionCharArray){
			addCharacter(c);
		}
	}
	
	/**
	 * 
	 * @param c
	 */
	public void addCharacter(char c){
		boolean clearRunningString = true;
		if(c == '('){
			String functionName = runningExpression.toString();
			Function function = new Function();
			function.setFunctionName(functionName);
			functionStack.push(function);
		}else if(c == ')'){
			Function poppedFunction = functionStack.pop();
			if(runningExpression.length() > 0){
				poppedFunction.addArgument(runningExpression.toString());
			}			
			String argument = poppedFunction.getFunctionExpression();
			if(functionStack.isEmpty()){
				derivedExpression = argument;
			}else{
				functionStack.peek().addArgument(argument);		
			}
		}else if(c == ','){
			String argument = runningExpression.toString();
			functionStack.peek().addArgument(argument);
		}else{
			runningExpression.append(c);
			clearRunningString = false;
		}
		if(clearRunningString){
			runningExpression = new StringBuilder();
		}
	}
	
	
	@Getter @Setter
	public class Function{
		String functionName;
		List<String> arguments = new ArrayList<String>();
		
		/**
		 * 
		 * @param argument
		 */
		public void addArgument(String argument){
			arguments = (arguments == null) ? new ArrayList<String>():arguments;
			int currentIndex = arguments.size();
			if(currentIndex == 0){
				StringBuilder argumentStr =new StringBuilder();
				argumentStr.append(PLATFORM_URI_RESOLVE_PREFIX).append(argument).append(PLATFORM_FUNCTION_END);
				argument = argumentStr.toString();
			}
			arguments.add(argument);
		}
		
		public String getFunctionExpression(){
			StringBuilder functionExpression = new StringBuilder();
			boolean containsPlatformFunction = false;
			if(functionName.startsWith(PLATFORM_FUNCTION_PREFIX)){
				String helperBean = functionToBeanMap.get(functionName);
				if(helperBean == null)
					helperBean = EXPRESSION_HELPER_PREFIX;
				functionExpression.append(helperBean).append(".");
				containsPlatformFunction = true;
			}
			functionExpression.append(functionName);
			functionExpression.append(PLATFORM_FUNCTION_START);
			if(arguments != null){
				List<String> functionArguments = new ArrayList<String>();
				if(containsPlatformFunction){
					functionArguments.addAll(platformDefaultArguments);
				}
				functionArguments.addAll(arguments);
				if(containsPlatformFunction){
					if(arguments.size() == 1)
						functionArguments.add("null");
				}
				functionExpression.append(StringUtils.join(functionArguments,PLATFORM_FUNCTION_ARG_SEPARATOR));
			}
			functionExpression.append(PLATFORM_FUNCTION_END);
			return functionExpression.toString();
		}
		
	}	
	
}

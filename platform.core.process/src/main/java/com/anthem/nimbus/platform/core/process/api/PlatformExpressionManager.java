/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.el.JsonNodeELResolver;
import org.activiti.engine.impl.el.ReadOnlyMapELResolver;
import org.activiti.engine.impl.el.VariableScopeElResolver;
import org.activiti.spring.ApplicationContextElResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component
public class PlatformExpressionManager extends ExpressionManager implements ApplicationContextAware{
	
	public static final String PLATFORM_EXP_START="!{";
	public static final String PLATFORM_EXP_END="!}";
	protected ApplicationContext applicationContext;
	
	@Getter @Setter
	private Map<String,String> functionToBeanMap;

	@Override
	public Expression createExpression(String expression) {
		if(expression.contains(PLATFORM_EXP_START)){
			expression = evaluate(expression);
		}
		return super.createExpression(expression);
	}
	
	/**
	 * 
	 * @param expression
	 * @return
	 */
	public String evaluate(String expression){
		StringBuilder modifiedExpression = new StringBuilder();
		List<String> expressionChunks = new ArrayList<String>();
		int startIndex = expression.indexOf(PLATFORM_EXP_START);
		while(startIndex != -1){
			String preChunk = expression.substring(0,startIndex);
			expressionChunks.add(preChunk);
			int endIndex = expression.indexOf(PLATFORM_EXP_END);
			if(endIndex == -1){
				throw new IllegalArgumentException("Expression :"+expression+" is not valid");
			}
			endIndex = endIndex + PLATFORM_EXP_END.length();
			String platformExpression = expression.substring(startIndex,endIndex);
			platformExpression = createHanlderDelegateExpression(platformExpression);
			expressionChunks.add(platformExpression);
			expression = expression.substring(endIndex);
			startIndex = expression.indexOf(PLATFORM_EXP_START);
		}
		expressionChunks.add(expression);
		for(String chunk: expressionChunks){
			modifiedExpression.append(chunk);
		}
		return modifiedExpression.toString();
	}
	

	private String createHanlderDelegateExpression(String expression){
		expression = expression.substring(PLATFORM_EXP_START.length(), expression.length() - PLATFORM_EXP_END.length());
		PlatFormExpression platformExpression = new PlatFormExpression(expression, functionToBeanMap);
		return platformExpression.getDerivedExpression().toString();
	}
	
	@Override
	protected ELResolver createElResolver(VariableScope variableScope) {
	    CompositeELResolver compositeElResolver = new CompositeELResolver();
	    compositeElResolver.add(new VariableScopeElResolver(variableScope));
	
	    if (beans != null) {
	      // Only expose limited set of beans in expressions
	      compositeElResolver.add(new ReadOnlyMapELResolver(beans));
	    } else {
	      // Expose full application-context in expressions
	      compositeElResolver.add(new ApplicationContextElResolver(applicationContext));
	    }
	
	    compositeElResolver.add(new ArrayELResolver());
	    compositeElResolver.add(new ListELResolver());
	    compositeElResolver.add(new MapELResolver());
	    compositeElResolver.add(new JsonNodeELResolver());
	    compositeElResolver.add(new BeanELResolver());
	    return compositeElResolver;
	}
	  
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	public static void main(String[] args){
		PlatformExpressionManager pm = new PlatformExpressionManager();
		pm.setFunctionToBeanMap(new HashMap<String,String>());
		String result = pm.evaluate("${px.help(!{_get(\"/pg1/caseId\")!})}");
		System.out.println(result);
		result = pm.evaluate("${!{_get('/pg3/caseName','{caseName:name}')!}}");
		System.out.println(result);
		result = pm.evaluate("${!{_update('/flow_autocase/pg3/caseName','testUpdate3')!}}");
		System.out.println(result);
		result = pm.evaluate("${!{_get('/autocase:585c0d33967e79bb2661aa61)!}}");
		System.out.println(result);
		result = pm.evaluate("${!{_search('/autocase','{\"caseName\":\"Case1\"}')!}}");
		System.out.println(result);
	}

}

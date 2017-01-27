package com.anthem.nimbus.platform.core.process.api.exec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.contract.task.TaskRouter;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
@Component("spElBasedTaskRouter")
public class SpELBasedTaskRouter implements TaskRouter {

	private JustLogit log = new JustLogit(SpELBasedTaskRouter.class);
	
	
	@Override
	public void route(QuadModel<?, ?> taskModel, String assignmentCriteria) {
		
		if(taskModel == null || taskModel.getCore() == null || taskModel.getCore().getState() == null || 
				StringUtils.isBlank(assignmentCriteria)) 
			return;
		
		log.info(()->"spELBasedTaskRouter called for task: "+taskModel.getCore().getState()+" with criteria: "+assignmentCriteria);
		
		String[] assignmentCriteriaTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(assignmentCriteria, ":");
		String[] params = StringUtils.substringsBetween(assignmentCriteriaTokens[0], "'", "'");
		String criteriaToEval = assignmentCriteriaTokens[0];
		if(params != null && params.length > 0) {
			for(String param: params) {
				criteriaToEval = criteriaToEval.replace("'"+param+"'", "getCore().findParamByPath(\""+param+"\").getState()");
			}
		}
		
		StandardEvaluationContext expCtx = new StandardEvaluationContext(taskModel);
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(criteriaToEval);
		Boolean b = exp.getValue(expCtx, Boolean.class);
		if(b != null && b == true) {
			taskModel.getCore().findStateByPath("/queueCode").setState(assignmentCriteriaTokens[1]);
		}
		
	}

}

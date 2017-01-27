/**
 * 
 */
package com.anthem.nimbus.platform.utils.reference.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

/**
 * @author Rakesh Patel
 *
 */
@Component("p/config/staticCodeValues")
public class MongoCodeValuesProvider implements Source {
	
	@Autowired MongoOperations mongoOps;
	
	
	@Override
	public List<ParamValue> getValues(String paramCode) {
		Query query = new Query(Criteria.where("paramPath").is(paramCode));
		StaticCodeValue codeValue = mongoOps.findOne(query, StaticCodeValue.class,"staticCodeValue");
		if(codeValue != null)
			return codeValue.getParamValues();
		return null;
	}

}

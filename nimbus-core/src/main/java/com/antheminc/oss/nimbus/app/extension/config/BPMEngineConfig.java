/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.app.extension.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.activiti.ActivitiExpressionManager;
import com.antheminc.oss.nimbus.domain.bpm.activiti.ActivitiUserTaskActivityBehavior;

import lombok.Getter;
import lombok.Setter;


/**
 * <p>This class configures BPM functions within the framework. Activiti BPM framework is being used to enable BPM capabilities.
 *
 * <p>This class provides the ability to configure/configures following BPM attributes:
 * <ul>
 * <li>Behavior Factory.{@link ActivitiBehaviorFactory}.
 * The framework extends the default User task, Service Task and Call Activity available from Activiti and adds additional capabilities to support framework needs.
 * <li>Audit History level
 * <li>Load process definition from a configured location.
 * <li>Load process rules from a configured location.
 * The framework makes to distinction between entity rules and process rules. This configuration only loads process rules.
 * For definition of entity rules, please refer{@link  com.anthem.oss.nimbus.core.rules.drools.DroolsRulesEngineFactory}
 * Process rules are defined as rules that can be defined across multiple entities and across flows. 
 * These rules are loaded using a single KnowlegeBuilder and can be directly accesses within bpmn processes as business rules task/ service task.
 * <li>Overrides the default expression manager to enhance expression capability. See {@link ActivitiExpressionManager}
 * <li>Datasource
 * <li>Custom Deployers
 * </ul>
 * 
 * @author Jayant Chaudhuri
 * 
 */
@Configuration
@ConfigurationProperties(prefix="process")
public class BPMEngineConfig extends AbstractProcessEngineAutoConfiguration {
	
	@Value("${process.database.driver}") 
	private String dbDriver;
	
	@Value("${process.database.url}") 
	private String dbUrl;
	
	@Value("${process.database.username}") 
	private String dbUserName;
	
	@Value("${process.database.password}") 
	private String dbPassword;
	
	@Value("${process.history.level}") 
	private String processHistoryLevel;
	
	@Value("${process.deployment.name:#{null}}")
	private Optional<String> deploymentName;
	
	@Getter @Setter
	private List<String> definitions = new ArrayList<String>();
	
	@Getter @Setter
	private List<String> rules = new ArrayList<String>();
	
	@Getter @Setter
	private List<String> customDeployers = new ArrayList<String>();	
	
	@Autowired
	private ActivitiExpressionManager platformExpressionManager;
	
	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(
			@Qualifier("processDataSource") DataSource processDataSource,
			PlatformTransactionManager jpaTransactionManager, SpringAsyncExecutor springAsyncExecutor,
			BeanResolverStrategy beanResolver, UserGroupManager userGroupManager) throws Exception {
		
		SpringProcessEngineConfiguration engineConfiguration = this
				.baseSpringProcessEngineConfiguration(processDataSource, jpaTransactionManager, springAsyncExecutor,userGroupManager);

		if (deploymentName.isPresent()) {
			engineConfiguration.setDeploymentName(deploymentName.get());
		}
		engineConfiguration.setExpressionManager(platformExpressionManager);
		engineConfiguration.setProcessDefinitionCache(new ActivitiProcessDefinitionCache());
		engineConfiguration.setActivityBehaviorFactory(platformActivityBehaviorFactory(beanResolver));
		engineConfiguration.setHistoryLevel(HistoryLevel.getHistoryLevelForKey(processHistoryLevel));
		addCustomDeployers(engineConfiguration);

		List<Resource> resources = new ArrayList<>(Arrays.asList(engineConfiguration.getDeploymentResources()));
		Resource[] supportingResources = loadBPMResources();
		if (supportingResources != null && supportingResources.length > 0) {
			resources.addAll(Arrays.asList(loadBPMResources()));
		}
		engineConfiguration.setDeploymentResources(resources.toArray(new Resource[resources.size()]));

		return engineConfiguration;
	}
    
	public DefaultActivityBehaviorFactory platformActivityBehaviorFactory(BeanResolverStrategy beanResolver) {
		
		return new DefaultActivityBehaviorFactory() {
			@Override
			public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
				ActivitiUserTaskActivityBehavior platformUserTaskBehavior = new ActivitiUserTaskActivityBehavior(userTask);
				platformUserTaskBehavior.setBeanResolver(beanResolver);
				return platformUserTaskBehavior;
			}
		};
	}
    
    /**
     * <p>Custom deployers provide the ability for client to control how process definitions and rules can be deployed.
     * This can come handy when a client already has the business process and/or rules defined in some other format. 
     * The custom deployer can read custom format and convert into a structure that can be processed by the framework.
     * 
     * <p>The framework by default adds the custom Rules Deployer
     * <p>Removed support for rules deployer by default since f/w does not support the patter of executing rules within activiti step as of now.
     * If needed, using application can inject one in the BPMEngineConfig.
     *
     * @param engineConfiguration
     */
    protected void addCustomDeployers(SpringProcessEngineConfiguration engineConfiguration) throws Exception{
    	List<Deployer> deployers = new ArrayList<>();
        //deployers.add(new RulesDeployer());
        if(customDeployers != null){
        	for(String customDeployerClass: customDeployers){
        		Class<?> clazz = Class.forName(customDeployerClass);
        		deployers.add((Deployer)clazz.newInstance());        		
        	}
        }
        engineConfiguration.setCustomPostDeployers(deployers);    	
    }
    
    protected Resource[] loadBPMResources() throws IOException{ 
    		List<Resource> bpmResources = new ArrayList<Resource>();
    		addBPMResources(bpmResources,definitions);
    		//==addBPMResources(bpmResources,rules);
  		return bpmResources.toArray(new Resource[bpmResources.size()]);
	}
    
    private void addBPMResources(List<Resource> bpmResources, List<String> bpmResourcePath) throws IOException{
    		PathMatchingResourcePatternResolver pmrs = new PathMatchingResourcePatternResolver();
	    	for(String path: bpmResourcePath){
	    		Resource[] resources = pmrs.getResources(path);
	    		for(Resource resource: resources){
	    			bpmResources.add(resource);
	    		}
	    	}
    }
   
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("processDataSource")DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("processDataSource")DataSource processDataSource) {
    		return new DataSourceTransactionManager(processDataSource);
    }
    
   
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    
    @Bean
  	public DataSource processDataSource() {
    		if(dbUrl.equals("embeddedH2")) {
    			return new EmbeddedDatabaseBuilder().
    					setType(EmbeddedDatabaseType.H2).
    					build();   			
    		}
    		BasicDataSource ds = new BasicDataSource();
    		ds.setUrl(dbUrl);
    		ds.setUsername(dbUserName);
    		ds.setPassword(dbPassword);
    		ds.setDriverClassName(dbDriver);
    		return ds;
  	}     
    
   
    
}
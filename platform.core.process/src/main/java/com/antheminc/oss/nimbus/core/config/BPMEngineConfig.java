package com.antheminc.oss.nimbus.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.rules.RulesDeployer;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
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

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.antheminc.oss.nimbus.core.bpm.activiti.PlatformUserTaskActivityBehavior;

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
 * For definition of entity rules, please refer{@link  com.antheminc.oss.nimbus.core.rules.drools.DroolsRulesEngineFactory}
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
            @Qualifier("processDataSource")DataSource processDataSource,
            PlatformTransactionManager jpaTransactionManager,
            SpringAsyncExecutor springAsyncExecutor, BeanResolverStrategy beanResolver) throws Exception {
    	SpringProcessEngineConfiguration engineConfiguration = this.baseSpringProcessEngineConfiguration(processDataSource, jpaTransactionManager, springAsyncExecutor);
    	engineConfiguration.setExpressionManager(platformExpressionManager);
    	
    	engineConfiguration.setActivityBehaviorFactory(platformActivityBehaviorFactory(beanResolver));
    	engineConfiguration.setHistoryLevel(HistoryLevel.getHistoryLevelForKey(processHistoryLevel));
    	addCustomDeployers(engineConfiguration);
    	
        List<Resource> resources = new ArrayList<>(Arrays.asList(engineConfiguration.getDeploymentResources()));
        Resource[] supportingResources = loadBPMResources();
        if(supportingResources != null && supportingResources.length > 0){
        	resources.addAll(Arrays.asList(loadBPMResources()));
        }
        engineConfiguration.setDeploymentResources(resources.toArray(new Resource[resources.size()])); 
        
        return engineConfiguration;
    }
    
	public DefaultActivityBehaviorFactory platformActivityBehaviorFactory(BeanResolverStrategy beanResolver) {
		return new DefaultActivityBehaviorFactory() {
			@Override
			public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
				PlatformUserTaskActivityBehavior platformUserTaskBehavior = new PlatformUserTaskActivityBehavior(userTask);
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
     * 
     * @param engineConfiguration
     */
    protected void addCustomDeployers(SpringProcessEngineConfiguration engineConfiguration) throws Exception{
    	List<Deployer> deployers = new ArrayList<>();
        deployers.add(new RulesDeployer());
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
    	return new EmbeddedDatabaseBuilder().
				setType(EmbeddedDatabaseType.H2).
				build();
	}  
    
//    @Bean
//	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
//		return new JpaTransactionManager(emf);
//	}
    
}

package com.anthem.oss.nimbus.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.rules.RulesDeployer;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiBehaviorFactory;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiDAO;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;


@Configuration
@AutoConfigureAfter(value={DataSourceAutoConfiguration.class})
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
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private ActivitiExpressionManager platformExpressionManager;
	
	
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager jpaTransactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {
    	
    	SpringProcessEngineConfiguration engineConfiguration = this.baseSpringProcessEngineConfiguration(dataSource, jpaTransactionManager, springAsyncExecutor);
    	engineConfiguration.setActivityBehaviorFactory(platformActivityBehaviorFactory());
    	engineConfiguration.setHistoryLevel(HistoryLevel.getHistoryLevelForKey(processHistoryLevel));
    	
    	List<Deployer> deployers = new ArrayList<>();
        deployers.add(new RulesDeployer());
        engineConfiguration.setCustomPostDeployers(deployers);
        
        List<Resource> resources = new ArrayList<>(Arrays.asList(engineConfiguration.getDeploymentResources()));
        Resource[] supportingResources = processResources();
        if(supportingResources != null && supportingResources.length > 0){
        	resources.addAll(Arrays.asList(processResources()));
        }
        Resource[] resss = new Resource[resources.size()];
        engineConfiguration.setDeploymentResources(resources.toArray(resss)); 
        engineConfiguration.setExpressionManager(platformExpressionManager);
        return engineConfiguration;
    }
    
    @Bean
   	public DefaultActivityBehaviorFactory platformActivityBehaviorFactory() {
   		return new ActivitiBehaviorFactory();
   	}
    
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    
    @Bean
    public ActivitiDAO platformProcessDAO(){
    	return new ActivitiDAO(dataSource);
    }
    
    
//    @Bean
//	public DriverManagerDataSource processDataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(dbDriver);
//		dataSource.setUrl(dbUrl);
//		dataSource.setUsername(dbUserName);
//		dataSource.setPassword(dbPassword);
//		return dataSource;
//	}  
    
    @Bean
	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
    
    protected Resource[] processResources() { 
        try {
        	PathMatchingResourcePatternResolver pmrs = new PathMatchingResourcePatternResolver();
        	//Resource[] rules = pmrs.getResources("rules-sample1/**.drl");
            Resource[] processDefs = pmrs.getResources("process-defs/**.xml");
			return ArrayUtils.addAll(processDefs);
		} catch (IOException e) {
			
		}
		return null;
    } 
	

}

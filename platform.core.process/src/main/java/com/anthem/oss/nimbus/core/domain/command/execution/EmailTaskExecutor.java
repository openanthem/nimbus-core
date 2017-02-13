/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.io.StringWriter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.anthem.nimbus.platform.core.process.api.QuadModelVelocityContext;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.builder.TemplateDefinition;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
public class EmailTaskExecutor extends AbstractProcessTaskExecutor implements HierarchyMatchProcessTaskExecutor {
    
	private static final String CHARSET_UTF8 = "UTF-8";
	
	@Autowired
    private VelocityEngine velocityEngine;
 
	@Setter@Getter
    private TemplateDefinition templateDefinition;
	
	//@Autowired
    private JavaMailSender javaMailSender;	

    @Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
    	
    	MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
    	
		Model<?> viewSAC = this.findQuad(cmdMsg).getView();
		
		// below is a test sample for test.vm. for actual flow quad model would be set with appropriate models
        viewSAC.findParamByPath("/pg1/patientInfo/patientInfoEntry/subscriberId").setState("123456789"); 
        
        QuadModelVelocityContext impl = new QuadModelVelocityContext(this.findQuad(cmdMsg));

        StringWriter stringWriter = new StringWriter();
        
        velocityEngine.mergeTemplate(templateDefinition.getId()+".vm", CHARSET_UTF8, impl, stringWriter);
        
        //TODO for Cheikh- email handler should be a seperate component 
		try {
			//TODO for Cheikh- email subject should also be read from velocity template
			helper.setSubject("hey");
			helper.setFrom("cheikh.niass@anthem.com");
			helper.setTo("rakeshkumar.patel@anthem.com");
			helper.setText(stringWriter.toString(),true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        return null;
	}

	@Override
	public String getUri() {
		return this.templateDefinition.getPath();
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
   

}
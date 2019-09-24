package com.antheminc.oss.nimbus.entity.fileUpload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.channel.web.WebCommandBuilder;
import com.antheminc.oss.nimbus.channel.web.WebCommandDispatcher;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.defn.FileUploadStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerReflection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileHandler {

	private static final String uploadStrategy = "fileUpload";
	private static final String uploadAction = "/upload";
	private static final String downloadAction = "/download";
	private static final String newActionKey = "/_new";
	public static final String strategyParam = "/strategy";

	protected final SessionProvider sessionProvider;
	protected final BeanResolverStrategy beanResolver;
	protected final JavaBeanHandlerReflection javaBeanHandler;
	private final ObjectMapper om;

	@Autowired
	WebCommandBuilder builder;

	@Autowired
	WebCommandDispatcher dispatcher;

	public FileHandler(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.sessionProvider = beanResolver.get(SessionProvider.class);
		this.javaBeanHandler = beanResolver.get(JavaBeanHandlerReflection.class);
		this.om = beanResolver.get(ObjectMapper.class);

	}

	public Object handleFileUpload(MultipartFile file, HttpServletRequest request) {
		// TODO : Scan File using virus scan - identify which one to use
		// Extract File Attributes
		if (file == null)
			throw new FrameworkRuntimeException("No File Sent  with 'upload' keyword. Please check usage and retry");
		else {

			String requestURI = request.getRequestURI();
			String uriInitial = StringUtils.substringBetween(requestURI, request.getContextPath(), uploadAction);

			String rootDomainAlias = StringUtils.substringBetween(requestURI, "/p/", uploadAction);

			String jsonPayload = createJsonPayload(request, file);

			String uri = uriInitial + newActionKey;
			Command cmd = builder.handleInternal(uri, request.getParameterMap());
			MultiOutput obj = dispatcher.handle(cmd, jsonPayload);
			Output<?> output = obj.findFirstParamOutputEndingWithPath("/" + rootDomainAlias);
			Param<?> newEntity = (Param<?>) output.getValue();
			String fileUploadStrategyIdentifier = getFileStrategy(rootDomainAlias, newEntity);

			FileUploadService fileUploadImpl = beanResolver.find(FileUploadService.class,
					uploadStrategy + "." + fileUploadStrategyIdentifier);
			if (fileUploadImpl == null) {
				throw new FrameworkRuntimeException(
						"No File Upload Strategy found for defined strategy  " + fileUploadStrategyIdentifier);
			} else {
				String externalId = fileUploadImpl.upload(file);
				newEntity.findParamByPath("/externalId").setState(externalId);
				newEntity.findParamByPath("/" + strategyParam).setState(fileUploadStrategyIdentifier);

				return newEntity.getState();
			}

		}
	}

	private String getFileStrategy(String rootDomainAlias, Param<?> newEntity) {
		// If strategy is passed as request Payload then load from request -if not then
		// load from Model Config

		if (newEntity.findStateByPath("/" + strategyParam) != null) {
			return newEntity.findStateByPath("/" + strategyParam).toString();
		} else {

			FileUploadStrategy fileUploadStrategyIdentifier = newEntity.getRootDomain().getConfig().getReferredClass()
					.getAnnotation(FileUploadStrategy.class);
			if (null == fileUploadStrategyIdentifier) {
				fileUploadStrategyIdentifier = newEntity.getRootDomain().getConfig().getReferredClass().getSuperclass()
						.getAnnotation(FileUploadStrategy.class);
			}
			if (null == fileUploadStrategyIdentifier) {
				throw new FrameworkRuntimeException(
						"No  Upload Strategy defined for class with domain alias :  " + rootDomainAlias);
			}
			return fileUploadStrategyIdentifier.value();
		}
	}

	private String createJsonPayload(HttpServletRequest request, MultipartFile file) {

		String fileName = file.getOriginalFilename();
		Long fileSize = file.getSize();
		String fileType = file.getContentType();
		StringBuilder sf = new StringBuilder();
		sf.append("{");
		addAttributeToStringBuilder("fileName", fileName, sf);
		addAttributeToStringBuilder("fileSize", fileSize, sf);
		addAttributeToStringBuilder("fileType", fileType, sf);
		addAttributeToStringBuilder("uploadedDateTime", ZonedDateTime.now(), sf);
		ClientUser loggedInUser = sessionProvider.getLoggedInUser();
		if (loggedInUser != null) {
			addAttributeToStringBuilder("uploadedBy", loggedInUser.getDisplayName(), sf);
			addAttributeToStringBuilder("uploadedByUserId", loggedInUser.getId(), sf);
		}

		request.getParameterMap().forEach((k, v) -> {
			addAttributeToStringBuilder(k, request.getParameter(k), sf);

		});
		sf.append("}");

		String jsonPayload = sf.toString().replace(",}", "}");
		return jsonPayload;
	}

	public void handleFileDownload(HttpServletRequest request,HttpServletResponse response) {
		// First determine the rootDomain Alias
		// Then determine the strategy to invoke
		// Then invoke the strategy to download the file
		String requestURI = request.getRequestURI();
		String rootDomainAlias = StringUtils.substringBetween(requestURI, "/p/", downloadAction);
		String fileId = request.getParameter("id");
		// Fire a Get url to get the strategy
		String uriInitial = StringUtils.substringBetween(requestURI, request.getContextPath(), downloadAction);
		// String jsonQuery =
		// "/_search?fn=query&where="+rootDomainAlias+".fileName.eq(\""+fileName+"\")&fetch=1";

		String jsonQuery = ":" + fileId + "/_get";

		String uri = uriInitial + jsonQuery;
		Command cmd = builder.handleInternal(uri, null);
		MultiOutput obj = dispatcher.handle(cmd, null);

		Output<?> output = obj.findFirstParamOutputEndingWithPath("/" + rootDomainAlias);
		Param<?> newEntity = (Param<?>) output.getValue();

		if (null == newEntity.findStateByPath("/" + strategyParam)) {
			throw new FrameworkRuntimeException(
					"No  Upload/download Strategy defined for file entry with domain alias :  " + rootDomainAlias
							+ "and ID :" + fileId);
		} else {
			String strategy = newEntity.findStateByPath("/" + strategyParam).toString();
			String externalId = newEntity.findStateByPath("/externalId").toString();
			String fileName =  newEntity.findStateByPath("/fileName").toString();
			String contentType =  newEntity.findStateByPath("/fileType").toString();

			FileUploadService fileUploadImpl = beanResolver.find(FileUploadService.class,
					uploadStrategy + "." + strategy);
			if (fileUploadImpl == null) {
				throw new FrameworkRuntimeException(
						"No File Download Strategy found for defined strategy  " + strategy);
			} else {
				
				InputStream inputStream = fileUploadImpl.download(externalId);
				// Set the content type and attachment header.
		    	response.addHeader("filename", "filename="+fileName+"."+contentType);
		    	response.addHeader("Content-disposition", "attachment");
		    	response.setContentType(contentType);
				try {
					IOUtils.copy(inputStream, response.getOutputStream());

				} catch (IOException e) {
					throw new FrameworkRuntimeException(
							"Unable to write Byte Input to Response for file external Id : "+ externalId, e);
				}
		    	
				
			}
		}
	}

	private void addAttributeToStringBuilder(String attributeName, Object attributeValue, StringBuilder sf) {
		sf.append("\"");
		sf.append(attributeName);
		sf.append("\"");
		sf.append(":");
		try {
			sf.append(om.writeValueAsString(attributeValue));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new FrameworkRuntimeException(
					"Unable to resolve request param with key " + attributeName + " and value :" + attributeValue, e);
		}
		sf.append(",");
	}
}

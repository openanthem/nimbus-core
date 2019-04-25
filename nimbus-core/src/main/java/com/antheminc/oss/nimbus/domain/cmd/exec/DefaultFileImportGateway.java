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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.channel.web.WebCommandBuilder;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.converter.Importer;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;

import lombok.Getter;

/**
 * <p> A gateway implementation to handle data file uploads. <p> Although it's
 * methods are not explicitly called within the framework code, this object
 * provides an entry point to centralize all import logic and give clients a way
 * to provide common data file import instructions to the gateway so that it may
 * process the request with minimal configuration from the client application.
 * 
 * @author Tony Lopez
 * @since 1.3
 *
 */
@Getter
public class DefaultFileImportGateway implements FileImportGateway {

	private final Collection<Importer> fileImporters;
	private final WebCommandBuilder webCommandBuilder;

	public DefaultFileImportGateway(BeanResolverStrategy beanResolver) {
		this.fileImporters = beanResolver.getMultiple(Importer.class);
		this.webCommandBuilder = beanResolver.get(WebCommandBuilder.class);
	}

	@Override
	public boolean doImport(HttpServletRequest req, String uploadToDomain, MultipartFile file) {
		if (file.isEmpty()) {
			return false;
		}

		// Build the upload command
		Command command = getWebCommandBuilder().build(req);
		Command uploadCommand = CommandBuilder
				.withPlatformRelativePath(command, Type.PlatformMarker, "/" + uploadToDomain).getCommand();
		uploadCommand.setRequestParams(command.getRequestParams());
		uploadCommand.setAction(Action._new);

		// Determine the importer to use
		String ext = FilenameUtils.getExtension(file.getName());
		Importer importer = getFileImporter(ext);
		if (null == importer) {
			throw new InvalidConfigException("Upload for file types of \"" + ext + "\" is not supported.");
		}

		// Handle the import
		try (InputStream inputStream = file.getInputStream()) {
			// TODO - add an entry to db when the file starts to process
			importer.doImport(uploadCommand, file.getInputStream());
			return true;
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Upload for " + file + " with command " + uploadCommand + " failed.",
					e);
		}
	}

	/**
	 * <p> Determine the file importer to use based on the given extension name.
	 * @param extension the extension for which to retrieve a file importer for.
	 * @return the file importer object
	 */
	protected Importer getFileImporter(String extension) {
		for (Importer fileImporter : getFileImporters()) {
			if (fileImporter.supports(extension)) {
				return fileImporter;
			}
		}
		return null;
	}
}

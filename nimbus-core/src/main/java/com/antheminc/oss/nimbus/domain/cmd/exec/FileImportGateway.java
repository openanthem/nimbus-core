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

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p> A gateway implementation to handle data file uploads.
 * 
 * @author Tony Lopez
 * @since 1.3
 *
 */
public interface FileImportGateway {

	/**
	 * <p> Import a file to the given domain by extracting import instructions
	 * from the given request object. <p> File upload is supported for as many
	 * types as there are defined {@link Importer} objects. The framework
	 * provides support for commonly used data file types (e.g. {@code ".csv"},
	 * {@code ".xlsx"}). If additional importers are needed, simply create an
	 * {@link Importer} bean and register it with the application context. <p>
	 * Special instructions for how the file should be parsed can be sent as
	 * query parameters alongside {@link Importer} implementation will pass
	 * along or use. Please refer to the specific {@link Importer}
	 * implementation for more details.
	 * @param req the request to use
	 * @param uploadToDomain the domain to upload the file contents to
	 * @param file the file to upload.
	 * @return {@code true} if successful, {@code false} otherwise.
	 */
	boolean doImport(HttpServletRequest req, String uploadToDomain, MultipartFile file);
}
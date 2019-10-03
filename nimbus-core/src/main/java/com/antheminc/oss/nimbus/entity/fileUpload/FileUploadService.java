package com.antheminc.oss.nimbus.entity.fileUpload;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;



public interface FileUploadService {
	
	

	public String upload(MultipartFile uploadFile) ;
	
	public InputStream download(String downloadFileExternalId);
	
	
}

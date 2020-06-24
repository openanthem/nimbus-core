package com.antheminc.oss.nimbus.entity.fileUpload;


import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.FileUploadStrategy;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="fileAttribute", includeListeners={ListenerType.persistence})
@Repo(alias="fileAttribute", value=Database.rep_mongodb)
@Getter @Setter @ToString
@FileUploadStrategy
public  class FileAttributes {


	private String fileName;
	private String fileType;
	private long fileSize;
	private ZonedDateTime uploadedDateTime;
	private String uploadedBy;
	private Long uploadedByUserId;
	private String uploadDomainName;
	@Id
	private long id;
	private String externalId;
	
	private String strategy;
	
}

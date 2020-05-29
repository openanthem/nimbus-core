package com.antheminc.oss.nimbus.entity.fileUpload;

import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo.DefaultMongoModelRepository;
import com.mongodb.client.gridfs.model.GridFSFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="fileUpload", includeListeners={ListenerType.update})

@Getter @Setter @ToString
public class FileUploadMongo implements FileUploadService{
	
	
	
	private MongoTemplate mongoTemplate;
	
	private GridFsTemplate gridFsTemplate ;
	

	
	public FileUploadMongo(BeanResolverStrategy beanResolver) {
		DefaultMongoModelRepository defaultMongoModelRepository = beanResolver.get(DefaultMongoModelRepository.class,"rep_mongodb");
		this.mongoTemplate = (MongoTemplate) defaultMongoModelRepository.getMongoOps();
		this.gridFsTemplate =  new GridFsTemplate(mongoTemplate.getMongoDbFactory(),mongoTemplate.getConverter());
		
	}
	
	@Override
	public String upload(MultipartFile uploadFile) {
		
		ObjectId externalId;
		try {
			externalId = gridFsTemplate.store(uploadFile.getInputStream(),uploadFile.getOriginalFilename());
			return externalId.toString();
		} catch (IOException e) {
			throw new FrameworkRuntimeException("Unable to upload File to GridFs with FileName "+uploadFile.getOriginalFilename(),e );
		}
		
	}

	@Override
	public InputStream download(String downloadFileExternalId) {
		Query q = new Query().addCriteria(Criteria.where("_id").is(downloadFileExternalId));
		GridFSFile gridFsFile = gridFsTemplate.findOne(q);
		if(null==gridFsFile) {
			throw new FrameworkRuntimeException("Unable to find file with Id "+downloadFileExternalId );
		}
		else {
			GridFsResource resource = gridFsTemplate.getResource(gridFsFile.getFilename());
			try {

				return resource.getInputStream();
				
			} catch (Exception e) {
				throw new FrameworkRuntimeException("Unable to write located file  with Id :"+downloadFileExternalId+" to InputStream "+e );
			}
		}
		
	}

}

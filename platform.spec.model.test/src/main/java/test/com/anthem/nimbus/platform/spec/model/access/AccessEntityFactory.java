/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.access;

import java.util.Arrays;

import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
import com.anthem.oss.nimbus.core.entity.access.Permission;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
public class AccessEntityFactory {

	public static Builder builder() {
		return new Builder();
	}

	@Getter 
	public static class Builder {
		
		public interface Callback {
			public void visit(DefaultAccessEntity ae);
		}
		
		private Builder root;
		
		private Builder parent;
		private DefaultAccessEntity current;
		
		private Builder() {
			this.root = this;
		}
		
		public Builder push(DefaultAccessEntity current) {
			Builder c = (this.current==null) ? this : new Builder();	//handle root node
			c.root = this.root;
			c.current = current;
			c.parent = this;	//root node points back to itself for parent
			return c;
		}
		
		private Builder addAndPush(DefaultAccessEntity ae) {
			if(this.current != null) {
				this.current.addNestedAccess(ae);
			}
			return this.push(ae);
		}
		
		public Builder pop() {
			return this.parent;
		}
		
		public static void buildEntity(DefaultAccessEntity ae, String code, String name) {
			ae.setCode(code);
			ae.setName(name);
		}
		
		public Builder buildCurrent(Callback cb) {
			cb.visit(this.current);
			return this;
		}

		private Builder buildCurrentWithCodeAndName(String code, String name, String domainURI) {
			return buildCurrent((ae) -> ae.setCode(code))
					.buildCurrent((ae) -> ae.setName(name))
					.buildCurrent((ae) -> ae.setDomainUri(domainURI));
					
		}
		
		public Builder createPlatform() {
			DefaultAccessEntity platform = new DefaultAccessEntity.Platform();
			return addAndPush(platform);
		}
		
		public Builder createPlatform(String code, String name) {
			return createPlatform()
					.buildCurrentWithCodeAndName(code, name,null);
		}
		

		public Builder createApplication() {
			DefaultAccessEntity app = new DefaultAccessEntity.Application();
			return addAndPush(app);
		}
		
		public Builder createApplication(String code, String name, String domainURI) {
			return createApplication()
					.buildCurrentWithCodeAndName(code, name, domainURI);	
		}

		public Builder createFeature() {
			DefaultAccessEntity feature = new DefaultAccessEntity.Feature();
			return addAndPush(feature);
		}
		
		public Builder createFeature(String code, String name, String domainURI) {
			return createFeature()
					.buildCurrentWithCodeAndName(code, name,domainURI);
		}
		
		public Builder createModule() {
			DefaultAccessEntity module = new DefaultAccessEntity.Module();
			return addAndPush(module);
		}
		
		public Builder createModule(String code, String name, String domainURI) {
			return createModule()
					.buildCurrentWithCodeAndName(code, name,domainURI);
		}
		
		public Builder addPermission(Permission...permissions) {
			Arrays.asList(permissions).forEach((p) -> this.current.addAvailablePermission(p));
			return this;
		}

		
	}

	public static String getApp1_code() {
		//return "provider-app";
		return "HRS";
	}
	
	public static String getApp2_code() {
		//return "um-app";
		return "ICR";
	}
	
	public static String getApp3_code() {
		return "dm-app";
	}
	
	public static String getApp4_code() {
		return "cm-app";
	}
	
	public static DefaultAccessEntity createPlatformAndSubTree() {

		return
		AccessEntityFactory.builder()
		.createPlatform("nimbus", "Nimbus")
			.createApplication(getApp1_code(), "um-web-provider",getApp1_code())
				.addPermission(new Permission.Access())
				.createFeature("dashboard", "Member Dashboard","member/dashboard")
					.addPermission(new Permission.Access(), new Permission.Read(), new Permission.Update())
				.pop()
				.createFeature("search", "Search Member","member/search")
					.addPermission(new Permission.Create())
				.pop()
			.pop()
			.createApplication(getApp2_code(), "um-web-clinician",getApp2_code())
				.createFeature("pcr-review", "Internal Physican Review","pcr-review")
					.addPermission(new Permission.Access(), new Permission.Update())
				.pop()
			.pop()	
			.createApplication(getApp3_code(), "um-web-intake",getApp3_code())
			.pop()
			.createApplication(getApp4_code(), "cm-web-clinician",getApp4_code())
			
		.getRoot()
		.getCurrent();
	}
	
	public static DefaultAccessEntity createPlatformAndSubTree1() {

		return
		AccessEntityFactory.builder()
		.createPlatform("nimbus", "Nimbus")
			.createApplication("HRS", "HRS",null)
				.createModule("Member", "Member module", "member")
					.addPermission(new Permission.Create(), new Permission.Read(), new Permission.Update())
						.createFeature("CreateMember", "CreateMember", "member/_new")
							.addPermission(new Permission.Create(), new Permission.Read())
						.pop()
						.createFeature("SearchMember", "SearchMember", "member/_search")
							.addPermission(new Permission.Read())
						.pop()
				.pop()
				.createModule("UMCase", "UM Case management", "umCase")
					.addPermission(new Permission.Create(), new Permission.Read(), new Permission.Update())
						.createFeature("CreateCase","create UM Case","um-case/_new")
							.addPermission(new Permission.Create(), new Permission.Read())
						.pop()
						.createFeature("UpdateCase","update UM Case","um-case/_update")
							.addPermission(new Permission.Update(), new Permission.Read())
						.pop()
				.pop()
			.pop()
			.createApplication("ICR", "ICR",null)
				.createModule("Provider", "Manage provider", "provider")
					.addPermission(new Permission.Create(), new Permission.Read(), new Permission.Update(),new Permission.Delete())
						.createFeature("SearchProvider", "SearchProvider","provider/_search")
							.addPermission(new Permission.Read())
						.pop()
						.createFeature("DeleteProvider", "DeleteProvider","provider/_delete")
							.addPermission( new Permission.Read(), new Permission.Delete())
						.pop()
				.pop()
			.pop()
		.getRoot()
		.getCurrent();
	}

}

/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core.events.listeners;

import java.util.List;

import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.Book.Publisher;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Converters;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.MultiSelect;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.model.state.internal.IdParamConverter;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Test data
 * @author Swetha Vemuri
 *
 */
abstract class TestModelFlowData {
	
	@Domain(value="core_book", includeListeners={ListenerType.persistence})
	@Model(value="core_book", excludeListeners={ListenerType.websocket})
	@Repo(Database.rep_mongodb)
	public static class Book extends AbstractEntity.IdString {

		private static final long serialVersionUID = 1L;
	    
		@Getter @Setter private String name;

		@Getter @Setter private String category;
		
		@Getter @Setter private Author author;
		
		@Getter @Setter private List<Publisher> publishers;
		
		@Getter @Setter private List<Publisher> supportingpublishers;
		
		@Getter @Setter
		public static class Author extends AbstractEntity.IdString {
			@Ignore
			private static final long serialVersionUID = 1L;
			
			private String firstName;

			private String lastName;
		}
		
		@Getter @Setter
		public static class Publisher extends AbstractEntity.IdString {
			@Ignore
			private static final long serialVersionUID = 1L;
			
			private String name;

			private String location;
			
			private String code;
		}
		
	}
	
	@Domain(value="view_book",includeListeners={ListenerType.websocket}) @MapsTo.Type(Book.class)
	@Getter @Setter
	public static class OrderBookFlow {
		private String orderId;
		
		@MapsTo.Path("id")
		@Converters(converters={IdParamConverter.class})
		private String orderDisplayId;
		
		@Page
		private Page_Pg1 pg1;	
		
		@MapsTo.Type(Book.class)
		@Getter @Setter
		public static class Page_Pg1 {
			
			@Path("/category") private @MultiSelect String genre;
			
			private @TextBox String isbn;

			@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
			private String back;		
			
			@Path("/publishers") private List<Publisher> publishingHouse;
			
			@Path("/supportingpublishers") private List<Publisher> shortListPublishers;
			
			@MapsTo.Type(Publisher.class)
			@Getter @Setter
			public static class Section_ServiceLine {
				
				private String someViewOnlyParam;
				
				@Path private String name;
			}
		}
	}
	
	@Domain(value="core_book1")
	@Model(value="core_book1", excludeListeners={ListenerType.websocket})
	@Repo(Database.rep_mongodb)
	public static class BookWithoutPersistenceListener extends AbstractEntity.IdString {

		private static final long serialVersionUID = 1L;
	    
		@Getter @Setter private String title;

		@Getter @Setter private String category;
		
	}
	
	@Domain(value="view_book1") @MapsTo.Type(BookWithoutPersistenceListener.class)
	@Getter @Setter
	public static class OrderBook1Flow {
		private String orderId;
		
		@MapsTo.Path("id")
		@Converters(converters={IdParamConverter.class})
		
		private String orderDisplayId;
	}
	
	@Domain(value="core_book2", includeListeners={ListenerType.persistence})
	@Model(value="core_book2", excludeListeners={ListenerType.websocket})
	public static class BookWithoutRepo extends AbstractEntity.IdString {

		private static final long serialVersionUID = 1L;
	    
		@Getter @Setter private String title;

		@Getter @Setter private String category;
		
	}	
	
	@Domain(value="view_book2",includeListeners={ListenerType.websocket}) @MapsTo.Type(BookWithoutRepo.class)
	@Getter @Setter
	public static class OrderBook2Flow {
		private String orderId;
		
		@MapsTo.Path("id")
		@Converters(converters={IdParamConverter.class})
		
		private String orderDisplayId;
	}
	
	@Domain(value="core_book3", includeListeners={ListenerType.persistence,ListenerType.websocket})
	@Model(value="core_book3", excludeListeners={ListenerType.persistence,ListenerType.websocket})
	public static class BookWithExcludedListener extends AbstractEntity.IdString {

		private static final long serialVersionUID = 1L;
	    
		@Getter @Setter private String title;

		@Getter @Setter private String category;
		
	}	
	
	@Domain(value="view_book3",includeListeners={ListenerType.websocket}) @MapsTo.Type(BookWithExcludedListener.class)
	@Getter @Setter
	public static class OrderBook3Flow {
		private String orderId;
		
		@MapsTo.Path("id")
		@Converters(converters={IdParamConverter.class})
		
		private String orderDisplayId;
	}
	
}

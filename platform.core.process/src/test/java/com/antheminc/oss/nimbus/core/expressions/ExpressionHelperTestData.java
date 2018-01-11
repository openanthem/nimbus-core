package com.antheminc.oss.nimbus.core.expressions;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.core.domain.definition.Converters;
import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Button;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Form;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.MultiSelect;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.IdParamConverter;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;
import com.antheminc.oss.nimbus.core.expressions.ExpressionHelperTestData.Book.Publisher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
abstract class ExpressionHelperTestData {
	
	@Domain(value="core_book", includeListeners={ListenerType.persistence})
	@Model(value="core_book", excludeListeners={ListenerType.websocket})
	@Repo(Database.rep_mongodb)
	public static class Book extends AbstractEntity.IdString {
		
		@Getter @Setter private String name;

		@Getter @Setter private String category;
		
		@Getter @Setter private Author author;
		
		@Getter @Setter private List<Author> coAuthors;
		
		@Getter @Setter private List<Publisher> publishers;
		
		@Getter @Setter private List<Publisher> supportingpublishers;
		
		
		@Getter @Setter
		public static class Publisher extends AbstractEntity.IdString {
			@Ignore
			private static final long serialVersionUID = 1L;
			
			private String name;

			private String location;
			
			private String code;
		}		
	}
	
	@Domain(value="author", includeListeners={ListenerType.persistence}) 
	@Repo(Repo.Database.rep_mongodb)
	@Getter @Setter @ToString(callSuper=true)
	public static class Author extends AbstractEntity.IdString {
		@Ignore
		private static final long serialVersionUID = 1L;
		
		private String firstName;

		private String lastName;
	}
	
	@Domain(value="view_book",includeListeners={ListenerType.websocket}) @MapsTo.Type(Book.class)
	@Getter @Setter
	public static class OrderBookFlow {
		private String orderId;
		
		@MapsTo.Path("id")
		@Converters(converters={IdParamConverter.class})
		private String orderDisplayId;
		
		@Page
		//@MapsTo.Path(linked=false,state=State.External)
		private Page_Pg1 pg1;
		
		@Page
	//	@MapsTo.Path(linked=false,state=State.External)
		private Page_Pg2 pg2;
		
		@Page
	//	@MapsTo.Path(linked=false,state=State.External)
		private Page_Pg3 pg3;
		
		@Model
		@MapsTo.Type(Book.class)
		@Getter @Setter
		public static class Page_Pg1 {
			
			@Path("/category") private @MultiSelect String genre;
			
			@Path("/name") private @MultiSelect String displayName;
			
			private @TextBox String isbn;

			@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
			private String back;		
			
			@Path("/publishers") private List<Publisher> publishingHouse;
			
			@MapsTo.Path(linked=false)
			private List<AuthorGrid> coAuthors;
			
			@Path("/supportingpublishers") private List<Publisher> shortListPublishers;			
		}
		@Model
		@MapsTo.Type(Book.class)
		@Getter @Setter
		public static class Page_Pg3 {
			
			@Section 
			private Section_overview bookOverview;
			
			@Getter @Setter
			@MapsTo.Type(Book.class)
			public static class Section_overview {
				 
			}
		}
		
		@Model
		@MapsTo.Type(Author.class)
		@Getter @Setter
		public static class AuthorGrid {
			
			@Path("lastName") private String name;
		}
		
		@Model
		@MapsTo.Type(Book.class)
		@Getter @Setter
		public static class Page_Pg2 {
			
			@Section
			private Section_pg2 section_1;
			
			@Path("/category") private @MultiSelect String genre;
			
			private @TextBox String isbn;

			@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
			private String back;		
			
			@Path("/publishers") private List<Publisher> publishingHouse;
			
			@Path("/supportingpublishers") private List<Publisher> shortListPublishers;
		}
		
		@Model
		@Getter @Setter
		public static class Section_pg2 {
			@Form(cssClass="threeColumn", submitUrl="/view_book:{id}/_submitOrder/_process", b="$executeAnd$config", submitButton=false)
			private Form_OrderBook orderBookForm;
		}
		
		@Model
		@MapsTo.Type(Book.class)
		@Getter @Setter
		public static class Form_OrderBook {
			
			@Path("/name") private String bookName;
			
			@Path private String category;
			
			@Path("author/firstName") private String authorName;
		}
	}
}

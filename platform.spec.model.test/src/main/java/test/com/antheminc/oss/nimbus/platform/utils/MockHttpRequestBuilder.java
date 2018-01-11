/**
 * 
 */
package test.com.antheminc.oss.nimbus.platform.utils;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.command.Behavior;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public final class MockHttpRequestBuilder {

	private final MockHttpServletRequest httpReq;
	
	public MockHttpServletRequest getMock() {
		return httpReq;
	}
	
	public static MockHttpRequestBuilder withUri(String uri) {
		return withUri(HttpMethod.GET, uri);
	}
	
	public static MockHttpRequestBuilder withUri(HttpMethod httpMethod, String uri) {
		return new MockHttpRequestBuilder(new MockHttpServletRequest(httpMethod.name(), uri));
	}
	
	public MockHttpRequestBuilder addRefId(String refId) {
		String uri = httpReq.getRequestURI() + ":" + refId;
		httpReq.setRequestURI(uri);
		
		return this;
	}

	public MockHttpRequestBuilder addNested(String nestedPath) {
		String uri = httpReq.getRequestURI() + nestedPath;
		httpReq.setRequestURI(uri);
		
		return this;
	}

	
	public MockHttpRequestBuilder addAction(Action a) {
		String uri = httpReq.getRequestURI() + "/" + a.name();
		httpReq.setRequestURI(uri);
		
		return this;
	}
	
	public MockHttpRequestBuilder addBehavior(Behavior b) {
		httpReq.addParameter(Constants.MARKER_URI_BEHAVIOR.code, b.name());
		
		return this;
	}
	
	public MockHttpRequestBuilder addParam(String name, String value) {
		httpReq.addParameter(name, value);
		
		return this;
	}
}

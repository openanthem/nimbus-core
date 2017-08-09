/**
 * 
 */
package test.com.anthem.nimbus.platform.utils;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.domain.command.Action;

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
}

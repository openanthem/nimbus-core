package com.antheminc.oss.nimbus.platform.spec.model.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
/**
 * @author Sandeep Mantha
 *
 */
public class ResponsePageRest<T> extends PageImpl<T> {
	
	private static final long serialVersionUID = 1L;
	
    
	public ResponsePageRest(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

    public ResponsePageRest(List<T> content) {
		super(content);
	}
	
    public ResponsePageRest() {
		super(new ArrayList<T>());
	}

}

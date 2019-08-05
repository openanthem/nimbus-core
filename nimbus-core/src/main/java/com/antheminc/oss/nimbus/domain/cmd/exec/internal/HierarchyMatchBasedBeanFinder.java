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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.HierarchyMatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class HierarchyMatchBasedBeanFinder implements ApplicationContextAware {
	
	ApplicationContext ctx;
	
	@Value("${nimbus.process.key.regex:([A-Za-z0-9_\\-\\*~\\?=\\.\\$]+)}") 
	private String processBeanRegex;
	
	public static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
	
	public<T extends HierarchyMatch> T findMatchingBean(Class<T> type, String beanIdToFind) {
		Map<String, T> beans = this.getCtx().getBeansOfType(type);
		if(MapUtils.isEmpty(beans)) return null;
		List<String> beanNamesToMatch = new ArrayList<>();
		beans.entrySet().forEach((entry) -> {
			beanNamesToMatch.add(entry.getKey());
		});
		String matchedBeanName = findMatchingBean(beanIdToFind, beanNamesToMatch);
		
		Entry<String, T> matchedEntry = beans.entrySet().stream().
				filter((bean)-> bean.getKey().equals(matchedBeanName)).
				findFirst().orElse(null);
		return Optional.ofNullable(matchedEntry).map(Entry::getValue).orElse(null);
	}	
	
	public String findMatchingBean(String beanIdToFind, List<String> beans) {
		Collections.sort(beans, (o1, o2) -> {
				Pattern p1 = createHierarchyMatchRegexPattern(o1);
				Pattern p2 = createHierarchyMatchRegexPattern(o2);
				Matcher m1 = p1.matcher(o1);
	            Matcher m2 = p2.matcher(o2);
	            m1.matches();
	            m2.matches();
	            if(m2.groupCount() > m1.groupCount()) {
	            	return 1;
	            }
	            else if(m2.groupCount() < m1.groupCount()) {
	            	return -1;
	            }
	            else{
		            for(int count = m1.groupCount();count > 0; count--) {
		        	   if(m2.group(count).compareTo(m1.group(count)) != 0) {
		        		   return m2.group(count).compareTo(m1.group(count));
		        	   }
		            }
	            }
	            return 0;
	        });
		List<BeanKeyForMatching> deployedBeans = new ArrayList<BeanKeyForMatching>();
		beans.forEach((bean)-> deployedBeans.add(new BeanKeyForMatching(bean)));
		for(BeanKeyForMatching deployedBean : deployedBeans){
			if(deployedBean.matches(beanIdToFind)){
				return deployedBean.getBeanId();
			}
		}
		return null;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
	
	
	private Pattern createHierarchyMatchRegexPattern(String beanUri) {
		StringBuilder ptrnStr = new StringBuilder();
		String[] cmdUriTokens = StringUtils.split(beanUri, Constants.SEPARATOR_URI.code);
		for(int i = 0; i < cmdUriTokens.length; i++) {
			if(i > 0) {
				ptrnStr.append(Constants.SEPARATOR_URI.code);
			}
			ptrnStr.append(getProcessBeanRegex());
		}
		return Pattern.compile(ptrnStr.toString());
	}
	
	@Getter @Setter
	class BeanKeyForMatching implements Comparator<BeanKeyForMatching>{
		
		private String beanId;
		private String beanIdForMatching;
		private Pattern pattern;
		
		public BeanKeyForMatching(String beanId){
			this.beanId = beanId;
			beanIdForMatching = SPECIAL_REGEX_CHARS.matcher(beanId).replaceAll("\\\\$0");
			beanIdForMatching = beanIdForMatching.replaceAll("default\\\\.", "(.*?)\\.");
			pattern = Pattern.compile(beanIdForMatching);
		}
		
		@Override
		public int compare(BeanKeyForMatching o1, BeanKeyForMatching o2) {
			return o1.getBeanId().compareTo(o2.getBeanId());
		}
		
		/**
		 * 
		 * @param key
		 * @return
		 */
		public boolean matches(String key){
			return pattern.matcher(key).matches();
		}
	}
	
	
}

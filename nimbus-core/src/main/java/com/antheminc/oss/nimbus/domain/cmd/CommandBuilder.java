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
package com.antheminc.oss.nimbus.domain.cmd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidArgumentException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public class CommandBuilder {

	private final Command cmd;
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	public Command getCommand() {
		if(CollectionUtils.isEmpty(cmd.getBehaviors())) 
			cmd.templateBehaviors().add(Behavior.$execute);
		
		return cmd;
	}
	
	public static CommandBuilder withUri(String absoluteUri) {
		CommandBuilder cb = new CommandBuilder(new Command(absoluteUri));
		cb.handleUriAndParamsIfAny(absoluteUri);
		return cb;
	}
	
	public static CommandBuilder withDomainRelativePath(Command rootCmd, String domainRelPath) {
		String rootDomainAlias = rootCmd.getRootDomainAlias();
		String nestedDomainPath = StringUtils.removeStart(Constants.SEPARATOR_URI.code + rootDomainAlias, domainRelPath);
		
		String absoluteParamUri = rootCmd.getAbsoluteUri() + nestedDomainPath;
		return withUri(absoluteParamUri);
	}

	public static CommandBuilder from(Command src, String replaceDomainRootAlias) {
		Command tmpCmd = src.createRootDomainCommand();
		tmpCmd.getRootDomainElement().setAlias(replaceDomainRootAlias);
		
		String newAbsoluteUri = tmpCmd.buildUri(Type.DomainAlias);
		return withUri(newAbsoluteUri);
	}
	
	public CommandBuilder stripRequestParams() {
		cmd.setRequestParams(null);
		return this;
	}
	
	/**
	 * 
	 * @param absoluteUri
	 * @return
	 */
	protected CommandBuilder handleUriAndParamsIfAny(String absoluteUri) {
		logit.debug(() -> "absoluteUri: " + absoluteUri);
		String splits[] = StringUtils.split(absoluteUri, "?", 2);	//if request params are present
		
		logit.debug(() -> "splits: " + Arrays.toString(splits));
		
		if (splits == null || splits.length < 1) {
			throw new InvalidArgumentException("invalid absoluteUri passed in: " + absoluteUri);
		}
		
		//splits.length == 1 or 2
		handleOnlyUri(splits[0]);
		
		if(splits.length == 1) return this;
		
		if(splits.length > 2) {
			throw new InvalidArgumentException("unsupported scenario which has multiple parameter separators (?) in uri. "
					+ "Expected 1 found " + splits.length + " for absoluteUri: " + absoluteUri);
		}
		
		//splits.length==2
		final Map<String, String[]> rParams = new HashMap<>();
		logit.debug(() -> "params: " + splits[1]);

		String pSplits[] = StringUtils.split(splits[1], '&');
		String key = null;
		for(String pSplit: pSplits) {
			String pair[] = StringUtils.split(pSplit, "=", 2);
			String val[] = null;
			if(pair.length == 2) {
				key = pair[0];
				val = (rParams.containsKey(key)) ? ArrayUtils.add(rParams.get(key), pair[1])
						: new String[] { pair[1] };
			}else {
				val = rParams.get(key);
				val[val.length-1] = val[val.length-1]+"&"+pair[0];
			}
			
			rParams.put(key, val);		
		}

		addParams(rParams);
		return this;
	} 
	
    /**
     * 
     * @param uri
     * @return
     */
	protected CommandBuilder handleOnlyUri(String uri) {
		String uriSplit[] = StringUtils.split(uri, Constants.SEPARATOR_URI.code);
		if(ArrayUtils.isEmpty(uriSplit)) return this;
		
		CommandElementLinked cmdElem;
		final int startingIndex;
		
		/* handle event */
		String uri_0 = uriSplit[0];
		if(StringUtils.startsWith(uri_0, Constants.PREFIX_EVENT_URI.code)) {
			String event = StringUtils.substringAfter(uri_0, Constants.PREFIX_EVENT_URI.code);
			cmd.setEvent(event);
			cmdElem = cmd.createRoot(Type.ClientAlias, uriSplit[1]);
			startingIndex = 2;
		} 
		else {
			cmdElem = cmd.createRoot(Type.ClientAlias, uri_0);
			startingIndex = 1;
		}
		
		Type type = Type.ClientOrgAlias;
		
		for(int i=startingIndex; i<uriSplit.length; i++) {
			String val = uriSplit[i];
			
			if(StringUtils.equals(val, Constants.MARKER_URI_PLATFORM.code)) {
				cmdElem = cmdElem.createNext(Type.PlatformMarker, val);
				type = Type.DomainAlias;
			} 
			else if(EnumUtils.isValidEnum(Action.class, val)) {
				cmd.setAction(Action.valueOf(val));
				
				/* back track and handle if Action is _process */
				if(Action._process == cmd.getAction()) {
					cmd.traverseElements(cmd.root().findFirstMatch(Type.DomainAlias).next(),
							(curr) -> curr.setType(Type.ProcessAlias));
				}
			} 
			else if (i + 1 < uriSplit.length && //look ahead for /p and handle {appCd}
					StringUtils.equals(uriSplit[i + 1], Constants.MARKER_URI_PLATFORM.code)) {
				
				cmdElem = cmdElem.createNext(Type.AppAlias, val);
				
			} 
			else {
				if (type == Type.DomainAlias && StringUtils.contains(val, Constants.SEPARATOR_URI_VALUE.code)) {
					String codeAndRefId[] = StringUtils.split(val, Constants.SEPARATOR_URI_VALUE.code);

					if (codeAndRefId.length != 2)
						throw new InvalidConfigException(
								"Command code and refId must have the format of /{domainAlias}:{refId}");

					cmdElem = cmdElem.createNext(type, codeAndRefId[0]);
					cmdElem.setRefId(Long.valueOf(codeAndRefId[1]));
				} 
				else {
                    cmdElem = cmdElem.createNext(type, val);
				}
			}
		}
		
		return this;
	}
	
	/**
	 * 
	 * @param rParams
	 * @return
	 */
	public CommandBuilder addParams(Map<String, String[]> rParams) {
		if(MapUtils.isEmpty(rParams))
			return this;
		
		String[] bArr = rParams.get(Constants.MARKER_URI_BEHAVIOR.code);
		if(ArrayUtils.isNotEmpty(bArr)) {
			LinkedList<Behavior> behaviors = new LinkedList<>();
			
			for(String bStr : bArr) {
				String bSplits[] = StringUtils.splitByWholeSeparator(bStr, Constants.SEPARATOR_AND.code);
				
				for(String bFinal : bSplits) {
					Behavior bh = Behavior.valueOf(bFinal);
					behaviors.add(bh);
				}
			}
			cmd.setBehaviors(behaviors);
		} 
		else {
			//TODO Implement client/app => heirarchy based config for default behavior
			cmd.setBehaviors(new LinkedList<>(Arrays.asList(Behavior.DEFAULT)));
		}
		if(MapUtils.isNotEmpty(cmd.getRequestParams()))
			cmd.getRequestParams().putAll(rParams);	
		else
			cmd.setRequestParams(rParams);
		
		return this;
	}
	
}

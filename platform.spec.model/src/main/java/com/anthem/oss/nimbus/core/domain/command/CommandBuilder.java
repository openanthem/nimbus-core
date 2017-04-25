/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.InvalidArgumentException;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.util.JustLogit;

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
		return cmd;
	}
	
	/**
	 * 
	 * @param absoluteUri
	 * @return
	 */
	public static CommandBuilder withUri(String absoluteUri) {
		CommandBuilder cb = new CommandBuilder(new Command(absoluteUri));
		cb.handleUriAndParamsIfAny(absoluteUri);
		return cb;
	}
	
	/**
	 * 
	 * @param absoluteUri
	 * @return
	 */
	protected CommandBuilder handleUriAndParamsIfAny(String absoluteUri) {
		logit.debug(() -> "absoluteUri: " + absoluteUri);
		String splits[] = StringUtils.split(absoluteUri, '?');	//if request params are present
		
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
		Arrays.asList(pSplits).forEach(kv -> {
			String pair[] = StringUtils.split(kv, '=');
            String key = pair[0];
			String val[] = (rParams.containsKey(key)) ? ArrayUtils.add(rParams.get(key), pair[1])
					: new String[] { pair[1] };

			rParams.put(key, val);
		});

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
					cmdElem.setRefId(codeAndRefId[1]);
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
		cmd.setRequestParams(rParams);
		
		return this;
	}
	
}

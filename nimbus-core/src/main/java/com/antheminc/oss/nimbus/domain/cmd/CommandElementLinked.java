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

import java.io.Serializable;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */ 
@Getter @Setter @ToString(callSuper=true)
public class CommandElementLinked extends CommandElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int seqNum;
	
	@Getter(value=AccessLevel.PROTECTED)
	private CommandElementLinked next;
	
	public CommandElementLinked() {
		
	}
	
	public CommandElementLinked(int seqNum, Type type, String alias, Long refId) {
		setSeqNum(seqNum);
		setType(type);
		setAlias(alias);
		setRefId(refId);
	}
	
	public CommandElementLinked(CommandElementLinked source) {
		this(source.getSeqNum(), source.getType(), source.getAlias(), source.getRefId());
		CommandElementLinked nextCloned = Optional.ofNullable(source.getNext()).map(n -> new CommandElementLinked(n)).orElse(null);
		setNext(nextCloned);
	}
	
	public CommandElementLinked cloneUpto(Type type, StringBuilder absoluteUri) {
		if(getType().equals(type))
			return null;
		CommandElementLinked cloned = new CommandElementLinked(getSeqNum(), getType(), getAlias(), getRefId());
		absoluteUri.append(getUri());
		CommandElementLinked nextCloned = Optional.ofNullable(getNext()).map(n -> n.cloneUpto(type, absoluteUri)).orElse(null);
		
		if(nextCloned != null)
			cloned.setNext(nextCloned);
		
		return cloned;
	}
	
	public CommandElementLinked next() {
		return next;
	}
	
	public boolean hasNext() {
		return next() != null;
	}

	@Override
	public void detachChildElements() {
		setNext(null);
	}
	
	public CommandElementLinked findFirstMatch(Type name) {
		if(getType() == null) return null;
		
		if(getType() == name) return this;
		
		if(next() == null) return null;
		
		return next().findFirstMatch(name);
	}
	
	public CommandElementLinked createNext(Type name, String uri) {
		CommandElementLinked nextElem = new CommandElementLinked();
		nextElem.setSeqNum(this.seqNum + 1);
		nextElem.setType(name);
		nextElem.setUri(uri);
		
		this.setNext(nextElem);
		return nextElem;
	}
	
	public void setNext(CommandElementLinked elem) {
		if(elem == null) {
			next = null;
			return;
		}
		
		int order = elem.getType().compareTo(getType());
		
		if(order < 0) throw new IllegalArgumentException();
		
		if (order == 0 && !getType().isRecursive())
			throw new IllegalArgumentException(
					"Element found which must not be recursive: " + elem.getType() + " with uri: " + elem.getUri());
		
		this.next = elem;
	}
	
}

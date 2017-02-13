/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command;

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
	
	
	/**
	 * 
	 */
	@Override
	public CommandElementLinked clone() {
		CommandElementLinked cloned = new CommandElementLinked();
		shallowCopy(cloned);

		CommandElementLinked nextCloned = Optional.ofNullable(getNext()).map(n -> n.clone()).orElse(null);
		cloned.setNext(nextCloned);

		return cloned;
	}
	
	public void shallowCopy(CommandElementLinked cloned) {
		cloned.setSeqNum(getSeqNum());
		super.shallowCopy(cloned);
	}
	
	/**
	 * 
	 * @param type
	 * @param absoluteUri
	 * @return
	 */
	public CommandElementLinked cloneUpto(Type type, StringBuilder absoluteUri) {
		if(getType().equals(type))
			return null;
		CommandElementLinked cloned = new CommandElementLinked();
		cloned.setSeqNum(getSeqNum());
		shallowCopy(cloned);
		absoluteUri.append(getUri());
		CommandElementLinked nextCloned = Optional.ofNullable(getNext()).map(n -> n.cloneUpto(type, absoluteUri)).orElse(null);
		
		if(nextCloned != null)
			cloned.setNext(nextCloned);
		
		return cloned;
	}
	
	/**
	 * 
	 * @return
	 */
	public CommandElementLinked next() {
		return next;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return next() != null;
	}
	
	/**
	 * 
	 */
	@Override
	public void detachChildElements() {
		setNext(null);
	}
	
	
	/*public void doIfFound(Supplier<CommandElement> predicate, Supplier<>) {
		
	}*/
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public CommandElementLinked findFirstMatch(Type name) {
		if(getType() == null) return null;
		
		if(getType() == name) return this;
		
		if(next() == null) return null;
		
		return next().findFirstMatch(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param uri
	 * @return
	 */
	public CommandElementLinked createNext(Type name, String uri) {
		CommandElementLinked nextElem = new CommandElementLinked();
		nextElem.setSeqNum(this.seqNum + 1);
		nextElem.setType(name);
		nextElem.setUri(uri);
		
		this.setNext(nextElem);
		return nextElem;
	}
	
	/**
	 * 
	 * @param elem
	 */
	public void setNext(CommandElementLinked elem) {
		if(elem == null) {
			next = null;
			return;
		}
		
		int order = elem.getType().compareTo(getType());
		
		if(order == -1) throw new IllegalArgumentException();
		
		if (order == 0 && !Type.allowedRecursive.contains(getType()))
			throw new IllegalArgumentException(
					"Element found which must not be recursive: " + elem.getType() + " with uri: " + elem.getUri());
		
		this.next = elem;
	}
	
}

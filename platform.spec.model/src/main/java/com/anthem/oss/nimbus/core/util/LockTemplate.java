/**
 * 
 */
package com.anthem.oss.nimbus.core.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class LockTemplate {

	@FunctionalInterface
	public interface CallbackReturn<T> {
		public T execute();
	}
	
	
	@FunctionalInterface
	public interface CallbackVoid {
		public void execute();
	}
	
	final private Lock lock;

	public LockTemplate() {
		this.lock = new ReentrantLock();
	}
	
	public LockTemplate(Lock lock) {
		this.lock = lock;
	}
	
	final public <L> L execute(CallbackReturn<L> cb) {
		lock.lock();
		try{
			return cb.execute();
		} finally {
			lock.unlock();
		}
	}
	
	final public void execute(CallbackVoid cb) {
		lock.lock();
		try{
			cb.execute();
		} finally {
			lock.unlock();
		}
	}
}

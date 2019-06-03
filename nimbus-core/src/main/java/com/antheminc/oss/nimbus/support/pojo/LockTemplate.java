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
package com.antheminc.oss.nimbus.support.pojo;

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
	
//	final public <L> L execute(CallbackReturn<L> cb) {
//		return cb.execute();
//	}
//	
//	final public void execute(CallbackVoid cb) {
//		cb.execute();
//	}
	
	
	final private Lock lock;

	public LockTemplate() {
		this.lock = new ReentrantLock();
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

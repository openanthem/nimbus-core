///**
// *  Copyright 2016-2019 the original author or authors.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package com.antheminc.oss.nimbus.support.pojo;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//
//import java.util.List;
//
//import org.apache.commons.lang3.time.StopWatch;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
//import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
//
//import lombok.Getter;
//import lombok.Setter;
//
///**
// * @author Soham Chakravarti
// *
// */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class JavaBeanHandlerTest {
//	
//	private static JavaBeanHandler reflection = new JavaBeanHandlerReflection();
//	private static JavaBeanHandlerLambda lambda = new JavaBeanHandlerLambda();
//	
//	private static StopWatch sw_d = new StopWatch();
//	private static StopWatch sw_r = new StopWatch();
//	private static StopWatch sw_l = new StopWatch();
//	
//	private static final int K_REPEAT = 1000 * 1000;
//	
//	
//	@SuppressWarnings("serial")
//	@Getter @Setter
//	public static class _SimplePojo extends IdLong {
//		
//		public String a1;
//		private List<String> a2;
//	}
//	
//	@BeforeClass
//	public static void beforeClass() {
//		sw_d.start();
//		sw_d.suspend();
//		
//		sw_r.start();
//		sw_r.suspend();
//		
//		sw_l.start();
//		sw_l.suspend();
//	}
//	
//	@AfterClass
//	public static void afterClass() {
//		sw_d.stop();
//		sw_r.stop();
//		sw_l.stop();
//		
//		System.out.println("@@@ direct	: "+sw_d);
//		System.out.println("@@@ reflect	: "+sw_r);
//		System.out.println("@@@ lambda	: "+sw_l);
//	}
//	
//	
//	//@Test
//	public void t01_instantiate() {
//		_SimplePojo r = reflection.instantiate(_SimplePojo.class);
//		assertNotNull(r);
//		assertTrue(_SimplePojo.class.isInstance(r));
//		
//		_SimplePojo l = lambda.instantiate(_SimplePojo.class);
//		assertNotNull(l);
//		assertTrue(_SimplePojo.class.isInstance(l));
//		
//		for(int i=0; i<K_REPEAT/10; i++) {
//			sw_d.resume();
//			new _SimplePojo();
//			sw_d.suspend();
//			
//			sw_r.resume();
//			reflection.instantiate(_SimplePojo.class);
//			sw_r.suspend();
//			
//			sw_l.resume();
//			lambda.instantiate(_SimplePojo.class);
//			sw_l.suspend();
//		}
//	}
//	
//	@Test
//	public void t02_set() {
//		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(_SimplePojo.class, "a1");
//		_SimplePojo target = new _SimplePojo();
//		String value = "A1";
//	
//		reflection.setValue(va, target, value);
//		assertSame(value, target.getA1());
//		
//		target.setA1(null);
//		lambda.setValue(va, target, value);
//		assertSame(value, target.getA1());
//		
//		for(int i=0; i<K_REPEAT; i++) {
//			sw_d.resume();
//			target.setA1(value);
//			sw_d.suspend();
//			
//			target.setA1(null);
//			
//			sw_r.resume();
//			reflection.setValue(va, target, value);
//			sw_r.suspend();
//			
//			target.setA1(null);
//			
//			sw_l.resume();
//			lambda.setValue(va, target, value);
//			sw_l.suspend();
//		}
//	}
//	
//	@Test
//	public void t03_get() {
//		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(_SimplePojo.class, "a1");
//		_SimplePojo target = new _SimplePojo();
//		String value = "A1";
//		target.setA1(value);
//		
//		assertSame(value, reflection.getValue(va, target));
//		assertSame(value, lambda.getValue(va, target));
//		
//		for(int i=0; i<K_REPEAT; i++) {
//			sw_d.resume();
//			target.getA1();
//			sw_d.suspend();
//			
//			sw_r.resume();
//			reflection.getValue(va, target);
//			sw_r.suspend();
//			
//			sw_l.resume();
//			lambda.getValue(va, target);
//			sw_l.suspend();
//		}
//	}
//}

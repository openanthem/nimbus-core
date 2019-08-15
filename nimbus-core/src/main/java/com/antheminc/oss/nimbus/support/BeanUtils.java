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
package com.antheminc.oss.nimbus.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * @author Tony Lopez
 *
 */
public class BeanUtils {

	public static void copyProperties(Object src, Map<String, String> properties) {
		if (null == properties || src == null) {
			return;
		}
		List<Field> fields = new ArrayList<>();
        Class<?> clazz = src.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
		for(Entry<String, String> entry: properties.entrySet()) {
			if (fields.stream().filter(f -> f.getName().equals(entry.getKey())).count() == 1) {
				try {
					org.apache.commons.beanutils.BeanUtils.copyProperty(src, entry.getKey(), entry.getValue());
				} catch (Throwable e) {
					throw new RuntimeException("Failed to copy property \"" + entry.getKey() + "\" into " + src, e);
				}
			}
		}
	}
	
}

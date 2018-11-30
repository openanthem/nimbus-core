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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.Iterator;
import java.util.List;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
@EnableLoggingInterceptor
public class DefaultActionExecutorUpdate extends AbstractCommandExecutor<Boolean> {

	public DefaultActionExecutorUpdate(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>Uses the provided {@code json} to explicitly update the state of the
	 * provided param {@code p}. Explicitly update means that only the values
	 * provided in the given {@code json} will be updated in {@code p}'s state.
	 * Other state values will be preserved.</p> <p>This process is as
	 * follows:</p> <ul> <li>Retrieves the key/value pairs from the provided
	 * {@code json} <i>(where key is the field name and value is the field's
	 * value)</i> <li>Uses the keys to traverse {@code p} and set the state of
	 * each leaf param with the corresponding value</li> </ul> <p>While
	 * traversing {@code p}, support is provided for nested params of the
	 * following:</p> <ol> <li>Leaf param (primitive or literal type)</li>
	 * <li>Complex param (object type)</li> <li>Collection&lt;{@code T}&gt;
	 * param</li> <ol> <li>{@code T} is a primitive or literal type</li>
	 * <li>{@code T} is an object type</li> </ol> </ol>
	 * 
	 * @param p the param to set
	 * @param json the json to read
	 */
	public void handleParam(Param<Object> p, String json) {
		// exit condition 1: if p is null -- can't traverse any further.
		if (null == p) {
			return;
		}

		// exit condition 2: p is a leaf -- can't traverse any further
		if (p.isLeaf()) {
			Object updated = getConverter().toReferredType(p, json);
			p.setState(updated);

			// otherwise, p is nested -- now traverse and handle it's nested
			// params
		} else {
			// iterate over the json string and essentially retrieve the
			// key/value pairs
			JsonNode tree = getConverter().toJsonNodeTree(json);
			
			if (null == tree) {
				// nothing to parse
				return;
				
			} else if (!tree.isArray()) {
				// traverse and update nested params with the provided json
				tree.fields().forEachRemaining(entry -> {
					Param<Object> pNested = p.findParamByPath(Constants.SEPARATOR_URI.code + entry.getKey());
					handleParam(pNested, getConverter().toJson(entry.getValue()));
				});

			} else {

				if (!p.isCollection()) {
					throw new FrameworkRuntimeException("Attempted to update " + p
							+ " as a collection, but it is not a collection. " + "JSON was: " + json);
				}

				// exit condition 3: collection param is not instantiated --
				// can't traverse any further so
				// set the state to the object created from the provided json
				if (null == p.findIfCollection().getValues() || p.findIfCollection().getValues().isEmpty()) {
					Object collectionState = getConverter().toReferredType(p.getConfig(), json);
					p.setState(collectionState);
					return;
				}

				// traverse and update a collection's collection elements with
				// the provided json
				ArrayNode array = (ArrayNode) tree;
				Iterator<JsonNode> iterator = array.iterator();
				for (int index = 0; iterator.hasNext(); index++) {
					JsonNode entry = iterator.next();
					Param<Object> pNested = p.findParamByPath(Constants.SEPARATOR_URI.code + index);
					handleParam(pNested, getConverter().toJson(entry));
				}
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();

		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		String json = eCtx.getCommandMessage().getRawPayload();

		if (p.isCollection()) {
			handleCollection(p.findIfCollection(), json);
		} else if (p.isLeaf()) {
			handleLeaf(p, json);
		} else {
			// remaining scenarios apply to nested params or collection elements
			handleParam(p, json);
		}

		// TODO use the Action output from the setState to check if the action
		// performed is _update to return true
		// , else false - right now it either return _new or _replace (change
		// detection is not yet implemented)
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}

	/**
	 * <p>Convert the provided {@code json} into the referred type of the
	 * collection, {@code p}. Assuming successful conversion, the resulting
	 * object(s) will be added to the state of {@code p}. <ul><li>If the
	 * provided {@code json} is a JSON array then each of the elements in the
	 * array will be converted to the referred type of the collection and
	 * added.</li> <li>If the provided {@code json} is a JSON object, then only
	 * one element will be converted to the referred type of the collection and
	 * added.</li>
	 * @param p the list param to update
	 * @param json the JSON payload of the object to convert and add into
	 *            {@code p}
	 */
	@SuppressWarnings("unchecked")
	protected void handleCollection(ListParam<Object> p, String json) {
		if (JsonUtils.isJsonArray(json)) {
			p.addAll(getConverter().toCollectionFromArray(p.getType().getModel().getElemConfig().getReferredClass(),
					List.class, json));
		} else {
			p.add((Object) getConverter().toReferredType(p.getType().getModel().getElemConfig(), json));
		}
	}

	/**
	 * <p>Convert the provided {@code json} into the referred type of the leaf
	 * param, {@code p}. Assuming successful conversion, the resulting object
	 * will be set into the state of {@code p}.
	 * @param p the leaf param to update
	 * @param json the JSON payload of the object to convert and set into
	 *            {@code p}
	 */
	protected void handleLeaf(Param<Object> p, String json) {
		p.setState(getConverter().toReferredType(p.getConfig(), json));
	}
}
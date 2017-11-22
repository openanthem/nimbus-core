package com.anthem.oss.nimbus.core.domain.model.state;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.util.LockTemplate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * A mock Param implementation intended for testing purposes.
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@Getter
@Setter
public class MockParam implements Param<Object> {

	private boolean active = true;
	private EntityStateAspectHandlers aspectHandlers = null;
	private String beanPath = "";
	private ParamConfig<Object> config = null;
	private List<MappedParam<?, Object>> consumers = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<Notification<Object>> emittedNotifications = new ArrayList<>();;
	private boolean enabled = true;
	private List<MappedParam<?, Object>> eventSubscribers = new ArrayList<>();
	private LockTemplate lockTemplate = null;
	private Message message = null;
	private Map<String, Model<Object>> modelMap;
	private Map<String, Param<Object>> paramMap;
	private Model<Object> parentModel = null;
	private String path = "";
	private PropertyDescriptor propertyDescriptor = null;
	private Object state = null;
	private boolean stateInitialized = false;
	private StateType type = null;
	private List<ParamValue> values = new ArrayList<>();
	private boolean visible = true;

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param#activate()
	 */
	@Override
	public void activate() {
		this.setActive(true);
		this.setEnabled(true);
		this.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param#deactivate()
	 */
	@Override
	public void deactivate() {
		this.setActive(false);
		this.setEnabled(false);
		this.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.Notification.Producer#deregisterConsumer(com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedParam)
	 */
	@Override
	public boolean deregisterConsumer(MappedParam<?, Object> consumer) {
		return this.consumers.remove(consumer);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.Notification.Dispatcher#emitNotification(com.anthem.oss.nimbus.core.domain.model.state.Notification)
	 */
	@Override
	public void emitNotification(Notification<Object> event) {
		this.emittedNotifications.add(event);
	}

	/**
	 * <p>Iterates through the values of <tt>arr</tt> and uses each element to invoke <tt>finderFn</tt>. The Function <tt>finderFn</tt> is 
	 * a search function that expects a key <b>K</b> provided by <tt>arr</tt>.</p>
	 * 
	 * <p>The first non-null value retrieved as a result of <tt>finderFn</tt> will be returned. If <tt>arr</tt> is null or no value is found,
	 * <tt>null</tt> will be returned.</p>
	 * 
	 * @param arr the array of key objects
	 * @param finderFn the search function to evaluate
	 * @return the first found element as a result of <tt>finderFn</tt>, otherwise null 
	 */
	private <T, K> T findFirstByPath(K[] arr, Function<K, T> finderFn) {
		if (null == arr) {
			return null;
		}
		for (final K key : arr) {
			final T found = finderFn.apply(key);
			if (null != found) {
				return found;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#findModelByPath(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S> Model<S> findModelByPath(String path) {
		return (Model<S>) this.modelMap.get(path);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#findModelByPath(java.lang.String[])
	 */
	@Override
	public <S> Model<S> findModelByPath(String[] pathArr) {
		return this.findFirstByPath(pathArr, k -> this.findModelByPath(k));
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#findParamByPath(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <P> Param<P> findParamByPath(String path) {
		return (Param<P>) this.paramMap.get(path);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#findParamByPath(java.lang.String[])
	 */
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		return this.findFirstByPath(pathArr, k -> this.findParamByPath(k));
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#fireRules()
	 */
	@Override
	public void fireRules() {
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param#getLeafState()
	 */
	@Override
	public Object getLeafState() {
		return this.state;
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#getRootDomain()
	 */
	@Override
	public Model<?> getRootDomain() {
		if (null == getParentModel()) {
			return null;
		}
		return getParentModel().getRootDomain();
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#getRootExecution()
	 */
	@Override
	public ExecutionModel<?> getRootExecution() {
		if (null == getParentModel()) {
			return null;
		}
		return getParentModel().getRootExecution();
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#initSetup()
	 */
	@Override
	public void initSetup() {
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.EntityState#initState()
	 */
	@Override
	public void initState() {
		this.setStateInitialized(true);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.Notification.Producer#registerConsumer(com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedParam)
	 */
	@Override
	public void registerConsumer(MappedParam<?, Object> consumer) {
		this.consumers.add(consumer);
	}

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.State#setState(java.lang.Object)
	 */
	@Override
	public Action setState(Object state) {
		this.state = state;
		this.setStateInitialized(true);
		return Action.DEFAULT;
	}
}

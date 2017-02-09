/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo.Path;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Param;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.RootModel;
import com.anthem.nimbus.platform.spec.model.dsl.config.DomainModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.DomainParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.MappedDomainParamConfigLinked;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */

@Repo(Database.rep_mongodb)
@Getter @Setter
public class ExecutionState<V, C> extends AbstractModel.IdString implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//==private CommandElement key;
	
	private C c;

	@Path("/c") private V v;
	
	private FlowState f;
	
	private Map<String, DetachedState<?, ?>> detachedStates;
	
	public C getCore() {return getC();}
	public void setCore(C c) {setC(c);}
	
	public V getView() {return getV();}
	public void setView(V v) {setV(v);}
	
	public FlowState getFlow() {return getF();}
	public void setFlow(FlowState f) {setF(f);}
	
	private ExecutionState<V, C> _this() {
		return this;
	}
	
	@Getter @Setter @RequiredArgsConstructor
	public static class Config<V, C> {
		final private ModelConfig<C> core;
		final private ModelConfig<V> view;
		final private ModelConfig<FlowState> flow;
	}
	
	@Getter @Setter 
	public class ExParamConfig extends DomainParamConfig<ExecutionState<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private ModelConfig<ExecutionState<V, C>> rootParent;
		
		public ExParamConfig(Config<V, C> exConfig) {
			super("root");
			this.rootParent = new ExModelConfig(exConfig); 
			
			ParamType.Nested<ExecutionState<V, C>> pType = new ParamType.Nested<>(_this().getClass().getSimpleName(), _this().getClass());
			pType.setModel(getRootParent());
			this.setType(pType);
		}
	}
	
	@Getter @Setter
	public class ExModelConfig extends DomainModelConfig<ExecutionState<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private ParamConfig<C> coreParam;
		final private ParamConfig<V> viewParam;
		final private ParamConfig<FlowState> flowParam;
		
		@SuppressWarnings("unchecked")
		public ExModelConfig(Config<V, C> exConfig) {
			super((Class<ExecutionState<V, C>>)_this().getClass());
			
			//TODO: change to client default 
			setRepo(AnnotationUtils.findAnnotation(ExecutionState.class, Repo.class));
			
			Objects.requireNonNull(exConfig.getCore(), "Core ModelConfig must be provided.");
			this.coreParam = attachParams("c", exConfig.getCore());
			
			this.viewParam = exConfig.getView()!=null ? attachParams("v", exConfig.getView()) : null;
			this.flowParam = exConfig.getFlow()!=null ? attachParams("f", exConfig.getFlow()) : null;
		}
		
		private <T> ParamConfig<T> attachParams(String pCode, ModelConfig<T> modelConfig) {
			DomainParamConfig<T> pConfig = createParam(pCode);
			Class<T> pClass = modelConfig.getReferredClass();
			
			ParamType.Nested<T> pType = new ParamType.Nested<>(ClassUtils.getShortName(pClass), pClass);
			pConfig.setType(pType);
			pType.setModel(modelConfig);
			
			templateParams().add(pConfig);
			return pConfig;
		}
		
		private <T> DomainParamConfig<T> createParam(String pCode) {
			Field f = FieldUtils.getDeclaredField(ExecutionState.class, pCode, true);
			MapsTo.Path path = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
			
			DomainParamConfig<T> pConfig = path==null ? new DomainParamConfig<>(pCode) : new MappedDomainParamConfigLinked<>(pCode, findParamByPath(path.value()), path);
			return pConfig;
		} 
	}
	
	@Setter 
	public class ExParam extends DomainParamState<ExecutionState<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private RootModel<ExecutionState<V, C>> rootModel;
		
		public ExParam(Command cmd, StateAndConfigSupportProvider provider, Config<V, C> exConfig) {
			super(null, new ExParamConfig(exConfig), provider);
			ExParamConfig pConfig = ((ExParamConfig)getConfig());
			
			this.rootModel = new ExModel(cmd, this, pConfig.getRootParent(), provider);
			this.rootModel.init();
			this.setType(new DomainStateType.Nested<>(getConfig().getType().findIfNested(), getRootModel()));
		}

		public ExParam(Command cmd, StateAndConfigSupportProvider provider, ParamConfig<ExecutionState<V, C>> rootParamConfig, RootModel<ExecutionState<V, C>> rootModel) {
			super(null, rootParamConfig, provider);
			this.rootModel = rootModel;
		}
		
		@Override
		public boolean isRoot() {
			return true;
		}
		
		@Override
		public RootModel<ExecutionState<V, C>> getRootModel() {
			return rootModel;
		}
	}
	
	@Getter @Setter
	public class ExModel extends DomainModelState<ExecutionState<V, C>> implements RootModel<ExecutionState<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private Command command;
		
		private String currentPage; //TODO change to state
		
		@JsonIgnore
		final private ExecutionRuntime executionRuntime;
		
		public ExModel(Command command, ExParam associatedParam, ModelConfig<ExecutionState<V, C>> modelConfig, StateAndConfigSupportProvider provider) {
			this(command, associatedParam, modelConfig, provider, new InternalExecutionRuntime(command));
		}
		
		public ExModel(Command command, ExParam associatedParam, ModelConfig<ExecutionState<V, C>> modelConfig, StateAndConfigSupportProvider provider, ExecutionRuntime executionRuntime) {
			super(associatedParam, modelConfig, provider);
			this.command = command;
			this.executionRuntime = executionRuntime;
		}
		
		@Override
		protected void initInternal() {
			getExecutionRuntime().start();
		}
		
		@Override
		public ExecutionState<V, C> instantiateOrGet() {
			return _this();
		}
		
		@Override
		public ExModelConfig getConfig() {
			return (ExModelConfig)super.getConfig();
		}
		
		@Override
		public ExParam getAssociatedParam() {
			return (ExParam)super.getAssociatedParam();
		}
		
		@JsonIgnore @Override
		public ExModel getRootModel() {
			return this;
		}
		
		@Override
		public ExecutionState<V, C> getState() {
			return _this();
		}
		
		@Override
		protected void finalize() throws Throwable {
			getExecutionRuntime().stop();
			
			super.finalize();
		}
	}
	
	@Getter
	public class InternalExecutionRuntime implements ExecutionRuntime {

		final private Command command;
		final private BlockingQueue<Notification<?>> notificationQueue;
		
		private String lockId;
		final private Lock lock = new ReentrantLock();
		
		final private Lock notificationLock = new ReentrantLock();
		final private Condition notComplete = notificationLock.newCondition();
		
		public InternalExecutionRuntime(Command cmd) {
			this.command = cmd;
			this.notificationQueue = new LinkedBlockingQueue<>();
		}
		
		@Override
		public void start() { }
		
		@Override
		public void stop() { }

		@Override
		public String tryLock() {
			if(isLocked()) return null;
			
			lock.lock();
			try{
				this.lockId = UUID.randomUUID().toString();
				return getLockId();
			} finally {
				lock.unlock();
			}
		}

		@Override
		public boolean isLocked() {
			return (getLockId()!=null);
		}
		
		@Override
		public boolean isLocked(String lockId) {
			if(!isLocked()) return false;
			
			return getLockId().equals(lockId);
		}
		
		@Override
		public boolean tryUnlock(String lockId) {
			if(!isLocked(lockId)) return true;
			
			lock.lock();
			try {
				if(isLocked(lockId)) {
					this.lockId = null;
					return true;
				}
			} finally {
				lock.unlock();
			}
			return false;
		}
		
		@Override
		public void notifySubscribers(Notification<Object> event) {
			try {
				getNotificationQueue().put(event);
			} catch (InterruptedException ex) {
				throw new PlatformRuntimeException("Failed to place notification event on queue. event: "+event, ex);
			}	
		}
		
		@Override
		public void awaitCompletion() {
			// create new single thread using executor service
			ExecutorService e = createExecutorService(getCommand());
			e.execute(new InternalNotificationEventDelegator(getNotificationQueue(), getNotificationLock(), getNotComplete()));

			// shutdown
			e.shutdown();
			
			// wait on lock condition for completion of all tasks
			// TODO: this is overkill, can be simplified by waiting on ExecutorServive.awaitTermination()
			getNotificationLock().lock();
			try {
				while(getNotificationQueue().size()!=0)
					notComplete.await();
				
			} catch (InterruptedException ex) {
				throw new PlatformRuntimeException("Failed while waiting for notification event processing to complete. queue: "+notificationQueue, ex);
			} finally {
				getNotificationLock().unlock();
			}
			
		}
		
		private ExecutorService createExecutorService(Command cmd) {
			return Executors.newFixedThreadPool(1, new ThreadFactory() {
				
				final private AtomicInteger counter = new AtomicInteger();
				
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "ExecState "+counter.incrementAndGet()+": "+cmd.getRootDomainElement().getUri());
				}
			});
		}
	} 
	
	@Getter @RequiredArgsConstructor
	public class InternalNotificationEventDelegator implements Runnable {
		final private BlockingQueue<Notification<?>> notificationQueue;
		final private Lock notificationLock;
		final private Condition notComplete;
		
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				while(true) {
					Notification<Object> event = (Notification<Object>)notificationQueue.take();
					Param<Object> source =  event.getSource();
					
					// create new list to avoid concurrent modification of subscriber list as part of event handling
					new ArrayList<>(source.getEventSubscribers())
						.stream()
						.forEach(subscribedParam->subscribedParam.handleNotification(event));
					
					notificationLock.lock();
					try {
						if(notificationQueue.size()==0)
							notComplete.signal();
					} finally {
						notificationLock.unlock();
					}
				}
				
			} catch (InterruptedException ex) {
				throw new PlatformRuntimeException("Failed to take event form queue: "+notificationQueue, ex);
			}
		}
	}
}

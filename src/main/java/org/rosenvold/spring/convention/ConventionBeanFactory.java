package org.rosenvold.spring.convention;

/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConventionBeanFactory
        extends DefaultListableBeanFactory {

    private final NameToClassResolver nameToClassResolver;

    private final CandidateEvaluator candidateEvaluator;
    private final String[] nothing = new String[]{};

    private final Map<Class, AnnotatedBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<Class, AnnotatedBeanDefinition>();
    private final Map<Class, RootBeanDefinition> mergedBeanDefinitions =
            new ConcurrentHashMap<Class, RootBeanDefinition>();
    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private final Map<String, Class> cache = new ConcurrentHashMap<String, Class>();


    /* Maps by type to bean names */
    private final Map<Class, String[]> byTypeMappingSingletonsEager = new ConcurrentHashMap<Class, String[]>();
    private final Map<Class, String[]> byTypeMappingNonSingletonsEager = new ConcurrentHashMap<Class, String[]>();

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
    private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();

    public ConventionBeanFactory(NameToClassResolver beanClassResolver,
                                 CandidateEvaluator candidateEvaluator) {
        this.nameToClassResolver = beanClassResolver;
        this.candidateEvaluator = candidateEvaluator;
    }

    private void clearTypeBasedCaches(){
    		byTypeMappingSingletonsEager.clear();
    		byTypeMappingNonSingletonsEager.clear();
    	}


    @Override
    public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
  		if (type == null || !allowEagerInit) {
  			return getBeanNamesForTypeImpl(type, includeNonSingletons, allowEagerInit);
  		}
  		Map<Class, String[]> cache = includeNonSingletons ? byTypeMappingNonSingletonsEager : byTypeMappingSingletonsEager;
  		String[] resolvedBeanNames = cache.get(type);
  		if (resolvedBeanNames != null) {
  			return resolvedBeanNames;
  		}
  		resolvedBeanNames = getBeanNamesForTypeImpl(type, includeNonSingletons, allowEagerInit);
    	cache.put( type, resolvedBeanNames);
  		return resolvedBeanNames;
  	}

    public synchronized String[] getBeanNamesForTypeImpl(Class type, boolean includeNonSingletons, boolean allowEagerInit) { // LBF, local only.
        final Class cacheEntry = getCacheEntry(type);
        if (cacheEntry == null) {
            final String[] beanNamesForType = super.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
            if (beanNamesForType.length > 0) return beanNamesForType;

            final Class aClass = resolveImplClass(type.getName());
            if (aClass != null) {
                return new String[]{aClass.getName()};
            }
            return new String[]{};
        } else {
            if (isCacheMiss(cacheEntry)) {
                return nothing;
            } else {
                return new String[]{cacheEntry.getName()};
            }

        }
    }

    @Override
    public synchronized <T> T getBean(Class<T> requiredType) throws BeansException {
        final Class cacheEntry = getCacheEntry(requiredType);
        if (cacheEntry == null) {
            if (super.getBeanNamesForType(requiredType).length > 0) {
                return super.getBean(requiredType);
            }
            final Class aClass = resolveClass(requiredType);
            //noinspection unchecked
            return (T) instantiate(aClass);
        } else {
            //noinspection unchecked
            return isCacheMiss(cacheEntry) ? null : (T) instantiate(cacheEntry);
        }
    }

    @Override
    public synchronized Object getBean(String name) throws BeansException {
        setupConventionBeanIfMissing(name);
        return super.getBean(name);
    }


    @Override
    public synchronized <T> T getBean(String name, Class<T> tClass) throws BeansException {
        setupConventionBeanIfMissing(name);
        return super.getBean(name, tClass);
    }

    private synchronized void registerByDirectNameToClassMapping(String name) {
        final Class<?> type = getLocalType(name);
        registerBeanByType(name, type);
    }

    @Override
    public synchronized Object getBean(String name, Object... objects) throws BeansException {
        setupConventionBeanIfMissing(name);
        return super.getBean(name, objects);
    }

    @Override
    public synchronized boolean containsBean(String name) {
        setupConventionBeanIfMissing(name);
        return super.containsBean(name);
    }

    @Override
    public synchronized boolean isSingleton(String name)
            throws NoSuchBeanDefinitionException {
        //noinspection SimplifiableIfStatement
        setupConventionBeanIfMissing(name);
        return super.isSingleton(name);
    }

    @Override
    public synchronized boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        setupConventionBeanIfMissing(name);
        return super.isPrototype(name);
    }

    private synchronized String getAnnotatedScope(Class<?> type) {
        if (type != null) {
            final Scope annotation = type.getAnnotation(Scope.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return AbstractBeanDefinition.SCOPE_DEFAULT;
    }

    @Override
    public synchronized boolean isTypeMatch(String name, Class aClass) throws NoSuchBeanDefinitionException {
        setupConventionBeanIfMissing(name);
        return super.isTypeMatch(name, aClass);
    }

    @Override
    public synchronized Class<?> getType(String name) throws NoSuchBeanDefinitionException {
//        setupConventionBeanIfMissing( name );  Hmpf.
        return super.getType(name);
    }

    @Override
    public synchronized String[] getAliases(String name) {
        setupConventionBeanIfMissing(name);
        return super.getAliases(name);
    }


    @Override
    public synchronized boolean containsBeanDefinition(String beanName) {  // LBF; local only
        //setupConventionBeanIfMissing(beanName);
        return super.containsBeanDefinition(beanName);
    }


    @Override
    protected synchronized RootBeanDefinition getMergedLocalBeanDefinition(String beanName)
            throws BeansException {
        if (super.containsBeanDefinition(beanName)) {
            return super.getMergedLocalBeanDefinition(beanName);
        }
        final Class<?> type = getLocalType(beanName);
        if (type != null) {
            RootBeanDefinition rootBeanDefinition = mergedBeanDefinitions.get(type);
            if (rootBeanDefinition != null) return rootBeanDefinition;
            rootBeanDefinition = new RootBeanDefinition(type);
            rootBeanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
            rootBeanDefinition.setScope(getAnnotatedScope(type));
            mergedBeanDefinitions.put(type, rootBeanDefinition);
            return rootBeanDefinition;
        }

        return null;
    }


    @Override
    public synchronized BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (super.containsBeanDefinition(beanName)) {
            return super.getBeanDefinition(beanName);
        }
        final Class<?> type = getType(beanName);
        return getOrCreateBeanDefinition(type);
    }

    private void setupConventionBeanIfMissing(String name) {
        if (!super.containsBeanDefinition(name)) {
            registerByDirectNameToClassMapping(name);
        }
    }

    private Class<?> getLocalType(String s) throws NoSuchBeanDefinitionException {
        final Class aClass = resolveImplClass(s);
        return aClass != null && candidateEvaluator.isBean(aClass) ? aClass : null;
    }


    private static class CacheMiss {
    }

    private Class resolveImplClass(final String beanName) {
        Class aClass = cache.get(beanName);
        if (aClass != null && !CacheMiss.class.equals(aClass)) return aClass;
        aClass = nameToClassResolver.resolveBean(beanName, candidateEvaluator);
        cache.put(beanName, aClass != null ? aClass : CacheMiss.class);
        return aClass;
    }

    private Class getCacheEntry(Class key) {
        return cache.get(beanNameFromClass(key));
    }

    private boolean isCacheMiss(Class cacheResult) {
        return CacheMiss.class.equals(cacheResult);
    }

    private Class resolveClass(final Class beanClass) {
        return resolveImplClass(beanNameFromClass(beanClass));
    }

    private String beanNameFromClass(final Class beanClass) {
        return beanClass.getName();
    }

    private Object instantiate(Class aClass) throws BeansException {
        return doGetBean(aClass.getName(), null, null, false);
    }

    private AnnotatedBeanDefinition getOrCreateBeanDefinition(Class<?> type) {
        final AnnotatedBeanDefinition beanDefinition = beanDefinitionMap.get(type);
        if (beanDefinition != null) return beanDefinition;

        final ScannedGenericBeanDefinition rootBeanDefinition = getScannedBeanDefinition(type);
        rootBeanDefinition.applyDefaults(this.beanDefinitionDefaults);

//        rootBeanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
        processCommonDefinitionAnnotations(rootBeanDefinition);

        rootBeanDefinition.setScope(getAnnotatedScope(type));

        beanDefinitionMap.put(type, rootBeanDefinition);
        return rootBeanDefinition;
    }

    private ScannedGenericBeanDefinition getScannedBeanDefinition(Class clazz) {
        try {
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(clazz.getName());
            return new ScannedGenericBeanDefinition(metadataReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void registerBeanByType(String beanName, Class<?> type) {
        if (type == null) return;
        final BeanDefinition orCreateBeanDefinition = getOrCreateBeanDefinition(type);
        createBeanDefinitionHolder(beanName, orCreateBeanDefinition);
    }

    private BeanDefinitionHolder createBeanDefinitionHolder(String realbeanName, BeanDefinition candidate) {
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, realbeanName);
        ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);

        definitionHolder = applyScopedProxyMode(scopeMetadata, definitionHolder, this);
        registerBeanDefinition(realbeanName, definitionHolder.getBeanDefinition());
        return definitionHolder;
    }

    static BeanDefinitionHolder applyScopedProxyMode(
            ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {

        ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
        if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
            return definition;
        }
        boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
        return ScopedProxyUtils.createScopedProxy(definition, registry, proxyTargetClass);
    }

    // Ripped from ActionConfigUtils
    static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd) {
        if (abd.getMetadata().isAnnotated(Primary.class.getName())) {
            abd.setPrimary(true);
        }
        if (abd.getMetadata().isAnnotated(Lazy.class.getName())) {
            Boolean value = (Boolean) abd.getMetadata().getAnnotationAttributes(Lazy.class.getName()).get("value");
            abd.setLazyInit(value);
        }
        if (abd.getMetadata().isAnnotated(DependsOn.class.getName())) {
            String[] value = (String[]) abd.getMetadata().getAnnotationAttributes(DependsOn.class.getName()).get("value");
            abd.setDependsOn(value);
        }
    }

    private final QualifierAnnotationAutowireCandidateResolver qualifierAnnotationAutowireCandidateResolver = new QualifierAnnotationAutowireCandidateResolver();

    public AutowireCandidateResolver getAutowireCandidateResolver() {
   		return this.qualifierAnnotationAutowireCandidateResolver;
   	}

    
    protected Map<String, Object> findAutowireCandidates(
   			String beanName, Class requiredType, DependencyDescriptor descriptor) {

        System.out.println("beanName = " + beanName + ", requiredType" + requiredType);
   		String[] candidateNames = getCandidateNames(requiredType, descriptor);

   		Map<String, Object> result = new LinkedHashMap<String, Object>(candidateNames.length);
   		for (Class autowiringType : this.resolvableDependencies.keySet()) {
   			if (autowiringType.isAssignableFrom(requiredType)) {
   				Object autowiringValue = this.resolvableDependencies.get(autowiringType);
   				autowiringValue = resolveAutowiringValue(autowiringValue, requiredType);
   				if (requiredType.isInstance(autowiringValue)) {
   					result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
   					break;
   				}
   			}
   		}
   		for (String candidateName : candidateNames) {
   			if (!candidateName.equals(beanName) && isAutowireCandidate(candidateName, descriptor)) {
   				result.put(candidateName, getBean(candidateName));
   			}
   		}
   		return result;
   	}


    private final Map<Class, Object> resolvableDependencies = new HashMap<Class, Object>();

    public void registerResolvableDependency(Class dependencyType, Object autowiredValue) {
   		Assert.notNull(dependencyType, "Type must not be null");
        this.resolvableDependencies.put(dependencyType, autowiredValue);
        super.registerResolvableDependency( dependencyType, autowiredValue);
   	}


    private final ConcurrentHashMap<Class, String[]> typeCache = new ConcurrentHashMap<Class, String[]>();
    
    private String[] getCandidateNames(Class requiredType, DependencyDescriptor descriptor) {
        String[] strings = typeCache.get(requiredType);
        if (strings != null) {
            return strings;
        }
        strings = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                this, requiredType, true, descriptor.isEager());
        typeCache.put( requiredType, strings);
        return strings;
    }


    	/**
    	 * Resolve the given autowiring value against the given required type,
    	 * e.g. an {@link org.springframework.beans.factory.ObjectFactory} value to its actual object result.
    	 * @param autowiringValue the value to resolve
    	 * @param requiredType the type to assign the result to
    	 * @return the resolved value
    	 */
    	public static Object resolveAutowiringValue(Object autowiringValue, Class requiredType) {
    		if (autowiringValue instanceof ObjectFactory && !requiredType.isInstance(autowiringValue)) {
    			ObjectFactory factory = (ObjectFactory) autowiringValue;
    			if (autowiringValue instanceof Serializable && requiredType.isInterface()) {
    				autowiringValue = Proxy.newProxyInstance(requiredType.getClassLoader(),
                            new Class[]{requiredType}, new ObjectFactoryDelegatingInvocationHandler(factory));
    			}
    			else {
    				return factory.getObject();
    			}
    		}
    		return autowiringValue;
    	}

    protected void resetBeanDefinition(String beanName) {
        clearTypeBasedCaches();
        super.resetBeanDefinition(beanName);
    }

    /**
   	 * Reflective InvocationHandler for lazy access to the current target object.
   	 */
   	private static class ObjectFactoryDelegatingInvocationHandler implements InvocationHandler, Serializable {

   		private final ObjectFactory objectFactory;

   		public ObjectFactoryDelegatingInvocationHandler(ObjectFactory objectFactory) {
   			this.objectFactory = objectFactory;
   		}

   		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
   			String methodName = method.getName();
   			if (methodName.equals("equals")) {
   				// Only consider equal when proxies are identical.
   				return (proxy == args[0]);
   			}
   			else if (methodName.equals("hashCode")) {
   				// Use hashCode of proxy.
   				return System.identityHashCode(proxy);
   			}
   			else if (methodName.equals("toString")) {
   				return this.objectFactory.toString();
   			}
   			try {
   				return method.invoke(this.objectFactory.getObject(), args);
   			}
   			catch (InvocationTargetException ex) {
   				throw ex.getTargetException();
   			}
   		}
   	}



}

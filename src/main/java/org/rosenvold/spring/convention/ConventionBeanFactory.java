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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;

public class ConventionBeanFactory extends DefaultListableBeanFactory {

    private final ConfigurableApplicationContext parent;
    private final BeanClassResolver beanClassResolver;

    public ConventionBeanFactory(ConfigurableApplicationContext parent) {
        super(parent);
        this.parent = parent;
        this.beanClassResolver = parent.getBean(BeanClassResolver.class);
    }


    public ConventionBeanFactory(ConfigurableApplicationContext parentBeanFactory, BeanClassResolver beanClassResolver) {
        super(parentBeanFactory);
        this.parent = parentBeanFactory;
        this.beanClassResolver = beanClassResolver;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        final Class aClass = resolveClass(requiredType);
        if (aClass == null) {
            return parent.getBean(requiredType);
        }
        return instantiate(aClass);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        final Class<?> type = getType(name);
        return type != null ? instantiate(type) : parent.getBean(name);
    }

    @Override
    public <T> T getBean(String s, Class<T> tClass) throws BeansException {
        final Class<?> type = getType(s);
        //noinspection unchecked
        return isTypeMatch(s, tClass) ? (T) instantiate(type) : parent.getBean(s, tClass);
    }

    @Override
    public Object getBean(String s, Object... objects) throws BeansException {
        throw new NoSuchBeanDefinitionException("Dont know");
    }

    @Override
    public boolean containsBean(String s) {
        final Class<?> type = getType(s);
        if (type == null) return false;
        final Annotation[] annotations = type.getAnnotations();
        return annotations.length > 0;
    }

    @Override
    public boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String s, Class aClass) throws NoSuchBeanDefinitionException {
        final Class aClass1 = resolveImplClass(s);
        if (aClass1 == null) return false;
        return aClass.isAssignableFrom(aClass1);
    }

    @Override
    public Class<?> getType(String s) throws NoSuchBeanDefinitionException {
        return resolveImplClass(s);
    }

    @Override
    public String[] getAliases(String s) {
        return new String[0];
    }

    private Class resolveClass(final String beanName) {
        try {
            return Class.forName(beanName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }


    private Class resolveImplClass(final String beanName) {
        return beanClassResolver.resolveBean( beanName);
/*        final Class aClass = resolveClass(beanName);
        if (aClass != null && aClass.isInterface()) {
            String target = "Default" + aClass.getSimpleName();
            final Class enclosingClass = aClass.getEnclosingClass();
            final String ifName = (enclosingClass != null) ?
                    enclosingClass.getName() + "$" + target :
                    aClass.getPackage().getName() + "." + target;
            return resolveClass(ifName);
        }
        return aClass;*/
    }

    private Class resolveClass(final Class beanClass) {
        return resolveImplClass(beanNameFromClass(beanClass));
    }

    private String beanNameFromClass(final Class beanClass) {
        return beanClass.getName();
    }

    private <T> T instantiate(Class aClass) throws BeansException {
        T bean = instantiateBean(aClass);
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(aClass.getName());
        final T fud = (T) createBean(aClass.getName(), rootBeanDefinition, new Object[]{});
        //initializeBean( "fud", bean, rootBeanDefinition);
        return fud;
    }

    private <T> T instantiateBean(Class aClass) throws BeansException {
        if (aClass == null) return null;
        try {
            //noinspection unchecked
            return (T) aClass.newInstance();
        } catch (InstantiationException e) {
            throw new BeanInitializationException("Fud", e);
        } catch (IllegalAccessException e) {
            throw new BeanInitializationException("Fud", e);
        }
    }

    @Override
    public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
        final Class aClass = resolveImplClass(type.getName());
        if (aClass != null) return new String[]{aClass.getName()};
        return parent.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

}

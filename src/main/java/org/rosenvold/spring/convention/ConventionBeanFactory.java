package org.rosenvold.spring.convention;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.annotation.Annotation;

public class ConventionBeanFactory implements BeanFactory {

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        final Class aClass = resolveClass(requiredType);
        if (aClass == null){
            return null;
        }
        return instantiate(aClass);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        final Class<?> type = getType(name);
        return instantiate(type);
    }

    @Override
    public <T> T getBean(String s, Class<T> tClass) throws BeansException {
        final Class<?> type = getType(s);
        //noinspection unchecked
        return isTypeMatch(s, tClass) ? (T) instantiate(type) : null;
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
        return aClass.isAssignableFrom( aClass1);
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


    private Class resolveImplClass(final String beanName){
        final Class aClass = resolveClass(beanName);
        if (aClass.isInterface()){
            String target = "Default" + aClass.getSimpleName();
            final Class enclosingClass = aClass.getEnclosingClass();
            final String ifName = (enclosingClass != null) ?
                    enclosingClass.getName() + "$" +  target:
                    aClass.getPackage().getName() +  "." + target;
            return resolveClass(ifName);
        }
        return aClass;
    }

    private Class resolveClass(final Class beanClass) {
        return resolveImplClass(beanNameFromClass(beanClass));
    }

    private String beanNameFromClass(final Class beanClass) {
        return beanClass.getName();
    }

    private <T> T instantiate(Class aClass) throws BeansException {
        try {
            //noinspection unchecked
            return (T) aClass.newInstance();
        } catch (InstantiationException e) {
            throw new BeanInitializationException("Fud", e);
        } catch (IllegalAccessException e) {
            throw new BeanInitializationException("Fud", e);
        }
    }


}

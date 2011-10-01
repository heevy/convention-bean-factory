package org.rosenvold.spring.convention.beanclassresolvers;

import org.rosenvold.spring.convention.BeanClassResolver;
import org.rosenvold.spring.convention.InterfaceMapper;

/**
 * @author Kristian Rosenvold
 */
public class GenericBeanClassResolver implements BeanClassResolver {
    private final InterfaceMapper[] mappers;

    public GenericBeanClassResolver(InterfaceMapper... mappers) {
        this.mappers = mappers;
    }

    public Class resolveBean(String name) {
        final Class aClass = resolveClass(name);
        if (aClass != null && aClass.isInterface()) {
            final int mapperCount = mappers.length;
            for (int i = 0; i < mapperCount; i++){
                final Class beanClass = mappers[i].getBeanClass(aClass);
                if (beanClass != null){
                    return null;
                }
            }
        }
        return aClass;
    }

    private Class resolveClass(final String beanName) {
        try {
            return Class.forName(beanName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }


}

package org.rosenvold.spring.convention.beanclassresolvers;

import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.InterfaceToImplementationMapper;
import org.rosenvold.spring.convention.NameToClassResolver;

import java.util.logging.Logger;

/**
 * @author Kristian Rosenvold
 */
public class GenericNameToClassResolver implements NameToClassResolver {
    private final InterfaceToImplementationMapper[] mappers;

    Logger logger = Logger.getLogger(GenericNameToClassResolver.class.getName());
    public GenericNameToClassResolver(InterfaceToImplementationMapper... mappers) {
        this.mappers = mappers;
    }

    public Class resolveBean(String name, final CandidateEvaluator candidateEvaluator) {
        final Class aClass = resolveClass(name);
        if (aClass != null && aClass.isInterface()) {
            final int mapperCount = mappers.length;
            for (int i = 0; i < mapperCount; i++){
                final String beanClass = mappers[i].getBeanClassName(aClass);
                if (beanClass != null) {
                    //logger.log(Level.INFO, "Found bean " + beanClass);
                    final Class prospect = resolveClass(beanClass);
                    if (prospect != null && candidateEvaluator.isBean(prospect)) return prospect;
                }
            }
            // logger.log(Level.INFO, "No resolution for " + name);
            return null;
        }
        //logger.log(Level.INFO, "Class resolution for " + name);
        return aClass != null && candidateEvaluator.isBean(aClass) ? aClass : null ;
    }

    private Class resolveClass(final String beanName) {
        try {
            return Class.forName(beanName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }





}

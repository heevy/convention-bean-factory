package org.rosenvold.spring.convention.beanclassresolvers;

import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.InterfaceToImplementationMapper;
import org.rosenvold.spring.convention.NameToClassResolver;

/**
 * @author Kristian Rosenvold
 */
public class GenericBeanClassResolver implements NameToClassResolver {
    private final CandidateEvaluator candidateEvaluator;
    private final InterfaceToImplementationMapper[] mappers;

    public GenericBeanClassResolver(CandidateEvaluator candidateEvaluator, InterfaceToImplementationMapper... mappers) {
        this.candidateEvaluator = candidateEvaluator;
        this.mappers = mappers;
    }

    public Class resolveBean(String name) {
        final Class aClass = resolveClass(name);
        if (aClass != null && aClass.isInterface()) {
            final int mapperCount = mappers.length;
            for (int i = 0; i < mapperCount; i++){
                final String beanClass = mappers[i].getBeanClassName(aClass);
                if (beanClass != null) {
                    final Class prospect = resolveClass(beanClass);
                    if (prospect != null && candidateEvaluator.isBean(prospect)) return prospect;
                }
            }
            return null;
        }
        return candidateEvaluator.isBean(aClass) ? aClass : null ;
    }

    private Class resolveClass(final String beanName) {
        try {
            return Class.forName(beanName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }





}

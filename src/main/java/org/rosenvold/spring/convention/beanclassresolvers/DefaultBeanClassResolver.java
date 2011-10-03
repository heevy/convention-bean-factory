package org.rosenvold.spring.convention.beanclassresolvers;

import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;
import org.rosenvold.spring.convention.interfacemappers.AdapterSuffix;
import org.rosenvold.spring.convention.interfacemappers.DefaultPrefix;

/**
 * @author Kristian Rosenvold
 */
public class DefaultBeanClassResolver extends GenericNameToClassResolver {
    public DefaultBeanClassResolver() {
        super(new DefaultPrefix(), new AdapterSuffix());
    }
}

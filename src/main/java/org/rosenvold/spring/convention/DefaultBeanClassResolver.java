package org.rosenvold.spring.convention;

import org.rosenvold.spring.convention.beanclassresolvers.GenericBeanClassResolver;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;
import org.rosenvold.spring.convention.interfacemappers.AdapterSuffix;
import org.rosenvold.spring.convention.interfacemappers.DefaultPrefix;

/**
 * @author Kristian Rosenvold
 */
public class DefaultBeanClassResolver extends GenericBeanClassResolver{
    private static final CandidateEvaluator candidateEvaluator = new DefaultCandidateEvaluator();
    public DefaultBeanClassResolver() {
        super(new DefaultPrefix(candidateEvaluator), new AdapterSuffix(candidateEvaluator));
    }
}

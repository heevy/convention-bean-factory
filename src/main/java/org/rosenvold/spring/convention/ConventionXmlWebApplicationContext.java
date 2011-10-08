package org.rosenvold.spring.convention;

import org.rosenvold.spring.convention.beanclassresolvers.DefaultBeanClassResolver;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Kristian Rosenvold
 */
public class ConventionXmlWebApplicationContext extends XmlWebApplicationContext
{
    private NameToClassResolver nameToClassResolver;
    private CandidateEvaluator candidateEvaluator;

    public ConventionXmlWebApplicationContext()
    {
        nameToClassResolver = new DefaultBeanClassResolver();
        candidateEvaluator = new DefaultCandidateEvaluator();
    }


    public void setNameToClassResolver( NameToClassResolver nameToClassResolver )
    {
        this.nameToClassResolver = nameToClassResolver;
    }

    public void setCandidateEvaluator( CandidateEvaluator candidateEvaluator )
    {
        this.candidateEvaluator = candidateEvaluator;
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        return new ConventionBeanFactory( nameToClassResolver, candidateEvaluator );
    }
}

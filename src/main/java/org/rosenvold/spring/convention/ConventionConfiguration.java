package org.rosenvold.spring.convention;

import org.rosenvold.spring.convention.beanclassresolvers.DefaultBeanClassResolver;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface ConventionConfiguration {
    public Class<? extends NameToClassResolver> nameToClassResolver() default DefaultBeanClassResolver.class;
    public Class<? extends CandidateEvaluator> candidateEvaluator() default DefaultCandidateEvaluator.class;

}


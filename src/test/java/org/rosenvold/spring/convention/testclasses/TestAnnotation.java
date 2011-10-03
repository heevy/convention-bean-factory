package org.rosenvold.spring.convention.testclasses;

import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.NameToClassResolver;
import org.rosenvold.spring.convention.beanclassresolvers.DefaultBeanClassResolver;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface TestAnnotation {
    public Class<? extends NameToClassResolver> nameToClassResolver() default DefaultBeanClassResolver.class;
    public Class<? extends CandidateEvaluator> candidateEvaluator() default DefaultCandidateEvaluator.class;

}


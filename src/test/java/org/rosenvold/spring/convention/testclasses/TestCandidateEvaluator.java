package org.rosenvold.spring.convention.testclasses;

import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;

/**
 * @author Kristian Rosenvold
 */
public class TestCandidateEvaluator extends DefaultCandidateEvaluator {

    @Override
    public boolean isBean(Class prospect) {
        return super.isBean(prospect)
                || prospect.isAnnotationPresent(TestAnnotation.class);
    }
}

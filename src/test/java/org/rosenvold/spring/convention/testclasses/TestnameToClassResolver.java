package org.rosenvold.spring.convention.testclasses;

import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.beanclassresolvers.GenericNameToClassResolver;
import org.rosenvold.spring.convention.interfacemappers.StubSuffix;

public class TestnameToClassResolver extends GenericNameToClassResolver {
    public TestnameToClassResolver() {
        super(new StubSuffix());
    }
}

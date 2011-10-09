package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.testclasses.requestscopedlazy.RequestScopedLazy;
import org.rosenvold.spring.convention.testclasses.TestCandidateEvaluator;
import org.rosenvold.spring.convention.testclasses.TestnameToClassResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * @author Kristian Rosenvold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-requestscoped-lazy.xml"
}, loader = ConventionContextLoader.class)

@ConventionConfiguration(candidateEvaluator = TestCandidateEvaluator.class, nameToClassResolver = TestnameToClassResolver.class)
public class RequestScopedLazyTest
{

    @SuppressWarnings({"SpringJavaAutowiringInspection"})
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RequestScopedLazy requestScopedLazy;

    @Test
    public void testLoadContext() throws Exception {
       // requestScopedLazy.fud();
    }
}

package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.aopclasses.TestInterface;
import org.rosenvold.spring.convention.scope.MockSessionScopeContextLoader;
import org.rosenvold.spring.convention.testclasses.TestCandidateEvaluator;
import org.rosenvold.spring.convention.testclasses.TestnameToClassResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-aop-convention.xml"
}, loader = ConventionContextLoader.class)

@ConventionConfiguration(candidateEvaluator = TestCandidateEvaluator.class, nameToClassResolver = TestnameToClassResolver.class)
public class ConventionAopTest
{

    @SuppressWarnings({"SpringJavaAutowiringInspection"})
    @Autowired
    TestInterface scopedProxy;

    @Test
    public void testLoadContext()
            throws Exception {
        scopedProxy.testMethod();
        assertTrue(  scopedProxy.isAopInvoked());
    }
}

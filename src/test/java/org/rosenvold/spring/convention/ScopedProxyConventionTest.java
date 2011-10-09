package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.scope.MockSessionScopeContextLoader;
import org.rosenvold.spring.convention.testclasses.DefaultScopedProxy;
import org.rosenvold.spring.convention.testclasses.DefaultScopedProxy2;
import org.rosenvold.spring.convention.testclasses.ScopedProxy;
import org.rosenvold.spring.convention.testclasses.ScopedProxy2;
import org.rosenvold.spring.convention.testclasses.TestCandidateEvaluator;
import org.rosenvold.spring.convention.testclasses.TestnameToClassResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Kristian Rosenvold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-totally-empty.xml"
}, loader = MockSessionScopeContextLoader.class)

@ConventionConfiguration(candidateEvaluator = TestCandidateEvaluator.class, nameToClassResolver = TestnameToClassResolver.class)
public class ScopedProxyConventionTest
{

    @SuppressWarnings({"SpringJavaAutowiringInspection"})
    @Autowired
    ScopedProxy2 scopedProxy;
    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testLoadContext()
            throws Exception {
        assertNotNull( scopedProxy);
//        assertFalse(scopedProxy instanceof DefaultScopedProxy2 );
        final ScopedProxy2 bean = applicationContext.getBean( ScopedProxy2.class );
    }
}

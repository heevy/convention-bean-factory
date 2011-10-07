package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.aopclasses.TestInterface;
import org.rosenvold.spring.convention.testclasses.PrototypeScoped;
import org.rosenvold.spring.convention.testclasses.PrototypeScopedAnnotated;
import org.rosenvold.spring.convention.testclasses.TestCandidateEvaluator;
import org.rosenvold.spring.convention.testclasses.TestnameToClassResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.net.ApplicationProxy;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-prototype-convention.xml"
}, loader = ConventionContextLoader.class)

@ConventionConfiguration(candidateEvaluator = TestCandidateEvaluator.class, nameToClassResolver = TestnameToClassResolver.class)
public class PrototypeTest
{

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testXmlBasedBean()
            throws Exception {
        final PrototypeScoped bean1 = (PrototypeScoped) applicationContext.getBean( "protoBean1" );
        final PrototypeScoped bean2 = (PrototypeScoped) applicationContext.getBean( "protoBean1" );
        assertNotSame( bean1, bean2 );
    }

    @Test
    public void testLoadContext()
            throws Exception {
        final PrototypeScopedAnnotated bean1 = (PrototypeScopedAnnotated) applicationContext.getBean( "org.rosenvold.spring.convention.testclasses.PrototypeScopedAnnotated" );
        final PrototypeScopedAnnotated bean2 = (PrototypeScopedAnnotated) applicationContext.getBean( "org.rosenvold.spring.convention.testclasses.PrototypeScopedAnnotated" );
        assertNotSame( bean1, bean2 );
    }

}

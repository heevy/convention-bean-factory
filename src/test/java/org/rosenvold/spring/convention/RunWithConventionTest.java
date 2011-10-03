package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.testclasses.*;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-test.xml"
}, loader = ConventionContextLoader.class)

@ConventionConfiguration(candidateEvaluator = TestCandidateEvaluator.class, nameToClassResolver = TestnameToClassResolver.class)
public class RunWithConventionTest {

    @Autowired
    AnnotatedClass annotatedClass;

    @Test
    public void testLoadContext()
            throws Exception {
        assertNotNull( annotatedClass);
    }
}

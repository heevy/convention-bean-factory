package org.rosenvold.spring.convention;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Kristian Rosenvold
 */
public class ConventionContextLoaderTest {
    @Test
    public void testLoadContext() throws Exception {
       ConventionContextLoader conventionContextLoader = new ConventionContextLoader();
        final ApplicationContext applicationContext = conventionContextLoader.loadContext("classpath:applicationContext-test.xml");
        TestService testService = (TestService) applicationContext.getBean("fud");
        assertNotNull( testService);
        DefaultTestService2 testService2 = (DefaultTestService2) applicationContext.getBean(TestService2.class);
        assertNotNull( testService2);
        assertNotNull( testService2.testService );
        assertNotNull( testService2.interfaceLessService );

    }
}

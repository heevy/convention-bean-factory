package org.rosenvold.spring.convention;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Kristian Rosenvold
 */
public class StandardContextLoaderTest
{
    @Test
    public void testLoadContext()
        throws Exception
    {
        GenericXmlContextLoader conventionContextLoader = new GenericXmlContextLoader();
        final ApplicationContext applicationContext =
            conventionContextLoader.loadContext( "classpath:applicationContext-test2.xml" );
        TestService testService = (TestService) applicationContext.getBean( "fud" );
        assertNotNull( testService );
        DefaultTestService2 testService2 = (DefaultTestService2) applicationContext.getBean( TestService2.class );
        assertNotNull( testService2 );
        assertNotNull( testService2.testService );

    }
}

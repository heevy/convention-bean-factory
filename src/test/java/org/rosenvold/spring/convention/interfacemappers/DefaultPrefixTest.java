package org.rosenvold.spring.convention.interfacemappers;

import org.junit.Test;
import org.rosenvold.spring.convention.TestService2;

import static org.junit.Assert.assertEquals;

/**
 * @author Kristian Rosenvold
 */
public class DefaultPrefixTest
{
    @Test
    public void testInnerClassIf()
        throws Exception
    {
        DefaultPrefix defaultPrefix = new DefaultPrefix();
        assertEquals( "org.rosenvold.spring.convention.interfacemappers.DefaultPrefixTest$DefaultFud",
                      defaultPrefix.getRemappedName( Fud.class ) );

    }

    @Test
    public void testRegularIf()
        throws Exception
    {
        DefaultPrefix defaultPrefix = new DefaultPrefix();
        assertEquals( "org.rosenvold.spring.convention.DefaultTestService2",
                      defaultPrefix.getRemappedName( TestService2.class ) );

    }

    static interface Fud
    {
    }
}

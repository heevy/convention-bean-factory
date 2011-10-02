package org.rosenvold.spring.convention.interfacemappers;

import org.junit.Test;
import org.rosenvold.spring.convention.CandidateEvaluator;
import org.rosenvold.spring.convention.TestService2;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;

import static org.junit.Assert.assertEquals;

/**
 * @author Kristian Rosenvold
 */
public class DefaultPrefixTest
{
    private static final CandidateEvaluator candidateEvaluator = new DefaultCandidateEvaluator();

    @Test
    public void testInnerClassIf()
        throws Exception
    {
        DefaultPrefix defaultPrefix = new DefaultPrefix();
        assertEquals( "org.rosenvold.spring.convention.interfacemappers.DefaultPrefixTest$DefaultFud",
                      defaultPrefix.getBeanClassName(Fud.class) );

    }

    @Test
    public void testRegularIf()
        throws Exception
    {
        DefaultPrefix defaultPrefix = new DefaultPrefix();
        assertEquals( "org.rosenvold.spring.convention.DefaultTestService2",
                      defaultPrefix.getBeanClassName(TestService2.class) );

    }

    static interface Fud
    {
    }
}

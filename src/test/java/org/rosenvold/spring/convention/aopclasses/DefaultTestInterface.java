package org.rosenvold.spring.convention.aopclasses;

import org.springframework.stereotype.Component;

/**
 * @author Kristian Rosenvold
 */
@Component
public class DefaultTestInterface implements TestInterface
{
    public boolean testMethodCalled = false;
    public boolean aopInvoked = false;
    public void testMethod()
    {
        testMethodCalled = true;
    }

    public void setAopCalled()
    {
        aopInvoked = true;
    }

    public boolean isAopInvoked()
    {
        return aopInvoked;
    }
}

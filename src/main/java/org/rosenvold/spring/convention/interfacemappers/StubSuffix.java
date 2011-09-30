package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public class StubSuffix
{
    public String prefixed( Class aClass )
    {
        return aClass.getName() + "Stub";
    }
}
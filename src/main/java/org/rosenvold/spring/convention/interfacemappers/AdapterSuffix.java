package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public class AdapterSuffix
{
    public String prefixed( Class aClass )
    {
        return aClass.getName() + "Adapter";
    }
}
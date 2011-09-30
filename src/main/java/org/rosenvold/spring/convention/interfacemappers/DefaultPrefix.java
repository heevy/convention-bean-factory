package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public class DefaultPrefix
{
    public String prefixed( Class aClass )
    {
        String target = "Default" + aClass.getSimpleName();
        final Class enclosingClass = aClass.getEnclosingClass();
        return ( enclosingClass == null )
            ? aClass.getPackage().getName() + "." + target
            : enclosingClass.getName() + "$" + target;
    }
}
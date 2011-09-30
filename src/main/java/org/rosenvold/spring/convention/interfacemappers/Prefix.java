package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public class Prefix extends InterfaceMapper
{
    private final String prefix;

    protected Prefix( String prefix )
    {
        this.prefix = prefix;
    }

    String getRemappedName( Class aClass )
    {
        String target = prefix + aClass.getSimpleName();
        final Class enclosingClass = aClass.getEnclosingClass();
        return ( enclosingClass == null )
            ? aClass.getPackage().getName() + "." + target
            : enclosingClass.getName() + "$" + target;
    }


}
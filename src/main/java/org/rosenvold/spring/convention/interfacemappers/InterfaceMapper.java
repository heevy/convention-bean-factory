package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public abstract class InterfaceMapper
{
    public Class prefixed( Class aClass )
    {
        final String s = getRemappedName( aClass );
        return resolveClass( s );
    }

    abstract String getRemappedName( Class aClass );


    protected Class resolveClass( final String beanName )
    {
        try
        {
            return Class.forName( beanName );
        }
        catch ( ClassNotFoundException ex )
        {
            return null;
        }
    }


}

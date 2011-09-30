package org.rosenvold.spring.convention;

import org.rosenvold.spring.convention.interfacemappers.DefaultPrefix;

/**
 * @author Kristian Rosenvold
 */
public class DefaultBeanClassResolver
    implements BeanClassResolver
{
    private final DefaultPrefix defaultPrefix = new DefaultPrefix();
    public Class resolveBean( String name )
    {
        final Class aClass = resolveClass( name );
        if ( aClass != null && aClass.isInterface() )
        {
            final Class prefixed = defaultPrefix.prefixed( aClass );
            if (prefixed != null) return prefixed;
        }
        return aClass;
    }

    private Class resolveClass( final String beanName )
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

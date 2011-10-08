package org.rosenvold.spring.convention;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;

/**
 * @author Kristian Rosenvold
 */
public class ConventionContextLoaderListener extends ContextLoaderListener
{

    private static final String NAME_TO_CLASS_RESOLVER = "nameToClassResolver";
    private static final String CANDDATEEVAULATOR = "candidateEvaluator";

    protected Class<?> determineContextClass(ServletContext servletContext) {
        return ConventionXmlWebApplicationContext.class;

    }

    protected void customizeContext(
            ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
        String ntoc = servletContext.getInitParameter( NAME_TO_CLASS_RESOLVER );
        Object nameTOcLASS =  instantiate( getClass( ntoc ) );
        if (nameTOcLASS != null){
            ((ConventionXmlWebApplicationContext)applicationContext).setNameToClassResolver(
                (NameToClassResolver) nameTOcLASS );
        }
        String ceval = servletContext.getInitParameter( CANDDATEEVAULATOR );
        Object candidateEvaluator =  instantiate( getClass( ceval ) );
        if (nameTOcLASS != null){
            ((ConventionXmlWebApplicationContext)applicationContext).setCandidateEvaluator(
                (CandidateEvaluator) candidateEvaluator );
        }
    }

    Object instantiate(Class clazz){
        if (clazz == null) return null;
        try
        {
            return clazz.newInstance();
        }
        catch ( InstantiationException e )
        {
            throw new RuntimeException( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    Class getClass(String name){
        if (name == null) return null;
        try
        {
            return Class.forName(  name );
        }
        catch ( ClassNotFoundException e )
        {
            return null;
        }
    }
}

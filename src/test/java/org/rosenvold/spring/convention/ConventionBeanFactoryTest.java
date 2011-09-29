package org.rosenvold.spring.convention;

import org.junit.Test;
import org.springframework.stereotype.Service;

import static junit.framework.Assert.*;

/**
 * @author Kristian Rosenvold
 */
public class ConventionBeanFactoryTest {
    private final ConventionBeanFactory conventionBeanFactory = new ConventionBeanFactory();

    @Test
    public void containsBean() throws Exception {
        assertTrue(conventionBeanFactory.containsBean(Annotated.class.getName()));
        assertFalse(conventionBeanFactory.containsBean(ConventionBeanFactoryTest.class.getName()));
    }

    @Test
    public void singleton() throws Exception {
        assertTrue(conventionBeanFactory.isSingleton(Annotated.class.getName()));
    }

    @Test
    public void prototype() throws Exception {
        assertFalse(conventionBeanFactory.isPrototype(Annotated.class.getName()));
    }

    @Test
    public void getType() throws Exception {
        assertEquals(DefaultAnnotated.class, conventionBeanFactory.getType(Annotated.class.getName()));
    }

    @Test
    public void typeMatch() throws Exception {
        assertTrue(conventionBeanFactory.isTypeMatch(Annotated.class.getName(), DefaultAnnotated.class));
        assertFalse(conventionBeanFactory.isTypeMatch(Annotated.class.getName(), ConventionBeanFactoryTest.class));
    }

    @Test
    public void getBeanByInterfaceWithClassRequirement() throws Exception {
        final DefaultAnnotated bean = conventionBeanFactory.getBean(Annotated.class.getName(), DefaultAnnotated.class);
        assertEquals(DefaultAnnotated.class, bean.getClass());
    }

    @Test
    public void getBeanByInterfaceWithClassRequirementUnsatisfied() throws Exception {
        final ConventionBeanFactoryTest bean = conventionBeanFactory.getBean(Annotated.class.getName(), ConventionBeanFactoryTest.class);
        assertNull(bean);
    }

    @Test
    public void getBeanByInterface() throws Exception {
        final DefaultAnnotated bean = (DefaultAnnotated) conventionBeanFactory.getBean(Annotated.class);
        assertEquals(DefaultAnnotated.class, bean.getClass());
    }

    @Test
    public void getBeanByImplClass() throws Exception {
        final Annotated bean = conventionBeanFactory.getBean(DefaultAnnotated.class);
        assertEquals(DefaultAnnotated.class, bean.getClass());
    }


    @Service
    public static class DefaultAnnotated implements Annotated {

    }

    interface Annotated {

    }
}

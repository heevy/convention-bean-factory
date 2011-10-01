package org.rosenvold.spring.convention;

/**
 * @author Kristian Rosenvold
 */
public class TestBeanClassResolver implements NameToClassResolver {
    public Class resolveBean(String name) {
        final Class aClass = resolveClass(name);
        if (aClass != null && aClass.isInterface()) {
            String target = "Default" + aClass.getSimpleName();
            final Class enclosingClass = aClass.getEnclosingClass();
            final String ifName = (enclosingClass != null)
                    ? enclosingClass.getName() + "$" + target
                    : aClass.getPackage().getName() + "." + target;
            return resolveClass(ifName);
        }
        return aClass;
    }

    private Class resolveClass(final String beanName) {
        try {
            return Class.forName(beanName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }


}

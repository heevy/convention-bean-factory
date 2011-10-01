package org.rosenvold.spring.convention;

/**
 * Resolves an interface to its implementation class
 *
 * @author Kristian Rosenvold
 */
public interface InterfaceToImplementationMapper {
    Class getBeanClass(Class aClass);
}

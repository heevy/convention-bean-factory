package org.rosenvold.spring.convention;

/**
 * Resolves an interface to its implementation class
 *
 * @author Kristian Rosenvold
 */
public interface InterfaceToImplementationMapper {
    String getBeanClassName(Class aClass);
}

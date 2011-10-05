package org.rosenvold.spring.convention.testclasses;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * @author Kristian Rosenvold
 */
//@TestAnnotation
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class DefaultScopedProxy implements ScopedProxy {
}

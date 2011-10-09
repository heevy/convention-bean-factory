package org.rosenvold.spring.convention.testclasses.scopedproxy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Rosenvold
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class DefaultScopedProxy2
    implements ScopedProxy2 {
}

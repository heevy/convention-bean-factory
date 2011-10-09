package org.rosenvold.spring.convention.testclasses.requestscopedlazy;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Rosenvold
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@Lazy
public class DefaultRequestScopedLazy implements RequestScopedLazy{

    public void fud() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

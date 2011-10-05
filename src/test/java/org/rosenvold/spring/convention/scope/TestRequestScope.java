package org.rosenvold.spring.convention.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.context.request.RequestScope;

/**
 * @author <a href="mailto:maja@zenior.no">Maja S Bratseth</a>
 */
public class TestRequestScope extends RequestScope {

    @Override
    public Object get(String name, ObjectFactory objectFactory) {
        return super.get(name, objectFactory);
    }

}

package org.rosenvold.spring.convention.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.context.request.SessionScope;

public class TestSessionScope extends SessionScope {

    @Override
    public Object get(String name, ObjectFactory objectFactory) {
        return super.get(name, objectFactory);
    }
}

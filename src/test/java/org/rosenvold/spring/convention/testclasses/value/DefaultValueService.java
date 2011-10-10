package org.rosenvold.spring.convention.testclasses.value;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultValueService implements ValueService {

    String foo;


    public String getFoo() {
        return foo;
    }

    @Value("${testValue}")
    public void setFoo(String foo) {
        this.foo = foo;
    }
}

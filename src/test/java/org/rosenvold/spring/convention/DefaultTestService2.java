package org.rosenvold.spring.convention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Rosenvold
 */
@Component
public class DefaultTestService2
    implements TestService2
{
    @Autowired
    public TestService testService;

    @Autowired
    public InterfaceLessService interfaceLessService;
}

package org.rosenvold.spring.convention.testclasses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author Kristian Rosenvold
 */
@Component
public class DefaultTestService2
        implements TestService2 {
    public TestService testService;
    public ApplicationContext applicationContext;

    @Resource(name = "testResource")
    public Properties test ;


    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public InterfaceLessService interfaceLessService;
}

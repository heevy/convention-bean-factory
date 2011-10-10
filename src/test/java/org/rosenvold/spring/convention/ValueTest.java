package org.rosenvold.spring.convention;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosenvold.spring.convention.testclasses.value.ValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;

import static junit.framework.Assert.assertEquals;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext-value.xml"
}, loader = GenericXmlContextLoader.class)
public class ValueTest {

    @Autowired
    private ValueService valueService;

    @Test
    public void checkInjectedValue(){
        final String foo = valueService.getFoo();
        assertEquals("23", foo);
        System.out.println("foo = " + foo);
    }
}

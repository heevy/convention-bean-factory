package org.rosenvold.spring.convention;

/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * @author Kristian Rosenvold
 */
public class ConventionContextLoader
    extends AbstractContextLoader
{

    private final GenericXmlContextLoader genericXmlContextLoader = new GenericXmlContextLoader();


    @Override
    protected String getResourceSuffix()
    {
        return "-convention";
    }

    public ApplicationContext loadContext( String... locations )
        throws Exception
    {

        final ConfigurableApplicationContext parent = genericXmlContextLoader.loadContext( locations );

        ConventionBeanFactory conventionBeanFactory = new ConventionBeanFactory( parent );

/*        RequiredAnnotationBeanPostProcessor requiredAnnotationBeanPostProcessor  =  new RequiredAnnotationBeanPostProcessor();
        requiredAnnotationBeanPostProcessor.setBeanFactory( conventionBeanFactory);
        conventionBeanFactory.addBeanPostProcessor(requiredAnnotationBeanPostProcessor);
  */
        AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator =
            new AnnotationAwareAspectJAutoProxyCreator();
        annotationAwareAspectJAutoProxyCreator.setBeanFactory( conventionBeanFactory );
        conventionBeanFactory.addBeanPostProcessor( annotationAwareAspectJAutoProxyCreator );
        final AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor =
            new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory( conventionBeanFactory );
        conventionBeanFactory.addBeanPostProcessor( autowiredAnnotationBeanPostProcessor );

        conventionBeanFactory.setAutowireCandidateResolver( new SimpleAutowireCandidateResolver() );
        final GenericApplicationContext genericApplicationContext = new GenericApplicationContext(conventionBeanFactory);
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName( GenericApplicationContext.class.getName());
        genericApplicationContext.registerBeanDefinition("ac_fud!!", beanDefinition);
        return genericApplicationContext;
    }
}

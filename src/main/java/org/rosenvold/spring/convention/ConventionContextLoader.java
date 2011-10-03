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

import org.rosenvold.spring.convention.beanclassresolvers.DefaultBeanClassResolver;
import org.rosenvold.spring.convention.candidateevaluators.DefaultCandidateEvaluator;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.AbstractContextLoader;

/**
 * @author Kristian Rosenvold
 */
public class ConventionContextLoader
        extends AbstractContextLoader {
    public Class<? extends CandidateEvaluator> candidateEvaluator = DefaultCandidateEvaluator.class;
    public Class<? extends NameToClassResolver> nameToClassResolver = DefaultBeanClassResolver.class;


    @Override
    protected String getResourceSuffix() {
        return "-convention";
    }

    protected String[] modifyLocations(Class<?> clazz, String... locations) {
        final ConventionConfiguration annotation = clazz.getAnnotation(ConventionConfiguration.class);
        if (annotation != null ){
        candidateEvaluator = annotation.candidateEvaluator();
        nameToClassResolver = annotation.nameToClassResolver();
        }
        return locations;
    }

    public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
        ConventionBeanFactory conventionBeanFactory = new ConventionBeanFactory(createNameToClassResolver(), createCandidateEvaluator());
        GenericApplicationContext context = new GenericApplicationContext(conventionBeanFactory);
        prepareContext(context);
        customizeBeanFactory(context.getDefaultListableBeanFactory());
        createBeanDefinitionReader(context).loadBeanDefinitions(locations);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        customizeContext(context);
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    private NameToClassResolver createNameToClassResolver(){
        try {
            return nameToClassResolver.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }  catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private CandidateEvaluator createCandidateEvaluator(){
        try {
            return candidateEvaluator.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }  catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void prepareContext(GenericApplicationContext context) {
    }

    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
    }

    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        return new XmlBeanDefinitionReader(context);
    }

    protected void customizeContext(GenericApplicationContext context) {
    }


}

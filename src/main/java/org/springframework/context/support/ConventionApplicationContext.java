package org.springframework.context.support;

import org.rosenvold.spring.convention.ConventionBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.*;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
public class ConventionApplicationContext extends GenericApplicationContext {
    public ConventionApplicationContext(ConventionBeanFactory conventionBeanFactory) {
        super(conventionBeanFactory);
    }

    public void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // Tell the internal bean factory to use the context's class loader etc.
        beanFactory.setBeanClassLoader(getClassLoader());
        beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver());
        beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this));

        // Configure the bean factory with context callbacks.
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
        beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

        // BeanFactory interface not registered as resolvable type in a plain factory.
        // MessageSource registered (and found for autowiring) as a bean.
        beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
        beanFactory.registerResolvableDependency(ResourceLoader.class, this);
        beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
        beanFactory.registerResolvableDependency(ApplicationContext.class, this);

        // Detect a LoadTimeWeaver and prepare for weaving, if found.
/*        if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
            beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
            // Set a temporary ClassLoader for type matching.
            beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
        }
  */
        // Register default environment beans.
      /* if (!beanFactory.containsBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
            Map systemProperties;
            try {
                systemProperties = System.getProperties();
            } catch (AccessControlException ex) {
                systemProperties = new ReadOnlySystemAttributesMap() {
                    @Override
                    protected String getSystemAttribute(String propertyName) {
                        try {
                            return System.getProperty(propertyName);
                        } catch (AccessControlException ex) {
                            if (logger.isInfoEnabled()) {
                                logger.info("Not allowed to obtain system property [" + propertyName + "]: " +
                                        ex.getMessage());
                            }
                            return null;
                        }
                    }
                };
            }
            beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, systemProperties);
        }*/

  /*      if (!beanFactory.containsBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
            Map<String, String> systemEnvironment;
            try {
                systemEnvironment = System.getenv();
            } catch (AccessControlException ex) {
                systemEnvironment = new ReadOnlySystemAttributesMap() {
                    @Override
                    protected String getSystemAttribute(String variableName) {
                        try {
                            return System.getenv(variableName);
                        } catch (AccessControlException ex) {
                            if (logger.isInfoEnabled()) {
                                logger.info("Not allowed to obtain system environment variable [" + variableName + "]: " +
                                        ex.getMessage());
                            }
                            return null;
                        }
                    }
                };
            }
            beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, systemEnvironment);
        }
    }
    */
    }
}

<h1>Spring 3 convention-bean-factory</h1>

A convention based bean factory/application context that
automatically defines spring beans based on conventions,
not configuration.

The convention context is entirely lazy and driven by "getBean"
requests, which means that it does not even load any classes unless they
have been requested for wiring by "some" client. So where other solutions
can make your entire context lazy-loaded, convention does not even
load the class for beans that haven't been requested. For the
/really large/ application contexts this might make a huge difference in startup
performance<p/>


The convention-bean-factory can also be configured with xml, component scanning or
javaconfig - just like your current context. This will be loaded
just like your current context, so for increased performance it is important
to keep this part lean.


In convention mode there is no "list of beans" but instead resolves
each bean upon request, effectively reconstructing the bean-definition every time<p/>
The "traditional" context has precedence over the "convention" context, meaning
convention will not scan for beans that are traditionally defined.


<h3 style="color:red">
Convention is totally alpha. Don't use this unless you're comfortable with debugging open source code. Submit pull requests instead of whining
 if something doesn't work.
</h3>

Convention is designed as an efficient replacement for component-scan based contexts,
and can be intermingled in the same configuration: Run convention with your tests
and run regular component-scan in your production environment. If the entire context is
always loaded anyway, regular spring is still a bit faster.


<h2>Using it</h2>

Add the following to your pom to download from central:</p>

     <dependency>
         <groupId>org.rosenvold.spring</groupId>
         <artifactId>convention-bean-factory</artifactId>
         <version>0.1</version>
     <dependency>


* Change a context loader.</p>

Change your context loader to org.rosenvold.spring.convention.ConventionContextLoader (or extend it if you already have your own).</p>

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {
    "/applicationContext.xml"
    }, loader = org.rosenvold.spring.convention.ConventionContextLoader.class)
    @ConventionConfiguration(candidateEvaluator = DefaultCandidateEvaluator.class, nameToClassResolver = DefaultBeanClassResolver.class)
    public class MyTestClass{
     ... Your test class...
    }

At this point you will have a convention mapping that requires your implementations to be annotated with @Component, @Service, @Repository or @Controller
the DefaultBeanClassResolver looks for implementation classes that are interface-name prefixed with "Default" or suffixed with "Adapter".

You can make your own strategy as described in the next step.

* Implement a naming strategy:</p>

You can make your own custom strategies for mapping interfaces->implementations, as show below.

    public class DefaultBeanClassResolver extends GenericNameToClassResolver {
    public DefaultBeanClassResolver() {
        super(new DefaultPrefix(), new AdapterSuffix());
    }

Look at the source code in package org.rosenvold.spring.convention.interfacemappers to see how to make new prefixes/suffixes and to change the
package.

You can also extend the criteria for accepting a class as a bean (the candidateEvaluator) by making your own implementation
of the CandidateEvaluator inteface or subclassing DefaultCandidateEvaluator.

* Trim your old context. You might want to consider disabling all kinds of component-scanning, and make
        convention pick it up instead.</p>
* Trimming your context is vital to making things start faster


<h1>Known limitations</h1>
- proptotype scope does probably not work for convention beans in 0.1
  (Works for beans defined in traditional modes)
- convention is not picky about deciding to use /anything/ satisfying a candidateEvaluator as a bean.
- Something like 30% slower than regular spring if the entire context is loaded anyway. This will improve in future versions.

*Submit pull-requests with testscases, no testcase no can do*

<h1>Community</h1>
Join the mailing list at https://groups.google.com/group/convention-bean-factory

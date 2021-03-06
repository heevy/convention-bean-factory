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
convention will not scan for beans that are traditionally defined. A single interface/bean
must /either/ be defined in traditional manner /or/ in convention mode (but a convention based bean
can /reference/ a traditional bean and vice versa)


Convention is designed as an efficient replacement for component-scan based contexts,
and can be intermingled in the same configuration: Run convention with your tests
and run regular component-scan in your production environment. Convention is normally
faster than regular spring contexts.

To avoid deadlocking, convention is quite heavily synchronized, which probably means
regular spring contexts are better for production environments. (But I will not be *totally* convinced this
is true since we're so much faster than conventional spring)


<h2>Changelog</h2>

0.6 <p/>
  More caching.

0.5 <p/>
  Optimized, optimized, optimized.
  Current cache implementation is a bit of a ragtag band of hashmaps, but really fast. Make pretty later ;)

0.4 <p/>
  scopeProxy mode annotation handled properly
  @Value injections work.
  Uses more of the existing spring code (more correct, a little slower)

0.3 <p/>
  Prototype scope fixed<p/>

0.2: <p/>
   scope proxies now work properly.<p/>
   AOP works, but remember that convention does not pick up your @Aspect classes automatically
   improved performance<p/>

0.1 <p/>
   First release <p/>


<h2>Using it</h2>

Add the following to your pom to download from central:</p>

     <dependency>
         <groupId>org.rosenvold.spring</groupId>
         <artifactId>convention-bean-factory</artifactId>
         <version>0.5</version>
     <dependency>


<h3>Change a context loader.</h3>

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

<h3>The default nametoClassResolver</h3>

DefaultBeanClassResolver implements the following mapping strategy for interfaces:

com.foo.Baz looks for com.foo.DefaultBaz then com.foo.BazAdapter.


<h3>Implement a custom nametoClassResolver</h3>

You can make your own custom strategies for mapping interfaces->implementations, as show below.

    public class MyBeanClassResolver extends GenericNameToClassResolver {
      private static final PackageManipulator packageManipulator =
          PackageManipulator.createFindReplace("myapp.integration", "myapp.stub.integration");
      public MyBeanClassResolver() {
          super(new HardCodedMappings(), new StubSuffix(), new StubSuffix(packageManipulator), new DefaultPrefix(), new ImplSuffix());
      }
    }

This mapping would prefer stub versions over "default" versions.

Look at the source code in package org.rosenvold.spring.convention.interfacemappers to see how to make new prefixes/suffixes and to change the
package.

<h3>Hard coded mappings</h3>
If you just want to test the stuff and don't want to move all the classes until you're convinced, you may make a file of
hard-coded mappings:

    public class HardCodedMappings extends  UserSpecifiedMapper {
       public HardCodedMappings() {
         put("com.xyz.Myif", "com.xyz.fud.MyIfImpl");
         put("com.xyz.OtherFud", "com.xyz.fud.FudFudImpl");
       }
    }

<h3>Determining if the resolved class is usabel as a bean or not</h3>
You can also extend the criteria for accepting a class as a bean (the candidateEvaluator) by making your own implementation
of the CandidateEvaluator inteface or subclassing DefaultCandidateEvaluator.

* Trim your old context. You might want to consider disabling all kinds of component-scanning, and make
        convention pick it up instead.</p>
* Trimming your context is vital to making things start faster


<h1>Known limitations</h1>
- convention is not picky about deciding to use /anything/ satisfying a candidateEvaluator as a bean.
- Convention does not handle wiring of abstract classes (should be easy to fix).
- Aspect implementations must be defined traditionally

*Submit pull-requests with testscases, no testcase no can do*

<h1>Community</h1>
Join the mailing list at https://groups.google.com/group/convention-bean-factory

<h1>convention-bean-factory</h1>

An application context that can be put in front of your current 
application context, implementing a convention-over-configuration
approach to defining beans. <p/>


Additionally, the convention context is entirely lazy and driven by "getBean"
requests, which means that it does not even load any classes unless they 
have been requested for wiring by "some" client. So where other solutions
can make your entire context lazy-loaded, convention does not even 
load the class for beans that haven't been requested.<p/>

Convention delegates to the parent application context if it cannot satisfy
a given request. This context is loaded "as usual", so it is critical for the
        success of convention to keep this context lean.

<h3 style="color:red">
Convention is totally alpha, as can be seen from the crappy design of this page. Don't assume anythign works.
Don't use this unless you're comfortable with debugging open source code. Submit pull requests instead of whining
 if something doesn't work
</h3>

<h2>Using it</h2>

Build this project and add the following to your pom:</p>
     &lt;dependency></p>
         &lt;groupId>org.rosenvold.spring&lt;/groupId></p>
         &lt;artifactId>convention-bean-factory&lt;/artifactId></p>
         &lt;version>0.1-SNAPSHOT&lt;/version></p>
     &lt;/dependency></p>

(It will be published to central as soon as we reach 0.1 ;)<br/>

1. Change a context loader.</p>
Change/Make your context loader (MyContextLoader) to extend org.rosenvold.spring.convention.ConventionContextLoader.</p>
    @RunWith(SpringJUnit4ClassRunner.class)</p>
    @ContextConfiguration(locations = {</p>
       "/applicationContext.xml"</p>
    }, loader = MyContextLoader.class)</p>
     ... Your test class...
2. Implement a naming strategy:</p>

{code}
@Component
public class ClassNameResolver implements  org.rosenvold.spring.convention.BeanClassResolver {
        .. implement a strategy ...
}
{code}

Make sure "ClassNameResolver" is registered as a spring bean, which will make convention pick it up.

A call to getBean(org.fud.MyInterface.class) will result in a request to resolveBean("org.fud.MyInterface").

The method resolveBean should return a class that maps/corresponds to the provided org.fud.MyInterface. If null
        is returned, it delegates onto the parent application context.

Todo: Describe existing resolveBean implementations and how they
<br/>
3. Trim your old context. You might want to consider disabling all kinds of component-scanning, and make
        convention pick it up instead.</p>


<h2>Known limitations, submit patches with testscases</h2>
- proptotype scope unsupported as of now
package org.rosenvold.spring.convention.interfacemappers;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Kristian Rosenvold
 */
public class PackageManipulatorTest {
    @Test
    public void testGetRemappedPackageName() throws Exception {
        PackageManipulator packageManipulator = PackageManipulator.createFindReplace(".fud", ".fud.stub");
        assertEquals("com.fud.stub.FooBar", packageManipulator.getRemappedPackageName( "com.fud.FooBar"));
    }
    @Test
    public void testAppendAtEnd() throws Exception {
        PackageManipulator packageManipulator = PackageManipulator.createApppend("stub2");
        assertEquals("com.fud.stub2.FooBar", packageManipulator.getRemappedPackageName( "com.fud.FooBar"));
    }

    @Test
    public void testFindReplaceAppendAtEnd() throws Exception {
        PackageManipulator packageManipulator = PackageManipulator.createFindReplaceWithAppend("fud", "bar.fuz", "stub2");
        assertEquals("com.bar.fuz.stub2.FooBar", packageManipulator.getRemappedPackageName( "com.fud.FooBar"));
    }

}

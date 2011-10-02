package org.rosenvold.spring.convention.interfacemappers;

/**
 * @author Kristian Rosenvold
 */
public class PackageManipulator {
    private final String find;
    private final String replace;
    private final String appendAtEnd;

    private PackageManipulator(String find, String replace, String appendAtEnd) {
        this.find = find;
        this.replace = replace;
        this.appendAtEnd = appendAtEnd;
    }

    public static PackageManipulator createFindReplaceWithAppend(String find, String replace, String appendAtEnd) {
        return new PackageManipulator(find, replace, appendAtEnd);
    }

    public static PackageManipulator createFindReplace(String find, String replace) {
        return new PackageManipulator(find, replace, null);
    }

    public static PackageManipulator createApppend(String appendAtEnd) {
        return new PackageManipulator(null, null, appendAtEnd);
    }

    public String getRemappedPackageName( String name){
        StringBuilder result = new StringBuilder();
        int i;
        if ( find != null && (i = name.indexOf( find)) >= 0){
           result.append(name.substring( 0, i));
           result.append(replace);
           result.append( name.substring( i + find.length()));
        } else {
            result.append( name);
        }

        if (appendAtEnd == null)
            return result.toString();

        final int dot = result.lastIndexOf(".");
        if (dot >= 0){
            result.insert( dot + 1, appendAtEnd + ".");
        }
        return result.toString();
    }
}

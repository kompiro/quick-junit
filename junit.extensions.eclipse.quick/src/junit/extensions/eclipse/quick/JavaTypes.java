package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.junit.util.TestSearchEngine;

public class JavaTypes {

    public static boolean isTest(IType type) throws JavaModelException {
        return TestSearchEngine.isTestImplementor(type);
    }

}

package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class JavaTypes {
	public static final String TEST_INTERFACE_NAME= "junit.framework.Test"; //$NON-NLS-1$

    public static boolean isTest(IType type) throws JavaModelException {
		ITypeHierarchy typeHier= type.newSupertypeHierarchy(null);
		IType[] superInterfaces= typeHier.getAllInterfaces();
		for (int i= 0; i < superInterfaces.length; i++) {
			if (superInterfaces[i].getFullyQualifiedName('.').equals(TEST_INTERFACE_NAME))
				return true;
		}
		return false;
	}
}

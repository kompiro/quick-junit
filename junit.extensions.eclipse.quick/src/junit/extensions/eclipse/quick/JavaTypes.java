package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class JavaTypes {
	private static final char ENCLOSING_TYPE_SEPARATOR = '.';
	public static final String TEST_INTERFACE_NAME= "junit.framework.Test"; //$NON-NLS-1$
	private static final String TEST_ANNOTATION_NAME = "Test"; //$NON-NLS-1$
	private static final String TEST_ANNOTATION_FULL_NAME = "org.junit.Test"; //$NON-NLS-1$

    public static boolean isTest(IType type) throws JavaModelException {
		ITypeHierarchy typeHier= type.newSupertypeHierarchy(null);
		IType[] superInterfaces= typeHier.getAllInterfaces();
		for (int i= 0; i < superInterfaces.length; i++) {
			if (superInterfaces[i].getFullyQualifiedName(ENCLOSING_TYPE_SEPARATOR).equals(TEST_INTERFACE_NAME))
				return true;
		}
		IMethod[] methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			IMethod method = methods[i];
			IAnnotation[] annotations = method.getAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				IAnnotation annotation = annotations[j];
				if(annotation.getElementName().equals(TEST_ANNOTATION_NAME) || 
						annotation.getElementName().equals(TEST_ANNOTATION_FULL_NAME)){
					return true;
				}
			}
		}
		return type.getCompilationUnit().getImport(TEST_ANNOTATION_FULL_NAME).exists();
	}
}

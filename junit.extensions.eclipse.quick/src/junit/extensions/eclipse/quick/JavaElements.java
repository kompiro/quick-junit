package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;


public class JavaElements {
    public static boolean isTestMethod(IJavaElement element) throws JavaModelException {
        if (!(element instanceof IMethod))
            return false;
        IMethod method = (IMethod) element;
        if (method.getNumberOfParameters() != 0)
            return false;
        if (!method.getReturnType().equals("V"))
            return false;
        int flags = method.getFlags();
        if (!Flags.isPublic(flags) || Flags.isStatic(flags))
            return false;
        return method.getElementName().startsWith("test");
    }

    public static IType getPrimaryTypeOf(IJavaElement element) {
        if (element == null)
            return null;
        ICompilationUnit cu = null;
        if (element instanceof ICompilationUnit) {
            cu = (ICompilationUnit) element;
        } else if (element instanceof IMember) {
            cu = ((IMember) element).getCompilationUnit();
        }
        return cu != null ? cu.findPrimaryType() : null; 
    }
    
    public static boolean isTestClass(IJavaElement element) throws JavaModelException {
        if (!(element instanceof IType))
            return false;
        IType type = (IType) element;
        if (!type.isClass())
            return false;
        int flags = type.getFlags();
        if (Flags.isAbstract(flags) || !Flags.isPublic(flags))
            return false;
        ITypeHierarchy superTypeHierarchy = type.newSupertypeHierarchy(null);
        IType superTypes[] = superTypeHierarchy.getAllInterfaces();
        for (int i = 0; i < superTypes.length; ++i) {
            IType superType = superTypes[i];
            if (superType.getFullyQualifiedName().equals(JavaTypes.TEST_INTERFACE_NAME))
                return true;
        }
        return false;
    }

    public static IJavaElement getTestMethodOrClass(IJavaElement element) throws JavaModelException {
        while (element != null) {
            if (isTestMethod(element))
                return element;
            if (isTestClass(element))
                return element;
            element = element.getParent();
        }
        return null;
    }
}

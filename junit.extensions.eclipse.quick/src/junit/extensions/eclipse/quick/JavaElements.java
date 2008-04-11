package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
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
}

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
        if(method.getElementName().startsWith("test")) return true;
        
        return hasTestAnnotationOnMethod(method);
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
    
    public static boolean isTestClass(IType type) throws JavaModelException {
        ITypeHierarchy superTypeHierarchy = type.newSupertypeHierarchy(null);
        IType superTypes[] = superTypeHierarchy.getAllInterfaces();
        for (int i = 0; i < superTypes.length; ++i) {
            IType superType = superTypes[i];
            if (superType.getFullyQualifiedName().equals(JavaTypes.TEST_INTERFACE_NAME))
                return true;
        }
        return false;
    }

    public static IJavaElement getTestMethodOrClass(IJavaElement element)
            throws JavaModelException {
        while (element != null) {
            if (isTestMethod(element))
                return element;
            
            if (isTestRunnerPassibleClass(element)) {
                IType type = (IType) element;
                if (isTestClass(type)) return element;
                if (hasSuiteMethod(type)) return element;
                if (hasSuiteAnnotation(type)) return element;
                if (hasTestAnnotation(type)) return element;
            }
            element = element.getParent();
        }
        return null;
    }

    private static boolean hasSuiteAnnotation(IType type) throws JavaModelException {
        return type.getSource() == null ? false:type.getSource().indexOf("@SuiteClasses") > -1;
	}

	private static boolean isTestRunnerPassibleClass(IJavaElement element) throws JavaModelException {
        if (!(element instanceof IType))
            return false;
        IType type = (IType) element;
        if (!type.isClass())
            return false;
        int flags = type.getFlags();
        if (Flags.isAbstract(flags) || !Flags.isPublic(flags))
            return false;

        return true;
    }
    
    private static boolean hasSuiteMethod(IType type) throws JavaModelException {
        IMethod[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (isStaticSuiteMethod(methods[i])) return true;
        }
        return false;
    }
    
    private static boolean hasTestAnnotation(IType type) throws JavaModelException{
        IMethod[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (hasTestAnnotationOnMethod(methods[i])) return true;
        }
        return false;
    }
    
    private static boolean hasTestAnnotationOnMethod(IMethod method) throws JavaModelException{
        return method.getSource() == null ? false:method.getSource().indexOf("@Test") > -1;
    }

    private static boolean isStaticSuiteMethod(IMethod method) throws JavaModelException {
        return ((method.getElementName().equals("suite")) &&
                method.getSignature().equals("()QTest;") &&
                Flags.isPublic(method.getFlags()) &&
                Flags.isStatic(method.getFlags())
        );
    }
}

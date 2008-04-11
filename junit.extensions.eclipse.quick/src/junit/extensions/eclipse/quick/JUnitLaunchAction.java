package junit.extensions.eclipse.quick;

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;


public class JUnitLaunchAction extends QuickJUnitAction {
    private String mode;
    private ILaunchShortcut launchShortcut;
    
    public JUnitLaunchAction(ILaunchShortcut launchShortcut, String mode) {
        this.launchShortcut = launchShortcut;
        this.mode = mode;
    }

    private IJavaElement getElementOfJavaEditor() throws JavaModelException {
        if (javaEditor == null)
            return null;
        IJavaElement element = SelectionConverter.getElementAtOffset(javaEditor);
        if (JavaElements.isTestMethod(element))
            return element;
        return JavaElements.getPrimaryTypeOf(element);
    }

    private IJavaElement getTargetElement(IAction action) throws JavaModelException {
        IJavaElement element = getSelectedElement();
        if (element == null || element.getElementType() < IJavaElement.COMPILATION_UNIT)
            return element;
        
        IType type = JavaElements.getPrimaryTypeOf(element);
        if (type == null)
            return element;

        ITypeHierarchy superTypeHierarchy = type.newSupertypeHierarchy(null);
        IType superTypes[] = superTypeHierarchy.getAllInterfaces();
        for (int i = 0; i < superTypes.length; ++i) {
            IType superType = superTypes[i];
            if (superType.getFullyQualifiedName().equals(JUnitPlugin.TEST_INTERFACE_NAME))
                return element;
        }
        openInformation(action, Messages.getString("JUnitLaunchAction.notJUnitElement")); //$NON-NLS-1$
        return null;
    }

    private IJavaElement getSelectedElement() throws JavaModelException {
        IJavaElement element = getElementOfJavaEditor();
        return element == null ? javaElement : element;
    }

    public void run(IAction action) {
        try {
            IJavaElement element = getTargetElement(action);
            if (element == null)
                return;
            ISelection sel = new StructuredSelection(new Object[] { element });
            launchShortcut.launch(sel, mode);
        } catch (JavaModelException e) {
            QuickJUnitPlugin.getDefault().handleSystemError(e, this);
        }
    }
}

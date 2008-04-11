package junit.extensions.eclipse.quick;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class QuickJUnitAction implements IEditorActionDelegate, IObjectActionDelegate {
    private Shell shell;
    protected IJavaElement javaElement;
    protected JavaEditor javaEditor;
    
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if (!(targetEditor instanceof JavaEditor)) {
            javaEditor = null;
            return;
        }
        javaEditor = (JavaEditor) targetEditor;
        shell = javaEditor.getSite().getShell();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        shell = targetPart.getSite().getShell();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (! (selection instanceof IStructuredSelection)) {
            javaElement = null;
            return;
        }
        Object element = ((IStructuredSelection) selection).getFirstElement();
        if (element instanceof IJavaElement)
            javaElement = (IJavaElement) element;
        else
            javaElement = null;
    }

    protected IJavaProject[] getJavaProjects() throws JavaModelException {
        return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
    }

    protected void openInformation(IAction action, String message) {
        MessageDialog.openInformation(shell, action.getText(), message);
    }

    protected Shell getShell() {
        return shell;
    }
}

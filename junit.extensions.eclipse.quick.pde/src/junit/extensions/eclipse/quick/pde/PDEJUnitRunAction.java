package junit.extensions.eclipse.quick.pde;

import junit.extensions.eclipse.quick.internal.action.JUnitLaunchAction;

import org.eclipse.core.runtime.CoreException;

public class PDEJUnitRunAction extends JUnitLaunchAction {

    public PDEJUnitRunAction() throws CoreException {
        super(ExtensionSupport.createJUnitWorkbenchShortcut(), "run");
    }
}

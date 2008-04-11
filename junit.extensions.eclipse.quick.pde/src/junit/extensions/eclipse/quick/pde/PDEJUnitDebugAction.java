package junit.extensions.eclipse.quick.pde;

import junit.extensions.eclipse.quick.JUnitLaunchAction;

import org.eclipse.core.runtime.CoreException;

public class PDEJUnitDebugAction extends JUnitLaunchAction {
    
    public PDEJUnitDebugAction() throws CoreException {
        super(ExtensionSupport.createJUnitWorkbenchShortcut(), "debug");
    }
}

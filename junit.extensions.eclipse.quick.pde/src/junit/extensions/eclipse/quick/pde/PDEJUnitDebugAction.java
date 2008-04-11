package junit.extensions.eclipse.quick.pde;

import junit.extensions.eclipse.quick.JUnitLaunchAction;

import org.eclipse.pde.internal.ui.launcher.JUnitWorkbenchShortcut;

public class PDEJUnitDebugAction extends JUnitLaunchAction {
    
    public PDEJUnitDebugAction() {
        super(new JUnitWorkbenchShortcut(), "debug");
    }
}

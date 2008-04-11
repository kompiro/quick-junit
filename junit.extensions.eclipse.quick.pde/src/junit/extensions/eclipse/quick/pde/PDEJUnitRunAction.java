package junit.extensions.eclipse.quick.pde;

import junit.extensions.eclipse.quick.JUnitLaunchAction;

import org.eclipse.pde.internal.ui.launcher.JUnitWorkbenchShortcut;

public class PDEJUnitRunAction extends JUnitLaunchAction {

    public PDEJUnitRunAction() {
        super(new JUnitWorkbenchShortcut(), "run");
    }
}

package junit.extensions.eclipse.quick;

import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchShortcut;

public class JUnitDebugAction extends JUnitLaunchAction {
    
    public JUnitDebugAction() {
        super(new JUnitLaunchShortcut(), "debug");
    }
}

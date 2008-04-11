package junit.extensions.eclipse.quick;

import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchShortcut;

public class JUnitRunAction extends JUnitLaunchAction {
    
    public JUnitRunAction() {
        super(new JUnitLaunchShortcut(), "run");
    }
}

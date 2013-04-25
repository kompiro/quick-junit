package junit.extensions.eclipse.quick.internal.action;

import junit.extensions.eclipse.quick.internal.ExtensionSupport;

import org.eclipse.core.runtime.CoreException;

public class JUnitRunAction extends JUnitLaunchAction {
    
    public JUnitRunAction() throws CoreException {
        super(ExtensionSupport.createJUnitLaunchShortcut(), "run"); //$NON-NLS-1$
    }
}

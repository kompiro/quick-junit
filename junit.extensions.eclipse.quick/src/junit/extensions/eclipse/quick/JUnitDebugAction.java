package junit.extensions.eclipse.quick;

import org.eclipse.core.runtime.CoreException;

public class JUnitDebugAction extends JUnitLaunchAction {
    
	public JUnitDebugAction() throws CoreException {
		super(ExtensionSupport.createJUnitLaunchShortcut(), "debug"); //$NON-NLS-1$
    }
}

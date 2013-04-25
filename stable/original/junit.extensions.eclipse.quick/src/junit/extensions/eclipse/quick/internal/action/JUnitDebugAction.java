package junit.extensions.eclipse.quick.internal.action;

import junit.extensions.eclipse.quick.internal.ExtensionSupport;

import org.eclipse.core.runtime.CoreException;

public class JUnitDebugAction extends JUnitLaunchAction {
    
	public JUnitDebugAction() throws CoreException {
		super(ExtensionSupport.createJUnitLaunchShortcut(), "debug"); //$NON-NLS-1$
    }
}

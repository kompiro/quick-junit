package junit.extensions.eclipse.quick.pde;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchShortcut;

public class ExtensionSupport extends junit.extensions.eclipse.quick.ExtensionSupport {

    public static ILaunchShortcut createJUnitWorkbenchShortcut() throws CoreException {
        return createLaunchShortcut("org.eclipse.pde.ui"); //$NON-NLS-1$
    }
}

package junit.extensions.eclipse.quick;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class HelpSupport {

    public static void setHelp(final Shell control, final String contextId) {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getHelpSystem().setHelp(control, contextId);
    }
}

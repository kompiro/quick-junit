package junit.extensions.eclipse.quick;

import java.util.Dictionary;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;


public class QuickJUnitPlugin extends AbstractUIPlugin {
    private static QuickJUnitPlugin plugin;

    public QuickJUnitPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static QuickJUnitPlugin getDefault() {
        return plugin;
    }

    public IStatus createSystemErrorStatus(Exception ex, Object caller) {
        int severity = IStatus.ERROR;

        String message;
        message  = ex.getMessage();
        if (message == null)
            message = ""; //$NON-NLS-1$
        MultiStatus errorStatus = new MultiStatus(getID(), severity, message, ex);
        
        Dictionary headers = getBundle().getHeaders();

        String providerName = "" + headers.get(Constants.BUNDLE_VENDOR);
        message = Messages.getString("QuickJUnitPlugin.systemError.providerNameLabel") + providerName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginName = "" + headers.get(Constants.BUNDLE_NAME);
        message = Messages.getString("QuickJUnitPlugin.systemError.pluginNameLabel") + pluginName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginId = getBundle().getSymbolicName();
        message = Messages.getString("QuickJUnitPlugin.systemError.pluginIdLabel") + pluginId; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String version = "" + headers.get(Constants.BUNDLE_VERSION);
        message = Messages.getString("QuickJUnitPlugin.systemError.versionLabel") + version; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        Class klass = caller instanceof Class ? (Class) caller : caller.getClass();
        message = Messages.getString("QuickJUnitPlugin.systemError.classLabel") + klass.getName(); //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message, IStatus.ERROR, ex));

        return errorStatus;
    }

    public IStatus createStatus(int severity, String message) {
        return createStatus(severity, message, 0, null);
    }

    private IStatus createStatus(int severity, String message, int code, Exception ex) {
        return new Status(severity, getID(), code, message, ex);
    }
    
    public void handleSystemError(Exception ex, Object caller) {
        IStatus status = createSystemErrorStatus(ex, caller);
        getLog().log(status);
        ErrorDialog.openError((Shell) null, Messages.getString("QuickJUnitPlugin.systemError.dialog.title"), Messages.getString("QuickJUnitPlugin.systemError.dialog.message"), status); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void logSystemError(Exception ex, Object caller) {
        IStatus status = createSystemErrorStatus(ex, caller);
        getLog().log(status);
    }

    public void logSystemErrorMessage(String message, Object caller) {
        IStatus status = createSystemErrorStatus(new QuickJUnitException(message), caller);
        getLog().log(status);
    }

    public String getID() {
        return getBundle().getSymbolicName();
    }
}

package junit.extensions.eclipse.quick.javadoc.ui;

import java.util.Dictionary;

import junit.extensions.eclipse.quick.javadoc.ui.exception.QuickJUnitJavaDocUIExtensionException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class JavaDocUIActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "junit.extensions.eclipse.quick.javadoc.ui";

	private static JavaDocUIActivator plugin;
	
	public JavaDocUIActivator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static JavaDocUIActivator getDefault() {
		return plugin;
	}

    public IStatus createSystemErrorStatus(Exception ex, Object caller) {
        int severity = IStatus.ERROR;

        String message;
        message  = ex.getMessage();
        if (message == null)
            message = ""; //$NON-NLS-1$
        MultiStatus errorStatus = new MultiStatus(getID(), severity, message, ex);
        
        Dictionary<?, ?> headers = getBundle().getHeaders();

        String providerName = "" + headers.get(Constants.BUNDLE_VENDOR);
        message = "provider:" + providerName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginName = "" + headers.get(Constants.BUNDLE_NAME);
        message = "plugin name:" + pluginName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginId = getBundle().getSymbolicName();
        message = "plugin id:" + pluginId; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String version = "" + headers.get(Constants.BUNDLE_VERSION);
        message = "version:" + version; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        Class<?> klass = caller instanceof Class<?> ? (Class<?>) caller : caller.getClass();
        message = "class:" + klass.getName(); //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message, IStatus.ERROR, ex));

        return errorStatus;
    }

    public IStatus createStatus(int severity, String message) {
        return createStatus(severity, message, 0, null);
    }

    private IStatus createStatus(int severity, String message, int code, Exception ex) {
        return new Status(severity, getID(), code, message, ex);
    }
    
    public void handleSystemError(Exception e, Object caller) {
        IStatus status = createSystemErrorStatus(e, caller);
        getLog().log(status);
        ErrorDialog.openError((Shell) null, "QuickJUnit JavaDoc UI Extension", "", status); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void logSystemError(Exception e, Object caller) {
        IStatus status = createSystemErrorStatus(e, caller);
        getLog().log(status);
    }

    public void logSystemErrorMessage(String message, Object caller) {
        IStatus status = createSystemErrorStatus(new QuickJUnitJavaDocUIExtensionException(message), caller);
        getLog().log(status);
    }

	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public String getID() {
        return getBundle().getSymbolicName();
    }

}

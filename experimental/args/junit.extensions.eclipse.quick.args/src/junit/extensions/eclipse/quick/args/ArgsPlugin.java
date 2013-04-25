package junit.extensions.eclipse.quick.args;

import java.util.Dictionary;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class ArgsPlugin extends AbstractUIPlugin {
	
	private static ArgsPlugin plugin;

	public ArgsPlugin() {
		ArgsPlugin.plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
	}

    protected void initializeImageRegistry(ImageRegistry reg) {
    	reg.put("over_error", imageDescriptorFromPlugin(getID(), "icon/ovr16/error.gif"));
    }
	
	public static ArgsPlugin getPlugin() {
		return plugin;
	}
	
    public void logSystemError(Exception ex, Object caller) {
        IStatus status = createSystemErrorStatus(ex, caller);
        getLog().log(status);
    }

    public IStatus createSystemErrorStatus(Exception ex, Object caller) {
        int severity = IStatus.ERROR;

        String message;
        message  = ex.getMessage();
        if (message == null)
            message = ""; //$NON-NLS-1$
        MultiStatus errorStatus = new MultiStatus(getID(), severity, message, ex);
        
        @SuppressWarnings("rawtypes")
		Dictionary headers = getBundle().getHeaders();

        String providerName = "" + headers.get(Constants.BUNDLE_VENDOR);
        message = "Provider Name:.... " + providerName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginName = "" + headers.get(Constants.BUNDLE_NAME);
        message = "Plug-in Name:....... " + pluginName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginId = getBundle().getSymbolicName();
        message = "Plug-in ID:........... " + pluginId; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String version = "" + headers.get(Constants.BUNDLE_VERSION);
        message = "Version:.............. " + version; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        Class<?> klass = caller instanceof Class ? (Class<?>) caller : caller.getClass();
        message = "The error was detected in Class: " + klass.getName(); //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message, IStatus.ERROR, ex));

        return errorStatus;
    }
    
    public IStatus createStatus(int severity, String message) {
        return createStatus(severity, message, 0, null);
    }

    private IStatus createStatus(int severity, String message, int code, Exception ex) {
        return new Status(severity, getID(), code, message, ex);
    }

    public String getID() {
        return getBundle().getSymbolicName();
    }

}

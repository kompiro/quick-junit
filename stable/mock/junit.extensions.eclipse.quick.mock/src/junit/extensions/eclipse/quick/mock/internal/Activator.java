package junit.extensions.eclipse.quick.mock.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	
	private static Activator plugin;
	
    public Activator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

	
	public static Activator getDefault(){
		return Activator.plugin;
	}

}

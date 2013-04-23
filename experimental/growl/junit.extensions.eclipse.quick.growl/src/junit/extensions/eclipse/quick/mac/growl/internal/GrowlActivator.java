package junit.extensions.eclipse.quick.mac.growl.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GrowlActivator extends AbstractUIPlugin {
	
	private static GrowlActivator plugin;
	
	public GrowlActivator(){
		GrowlActivator.plugin = this;
	}
	
	public static GrowlActivator getDefault(){
		return GrowlActivator.plugin;
	}

}

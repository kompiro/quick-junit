package junit.extensions.eclipse.quick.process.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ProcessActivator extends AbstractUIPlugin {
	
	private static ProcessActivator plugin;
	
	public ProcessActivator(){
		ProcessActivator.plugin = this;
	}
	
	public static ProcessActivator getDefault(){
		return ProcessActivator.plugin;
	}

}

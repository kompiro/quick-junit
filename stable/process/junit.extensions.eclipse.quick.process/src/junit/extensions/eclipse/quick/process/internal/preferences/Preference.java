package junit.extensions.eclipse.quick.process.internal.preferences;

import junit.extensions.eclipse.quick.process.internal.ProcessActivator;

import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Constants for plug-in preferences
 */
public enum Preference {

	TEMPLATE, PROCESS;
	
	public String getValue() {
		IPreferenceStore store = ProcessActivator.getDefault().getPreferenceStore();
		return store.getString(name());
	}
	
	public void setValue(String value){
		IPreferenceStore store = ProcessActivator.getDefault().getPreferenceStore();
		store.setValue(name(), value);
	}
	
}

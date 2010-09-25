package junit.extensions.eclipse.quick.mac.growl.internal.preferences;

import junit.extensions.eclipse.quick.growl.internal.GrowlActivator;

import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Constants for plug-in preferences
 */
public enum Preference {

	TEMPLATE;
	
	public String getValue() {
		IPreferenceStore store = GrowlActivator.getDefault().getPreferenceStore();
		return store.getString(name());
	}
	
	public void setValue(String value){
		IPreferenceStore store = GrowlActivator.getDefault().getPreferenceStore();
		store.setValue(name(), value);
	}
	
}

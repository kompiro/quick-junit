package junit.extensions.eclipse.quick.process.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import org.eclipse.jface.preference.IPreferenceStore;

import junit.extensions.eclipse.quick.process.internal.ProcessActivator;

import static junit.extensions.eclipse.quick.process.internal.preferences.Preference.*;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = ProcessActivator.getDefault().getPreferenceStore();
		store.setDefault(PROCESS.name(), "/usr/local/bin/growlnotify -n \"Quick JUnit\" -m ${detail} ${summary}"); //$NON-NLS-1$
		store.setDefault(TEMPLATE.name(), "${name} passed:${ok_counts} failure:${fail_counts} Total:${total_counts}"); //$NON-NLS-1$
	}

}

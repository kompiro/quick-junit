package junit.extensions.eclipse.quick.notifications.internal.preference;

import static junit.extensions.eclipse.quick.notifications.internal.preference.Preference.TEMPLATE;
import junit.extensions.eclipse.quick.notifications.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(TEMPLATE.name(), "passed:${ok_counts} failure:${fail_counts} Total:${total_counts}"); //$NON-NLS-1$
	}

}

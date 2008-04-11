package junit.extensions.eclipse.quick;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author koichik
 */
public class FindPairRules {
    private static final String STORE_ID = "FindPairRules";

    private final IPreferenceStore store;

    public FindPairRules(final IPreferenceStore store) {
        this.store = store;
    }

    public boolean get() {
        return store.getBoolean(STORE_ID);
    }

    public void set(final boolean findPairRules) {
        store.setValue(STORE_ID, findPairRules);
    }

    public boolean getDefault() {
        return true;
    }
}

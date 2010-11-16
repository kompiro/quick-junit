package junit.extensions.eclipse.quick.mock.internal.preference;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.extensions.eclipse.quick.mock.internal.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Initializes the default preferences if none exist.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer implements IStartup, IPropertyChangeListener {

	private static final String		EMPTY										= "";											//$NON-NLS-1$
	private static final String		SEMI_COLON									= ";";											//$NON-NLS-1$

	private final IPreferenceStore	quickjunitPreferenceStore;
	private final IPreferenceStore	jdtPreferenceStore;
	static final String				ENABLE_ADDITIONAL_AUTOCOMPLETE_FAVOURTES	= "ENABLE_ADDITIONAL_AUTOCOMPLETE_FAVOURTES";	//$NON-NLS-1$

	/**
	 * Creates a default preference initializer.
	 */
	public PreferenceInitializer() {
		this(Activator.getDefault().getPreferenceStore(), PreferenceConstants.getPreferenceStore());
	}

	/**
	 * Create a preference initializer with the two preference stores.
	 * 
	 * @param quickjunitPreferenceStore used by quick junit.
	 * @param jdtPreferenceStore used by JDT.
	 */
	public PreferenceInitializer(IPreferenceStore quickjunitPreferenceStore, IPreferenceStore jdtPreferenceStore) {
		this.quickjunitPreferenceStore = quickjunitPreferenceStore;
		this.jdtPreferenceStore = jdtPreferenceStore;
		quickjunitPreferenceStore.addPropertyChangeListener(this);
	}

	public void initializeDefaultPreferences() {
		quickjunitPreferenceStore.setDefault(PreferenceInitializer.ENABLE_ADDITIONAL_AUTOCOMPLETE_FAVOURTES, true);
	}

	public void earlyStartup() {
		new PreferenceInitializer().initializeFavorites();
	}

	void initializeFavorites() {
		Set<String> imports;
		if (quickjunitPreferenceStore.getBoolean(PreferenceInitializer.ENABLE_ADDITIONAL_AUTOCOMPLETE_FAVOURTES)) {
			imports = new LinkedHashSet<String>(getJDTImports());
			imports.addAll(getDefaultFavorites());
		} else {
			imports = getJDTImports();
			if (imports.containsAll(getDefaultFavorites()))
				imports.removeAll(getDefaultFavorites());
		}
		String join = join(imports, SEMI_COLON);
		jdtPreferenceStore.setValue(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS, join);
		try {
			((ScopedPreferenceStore) jdtPreferenceStore).save();
		} catch (IOException e) {
		}
	}

	/**
	 * @return the JDT favourite imports.
	 */
	public Set<String> getJDTImports() {
		String preference = jdtPreferenceStore.getString(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS);
		if (EMPTY.equals(preference.trim())) {
			return new HashSet<String>();
		}
		String[] imports = preference.split(SEMI_COLON);
		return new LinkedHashSet<String>(Arrays.asList(imports));
	}

	private LinkedHashSet<String> getDefaultFavorites() {
		LinkedHashSet<String> orderedSet = new LinkedHashSet<String>();
		orderedSet.add(importStatement("org.mockito.Mockito"));
		orderedSet.add(importStatement("org.hamcrest.Matchers"));
		orderedSet.add(importStatement("org.hamcrest.MatcherAssert"));
		orderedSet.add(importStatement("org.junit.Assert"));
		return orderedSet;
	}

	public void propertyChange(PropertyChangeEvent event) {
		initializeFavorites();
	}

	private String importStatement(String clazz) {
		return clazz + ".*"; //$NON-NLS-1$;
	}

	private String join(Collection<?> toJoin, String delimiter) {
		if ((toJoin == null) || (toJoin.size() == 0))
			return ""; 
		StringBuffer result = new StringBuffer();

		for (Object object : toJoin) {
			result.append(object);
			result.append(delimiter);
		}

		result.lastIndexOf(delimiter);
		result.replace(result.length() - delimiter.length(), result.length(), ""); //$NON-NLS-1$
		return result.toString();
	}

}
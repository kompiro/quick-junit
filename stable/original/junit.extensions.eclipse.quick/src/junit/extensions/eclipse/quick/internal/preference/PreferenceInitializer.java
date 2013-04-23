package junit.extensions.eclipse.quick.internal.preference;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Initializes the default preferences
 * 
 */
public class PreferenceInitializer implements IStartup{

	private static final String		EMPTY										= "";											//$NON-NLS-1$
	private static final String		SEMI_COLON									= ";";											//$NON-NLS-1$

	private final IPreferenceStore	jdtPreferenceStore;

	/**
	 * Creates a default preference initializer.
	 */
	public PreferenceInitializer() {
		this(PreferenceConstants.getPreferenceStore());
	}

	/**
	 * Create a preference initializer with the two preference stores.
	 * 
	 * @param jdtPreferenceStore used by JDT.
	 */
	public PreferenceInitializer(IPreferenceStore jdtPreferenceStore) {
		this.jdtPreferenceStore = jdtPreferenceStore;
	}

	void initializeFavorites() {
		Set imports = new LinkedHashSet(getJDTImports());
		imports.addAll(getDefaultFavorites());
		String join = join(imports, SEMI_COLON);
		jdtPreferenceStore.setValue(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS, join);
		try {
			((ScopedPreferenceStore) jdtPreferenceStore).save();
		} catch (IOException e) {
		}
	}

	/**
	 * @return the JDT favorite imports.
	 */
	public Set getJDTImports() {
		String preference = jdtPreferenceStore.getString(PreferenceConstants.CODEASSIST_FAVORITE_STATIC_MEMBERS);
		if (EMPTY.equals(preference.trim())) {
			return new HashSet();
		}
		String[] imports = preference.split(SEMI_COLON);
		return new LinkedHashSet(Arrays.asList(imports));
	}

	private LinkedHashSet getDefaultFavorites() {
		LinkedHashSet orderedSet = new LinkedHashSet();
		orderedSet.add(importStatement("org.hamcrest.MatcherAssert"));
		orderedSet.add(importStatement("org.hamcrest.CoreMatchers"));
		orderedSet.add(importStatement("org.junit.matchers.JUnitMatchers"));
		orderedSet.add(importStatement("org.junit.Assert"));
		return orderedSet;
	}

	public void propertyChange(PropertyChangeEvent event) {
		initializeFavorites();
	}

	private String importStatement(String clazz) {
		return clazz + ".*"; //$NON-NLS-1$;
	}

	private String join(Collection toJoin, String delimiter) {
		if ((toJoin == null) || (toJoin.size() == 0))
			return ""; 
		StringBuffer result = new StringBuffer();
		Iterator iterator = toJoin.iterator();
		while(iterator.hasNext()){
			Object object = iterator.next();
			result.append(object);
			result.append(delimiter);
		}

		result.lastIndexOf(delimiter);
		result.replace(result.length() - delimiter.length(), result.length(), ""); //$NON-NLS-1$
		return result.toString();
	}
	
	public void earlyStartup() {
		new PreferenceInitializer().initializeFavorites();
	}

}
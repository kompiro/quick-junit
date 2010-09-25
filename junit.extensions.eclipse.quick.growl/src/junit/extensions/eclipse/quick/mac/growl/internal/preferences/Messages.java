package junit.extensions.eclipse.quick.mac.growl.internal.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.mac.growl.internal.preferences.messages"; //$NON-NLS-1$
	public static String GrowlPreferencePage_description;
	public static String GrowlPreferencePage_template_group;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

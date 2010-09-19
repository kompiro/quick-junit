package junit.extensions.eclipse.quick.process.internal.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.process.internal.preferences.messages"; //$NON-NLS-1$
	public static String ProcessPreferencePage_description;
	public static String ProcessPreferencePage_template_group;
	public static String ProcessPreferencePage_process_group;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

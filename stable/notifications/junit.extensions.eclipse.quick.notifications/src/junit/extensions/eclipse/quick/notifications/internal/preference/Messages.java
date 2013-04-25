package junit.extensions.eclipse.quick.notifications.internal.preference;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.notifications.internal.preference.messages"; //$NON-NLS-1$
	public static String QuickJUnitNotificationPreferencePage_description;
	public static String QuickJUnitNotificationPreferencePage_template_group;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

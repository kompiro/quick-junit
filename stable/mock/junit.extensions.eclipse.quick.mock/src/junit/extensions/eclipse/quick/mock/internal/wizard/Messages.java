package junit.extensions.eclipse.quick.mock.internal.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.mock.internal.wizard.messages"; //$NON-NLS-1$
	public static String MockitoPage_AddedLabel;
	public static String MockitoPage_AddMockitoLibrary;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

package junit.extensions.eclipse.quick.mock.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.mock.internal.messages"; //$NON-NLS-1$
	public static String MockitoClasspathFixProcessor_AdditionalProposalInfo;
	public static String MockitoClasspathFixProcessor_beginAddMockitoLibraryTask;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

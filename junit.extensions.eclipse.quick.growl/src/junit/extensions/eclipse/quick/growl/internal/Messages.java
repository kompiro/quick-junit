package junit.extensions.eclipse.quick.growl.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.mac.growl.internal.messages"; //$NON-NLS-1$
	public static String TemplateKey_ERROR_COUNT_DESCRIPTION;
	public static String TemplateKey_FAILURE_COUNT_DESCRIPTION;
	public static String TemplateKey_IGNORE_COUNT_DESCRIPTION;
	public static String TemplateKey_NAME_DESCRIPTION;
	public static String TemplateKey_OK_COUNT_DESCRIPTION;
	public static String TemplateKey_RESULT_DESCRIPTION;
	public static String TemplateKey_TOTAL_COUNT_DESCRIPTION;
	public static String TestRunListener_ERROR_MESSAGE;
	public static String TestRunListener_TWEET_JOB_MESSAGE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

package junit.extensions.eclipse.quick.notifications.internal;


import java.util.Collections;

import junit.extensions.eclipse.quick.notifications.Activator;
import junit.extensions.eclipse.quick.notifications.ImageDesc;
import junit.extensions.eclipse.quick.notifications.internal.preference.Preference;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.mylyn.commons.ui.notifications.AbstractNotification;
import org.eclipse.mylyn.commons.ui.notifications.Notifications;
import org.eclipse.swt.graphics.Image;


public class TestNotificationListener extends TestRunListener {
	private static final String QUICK_JUNIT_NOTIFICATION_EVENT_ID = "junit.extensions.eclipse.quick.notifications.event";
	private TemplateParser parser = new TemplateParser();

	private final class QuickJUnitNotification extends
			AbstractNotification {
		private final ITestRunSession session;
		private String title;
		private Image kind;

		private QuickJUnitNotification(String eventId,
				Result testResult, ITestRunSession session) {
			super(eventId);
			this.session = session;
			if(Result.ERROR.equals(testResult)){
				title = TEST_ERROR;
				kind = getImageRegistry().get(ImageDesc.ERROR.name());
			}else if(Result.FAILURE.equals(testResult)){
				title = TEST_FAILURE;
				kind = getImageRegistry().get(ImageDesc.FAILURE.name());
			}else{
				title = TEST_OK;
				kind = getImageRegistry().get(ImageDesc.OK.name());
			}
		}


		public Object getAdapter(Class adapter) {
			return null;
		}

		public int compareTo(AbstractNotification o) {
			return 0;
		}

		@Override
		public void open() {
		}

		@Override
		public Image getNotificationKindImage() {
			return kind;
		}

		@Override
		public Image getNotificationImage() {
			return getImageRegistry().get(ImageDesc.ICON.name());
		}


		private ImageRegistry getImageRegistry() {
			return Activator.getDefault().getImageRegistry();
		}

		@Override
		public String getLabel() {
			return title;
		}

		@Override
		public String getDescription() {
			String parseTemplate = parser.parseTemplate(session);
			return parseTemplate;
		}
	}

	private static final String QUICK_J_UNIT = "Quick JUnit ";
	private static final String TEST_OK = QUICK_J_UNIT + "Test OK";
	private static final String TEST_FAILURE = QUICK_J_UNIT + "Test FAILURE";
	private static final String TEST_ERROR = QUICK_J_UNIT + "Test ERROR";

	public TestNotificationListener() {

		JUnitCore.addTestRunListener(new org.eclipse.jdt.junit.TestRunListener() {
			@Override
			public void sessionFinished(final ITestRunSession session) {
				String template = Preference.TEMPLATE.getValue();
				parser.setTemplate(template);
				final Result testResult = session.getTestResult(true);
				AbstractNotification notification = new QuickJUnitNotification(QUICK_JUNIT_NOTIFICATION_EVENT_ID, testResult, session);
				Notifications.getService().notify(Collections.singletonList(notification));
			}
		});
	}

	public void testEnded(String testId, String testName) {
	}

	public void testFailed(int status, String testId, String testName,
			String trace) {
	}

	public void testReran(String testId, String testClass, String testName,
			int status, String trace) {
	}

	public void testRunEnded(long elapsedTime) {
	}

	public void testRunStarted(int testCount) {
	}

	public void testRunStopped(long elapsedTime) {
	}

	public void testRunTerminated() {
	}

	public void testStarted(String testId, String testName) {
	}

}

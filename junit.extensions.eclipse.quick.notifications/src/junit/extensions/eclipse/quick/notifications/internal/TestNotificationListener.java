package junit.extensions.eclipse.quick.notifications.internal;


import java.util.Collections;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.mylyn.commons.ui.notifications.AbstractNotification;
import org.eclipse.mylyn.commons.ui.notifications.Notifications;


@SuppressWarnings("restriction")
public class TestNotificationListener extends TestRunListener {
	private static final String QUICK_JUNIT_NOTIFICATION_EVENT_ID = "junit.extensions.eclipse.quick.notifications.event";

	public TestNotificationListener() {

		JUnitCore.addTestRunListener(new org.eclipse.jdt.junit.TestRunListener() {
			@Override
			public void sessionFinished(ITestRunSession session) {
				Result testResult = session.getTestResult(true);
				AbstractNotification notification = new JUnitNotification(QUICK_JUNIT_NOTIFICATION_EVENT_ID, testResult, session);
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

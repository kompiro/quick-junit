package junit.extensions.eclipse.quick.mac.growl.internal;


import info.growl.Growl;
import info.growl.GrowlException;
import info.growl.GrowlUtils;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import junit.extensions.eclipse.quick.mac.growl.internal.preferences.Preference;

import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;


@SuppressWarnings("deprecation")
public class TestListenerForGrowl implements ITestRunListener {
	
	private static final String QUICK_J_UNIT = "Quick JUnit ";
	private static final String TEST_OK = QUICK_J_UNIT + "Test OK";
	private static final String TEST_FAILURE = QUICK_J_UNIT + "Test FAILURE";
	private static final String TEST_ERROR = QUICK_J_UNIT + "Test ERROR";

	public TestListenerForGrowl() {
		final Growl growl = GrowlUtils.getGrowlInstance(QUICK_J_UNIT);
		growl.addNotification(TEST_OK, true);
		growl.addNotification(TEST_FAILURE, true);
		growl.addNotification(TEST_ERROR, true);
		try {
			growl.register();
		} catch (GrowlException e) {
		}

		JUnitCore.addTestRunListener(new org.eclipse.jdt.junit.TestRunListener() {
			private TemplateParser parser = new TemplateParser();
			@Override
			public void sessionFinished(ITestRunSession session) {
				String template = Preference.TEMPLATE.getValue();
				parser.setTemplate(template);
				RenderedImage icon;
				Result testResult = session.getTestResult(true);
				InputStream input;
				String notification;
				if(Result.ERROR.equals(testResult)){
					notification = TEST_ERROR;
					input = getClass().getResourceAsStream("/icons/tsuiteerror.gif");
				}else if(Result.FAILURE.equals(testResult)){
					notification = TEST_FAILURE;
					input = getClass().getResourceAsStream("/icons/tsuitefail.gif");
				}else{
					notification = TEST_OK;
					input = getClass().getResourceAsStream("/icons/tsuiteok.gif");					
				}
				String parseTemplate = parser.parseTemplate(session);
				try {
					icon = ImageIO.read(input);
					growl.sendNotification(notification, testResult.toString(), parseTemplate, icon);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (GrowlException e) {
					e.printStackTrace();
				}
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

package junit.extensions.eclipse.quick.process.internal;



import java.io.IOException;

import junit.extensions.eclipse.quick.process.internal.preferences.Preference;

import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;


@SuppressWarnings("deprecation")
public class TestListenerForProcess implements ITestRunListener {
	
	private static final String QUICK_J_UNIT = "Quick JUnit ";
	private static final String TEST_OK = QUICK_J_UNIT + "Test OK";
	private static final String TEST_FAILURE = QUICK_J_UNIT + "Test FAILURE";
	private static final String TEST_ERROR = QUICK_J_UNIT + "Test ERROR";

	public TestListenerForProcess() {

		JUnitCore.addTestRunListener(new org.eclipse.jdt.junit.TestRunListener() {
			private TemplateParser tmpParser = new TemplateParser();
			private ProcessParser processParser = new ProcessParser();
			@Override
			public void sessionFinished(ITestRunSession session) {
				String template = Preference.TEMPLATE.getValue();
				tmpParser.setTemplate(template);
				Result testResult = session.getTestResult(true);
				String summary;
				if(Result.ERROR.equals(testResult)){
					summary = TEST_ERROR;
				}else if(Result.FAILURE.equals(testResult)){
					summary = TEST_FAILURE;
				}else{
					summary = TEST_OK;
				}
				String detail = tmpParser.parseTemplate(session);
				String command = Preference.PROCESS.getValue();
				String[] parsed = processParser.parse(command,summary,detail);
				ProcessBuilder builder = new ProcessBuilder(parsed);
				try {
					builder.start();
				} catch (IOException e) {
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

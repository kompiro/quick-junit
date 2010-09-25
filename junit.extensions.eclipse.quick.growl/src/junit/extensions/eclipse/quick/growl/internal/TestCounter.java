package junit.extensions.eclipse.quick.growl.internal;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class TestCounter {

	private int totalTests;
	private int okTests;
	private int failureTests;
	private int ignoreTests;
	private int errorTests;

	public TestCounter() {
	}
	
	public void count(ITestRunSession session){
		reset();
		count((ITestElementContainer)session);
	}
	
	private void reset() {
		totalTests = 0;
		okTests = 0;
		failureTests = 0;
		ignoreTests = 0;
		errorTests = 0;
	}

	private void count(ITestElementContainer container) {
		ITestElement[] children = container.getChildren();
		if(children == null) return;
		for(ITestElement element : children){
			if (element instanceof ITestElementContainer) {
				ITestElementContainer cont = (ITestElementContainer) element;
				count(cont);
				continue;
			}
			totalTests++;
			Result result = element.getTestResult(false);
			if(result == null) continue;
			if(result.equals(Result.IGNORED)) ignoreTests++;
			if(result.equals(Result.OK)) okTests++;
			if(result.equals(Result.FAILURE)) failureTests++;
			if(result.equals(Result.ERROR)) errorTests++;
		}
	}

	public int getTotalTests(){
		return totalTests;
	}
	
	public int getOKTests(){
		return okTests;
	}

	public int getFailureTests() {
		return failureTests;
	}

	public int getIgnoreTests() {
		return ignoreTests;
	}

	public int getErrorTests() {
		return errorTests;
	}
	
}

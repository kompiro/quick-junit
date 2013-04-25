package junit.extensions.eclipse.quick.notifications.internal;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

	public void earlyStartup() {
		JUnitCore.addTestRunListener(new TestNotificationListener());
	}

}

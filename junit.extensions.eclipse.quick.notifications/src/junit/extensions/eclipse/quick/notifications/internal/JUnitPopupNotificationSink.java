package junit.extensions.eclipse.quick.notifications.internal;

import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.mylyn.commons.ui.notifications.AbstractNotification;
import org.eclipse.mylyn.commons.ui.notifications.NotificationSink;
import org.eclipse.mylyn.commons.ui.notifications.NotificationSinkEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class JUnitPopupNotificationSink extends NotificationSink {

	private static final boolean runSystem = true;

	private final WeakHashMap<Object, Object> cancelledTokens = new WeakHashMap<Object, Object>();

	private JUnitNotification currentlyNotifying;

	private final Job openJob = new Job("JUnit Result popup notifier") {
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				if (Platform.isRunning() && PlatformUI.getWorkbench() != null
						&& PlatformUI.getWorkbench().getDisplay() != null
						&& !PlatformUI.getWorkbench().getDisplay().isDisposed()) {
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

						public void run() {
							collectNotifications();

							if (popup != null && popup.getReturnCode() == Window.CANCEL) {
								AbstractNotification notification = popup.getNotification();
								if (notification.getToken() != null) {
									cancelledTokens.put(notification.getToken(), null);
								}
							}

							synchronized (JUnitPopupNotificationSink.class) {
								if (currentlyNotifying != null) {
//										popup.close();
									showPopup();
								}
							}
						}
					});
				}
			} finally {
				if (popup != null) {
					schedule(popup.getDelayClose() / 2);
				}
			}

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			return Status.OK_STATUS;
		}

	};

	private JUnitNotificationPopup popup;

	public JUnitPopupNotificationSink() {
	}

	private void cleanNotified() {
		currentlyNotifying = null;
	}

	/** public for testing */
	public void collectNotifications() {
	}

	@Override
	public void notify(NotificationSinkEvent event) {
		List<AbstractNotification> notifications = event.getNotifications();
		if(notifications.isEmpty()) return;
		AbstractNotification notification = notifications.get(0);
		if ((notification instanceof JUnitNotification) == false) {
			return;
		}
		currentlyNotifying = (JUnitNotification) notification;
		
		if (!openJob.cancel()) {
			try {
				openJob.join();
			} catch (InterruptedException e) {
				// ignore
			}
		}
		openJob.setSystem(runSystem);
		openJob.schedule();
	}

	public void showPopup() {
		if (popup != null) {
			popup.close();
		}

		Display display = PlatformUI.getWorkbench().getDisplay();
		Shell shell = new Shell(display);

		popup = new JUnitNotificationPopup(shell,currentlyNotifying);
		cleanNotified();
		popup.setBlockOnOpen(false);
		popup.open();
	}

}

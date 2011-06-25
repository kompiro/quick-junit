package junit.extensions.eclipse.quick.notifications.internal;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.commons.ui.notifications.AbstractNotification;
import org.eclipse.mylyn.internal.commons.ui.notifications.popup.NotificationPopup;
import org.eclipse.mylyn.internal.provisional.commons.ui.CommonColors;
import org.eclipse.mylyn.internal.provisional.commons.ui.CommonUiUtil;
import org.eclipse.mylyn.internal.provisional.commons.ui.ScalingHyperlink;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;

@SuppressWarnings("restriction")
public class JUnitNotificationPopup	extends	NotificationPopup {
	
	private Shell shell;
	private List<AbstractNotification> notifications;
	private static final int NUM_NOTIFICATIONS_TO_DISPLAY = 4;

	public JUnitNotificationPopup(Shell parent) {
		super(parent);
		setDelayClose(3 * 1000);
		setFadingEnabled(false);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		shell = newShell;
	}
	
	@Override
	protected void createContentArea(Composite parent) {
		int count = 0;
		for (final AbstractNotification notification : notifications) {
			Composite notificationComposite = new Composite(parent, SWT.NO_FOCUS);
			GridLayout gridLayout = new GridLayout(2, false);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(notificationComposite);
			notificationComposite.setLayout(gridLayout);
			notificationComposite.setBackground(parent.getBackground());

			if (count < NUM_NOTIFICATIONS_TO_DISPLAY) {
				final Label notificationLabelIcon = new Label(notificationComposite, SWT.NO_FOCUS);
				notificationLabelIcon.setBackground(parent.getBackground());
				notificationLabelIcon.setImage(notification.getNotificationKindImage());
				// FIXME
//				if (!(notification instanceof TaskListNotificationReminder)) {
//					final AbstractTask task = (AbstractTask) notification.getAdapter(AbstractTask.class);
//					if (task != null) {
//						notificationLabelIcon.addMouseListener(new MouseAdapter() {
//							@Override
//							public void mouseUp(MouseEvent e) {
//								TasksUiPlugin.getTaskDataManager().setTaskRead(task, true);
//								notificationLabelIcon.setImage(null);
//								notificationLabelIcon.setToolTipText(null);
//							}
//						});
//						notificationLabelIcon.setToolTipText(Messages.TaskListNotificationPopup_Mark_Task_Read);
//					}
//				}

				// FIXME
//				final TaskScalingHyperlink itemLink = new TaskScalingHyperlink(notificationComposite, SWT.BEGINNING
//						| SWT.NO_FOCUS);

				final ScalingHyperlink itemLink = new ScalingHyperlink(notificationComposite, SWT.BEGINNING
						| SWT.NO_FOCUS);
				GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(itemLink);
				itemLink.setForeground(CommonColors.HYPERLINK_WIDGET);
				itemLink.registerMouseTrackListener();
				itemLink.setText(CommonUiUtil.toLabel(notification.getLabel()));
				itemLink.setImage(notification.getNotificationImage());
				itemLink.setBackground(parent.getBackground());
				itemLink.addHyperlinkListener(new HyperlinkAdapter() {
					public void linkActivated(HyperlinkEvent e) {
						notification.open();
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						if (window != null) {
							Shell windowShell = window.getShell();
							if (windowShell != null) {
								if (windowShell.getMinimized()) {
									windowShell.setMinimized(false);
								}

								windowShell.open();
								windowShell.forceActive();
							}
						}
					}
				});

				String descriptionText = null;
				if (notification.getDescription() != null) {
					descriptionText = notification.getDescription();
				}
				if (descriptionText != null && !descriptionText.trim().equals("")) { //$NON-NLS-1$
					Label descriptionLabel = new Label(notificationComposite, SWT.NO_FOCUS|SWT.WRAP);
					descriptionLabel.setText(CommonUiUtil.toLabel(descriptionText));
					descriptionLabel.setBackground(parent.getBackground());
					GridDataFactory.fillDefaults()
							.span(2, SWT.DEFAULT)
							.grab(true, false)
							.align(SWT.FILL, SWT.TOP)
							.applyTo(descriptionLabel);
				}
			} else {
				int numNotificationsRemain = notifications.size() - count;
				ScalingHyperlink remainingLink = new ScalingHyperlink(notificationComposite, SWT.NO_FOCUS);
				remainingLink.setForeground(CommonColors.HYPERLINK_WIDGET);
				remainingLink.registerMouseTrackListener();
				remainingLink.setBackground(parent.getBackground());

				remainingLink.setText(NLS.bind("{0} more", numNotificationsRemain)); //$NON-NLS-1$
				GridDataFactory.fillDefaults().span(2, SWT.DEFAULT).applyTo(remainingLink);
				remainingLink.addHyperlinkListener(new HyperlinkAdapter() {
					public void linkActivated(HyperlinkEvent e) {
						// FIXME
						//						TasksUiUtil.openTasksViewInActivePerspective().setFocus();
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						if (window != null) {
							Shell windowShell = window.getShell();
							if (windowShell != null) {
								windowShell.setMaximized(true);
								windowShell.open();
							}
						}
					}
				});
				break;
			}
			count++;
		}
	}

	
	@Override
	public int open() {
		int open = super.open();
		Point location = shell.getLocation();
		shell.setLocation(location.x, 32);
		return open;
	}
	
	public void setContents(List<AbstractNotification> notifications) {
		this.notifications = notifications;
	}

	public List<AbstractNotification> getNotifications() {
		return new ArrayList<AbstractNotification>(notifications);
	}
}

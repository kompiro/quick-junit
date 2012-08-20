package junit.extensions.eclipse.quick.notifications.internal;

import junit.extensions.eclipse.quick.notifications.ImageDesc;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.commons.ui.CommonUiUtil;
import org.eclipse.mylyn.commons.ui.compatibility.CommonColors;
import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

@SuppressWarnings("restriction")
public class JUnitNotificationPopup	extends	AbstractNotificationPopup {
	
	private Shell shell;
	private Image lastUsedBgImage;
	private JUnitNotification notification;
	private JUnitNotificationPopupColors color;

	public JUnitNotificationPopup(Shell parent, JUnitNotification notification) {
		super(parent.getDisplay());
		setDelayClose(3 * 1000);
		setFadingEnabled(false);
		color = new JUnitNotificationPopupColors(parent.getDisplay(), resources, notification);
		this.notification = notification;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		shell = newShell;
	}
	
	@Override
	protected String getPopupShellTitle() {
		return notification.getResultLabel();
	}
	
	@Override
	protected Image getPopupShellImage(int maximumHeight) {
		return ImageDesc.ICON.getImage();
	}
	
	@Override
	protected void createContentArea(Composite parent) {
		Composite notificationComposite = new Composite(parent, SWT.NO_FOCUS);
		GridLayout gridLayout = new GridLayout(2, false);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(notificationComposite);
		notificationComposite.setLayout(gridLayout);
		notificationComposite.setBackground(parent.getBackground());

		final Label notificationLabelIcon = new Label(notificationComposite, SWT.NO_FOCUS);
		notificationLabelIcon.setBackground(parent.getBackground());
		notificationLabelIcon.setImage(notification.getNotificationKindImage());

		final ImageHyperlink itemLink = new ImageHyperlink(notificationComposite, SWT.BEGINNING
				| SWT.NO_FOCUS|SWT.WRAP);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(itemLink);
		itemLink.setForeground(CommonColors.HYPERLINK_WIDGET);
//		itemLink.registerMouseTrackListener();
		itemLink.setText(CommonUiUtil.toLabel(notification.getLabel()));
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
					.grab(true, true)
					.align(SWT.FILL, SWT.TOP)
					.applyTo(descriptionLabel);
		}
	}

	
	@Override
	public int open() {
		int open = super.open();
		Point location = shell.getLocation();
		shell.setLocation(location.x, 32);
		return open;
	}
	

	public JUnitNotification getNotification() {
		return notification;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		((GridLayout) parent.getLayout()).marginWidth = 1;
		((GridLayout) parent.getLayout()).marginHeight = 1;

		/* Outer Composite holding the controls */
		final Composite outerCircle = new Composite(parent, SWT.NO_FOCUS);
		outerCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		outerCircle.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				Rectangle clArea = outerCircle.getClientArea();
				lastUsedBgImage = new Image(outerCircle.getDisplay(), clArea.width, clArea.height);
				GC gc = new GC(lastUsedBgImage);

				/* Gradient */
				drawGradient(gc, clArea);

				/* Fix Region Shape */
				fixRegion(gc, clArea);

				gc.dispose();

				Image oldBGImage = outerCircle.getBackgroundImage();
				outerCircle.setBackgroundImage(lastUsedBgImage);

				if (oldBGImage != null) {
					oldBGImage.dispose();
				}
			}

			private void drawGradient(GC gc, Rectangle clArea) {
				gc.setForeground(color.getGradientBegin());
				gc.setBackground(color.getGradientEnd());
				gc.fillGradientRectangle(clArea.x, clArea.y, clArea.width, clArea.height, true);
			}

			private void fixRegion(GC gc, Rectangle clArea) {
				gc.setForeground(color.getBorder());

				/* Fill Top Left */
				gc.drawPoint(2, 0);
				gc.drawPoint(3, 0);
				gc.drawPoint(1, 1);
				gc.drawPoint(0, 2);
				gc.drawPoint(0, 3);

				/* Fill Top Right */
				gc.drawPoint(clArea.width - 4, 0);
				gc.drawPoint(clArea.width - 3, 0);
				gc.drawPoint(clArea.width - 2, 1);
				gc.drawPoint(clArea.width - 1, 2);
				gc.drawPoint(clArea.width - 1, 3);

				/* Fill Bottom Left */
				gc.drawPoint(2, clArea.height - 0);
				gc.drawPoint(3, clArea.height - 0);
				gc.drawPoint(1, clArea.height - 1);
				gc.drawPoint(0, clArea.height - 2);
				gc.drawPoint(0, clArea.height - 3);

				/* Fill Bottom Right */
				gc.drawPoint(clArea.width - 4, clArea.height - 0);
				gc.drawPoint(clArea.width - 3, clArea.height - 0);
				gc.drawPoint(clArea.width - 2, clArea.height - 1);
				gc.drawPoint(clArea.width - 1, clArea.height - 2);
				gc.drawPoint(clArea.width - 1, clArea.height - 3);
			}
		});

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;

		outerCircle.setLayout(layout);

		/* Title area containing label and close button */
		final Composite titleCircle = new Composite(outerCircle, SWT.NO_FOCUS);
		titleCircle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		titleCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(4, false);
		layout.marginWidth = 3;
		layout.marginHeight = 0;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 3;

		titleCircle.setLayout(layout);

		/* Create Title Area */
		createTitleArea(titleCircle);

		/* Outer composite to hold content controlls */
		Composite outerContentCircle = new Composite(outerCircle, SWT.NONE);
		outerContentCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		outerContentCircle.setLayout(layout);
		outerContentCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerContentCircle.setBackground(outerCircle.getBackground());

		/* Middle composite to show a 1px black line around the content controls */
		Composite middleContentCircle = new Composite(outerContentCircle, SWT.NO_FOCUS);
		middleContentCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginTop = 1;

		middleContentCircle.setLayout(layout);
		middleContentCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		middleContentCircle.setBackground(color.getBorder());

		/* Inner composite containing the content controls */
		Composite innerContent = new Composite(middleContentCircle, SWT.NO_FOCUS);
		innerContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		innerContent.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 5;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		innerContent.setLayout(layout);

		innerContent.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		/* Content Area */
		createContentArea(innerContent);

		setNullBackground(outerCircle);

		return outerCircle;
	}
	
	private void setNullBackground(final Composite outerCircle) {
		for (Control c : outerCircle.getChildren()) {
			c.setBackground(null);
			if (c instanceof Composite) {
				setNullBackground((Composite) c);
			}
		}
	}

}

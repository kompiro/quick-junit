package junit.extensions.eclipse.quick;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsMessages;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @see org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationTabGroupViewer
 */
public class QuickJUnitInfoPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private ILaunchConfigurationTab[] tabs;
	private CTabFolder fTabFolder;
	private int fCurrentTabIndex = -1;

	private boolean fInitializingTabs = false;
	private boolean fDisposingTabs = false;

	private ILaunchConfigurationWorkingCopy fWorkingCopy;
	private ILaunchConfiguration fOriginal;
	
	public QuickJUnitInfoPage() {
		tabs = new ILaunchConfigurationTab[]{
				new JavaArgumentsTab(),
				new JavaJRETab(),
				new JavaClasspathTab(),
				new SourceLookupTab(),
				new EnvironmentTab(),
				new CommonTab()
		};
		try {
			fWorkingCopy = ExtensionSupport.createLaunchConfigurationWorkingCopy();
			fOriginal = fWorkingCopy.doSave();
		} catch (CoreException e) {
			getPlugin().logSystemError(e, this);
		}
	}
	
	protected Control createContents(Composite parent) {
		createTabFolder(parent);
		CTabItem tab = null;
		String name = "";
		Control control = null;
		for (int i = 0; i < tabs.length; i++) {
			tab = new CTabItem(fTabFolder, SWT.BORDER);
			name = tabs[i].getName();
			if (name == null) {
				name = "unspecified"; 
			}
			tab.setText(name);
			tab.setImage(tabs[i].getImage());
			tabs[i].createControl(tab.getParent());
			control = tabs[i].getControl();
			if (control != null) {
				tab.setControl(control);
			}
			tabs[i].initializeFrom(fOriginal);
		}

		setActiveTab(0);
		return fTabFolder;
	}

	private void createTabFolder(Composite parent) {
		if (fTabFolder == null) {
			ColorRegistry reg = JFaceResources.getColorRegistry();
			Color c1 = reg.get("org.eclipse.ui.workbench.ACTIVE_TAB_BG_START"), //$NON-NLS-1$
				  c2 = reg.get("org.eclipse.ui.workbench.ACTIVE_TAB_BG_END"); //$NON-NLS-1$
			fTabFolder = new CTabFolder(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_TRIM | SWT.FLAT);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			fTabFolder.setSelectionBackground(new Color[] {c1, c2},	new int[] {100}, true);
			fTabFolder.setSelectionForeground(reg.get("org.eclipse.ui.workbench.ACTIVE_TAB_TEXT_COLOR")); //$NON-NLS-1$
			fTabFolder.setSimple(PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
			fTabFolder.setLayoutData(gd);
	        fTabFolder.setBorderVisible(true);
			fTabFolder.setFont(parent.getFont());
			fTabFolder.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					if (!fInitializingTabs) {
						handleTabSelected();
						refresh();
					}
				}
			});	
		}
	}
	
	public void setActiveTab(int index) {
		ILaunchConfigurationTab[] tabs = getTabs();
		if (index >= 0 && index < tabs.length) {
			fTabFolder.setSelection(index);
			handleTabSelected();
		}
	}

	public boolean performOk() {
		try {
			for(int i = 0; i < tabs.length; i++){
				ILaunchConfigurationTab tab = tabs[i];
				tab.performApply(fWorkingCopy);
			}
			fWorkingCopy.doSave();
		} catch (CoreException e) {
			getPlugin().logSystemError(e, this);
		}
		return super.performOk();
	}
	
	protected void handleTabSelected() {
		if (fDisposingTabs || fInitializingTabs) {
			return;
		}
		ILaunchConfigurationTab[] tabs = getTabs();
		if (fCurrentTabIndex == fTabFolder.getSelectionIndex() || tabs == null || tabs.length == 0 || fCurrentTabIndex > (tabs.length - 1)) {
			return;
		}
		if (fCurrentTabIndex != -1) {
			ILaunchConfigurationTab tab = tabs[fCurrentTabIndex];
			ILaunchConfigurationWorkingCopy wc = getWorkingCopy();
			if (wc != null) {
				tab.deactivated(wc);
				getActiveTab().activated(wc);
			}
		}
		fCurrentTabIndex = fTabFolder.getSelectionIndex();
	}

	private ILaunchConfigurationTab getActiveTab() {
		ILaunchConfigurationTab[] tabs = getTabs();
		if (fTabFolder != null && tabs != null) {
			int pageIndex = fTabFolder.getSelectionIndex();
			if (pageIndex >= 0) {
				return tabs[pageIndex];
			}
		}
		return null;
	}

	public void refresh() {
		if (fInitializingTabs) {
			return;
		}
		if(fOriginal != null && fOriginal.isReadOnly()) {
			updateApplyButton();
			return;
		}
		ILaunchConfigurationTab[] tabs = getTabs();
		if (tabs != null) {
			// update the working copy from the active tab
			boolean newwc = !getWorkingCopy().isDirty();
			getActiveTab().performApply(getWorkingCopy());
			if((fOriginal instanceof ILaunchConfigurationWorkingCopy) && newwc) {
				try {
					ILaunchConfigurationWorkingCopy copy = getWorkingCopy();
					if(copy != null) {
						copy.doSave();
					}
				} 
				catch (CoreException e) {
					getPlugin().logSystemError(e, this);
				};
			}
			updateApplyButton();
			// update error ticks
			CTabItem item = null;
			boolean error = false;
			Image image = null;
			for (int i = 0; i < tabs.length; i++) {
				item = fTabFolder.getItem(i);
				image = tabs[i].getImage();
				item.setImage(image);
				if(!tabs[i].isValid(getWorkingCopy())) {
					error = tabs[i].getErrorMessage() != null;
					if(error) {
						item.setImage(DebugUIPlugin.getDefault().getLaunchConfigurationManager().getErrorTabImage(tabs[i]));
					}
				}
			}
			setErrorMessage(getErrorMessage());
			setMessage(getMessage());	
		}
	}

	public String getErrorMesssage() {
		if (fInitializingTabs) {
			return null;
		}
		
		if (getWorkingCopy() == null) {
			return null;
		}
//		try {
//			verifyName();
//		} catch (CoreException ce) {
//			return ce.getStatus().getMessage();
//		}
//	
		String message = null;
		ILaunchConfigurationTab activeTab = getActiveTab();
		if (activeTab == null) {
			return null;
		} 
		message = activeTab.getErrorMessage();
		if (message != null) {
			return message;
		}
		
		ILaunchConfigurationTab[] allTabs = getTabs();
		for (int i = 0; i < allTabs.length; i++) {
			ILaunchConfigurationTab tab = allTabs[i];
			if (tab == activeTab) {
				continue;
			}
			message = tab.getErrorMessage();
			if (message != null) {
				StringBuffer temp= new StringBuffer();
				temp.append('[');
				temp.append(DebugUIPlugin.removeAccelerators(tab.getName()));
				temp.append("]: "); //$NON-NLS-1$
				temp.append(message);
				return temp.toString();
			}
		}
		if(getWorkingCopy().isReadOnly()) {
			return "readonly";
		}
		return null;
	}
	
	public String getMessage() {
		if (fInitializingTabs) {
			return null;
		}
		
		String message = "";
		
		ILaunchConfigurationTab tab = getActiveTab();
		if (tab != null) {
			String tabMessage = tab.getMessage();
			if (tabMessage != null) {
				message = tabMessage;
			}
		}
		
		return message;
	}	
	
	private ILaunchConfigurationTab[] getTabs() {
		return tabs;
	}
	
	protected ILaunchConfigurationWorkingCopy getWorkingCopy() {
		return fWorkingCopy;
	}
	
	protected ILaunchConfiguration getOriginal() {
		return fOriginal;
	}
	
	private QuickJUnitPlugin getPlugin() {
		return QuickJUnitPlugin.getDefault();
	}

}

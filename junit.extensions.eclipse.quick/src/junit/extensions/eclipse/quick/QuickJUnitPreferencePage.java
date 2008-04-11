package junit.extensions.eclipse.quick;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class QuickJUnitPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private NamingRules namingRules;
    private NamingRulesPreference namingRulesPreference;
    private FindPairRules findPairRules;
    private FindPairRulesPreference findPairRulesPreference;
    
    public QuickJUnitPreferencePage() {
        setPreferenceStore(QuickJUnitPlugin.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench)  {
        IPreferenceStore store = getPreferenceStore();
        namingRules = new NamingRules(store);
        namingRulesPreference = new NamingRulesPreference(this);
        findPairRules = new FindPairRules(store);
        findPairRulesPreference = new FindPairRulesPreference();
	}

	protected Control createContents(Composite parent)  {
        Composite composite= new Composite(parent, SWT.NULL);
        GridLayout layout= new GridLayout();
        layout.numColumns= 1;
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        composite.setLayout(layout);
        GridData data= new GridData();
        data.verticalAlignment= GridData.FILL;
        data.horizontalAlignment= GridData.FILL;
        composite.setLayoutData(data);
        findPairRulesPreference.create(findPairRules.get(), composite);
        namingRulesPreference.create(namingRules.get(), composite);
        Dialog.applyDialogFont(composite);
        return composite;
	}

    GridData getButtonGridData(Button button) {
        GridData gd= new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        int widthHint= convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        gd.widthHint= Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        return gd;
    }

    protected void performDefaults() {
        super.performDefaults();
        namingRulesPreference.setValue(namingRules.getDefault());
        findPairRulesPreference.setValue(findPairRules.getDefault());
    }

    public boolean performOk() {
        namingRules.set(namingRulesPreference.getValue());
        findPairRules.set(findPairRulesPreference.getValue());
        return true;
    }
}

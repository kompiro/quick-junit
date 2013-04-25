package junit.extensions.eclipse.quick.process.internal.preferences;

import static junit.extensions.eclipse.quick.process.internal.preferences.Preference.TEMPLATE;
import static junit.extensions.eclipse.quick.process.internal.preferences.Preference.PROCESS;
import junit.extensions.eclipse.quick.process.internal.ProcessActivator;
import junit.extensions.eclipse.quick.process.internal.ProcessKey;
import junit.extensions.eclipse.quick.process.internal.TemplateKey;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ProcessPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {


	public ProcessPreferencePage() {
		super(FLAT);
		setPreferenceStore(ProcessActivator.getDefault().getPreferenceStore());
		setDescription(Messages.ProcessPreferencePage_description);
		noDefaultAndApplyButton();
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		createTemplateArea();
		createProcessArea();
	}

	private void createProcessArea() {
		Composite comp = getFieldEditorParent();
		comp.setLayout(new GridLayout());
		Group group = new Group(comp , SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(1, false));
		group.setText(Messages.ProcessPreferencePage_process_group);
		StringFieldEditor templateField  = new StringFieldEditor(PROCESS.name(), "", group); //$NON-NLS-1$
		addField(templateField);
		Composite keyDescription = new Composite(group, SWT.NONE);
		keyDescription.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().span(2, 1).applyTo(keyDescription);
		
		for(ProcessKey key:ProcessKey.values()){
			String text = String.format("%s:%s",key.key(),key.descrpition()); //$NON-NLS-1$
			Label label = new Label(keyDescription, SWT.NONE);
			label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
					false));
			label.setText(text);
			
		}
	}

	private void createTemplateArea() {
		Composite comp = getFieldEditorParent();
		comp.setLayout(new GridLayout());
		Group group = new Group(comp , SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(1, false));
		group.setText(Messages.ProcessPreferencePage_template_group);
		StringFieldEditor templateField  = new StringFieldEditor(TEMPLATE.name(), "", group); //$NON-NLS-1$
		addField(templateField);
		Composite keyDescription = new Composite(group, SWT.NONE);
		keyDescription.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().span(2, 1).applyTo(keyDescription);
		
		for(TemplateKey key:TemplateKey.values()){
			String text = String.format("%s:%s",key.key(),key.descrpition()); //$NON-NLS-1$
			Label label = new Label(keyDescription, SWT.NONE);
			label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
					false));
			label.setText(text);
			
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}
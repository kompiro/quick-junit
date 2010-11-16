package junit.extensions.eclipse.quick.mock.internal.preference;

import junit.extensions.eclipse.quick.mock.internal.Activator;
import junit.extensions.eclipse.quick.mock.internal.Messages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Displays the preference page for Quick JUnit Mockito integration.
 * 
 * @version $Id$
 */
public class QuickJUnitMockitoPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Create a preference page and initialize it with some sane default values.
	 */
	public QuickJUnitMockitoPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.QuickJUnitMockitoPreferencePage_description);
	}

	public void createFieldEditors() {
		BooleanFieldEditor editor = new BooleanFieldEditor(PreferenceInitializer.ENABLE_ADDITIONAL_AUTOCOMPLETE_FAVOURTES,
				Messages.QuickJUnitMockitoPreferencePage_enable_label, getFieldEditorParent());
		addField(editor);
	}

	public void init(IWorkbench workbench) {
	}

}
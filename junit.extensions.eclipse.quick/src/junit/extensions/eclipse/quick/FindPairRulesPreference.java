package junit.extensions.eclipse.quick;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author koichik
 */
public class FindPairRulesPreference {
    private boolean includeBinaryClass;

    private Button includeBinaryClassButton;

    public FindPairRulesPreference() {
    }

    public void create(final boolean includeBinaryClass, final Composite parent) {
        this.includeBinaryClass = includeBinaryClass;
        final Composite container = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        container.setLayout(layout);
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);
        createButton(container);
        update();
    }

    public boolean getValue() {
        return includeBinaryClass;
    }

    public void setValue(final boolean includeBinaryClass) {
        this.includeBinaryClass = includeBinaryClass;
        this.includeBinaryClassButton.setSelection(includeBinaryClass);
    }

    private void createButton(final Composite container) {
    	includeBinaryClassButton = new Button(container, SWT.CHECK);
        includeBinaryClassButton.setText(Messages
                .getString("FindPairRulesPreference.includeBinaryClass.label")); //$NON-NLS-1$
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL
                | GridData.VERTICAL_ALIGN_BEGINNING);
        includeBinaryClassButton.setLayoutData(gd);
        includeBinaryClassButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                includeBinaryClass = includeBinaryClassButton.getSelection();
            }
        });
    }

    private void update() {
        includeBinaryClassButton.setSelection(includeBinaryClass);
    }
}
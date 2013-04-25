package junit.extensions.eclipse.quick.mock.internal.wizard;

import junit.extensions.eclipse.quick.mock.internal.MockitoEntry;

import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MockitoPage extends WizardPage implements IClasspathContainerPage,IClasspathContainerPageExtension {

	private MockitoEntry entry;
	private IJavaProject project;

	public MockitoPage() {
		super("mockitoPage"); //$NON-NLS-1$
		setTitle("Mockito Library"); //$NON-NLS-1$
		setDescription(Messages.MockitoPage_AddMockitoLibrary);
		entry = new MockitoEntry();
	}
	
	public boolean finish() {
		try {
			IJavaProject[] javaProjects= new IJavaProject[] { project };
			IClasspathContainer[] containers= { null };
			JavaCore.setClasspathContainer(entry.getContainerPath(), javaProjects, containers, null);
		} catch (JavaModelException e) {
			return false;
		}

		return true;
	}

	public IClasspathEntry getSelection() {
		return entry.getContainer();
	}

	public void setSelection(IClasspathEntry containerEntry) {
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));
		label.setText(Messages.MockitoPage_AddedLabel);
		setControl(composite);
	}

	public void initialize(IJavaProject project,
			IClasspathEntry[] currentEntries) {
		this.project = project;
	}

}

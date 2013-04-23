package junit.extensions.eclipse.quick.mock.internal;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.swt.graphics.Image;

public class MockitoClasspathFixProcessor extends ClasspathFixProcessor {

	public MockitoClasspathFixProcessor() {
	}

	@Override
	public ClasspathFixProposal[] getFixImportProposals(final IJavaProject project,
			String missingType) throws CoreException {
		if(missingType == null) return null;
		if(missingType.startsWith("org.mockito")){ //$NON-NLS-1$
			return new ClasspathFixProposal[]{new ClasspathFixProposal(){

				@Override
				public Change createChange(IProgressMonitor monitor)
						throws CoreException {
					if (monitor == null) {
						monitor= new NullProgressMonitor();
					}
					monitor.beginTask(Messages.MockitoClasspathFixProcessor_beginAddMockitoLibraryTask,1);
					IClasspathEntry entry = new MockitoEntry().getContainer();
					IClasspathEntry[] oldEntries= project.getRawClasspath();
					ArrayList<IClasspathEntry> newEntries= new ArrayList<IClasspathEntry>(oldEntries.length + 1);
					boolean added= false;
					for (int i= 0; i < oldEntries.length; i++) {
						IClasspathEntry curr= oldEntries[i];
						if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
							IPath path= curr.getPath();
							if (path.equals(entry.getPath())) {
								return new NullChange(); // already on build path
							} else if (path.matchingFirstSegments(entry.getPath()) > 0) {
								if (!added) {
									curr= entry; // replace
									added= true;
								} else {
									curr= null;
								}
							}
						}
						if (curr != null) {
							newEntries.add(curr);
						}
					}
					if (!added) {
						newEntries.add(entry);
					}

					final IClasspathEntry[] newCPEntries= (IClasspathEntry[]) newEntries.toArray(new IClasspathEntry[newEntries.size()]);
					Change newClasspathChange= newClasspathChange(project, newCPEntries, project.getOutputLocation());
					if (newClasspathChange != null) {
						return newClasspathChange;
					}
				return new NullChange();

				}

				@Override
				public String getAdditionalProposalInfo() {
					return Messages.MockitoClasspathFixProcessor_AdditionalProposalInfo;
				}

				@Override
				public String getDisplayString() {
					return getAdditionalProposalInfo();
				}

				@Override
				public Image getImage() {
					return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
				}

				@Override
				public int getRelevance() {
					return 15;
				}
				
			}};
		}
		return null;
	}

}

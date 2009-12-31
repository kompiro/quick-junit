package junit.extensions.eclipse.quick.mock.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class MockitoClasspathContainerInitializer extends
		ClasspathContainerInitializer {

	public MockitoClasspathContainerInitializer() {
	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		IClasspathContainer container = new IClasspathContainer() {
			
			public IPath getPath() {
				return 	new Path(MockitoEntry.CONTAINER_PATH);
			}
			
			public int getKind() {
				return K_APPLICATION;
			}
			
			public String getDescription() {
				return "Mockito";
			}
			
			public IClasspathEntry[] getClasspathEntries() {
				MockitoEntry entry = new MockitoEntry();
				IClasspathEntry[] result = new IClasspathEntry[]{
					JavaCore.newLibraryEntry(entry.getPath(), null, null)
				};
				return result;
			}
		};
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container  }, null);
	}

}

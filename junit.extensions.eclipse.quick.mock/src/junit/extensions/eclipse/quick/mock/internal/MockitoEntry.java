package junit.extensions.eclipse.quick.mock.internal;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class MockitoEntry {

	public static final String CONTAINER_PATH = "junit.extensions.eclipse.quick.mock.MOCKITO_CONTAINER";

	public IPath getPath(){
		Bundle bundle = Platform.getBundle("junit.extensions.eclipse.quick.mock");
		URL entry = bundle.getEntry("mockito-all-1.8.2.jar");
		String fileURL = null;
		try {
			fileURL = URLDecoder.decode(FileLocator.toFileURL(entry).getFile(), "UTF-8");
		} catch (IOException e) {
		}
		return new Path(fileURL);
	}
	
	public IClasspathEntry getContainer(){
		IPath path = getContainerPath();
		IClasspathEntry entry = JavaCore.newContainerEntry(path );
		return entry;
	}

	public IPath getContainerPath() {
		IPath path = new Path(CONTAINER_PATH);
		return path;
	}
	
}

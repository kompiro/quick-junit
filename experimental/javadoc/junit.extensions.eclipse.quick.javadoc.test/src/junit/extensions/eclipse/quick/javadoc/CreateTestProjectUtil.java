package junit.extensions.eclipse.quick.javadoc;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.contribution.junit.javadoc.test.TestProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public final class CreateTestProjectUtil {
	
	public static TestProject createTestProject() throws CoreException, MalformedURLException, IOException{
		final TestProject project = new TestProject();
		project.addJar("org.junit", "junit.jar");
		return project;
	}
	
	public static IPackageFragment createPackageFragment(final TestProject project, final String packageName) throws CoreException{
		IPackageFragment created = project.createPackage(packageName);
		return created;
	}
	
	public static IType createType(
			final TestProject project,
			final String packageName,
			final String typeName,
			final String source) throws CoreException{
		return project.createType(project.getPackage(packageName), typeName + ".java",source);
	}
	
	private CreateTestProjectUtil(){
		
	}

}

package junit.extensions.eclipse.quick.javadoc;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.contribution.junit.test.TestProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;

public final class CreateTestProjectUtil {
	
	public static TestProject createTestProject() throws CoreException, MalformedURLException, IOException{
		final TestProject project = new TestProject();
		project.addJar("org.junit4", "junit.jar");
		IWorkspaceRunnable runnable = new IWorkspaceRunnable(){

			public void run(IProgressMonitor monitor) throws CoreException {
				monitor.beginTask("create test project", 10);
				IPackageFragment pack = project.createPackage("test");
				monitor.setTaskName("create TestClass");
				project.createType(pack, "TestClass.java", 
						"public class TestClass{\n" +
						"	/**\n" +
						"	 *	@see test.TestClass\n" +
						"	 */\n" +
						"	@org.junit.Test\n" +
						"	public void do_test() throws Exception{\n" +
						"	}\n" +
						"	public void do_test(String str) throws Exception{\n" +
						"	}\n" +
						"	public void do_test(String str,Object obj) throws Exception{\n" +
						"	}\n" +
						"}\n"
				);
				monitor.setTaskName("create TestClass2");
				project.createType(pack, "TestClass2.java", 
						"public class TestClass2{\n" +
						"	/**\n" +
						"	 *	@see test.TestClass\n" +
						"	 */\n" +
						"	@org.junit.Test\n" +
						"	public void do_test() throws Exception{\n" +
						"	}\n" +
						"	public void do_test2(){\n" +
						"	}\n" +
						"}\n"
				);
				monitor.setTaskName("create TestClassExtendsTestClass");
				project.createType(pack, "TestClassExtendsTestClass.java", 
						"import junit.framework.TestCase\n" +
						"import org.junit.Test\n" +
						"\n" +
						"public class TestClassExtendsTestClass extends TestCase{\n" +
						"	/**\n" +
						"	 *	@see test.TestClass\n" +
						"	 */\n" +
						"	@org.junit.Test\n" +
						"	public void do_test() throws Exception{\n" +
						"	}\n" +
						"	public void do_test2(){\n" +
						"	}\n" +
						"	public void setUp() throws Exception{\n" +
						"	}" +
						"}\n"
				);
				project.createType(pack, "Priority.java", 
						"public enum Priority {\n" + 
						"	BLOCKER,CRITICAL,MAJOR,MINOR,TRIVIAL;\n" +
						"	public void do_test(){\n" +
						"	}\n" + 
						"}");
				project.createType(pack, "IDocService.java", 
						"public interface IDocService {\n" + 
						"	public void do_test();\n" + 
						"}");
				project.getJavaProject().open(monitor);
			}
		};
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.run(runnable, null);
		return project;
	}
	
	
	private CreateTestProjectUtil(){
		
	}

}

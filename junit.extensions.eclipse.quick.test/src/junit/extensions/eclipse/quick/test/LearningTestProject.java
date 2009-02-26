package junit.extensions.eclipse.quick.test;

import static org.junit.Assert.*;

import org.eclipse.contribution.junit.test.TestProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LearningTestProject {

	private TestProject project;
	private IWorkspace workspace;
	
	@Test
	public void leaning_JavaDocのみ取得する() throws Exception {
		assertTrue(project.getProject().exists());
		assertTrue(workspace.getRoot().exists(new Path("TestProject")));
		assertTrue(workspace.getRoot().exists(new Path("TestProject/src/test")));
		assertTrue(workspace.getRoot().exists(new Path("TestProject/src/test/TestClass.java")));
		IType testClass = project.getJavaProject().findType("test.TestClass");
		assertTrue(testClass.exists());
		IMethod method = testClass.getMethod("test_test", null);
		assertFalse(method.exists());
		IMethod doMethod = testClass.getMethod("do_test", null);
		IAnnotation annotation = doMethod.getAnnotation("org.junit.Test");
		assertTrue(annotation.exists());
//		int offset = doMethod.getJavadocRange().getOffset();
		int length = doMethod.getJavadocRange().getLength();
		System.out.println(doMethod.getSource().substring(0,length));
	}
	
	@Before
	public void before() throws Exception{
		project = new TestProject();
		project.addJar("org.junit4", "junit.jar");
		IWorkspaceRunnable runnable = new IWorkspaceRunnable(){
			public void run(IProgressMonitor monitor) throws CoreException {
				IPackageFragment pack = project.createPackage("test");
				IType testClass = project.createType(pack, "TestClass.java", 
						"public class TestClass{\n" +
						"	/**\n" +
						"	 *	@see test.TestClass\n" +
						"	 */\n" +
						"	@org.junit.Test\n" +
						"	public void do_test() throws Exception{\n" +
						"	}\n" +
						"}\n"
				);
				project.getJavaProject().open(monitor);
			}
		};
		workspace = ResourcesPlugin.getWorkspace();
		workspace.run(runnable, null);
//		IWorkbench workbench = PlatformUI.getWorkbench();
//		IIntroManager introManager = workbench.getIntroManager();
//		introManager.closeIntro(introManager.getIntro());
	}
	
	@After
	public void after() throws Exception{
		project.dispose();
	}
}

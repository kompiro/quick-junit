package junit.extensions.eclipse.quick.test;

import static org.junit.Assert.*;

import org.eclipse.contribution.junit.test.TestProject;
import org.eclipse.core.runtime.NullProgressMonitor;
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
	
	@Test
	public void leaning() throws Exception {
		IPackageFragment pack = project.createPackage("test");
		IType testClass = project.createType(pack, "TestClass.java", 
				"public class TestClass{" +
				"	/**" +
				"	 *	@see test.TestClass" +
				"	 */" +
				"	@org.junit.Test" +
				"	public void do() throws Exception{" +
				"	}" +
				"}"
		);
		IMethod method = testClass.getMethod("test_test", null);
		assertFalse(method.exists());
		IMethod doMethod = testClass.getMethod("do", null);
		assertNotNull(doMethod.getAnnotation("org.junit.Test"));
		System.out.println(doMethod.getAttachedJavadoc(new NullProgressMonitor()));
	}
	
	@Before
	public void before() throws Exception{
		project = new TestProject();
		project.addJar("org.junit4", "junit.jar");
		IWorkbench workbench = PlatformUI.getWorkbench();
		IIntroManager introManager = workbench.getIntroManager();
		introManager.closeIntro(introManager.getIntro());
	}
	
	@After
	public void after() throws Exception{
		project.dispose();
	}
}

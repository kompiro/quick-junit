package junit.extensions.eclipse.quick;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.contribution.junit.test.TestProject;
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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
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
		int length = doMethod.getJavadocRange().getLength();
		System.out.println(doMethod.getSource().substring(0,length));
		createAST(testClass);
	}
	
	private void createAST(IType testClass) throws Exception {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		
		parser.setSource(testClass.getCompilationUnit());
		ASTNode node = parser.createAST(new NullProgressMonitor());
	}

	@Before
	public void before() throws Exception{
		project = new TestProject();
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
						"}\n"
				);

				project.getJavaProject().open(monitor);
			}
		};
		workspace = ResourcesPlugin.getWorkspace();
		workspace.run(runnable, null);
		closeIntro();
	}

	private void closeIntro() {
		IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
		IIntroPart intro = introManager.getIntro();
		if(intro != null && introManager.isIntroStandby(intro)){
			introManager.closeIntro(intro);
		}
	}
	
	@Test
	public void learning_SearchEngine() throws Exception {
		SearchEngine engine = new SearchEngine();
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchPattern pattern = SearchPattern.createPattern("TestClass2", IJavaSearchConstants.CLASS, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_FULL_MATCH);
		SearchParticipant[] participants = new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()};
		SearchRequestor requestor = new SearchRequestor(){
		
			@Override
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				Object element = match.getElement();
				System.out.println(element.getClass().getName());
				System.out.println(element);
			}
		};
		engine.search(pattern, participants, scope, requestor, new NullProgressMonitor());
	}
	
	@After
	public void after() throws Exception{
		project.dispose();
	}
}

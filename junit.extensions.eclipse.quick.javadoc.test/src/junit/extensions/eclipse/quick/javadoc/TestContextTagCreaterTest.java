package junit.extensions.eclipse.quick.javadoc;

import static junit.extensions.eclipse.quick.javadoc.CreateTestProjectUtil.createPackageFragment;
import static junit.extensions.eclipse.quick.javadoc.CreateTestProjectUtil.createTestProject;
import static junit.extensions.eclipse.quick.javadoc.CreateTestProjectUtil.createType;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.extensions.eclipse.quick.javadoc.internal.JavaDocActivator;

import org.eclipse.contribution.junit.test.TestProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestContextTagCreaterTest {
	
	private TestProject project;
	private IType type;
	private TestContextTagCreator creator;
	private final String TEST_CONTEXT_ANNOTATION = QuickJUnitDocTagConstants.TestContext.toAnnotation();

	@Before
	public void setup() throws Exception{
		project = createTestProject();
		assertNotNull(project);
		String packageName = "testcontext";
		createPackageFragment(project, packageName);
		type = createType(project, packageName, "Target", 
				"class Target{}");
		assertNotNull(type);
		creator = new TestContextTagCreator();
	}
	
	@After
	public void teardown() throws Exception{
		project.dispose();
	}
	
	@Test
	public void createTestContextTag() throws Exception {
		creator.addTag(type,"test");
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);
	}
	
	@Test
	public void createTestContextTagEmptyString() throws Exception {
		creator.addTag(type,"");
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);		
	}

	@Test
	public void createTestContextTagNull() throws Exception {
		creator.addTag(type,null);
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);		
	}

	@Test
	public void createTestContextTwiceAndThreeTimes() throws Exception{
		creator.addTag(type,"test");
		String source = type.getSource();
		creator.addTag(type,"test2");
		source = type.getSource();
		assertStringCount(2,TEST_CONTEXT_ANNOTATION,source);
		
		creator.addTag(type,"test3");
		source = type.getSource();
		assertStringCount(3,TEST_CONTEXT_ANNOTATION,source);
		
	}
	
	private void assertStringCount(int count,String target,String source){
        Pattern pattern = Pattern.compile(Pattern.quote(target));
        Matcher matcher = pattern.matcher(source);
        int hitCount = 0;
        while (matcher.find()) {
            hitCount++;
        }
        assertEquals(count,hitCount);		
	}
	
	public static class TestContextTagCreator{

		private ASTParser parser;

		public TestContextTagCreator(){
			parser = ASTParser.newParser(AST.JLS3);
			parser.setBindingsRecovery(true);			
		}
		
		public void addTag(IType type,String clazz) {
			ICompilationUnit jdtUnit = type.getCompilationUnit();
			try {
				parser.setSource(jdtUnit);
				IProgressMonitor monitor = new NullProgressMonitor();
				ASTNode node = parser.createAST(monitor);
				if(node instanceof CompilationUnit){
					CompilationUnit unit = (CompilationUnit) node;
					List<?> types = unit.types();
					unit.recordModifications();
					AbstractTypeDeclaration declaratingNode = (AbstractTypeDeclaration)types.get(0);
					AST ast = unit.getRoot().getAST();
					Javadoc javadoc = declaratingNode.getJavadoc();
					if(javadoc == null){
						javadoc = ast.newJavadoc();
						declaratingNode.setJavadoc(javadoc);
					}
					TagElement tag = createTag(ast, clazz);
					javadoc.tags().add(tag);

					Document document = new Document();
					document.set(jdtUnit.getSource());
					TextEdit edits = unit.rewrite(document,jdtUnit.getJavaProject().getOptions(true));
					edits.apply(document);
					String newSource = document.get();
					ICompilationUnit workingCopy = jdtUnit.getWorkingCopy(monitor);
					IBuffer buffer = workingCopy.getBuffer();
					buffer.setContents(newSource);
					workingCopy.commitWorkingCopy(true, monitor);
				}
			} catch (Exception e) {
				JavaDocActivator.getDefault().handleSystemError(e, this);
				e.printStackTrace();
			}
		}

		private TagElement createTag(AST ast, String clazz) {
			TagElement tag = ast.newTagElement();
			tag.setTagName(QuickJUnitDocTagConstants.TestContext.toAnnotation());
			if(clazz == null || clazz.equals("")){
				return tag;
			}
			SimpleName newSimpleName = ast.newSimpleName(clazz);
			tag.fragments().add(newSimpleName);
			return tag;
		}
		
	}

}

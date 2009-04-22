package junit.extensions.eclipse.quick.javadoc;

import static org.junit.Assert.assertEquals;
import static junit.extensions.eclipse.quick.javadoc.CreateTestProjectUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.contribution.junit.test.TestProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SearchJavaClassFromDocTagVisitorTest {

	private static TestProject project;
	private static ASTParser parser;
	private static IType type;
	private List<Object> results = new ArrayList<Object>();
	private String source;
	protected static IType extendsType;
	
	@BeforeClass
	public static void before() throws Exception{
		project = createTestProject();
		createType(project,"test","TestClass", 
				"public class TestClass{\n" +
				"	/**\n" +
				"	 *	@TestContext test.TestClass\n" +
				"	 */\n" +
				"	@org.junit.Test\n" +
				"	public void do_test() throws Exception{\n" +
				"	}\n" +
				"	public void do_test(String str) throws Exception{\n" +
				"	}\n" +
				"	public void do_test(String str,Object obj) throws Exception{\n" +
				"	}\n" +
				"}\n");
		createType(project,"test", "TestClass2", 
				"public class TestClass2{\n" +
				"	/**\n" +
				"	 *	@TestContext test.TestClass\n" +
				"	 */\n" +
				"	@org.junit.Test\n" +
				"	public void do_test() throws Exception{\n" +
				"	}\n" +
				"	public void do_test2(){\n" +
				"	}\n" +
				"}\n");
		createType(project,"test", "TestClassExtendsTestClass", 
				"import junit.framework.TestCase\n" +
				"import org.junit.Test\n" +
				"\n" +
				"public class TestClassExtendsTestClass extends TestCase{\n" +
				"	/**\n" +
				"	 *	@TestContext test.TestClass\n" +
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
		createType(project,"test", "Priority", 
				"public enum Priority {\n" + 
				"	BLOCKER,CRITICAL,MAJOR,MINOR,TRIVIAL;\n" +
				"	public void do_test(){\n" +
				"	}\n" + 
				"}");
		createType(project,"test", "IDocService", 
				"public interface IDocService {\n" + 
				"	public void do_test();\n" + 
				"}");

		type = project.getJavaProject().findType("test.TestClass");
		extendsType = project.getJavaProject().findType("test.TestClassExtendsTestClass");
	}
	
	@Before
	public void setUp() throws Exception{
		createNewParser();
	}

	private  void createNewParser() {
		parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		parser.setBindingsRecovery(true);
	}
	
	@Test
	public void emptyStringAccepted() throws Exception {
		source = "";
		assertExpectZeroResultAndVisit();
	}
	
	
	@Test
	public void illegalJavaSourceAccepted() throws Exception {
		source = "test";
		assertExpectZeroResultAndVisit();
		source = 
				"public class TestClass{\n" +
				"]";// <- Illegal Close Blanket
		assertExpectZeroResultAndVisit();
		source = 
			"public void test(){\n" +
			"]"; // <- Illegal Close Blanket
		assertExpectZeroResultAndVisit();
	}

	@Test
	public void assertNoTagsContainedOnMethod() {
		source = 
			"public void test(){}";
		assertExpectZeroResultAndVisit();
		source = 
			"public void test(){\n" +
			"	System.out.println(\"test\")\n" +
			"}";
		assertExpectZeroResultAndVisit();
	}

	@Test
	public void assertNoTagsContainedOnClass() {
		source = 
			"public class TestOnClassOnlyClassDecralation{\n" +
			"}\n";
		assertExpectZeroResultAndVisit();
		source = 
			"public class TestOnClass{\n" +
			"	public void test(){\n" +
			"		System.out.println(\"test\")\n" +
			"	}\n" +
			"}\n";
		assertExpectZeroResultAndVisit();
	}
	
	@Test
	public void assertOneTagContainedOnMethod() {
		assertClassOnMethod();
		assertFQCNClassOnMethod();
//		assertCurrentMethodOnMethod();
//		assertMethodOnMethod();
		assertNotExistClassOnMethod();
//		assertSameSigunatureMethodFromExtendsClassOnMethod();
		assertInterface();
		assertEnum();
		assertAnnotation();
	}

	@Test
	@Ignore
	public void assertPolimophism() {
		source = 
			"/**\n" +
			" * @TestContext TestClass#do_test(String)\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
		source = 
			"/**\n" +
			" * @TestContext TestClass#do_test(String,Object)\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}
	
	@Test
	@Ignore
	public void assertPoliphonismSameClass() throws Exception {
		source = 
			"/**\n" +
			" * @TestContext #do_test(String)\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
		source = 
			"/**\n" +
			" * @TestContext #do_test(String,Object)\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}

	private void assertAnnotation() {
		source = 
			"/**\n" +
			" * @TestContext org.junit.Test\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}

	private void assertEnum() {
		source = 
			"/**\n" +
			" * @TestContext test.Priority\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
		source = 
			"/**\n" +
			" * @TestContext Priority\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();	}

	private void assertNotExistClassOnMethod() {
		source = 
			"/**\n" +
			// wrong word TestCase -> TestCaze
			" * @TestContext junit.framework.TestCaze\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectZeroResultAndVisit();
	}

	private void assertFQCNClassOnMethod() {
		source = 
			"/**\n" +
			" * @TestContext junit.framework.TestCase\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();		
		source = 
		"/**\n" +
		" * @TestContext test.TestClass2" +
		" */\n" +
		"public void do_test() throws Exception{\n" +
		"}\n";
		assertExpectOneResultAndVisit();		
	}

//	private void assertMethodOnMethod() {
//		source = 
//			"/**\n" +
//			" * @TestContext junit.framework.TestCase#setUp()\n" +
//			" */\n" +
//			"public void do_test() throws Exception{\n" +
//			"}\n";
//		assertExpectOneResultAndVisit();		
//		source = 
//			"/**\n" +
//			" * @TestContext TestClass2#do_test()\n" +
//			" */\n" +
//			"public void do_test() throws Exception{\n" +
//			"}\n";
//		assertExpectOneResultAndVisit();
//	}
//
//	private void assertCurrentMethodOnMethod() {
//		source = 
//			"/**\n" +
//			" * @TestContext #do_test()\n" +
//			" */\n" +
//			"public void do_test() throws Exception{\n" +
//			"}\n";
//		assertExpectOneResultAndVisit();
//	}

	private void assertClassOnMethod() {
		source = 
			"/**\n" +
			" * @TestContext TestClass2\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}
	
	private void assertInterface() {
		source = 
			"/**\n" +
			" * @TestContext test.IDocService\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
		source = 
			"/**\n" +
			" * @TestContext IDocService\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}

//	private void assertSameSigunatureMethodFromExtendsClassOnMethod() {
//		source = 
//			"/**\n" +
//			" * @TestContext #setUp()\n" +
//			" */\n" +
//			"public void do_test() throws Exception{\n" +
//			"}\n";
//		assertAndVisit(SearchJavaClassFromDocTagVisitorTest.extendsType);
//		assertEquals(1,results.size());
//		results.clear();
//	}

	@Test
	@Ignore
	public void assertSameSignatureMethodFromExtendsClassOnMethods() {
		source = 
			"/**\n" +
			" * @TestContext TestClass#do_test()\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit();
	}

	@Test
	@Ignore
	public void assertTwoTagsContainedOnMethod() {
		assertTwoTagsClassTwoTagsOnMethod();
		assertTwoTagsMethodOnMethod();
		assertTwoTagsNotExistClassOnMethod();
	}
	
	private void assertTwoTagsNotExistClassOnMethod() {
		source = 
			"/**\n" +
			" * @TestContext #do_tast()\n" + // #do_test <= #do_tast
			" * @TestContext #start()\n" +  // not exist method
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectZeroResultAndVisit();
	}

	private void assertTwoTagsMethodOnMethod() {
		source = 
			"/**\n" +
			" * @TestContext #do_test()\n" + 
			" * @TestContext junit.framework.TestCase#setUp()\n" +  
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit();
	}

	private void assertTwoTagsClassTwoTagsOnMethod() {
		source = 
			"/**\n" +
			" * @TestContext TestSuite\n" +
			" * @TestContext TestResult\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit();		
		source = 
			"/**\n" +
			" * @TestContext test.TestClass\n" +
			" * @TestContext TestSuite\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit();		
		source = 
			"/**\n" +
			" * @TestContext test.TestClass\n" +
			" * @TestContext junit.framework.TestCase\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit();		
	}

	private void assertExpectTwoResultsAndVisit() {
		assertAndVisit();
		assertResults(2);
	}

	private void assertResults(int i) {
		try{
			assertEquals(i,results.size());
		}catch(AssertionError e){
			System.err.println(source);
			System.err.println(results);
			throw e;
		}finally{
			results.clear();
		}
	}

	private void assertExpectOneResultAndVisit() {
		assertAndVisit();
		assertResults(1);
	}

	private void assertExpectZeroResultAndVisit() {
		assertAndVisit();
		assertResults(0);
	}


	private void assertAndVisit() {
		assertAndVisit(SearchJavaClassFromDocTagVisitorTest.type);
	}
	
	private void assertAndVisit(IType type) {
		SearchRequestor requester = new SearchRequestor(){
			@Override
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				Object element = match.getElement();
				if (element instanceof IType) {
					IType type = (IType) element;
					System.out.println(String.format("match type:'%s'",type.getFullyQualifiedName()));
				}else if(element instanceof IMethod){
					IMethod method = (IMethod) element;
					System.out.println(String.format("match method:'%s'",method.getElementName()));
				}
				results.add(element);
			}
		};
		ASTVisitor visitor = new SearchJavaClassFromDocTagVisitor(type,requester);
		createNewParser();
		if(source != null){
			parser.setSource(source.toCharArray());
		}
		ASTNode node = parser.createAST(null);
		node.accept(visitor);
	}
	
	@AfterClass
	public static void after() throws Exception{
		project.dispose();
	}

	/*
	 * 渡ってくるSourceがNullの場合はNullPointerExceptionが発生する。
	 * NullPointerExceptionを期待している訳ではないので、assertはしない
	 * Testも取り合えず行わない
	 */
	@Test
	@Ignore
	public void nullStringAccepted() throws Exception {
		source = null;
		assertExpectZeroResultAndVisit();		
	}
	
//	public void pattern() throws Exception {
//		CharSequence patternString = "#test(param,param2)";
//		Pattern methodPattern = Pattern.compile("#(.*)\\((.*)\\)");
//		Matcher matcher = methodPattern.matcher(patternString);
//		assertTrue(matcher.matches());
//		String name = matcher.group(1);
//		assertEquals("test",name);
//		String param = matcher.group(2);
//		assertEquals("param,param2",param);
//	}

}

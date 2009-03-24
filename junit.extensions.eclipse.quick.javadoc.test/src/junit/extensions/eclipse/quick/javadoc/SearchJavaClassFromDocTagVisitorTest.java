package junit.extensions.eclipse.quick.javadoc;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

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
	protected static IType extendsType;
	
	@BeforeClass
	public static void before() throws Exception{
		project = CreateTestProjectUtil.createTestProject();
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
		assertExpectZeroResultAndVisit("");
	}
	
	
	@Test
	public void illegalJavaSourceAccepted() throws Exception {
		assertExpectZeroResultAndVisit("test");
		String source = 
				"public class TestClass{\n" +
				"]";// <- Illegal Close Blanket
		assertExpectZeroResultAndVisit(source);
		source = 
			"public void test(){\n" +
			"]"; // <- Illegal Close Blanket
		assertExpectZeroResultAndVisit(source);
	}

	@Test
	public void assertNoTagsContainedOnMethod() {
		String source = 
			"public void test(){}";
		assertExpectZeroResultAndVisit(source);
		source = 
			"public void test(){\n" +
			"	System.out.println(\"test\")\n" +
			"}";
		assertExpectZeroResultAndVisit(source);
	}

	@Test
	public void assertNoTagsContainedOnClass() {
		String source = 
			"public class TestOnClassOnlyClassDecralation{\n" +
			"}\n";
		assertExpectZeroResultAndVisit(source);
		source = 
			"public class TestOnClass{\n" +
			"	public void test(){\n" +
			"		System.out.println(\"test\")\n" +
			"	}\n" +
			"}\n";
		assertExpectZeroResultAndVisit(source);
	}
	
	/**
	 * @see SearchJavaClassFromDocTagVisitor#visit(org.eclipse.jdt.core.dom.TagElement)
	 */
	@Test
	public void assertOneTagContainedOnMethod() {
		assertClassOnMethod();
		assertFQCNClassOnMethod();
		assertCurrentMethodOnMethod();
		assertMethodOnMethod();
		assertNotExistClassOnMethod();
		assertSameSigunatureMethodFromExtendsClassOnMethod();
		assertInterface();
		assertEnum();
		assertAnnotation();
	}

	private void assertAnnotation() {
		String source = 
			"/**\n" +
			" * @see org.junit.Test\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
	}

	private void assertEnum() {
		String source = 
			"/**\n" +
			" * @see test.Priority\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
		source = 
			"/**\n" +
			" * @see Priority\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);	}

	private void assertNotExistClassOnMethod() {
		String source = 
			"/**\n" +
			// wrong word TestCase -> TestCaze
			" * @see junit.framework.TestCaze\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectZeroResultAndVisit(source);
	}

	private void assertFQCNClassOnMethod() {
		String source = 
			"/**\n" +
			" * @see junit.framework.TestCase\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);		
		source = 
		"/**\n" +
		" * @see test.TestClass2" +
		" */\n" +
		"public void do_test() throws Exception{\n" +
		"}\n";
		assertExpectOneResultAndVisit(source);		
	}

	private void assertMethodOnMethod() {
		String source = 
			"/**\n" +
			" * @see junit.framework.TestCase#setUp()\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);		
		source = 
			"/**\n" +
			" * @see TestClass2#do_test()\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
	}


	private void assertCurrentMethodOnMethod() {
		String source = 
			"/**\n" +
			" * @see #do_test()\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
	}

	private void assertClassOnMethod() {
		String source = 
			"/**\n" +
			" * @see TestClass2\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
	}
	
	private void assertInterface() {
		String source = 
			"/**\n" +
			" * @see test.IDocService\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
		source = 
			"/**\n" +
			" * @see IDocService\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectOneResultAndVisit(source);
	}


	private void assertSameSigunatureMethodFromExtendsClassOnMethod() {
		String source = 
			"/**\n" +
			" * @see #setUp()\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertAndVisit(source,SearchJavaClassFromDocTagVisitorTest.extendsType);
		assertEquals(1,results.size());
		results.clear();		
	}

	@Test
	public void assertTwoTagsContainedOnMethod() {
		assertTwoTagsClassTwoTagsOnMethod();
		assertTwoTagsMethodOnMethod();
		assertTwoTagsNotExistClassOnMethod();
	}
	
	private void assertTwoTagsNotExistClassOnMethod() {
		String source = 
			"/**\n" +
			" * @see #do_tast()\n" + // #do_test <= #do_tast
			" * @see #start()\n" +  // not exist method
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectZeroResultAndVisit(source);
	}

	private void assertTwoTagsMethodOnMethod() {
		String source = 
			"/**\n" +
			" * @see #do_test()\n" + 
			" * @see junit.framework.TestCase#setUp()\n" +  
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit(source);
	}

	private void assertTwoTagsClassTwoTagsOnMethod() {
		String source = 
			"/**\n" +
			" * @see TestSuite\n" +
			" * @see TestResult\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit(source);		
		source = 
			"/**\n" +
			" * @see test.TestClass\n" +
			" * @see TestSuite\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit(source);		
		source = 
			"/**\n" +
			" * @see test.TestClass\n" +
			" * @see junit.framework.TestCase\n" +
			" */\n" +
			"public void do_test() throws Exception{\n" +
			"}\n";
		assertExpectTwoResultsAndVisit(source);		
	}

	private void assertExpectTwoResultsAndVisit(String source) {
		assertAndVisit(source);
		assertResults(2);
	}

	private void assertResults(int i) {
		try{
			assertEquals(i,results.size());
		}catch(AssertionError e){
			System.err.println(results);
			throw e;
		}finally{
			results.clear();
		}
	}

	private void assertExpectOneResultAndVisit(String source) {
		assertAndVisit(source);
		assertResults(1);
	}

	private void assertExpectZeroResultAndVisit(String source) {
		assertAndVisit(source);
		assertResults(0);
	}


	private void assertAndVisit(String source) {
		assertAndVisit(source,SearchJavaClassFromDocTagVisitorTest.type);
	}
	
	private void assertAndVisit(String source,IType type) {
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
		assertExpectZeroResultAndVisit(null);		
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



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.junit.Ignore;
import org.junit.Test;

public class LeaningAST {

	@Test
	public void learning() throws Exception {
		String source = "public class TestClass{\n" +
		"	/**\n" +
		"	 *	@see test.TestClass\n" +
		"    *  @see org.junit.Test\n" +
		"	 */\n" +
		"	@org.junit.Test\n" +
		"	public void do_test() throws Exception{\n" +
		"	}\n" +
		"}\n";
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		ASTNode node = parser.createAST(null);
//		for( Object obj : node.structuralPropertiesForType()){
//			extractPropertyDescriptor(node, obj);
//		}
		List<String> expected = new ArrayList<String>();
		expected.add("test.TestClass");
		expected.add("org.junit.Test");
		final List<String> actual = new ArrayList<String>();
		ASTVisitor visitor = new ASTVisitor(true){
			public boolean visit(Javadoc doc){
				return super.visit(doc);
			}
			@Override
			public boolean visit(TagElement node) {
				for(Object obj : node.fragments()){
					actual.add(obj.toString());
					System.out.println(obj.toString());
				}
				System.out.println(node.fragments().size());
				return super.visit(node);
			}
		};
		node.accept(visitor);
		assertEquals(expected,actual);
		actual.clear();
		source =
		"/**\n" +
		" *	@see test.TestClass\n" +
		" * @see org.junit.Test\n" +
		" */\n" +
		"@org.junit.Test\n" +
		"public void do_test() throws Exception{\n" +
		"}\n";
		parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		node = parser.createAST(null);
		node.accept(visitor);
		assertEquals(expected,actual);
	}

	
//	@Test
//	public void learning_addSource() throws Exception {
//		String source = "public class TestClass{\n" +
//		"	/**\n" +
//		"	 *	@see test.TestClass\n" +
//		"    *  @see org.junit.Test\n" +
//		"	 */\n" +
//		"	@org.junit.Test\n" +
//		"	public void do_test() throws Exception{\n" +
//		"	}\n" +
//		"}\n";
//		ASTParser parser = ASTParser.newParser(AST.JLS3);
//		parser.setSource(source.toCharArray());
//		final ASTNode node = parser.createAST(null);
//		final List<String> actual = new ArrayList<String>();
//		ASTVisitor visitor = new ASTVisitor(){
//			public boolean visit(Javadoc doc){
//				AST ast = doc.getAST();
//				TagElement tag = ast.newTagElement();
//				tag.setTagName(TagElement.TAG_SEE);
//				doc.tags().add(tag);
//				MethodRef method = ast.newMethodRef();
//				tag.fragments().add(method);
//				SimpleName name = ast.newSimpleName("Test");
//				method.setQualifier(ast.newQualifiedName(ast.newQualifiedName(ast.newName("org"), ast.newSimpleName("junit")), name));
//				name = ast.newSimpleName("test");
//				method.setName(name);
//				return super.visit(doc);
//			}
//		};
//		node.accept(visitor);
//		System.out.println(node);
//	}
	
	
	@Test
	@Ignore
	/*
	 * resolveBindingsはIProjectなど実際のJavaModelと関連付ける必要がある。
	 * ソースだけでは何とも出来ないみたい。
	 */
	public void learning_ASTRewrite_use_resolveBindings() throws Exception {
		String source = "public class TestClass{\n" +
//		"	/**\n" +
//		"	 *	@see test.TestClass\n" +
//		"    *  @see org.junit.Test\n" +
//		"	 */\n" +
//		"	@org.junit.Test\n" +
		"	public void do_test() throws Exception{\n" +
		"	}\n" +
		"}\n";
		Document doc = new Document(source);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source.toCharArray()); // set source
		parser.setResolveBindings(true); // we need bindings later on
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//		List types = cu.structuralPropertiesForType();
		for(Object o:cu.types()){
			AbstractTypeDeclaration dec = (AbstractTypeDeclaration) o;
			List bodyDeclarations = dec.bodyDeclarations();
			assertNotNull(dec.resolveBinding());
			for(Object body:bodyDeclarations){
				MethodDeclaration methodDec = (MethodDeclaration) body;
				IMethodBinding binding = methodDec.resolveBinding();
				assertNotNull(binding);
				ASTNode node = cu.findDeclaringNode(binding);
				assertNotNull("can't find declaring node",node);
				System.out.println(node);
			}
		}
//		ASTRewrite rewriter = ASTRewrite.create(cu.getAST());
//		ListRewrite listRewrite = rewriter.getListRewrite(cu, Javadoc.TAGS_PROPERTY);
		
	}

	@Test
	public void learning_ASTRewrite() throws Exception {
		String source = "public class TestClass{\n" +
		"	/**\n" +
		"	 *	@see test.TestClass\n" +
		"    *  @see org.junit.Test\n" +
		"	 */\n" +
//		"	@org.junit.Test\n" +
		"	public void do_test() throws Exception{\n" +
		"	}\n" +
		"}\n";
		Document doc = new Document(source);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source.toCharArray()); // set source
		parser.setResolveBindings(true); // we need bindings later on
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//		List types = cu.structuralPropertiesForType();
		ASTRewrite rewriter = ASTRewrite.create(cu.getAST());
		for(Object o:cu.types()){
			AbstractTypeDeclaration dec = (AbstractTypeDeclaration) o;
			List bodyDeclarations = dec.bodyDeclarations();
			for(Object body:bodyDeclarations){
				MethodDeclaration methodDec = (MethodDeclaration) body;
				ListRewrite listRewrite = rewriter.getListRewrite(methodDec.getJavadoc(), Javadoc.TAGS_PROPERTY);
				List originalList = listRewrite.getOriginalList();
				ASTNode node = createNode(cu.getAST());
				listRewrite.insertLast(node, null);
				System.out.println(originalList);
				System.out.println(listRewrite.getRewrittenList());
			}
		}
		TextEdit rewriteAst = rewriter.rewriteAST(doc, null);
		rewriteAst.apply(doc);
		System.out.println(doc.get());
	}


	private ASTNode createNode(AST ast) {
		TagElement tag = ast.newTagElement();
		tag.setTagName(TagElement.TAG_SEE);
		MethodRef method = ast.newMethodRef();
		tag.fragments().add(method);
		SimpleName name = ast.newSimpleName("Test");
		method.setQualifier(ast.newQualifiedName(ast.newQualifiedName(ast.newName("org"), ast.newSimpleName("junit")), name));
		name = ast.newSimpleName("setUp");
		method.setName(name);
		return tag;
	}


}

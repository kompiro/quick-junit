package junit.extensions.eclipse.quick;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
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
				}
				System.out.println();
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

	
	@Test
	public void learning_addSource() throws Exception {
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
		final ASTNode node = parser.createAST(null);
		final List<String> actual = new ArrayList<String>();
		ASTVisitor visitor = new ASTVisitor(){
			public boolean visit(Javadoc doc){
				AST ast = doc.getAST();
				TagElement tag = ast.newTagElement();
				tag.setTagName(TagElement.TAG_SEE);
				doc.tags().add(tag);
				MethodRef method = ast.newMethodRef();
				tag.fragments().add(method);
				SimpleName name = ast.newSimpleName("Test");
				method.setQualifier(ast.newQualifiedName(ast.newQualifiedName(ast.newName("org"), ast.newSimpleName("junit")), name));
				name = ast.newSimpleName("test");
				method.setName(name);
				return super.visit(doc);
			}
		};
		node.accept(visitor);
		System.out.println(node);
	}

	private void extractPropertyDescriptor(ASTNode node, Object obj) {
		if(obj == null) return;
		System.out.println(String.format("%s %s", obj.getClass().getName(),obj));
		if (obj instanceof StructuralPropertyDescriptor) {
			StructuralPropertyDescriptor desc = (StructuralPropertyDescriptor) obj;
			Object structuralProperty = node.getStructuralProperty(desc);
			extractPropertyDescriptor(node, structuralProperty);
			return;
		}
		if (obj instanceof List<?>) {
			List<?> list = (List<?>) obj;
			for(Object o : list){
				extractPropertyDescriptor(node, o);
			}
			return;
		}
		if(obj instanceof TypeDeclaration){
			TypeDeclaration type = (TypeDeclaration) obj;
//			System.out.println(type.getName().getFullyQualifiedName());
//			System.out.println(String.format("JavaDoc         : %s",type.getJavadoc()));
//			System.out.println(String.format("JavaDocProperty : %s",type.getJavadocProperty()));
			for (MethodDeclaration method : type.getMethods()) {
				extractPropertyDescriptor(node, method);
			}
		}
		if(obj instanceof MethodDeclaration){
			MethodDeclaration method = (MethodDeclaration) obj;
			System.out.println(method.getName());
			Javadoc javadoc = method.getJavadoc();
			List list = javadoc.structuralPropertiesForType();
			for(Object o : list){
				extractPropertyDescriptor(javadoc, o);
			}
//			System.out.println(String.format("JavaDoc         : %s",javadoc));
//			System.out.println(String.format("JavaDocProperty : %s",method.getJavadocProperty()));
		}
	}
	
}

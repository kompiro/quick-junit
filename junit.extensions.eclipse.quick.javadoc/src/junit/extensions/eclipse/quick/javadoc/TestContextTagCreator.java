package junit.extensions.eclipse.quick.javadoc;

import java.util.List;

import junit.extensions.eclipse.quick.javadoc.internal.JavaDocActivator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class TestContextTagCreator{

	private ASTParser parser;

	public TestContextTagCreator(){
		parser = ASTParser.newParser(AST.JLS3);
		parser.setBindingsRecovery(true);			
	}
	
	public void addTag(IType type,String clazz) {
		if(type == null){
			throw new IllegalArgumentException("addTag is needed 'type' value.");
		}
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
				addTagToJavaDoc(clazz, declaratingNode, ast);

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

	@SuppressWarnings("unchecked")
	private void addTagToJavaDoc(String clazz,
			AbstractTypeDeclaration declaratingNode, AST ast) {
		Javadoc javadoc = declaratingNode.getJavadoc();
		if(javadoc == null){
			javadoc = ast.newJavadoc();
			declaratingNode.setJavadoc(javadoc);
		}
		TagElement tag = createTag(ast, clazz);
		javadoc.tags().add(tag);
	}

	@SuppressWarnings("unchecked")
	private TagElement createTag(AST ast, String clazz) {
		TagElement tag = ast.newTagElement();
		tag.setTagName(QuickJUnitDocTagConstants.TestContext.toAnnotation());
		if(clazz == null || clazz.equals("")){
			return tag;
		}
		Name newName = ast.newName(clazz);
		tag.fragments().add(newName);
		return tag;
	}
	
}

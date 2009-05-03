package junit.extensions.eclipse.quick.javadoc.ui.handlers;

import junit.extensions.eclipse.quick.javadoc.TestContextTagCreator;
import junit.extensions.eclipse.quick.javadoc.ui.JavaDocUIActivator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.*;

/**
 * @TestContext junit.extensions.eclipse.quick.javadoc.TestContextTagCreator
 */
public class CreateTestContextTagHandler extends AbstractJavaDocHandler {

	public CreateTestContextTagHandler() {
	}

	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		try {
			ICompilationUnit compilationUnit = getCompilationUnitOfJavaEditor();
			TestContextTagCreator creater = new TestContextTagCreator();
			IJavaElement element = getElementOfCurrentCursor();
			String clazz = "";
			if(element instanceof IType){
				IType type = (IType)element;					
				clazz = type.getFullyQualifiedName();
			}else if(element instanceof IMember){
				IMember member = (IMember) element;
				IType type = member.getDeclaringType();
				clazz = type.getFullyQualifiedName();
			}else if(element != null){
				clazz = element.getPrimaryElement().getElementName();
			}
			creater.addTag(compilationUnit.findPrimaryType(), clazz);
		} catch (Exception e) {
			JavaDocUIActivator.getDefault().handleSystemError(e, this);
		}
		return null;
	}
}

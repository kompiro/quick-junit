package junit.extensions.eclipse.quick.javadoc.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateTestContextTagHandler extends AbstractJavaDocHandler {

	public CreateTestContextTagHandler() {
	}

	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		try {
			ICompilationUnit compilationUnit = getCompilationUnitOfJavaEditor();
		} catch (JavaModelException e) {
		}
//		String id = "org.eclipse.jdt.ui.actions.AddJavaDocComment";
//		IAction handler = getTextEdtior().getEditorSite().getActionBars().getGlobalActionHandler(id);
//		handler.run();
		return null;
	}
}

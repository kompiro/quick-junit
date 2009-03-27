package junit.extensions.eclipse.quick.javadoc.ui.handlers;

import junit.extensions.eclipse.quick.javadoc.ui.JavaDocUIActivator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;

public class CreateSeeTagHandler extends AbstractJavaDocHandler {

	public CreateSeeTagHandler() {
	}

	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		try {
			IJavaElement elementOnCursor = getElementOfCurrentCursor();
			IJavaElement elementOfJavaEditor = getElementOfJavaEditor();
			if(elementOfJavaEditor instanceof IMethod || elementOfJavaEditor instanceof IType){
				IMember member = (IMember) elementOfJavaEditor;
			}
//			MessageDialog.openInformation(
//					getWorkbenchWindow().getShell(),
//					"Quick JUnit JavaDoc UI Plug-in",
//			"element:" + elementOnCursor);
		} catch (JavaModelException e) {
			JavaDocUIActivator.getDefault().handleSystemError(e, this);
		}
		return null;
	}

}

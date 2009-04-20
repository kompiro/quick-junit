package junit.extensions.eclipse.quick.javadoc.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractJavaDocHandler extends AbstractHandler {

	private ITextEditor textEditor;
	private IWorkbenchWindow window;

	public final Object execute(ExecutionEvent event) throws ExecutionException {
		window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		textEditor = (ITextEditor)HandlerUtil.getActiveEditor(event);
		return doExecute(event);
	}
	
	protected abstract Object doExecute(ExecutionEvent event) throws ExecutionException;
	
	protected ITextEditor getTextEdtior(){
		return textEditor;
	}
	
	protected IWorkbenchWindow getWorkbenchWindow(){
		return window;
	}
	
    protected IJavaElement getElementOfJavaEditor() throws JavaModelException {
        ICompilationUnit unit = getCompilationUnitOfJavaEditor();
        if (unit == null)
            return null;
        ITextSelection text = getTextSelectionOfJavaEditor();
		int offset = text.getOffset();
        IJavaElement element = unit.getElementAt(offset);
        return element;
    }
    
    protected IJavaElement getElementOfCurrentCursor() throws JavaModelException{
        ICompilationUnit unit = getCompilationUnitOfJavaEditor();
        if (unit == null)
            return null;
        ITextSelection text = getTextSelectionOfJavaEditor();
		int offset = text.getOffset();
		int length = text.getLength();
        IJavaElement[] elements = unit.codeSelect(offset, length);
        return elements[0];
    }
    
    private ITextSelection getTextSelectionOfJavaEditor() throws JavaModelException {
        ISelectionProvider provider = getTextEdtior().getSelectionProvider();
        ISelection selection = provider.getSelection();
        if (!(selection instanceof ITextSelection))
            return null;
        return (ITextSelection) selection;
    }
        
    protected ICompilationUnit getCompilationUnitOfJavaEditor() throws JavaModelException {
        ITextEditor textEdtior = getTextEdtior();
		if (textEdtior == null)
            return null;
        IEditorInput input = textEdtior.getEditorInput();
        IJavaElement element = (IJavaElement) input.getAdapter(IJavaElement.class);
        if (element instanceof ICompilationUnit)
            return (ICompilationUnit) element;
        return null;
    }



}

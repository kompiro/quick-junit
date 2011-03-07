package junit.extensions.eclipse.quick;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class ITypeMockBuilder {
	
	private IType result = mock(IType.class);
	private int flags;
	private List<IMethod> methods = new ArrayList<IMethod>();
	private StringBuilder source = new StringBuilder();
	
	{
		ITypeHierarchy typeHierarchy = mock(ITypeHierarchy.class);
		when(typeHierarchy.getAllInterfaces()).thenReturn(new IType[]{});
		try {
			when(result.newSupertypeHierarchy((IProgressMonitor)any())).thenReturn(typeHierarchy);
			when(result.isClass()).thenReturn(true);
			when(result.getMethods()).thenReturn(new IMethod[]{});
		} catch (JavaModelException e) {
		}

	}

	public IType build(){
		return result;
	}
	
	public ITypeMockBuilder setPublic() {
		flags |= Flags.AccPublic;
		try {
			when(result.getFlags()).thenReturn(flags);
		} catch (JavaModelException e) {
		}
		return this;
	}

	public ITypeMockBuilder normal_class() {
		setPublic();
		return this;
	}

	public ITypeMockBuilder addMethod(IMethod method) {
		try {
			methods.add(method);
			IMethod[] methodsArray = (IMethod[]) methods.toArray(new IMethod[]{});
			when(result.getMethods()).thenReturn(methodsArray);
			when(method.getDeclaringType()).thenReturn(result);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public ITypeMockBuilder junit3_class() {
		setPublic();
		ITypeHierarchy typeHierarchy = mock(ITypeHierarchy.class);
		IType test = mock(IType.class);
		when(test.getFullyQualifiedName()).thenReturn(JavaTypes.TEST_INTERFACE_NAME);
		when(test.getFullyQualifiedName(anyChar())).thenReturn(JavaTypes.TEST_INTERFACE_NAME);
		when(typeHierarchy.getAllInterfaces()).thenReturn(new IType[]{test });
		try {
			when(result.newSupertypeHierarchy((IProgressMonitor)any())).thenReturn(typeHierarchy);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		return this;
	}

	public ITypeMockBuilder setRunWith(String clazz) {
		try {
			source.append("@RunWith(" + clazz + ")");
			when(result.getSource()).thenReturn(source.toString());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ITypeMockBuilder setSuiteClasses() {
		try {
			source.append("@SuiteClasses");
			when(result.getSource()).thenReturn(source.toString());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ITypeMockBuilder junit4_suite() {
		setPublic();
		setSuiteClasses();
		setRunWith("Suite.class");
		return this;
	}
	
	
	
}

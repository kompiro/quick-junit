package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import static org.mockito.Mockito.*;

public class IMethodMockBuilder {

	private IMethod element = mock(IMethod.class);

	public IMethod build() {
		return element;
	}
	
	public IMethodMockBuilder setPublic(){
		try {
			when(element.getFlags()).thenReturn(Flags.AccPublic);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder returnVoid(){
		try {
			when(element.getReturnType()).thenReturn("V");
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder normal() {
		setPublic();
		returnVoid();
		setMethodName("should_normal");
		return this;
	}

	public IMethodMockBuilder setMethodName(String name) {
		when(element.getElementName()).thenReturn(name);
		return this;
	}

	public IMethodMockBuilder addTestAnnotation() {
		try {
			when(element.getSource()).thenReturn("@Test public void should_normal(){\n\n}");
		} catch (JavaModelException e) {
		}
		return this;
	}

	public IMethodMockBuilder setNumberOfParameters(int i) {
		when(element.getNumberOfParameters()).thenReturn(i);
		return this;
	}

	public IMethodMockBuilder setPrivate() {
		try {
			when(element.getFlags()).thenReturn(Flags.AccPrivate);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder setProtected() {
		try {
			when(element.getFlags()).thenReturn(Flags.AccProtected);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder setStatic() {
		// TODO Auto-generated method stub
		return null;
	}

}

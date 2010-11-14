package junit.extensions.eclipse.quick;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import static org.mockito.Mockito.*;

public class IMethodMockBuilder {

	private IMethod element = mock(IMethod.class);
	private int flags;

	public IMethodMockBuilder returnVoid(){
		try {
			when(element.getReturnType()).thenReturn("V");
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder normal_test3() {
		setPublic();
		returnVoid();
		setMethodName("test_normal");
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

	public IMethodMockBuilder setPublic(){
		flags |= Flags.AccPublic;
		try {
			when(element.getFlags()).thenReturn(flags);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder setPrivate() {
		flags |= Flags.AccPrivate;
		try {
			when(element.getFlags()).thenReturn(Flags.AccPrivate);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder setProtected() {
		flags ^= Flags.AccPublic;
		flags |= Flags.AccProtected;
		try {
			when(element.getFlags()).thenReturn(flags);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethodMockBuilder setStatic() {
		try {
			flags |= Flags.AccStatic;
			when(element.getFlags()).thenReturn(flags);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return this;
	}

	public IMethod build() {
		return element;
	}

}

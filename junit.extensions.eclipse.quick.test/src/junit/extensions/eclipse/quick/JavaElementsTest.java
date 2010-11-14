package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * This test class shows JavaElements specification.
 */
public class JavaElementsTest {
	
	private IMethodMockBuilder methodBuilder;
	private ITypeMockBuilder typeBuilder;
	
	@Before
	public void before() throws Exception {
		methodBuilder = new IMethodMockBuilder();
		typeBuilder = new ITypeMockBuilder();
	}

	@Test
	public void recognition_junit4_test_method() throws Exception {
		
		IMethod element = methodBuilder.junit4_method().build();	
		assertThat(JavaElements.isTestMethod(element),is(true));
		
	}
	
	@Test
	public void recognition_junit3_test_method() throws Exception {
		
		IMethod element = methodBuilder.junit3_method().build();	
		assertThat(JavaElements.isTestMethod(element),is(true));
		
	}

	@Test
	public void test_method_should_has_no_args() throws Exception {
		
		IMethod element = methodBuilder.setNumberOfParameters(1).build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
	}
	
	@Test
	public void test_method_should_public_method() throws Exception {
		
		IMethod element = methodBuilder.returnVoid().build(); // package private
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.junit3_method().setPrivate().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.junit3_method().setProtected().build();
		assertThat(JavaElements.isTestMethod(element),is(false));

	}

	@Test
	public void test_method_should_not_static_method() throws Exception {
		
		IMethod element = methodBuilder.junit3_method().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.junit3_method().setPrivate().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.junit3_method().setPrivate().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
	}

	
	@Test
	public void should_return_null_when_empty_class() throws Exception {
		IType element = typeBuilder.normal_class().build();
		assertThat(JavaElements.getTestMethodOrClass(element),is(nullValue()));
	}
	
	@Test
	public void should_return_class_when_junit4_class_is_selected() throws Exception {
		
		IMethod method = methodBuilder.junit4_method().build();
		IType element = typeBuilder.normal_class().addMethod(method).build();
		assertThat(JavaElements.getTestMethodOrClass(element),is(notNullValue()));
		assertThat((IType)JavaElements.getTestMethodOrClass(element),is(element));
		
	}

	@Test
	public void should_return_class_when_junit3_class_is_selected() throws Exception {
		
		IMethod method = methodBuilder.junit3_method().build();
		IType element = typeBuilder.junit3_class().addMethod(method).build();
		assertThat(JavaElements.getTestMethodOrClass(element),is(notNullValue()));
		assertThat((IType)JavaElements.getTestMethodOrClass(element),is(element));
		
	}

	@Ignore
	@Test
	public void parameterized_test_should_return_class() throws Exception {
		
		ITypeHierarchy typeHierarchy = mock(ITypeHierarchy.class);
		when(typeHierarchy.getAllInterfaces()).thenReturn(new IType[]{});

		IType element = mock(IType.class);
		when(element.isClass()).thenReturn(true);
		when(element.getFlags()).thenReturn(Flags.AccPublic);
		when(element.newSupertypeHierarchy(null)).thenReturn(typeHierarchy);
		when(element.getMethods()).thenReturn(new IMethod[]{});
		
		assertThat(JavaElements.getTestMethodOrClass(element) == element,is(true));
		
	}

}

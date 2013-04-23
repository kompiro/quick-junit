package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.junit.Test;

public class JavaTypesTest {
	
	@Test
	public void not_test_class_should_return_false() throws Exception {
		
		IType result = new ITypeMockBuilder().build();
		assertThat(JavaTypes.isTest(result),is(false));
		
	}
	
	@Test
	public void junit3_class_should_return_true() throws Exception {
		
		IType result = new ITypeMockBuilder().junit3_class().build();
		assertThat(JavaTypes.isTest(result),is(true));
		
	}
	
	@Test
	public void junit4_class_should_return_true() throws Exception {
		
		IMethod method = new IMethodMockBuilder().addTestAnnotation().build();
		IType result = new ITypeMockBuilder().addMethod(method ).build();
		assertThat(JavaTypes.isTest(result),is(true));
		
	}

}

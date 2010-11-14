package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;

public class IMethodMockBuilderTest {
	
	private IMethodMockBuilder builder;

	@Before
	public void before() throws Exception {
		
		builder = new IMethodMockBuilder();
	}

	@Test
	public void should_build_mock_object() throws Exception {
		
		IMethod result = builder.build();
		assertThat(result, is(instanceOf(IMethod.class)));
		
	}
	
	@Test
	public void normal_method_should_initialized() throws Exception {
		
		IMethod result = builder.normal_method().build();

		assertThat(result, is(instanceOf(IMethod.class)));
		assertThat(Flags.isPublic(result.getFlags()), is(true));
		assertThat(result.getReturnType(), is("V"));
		assertThat(result.getElementName(), is("normal"));
		
	}
	
	@Test
	public void should_change_by_set_name() throws Exception {
		
		IMethod result = builder.normal_method().setName("shouldChanged").build();
		assertThat(result.getElementName(), is("shouldChanged"));
		
	}
	
	@Test
	public void should_by_add_test_annotation() throws Exception {
		
		IMethod result = builder.normal_method().addTestAnnotation().build();
		
		hasTestAnnotation(result);
		
	}

	private void hasTestAnnotation(IMethod result) throws JavaModelException {
		assertThat(result.getSource().indexOf("@Test"),is(not(-1)));
	}
	
	
	
	@Test
	public void should_set_number_of_parameters() throws Exception {
		
		IMethod result = builder.normal_method().setNumberOfParameters(2).build();
		assertThat(result.getNumberOfParameters(),is(2));
		
	}
	
	@Test
	public void should_set_private_mode() throws Exception {
		
		IMethod result = builder.normal_method().setPrivate().build();
		assertThat(Flags.isPrivate(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(false));

	}

	@Test
	public void should_set_protcted_mode() throws Exception {
		
		IMethod result = builder.normal_method().setProtected().build();
		assertThat(Flags.isProtected(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(false));

	}
	
	@Test
	public void should_set_static_mode() throws Exception {
		
		IMethod result = builder.normal_method().setStatic().build();
		assertThat(Flags.isStatic(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(true));

	}
	
	@Test
	public void should_combinate_to_set_mode() throws Exception {
		
		IMethod result = builder.setPublic().setStatic().build();
		assertThat(Flags.isStatic(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(true));
		
		result = builder.setProtected().setStatic().build();
		assertThat(Flags.isStatic(result.getFlags()), is(true));
		assertThat(Flags.isProtected(result.getFlags()), is(true));

		result = builder.setPrivate().setStatic().build();
		assertThat(Flags.isStatic(result.getFlags()), is(true));
		assertThat(Flags.isPrivate(result.getFlags()), is(true));

	}
	
	@Test
	public void junit4_should_initialized_these_setting() throws Exception {
		
		IMethod result = builder.junit4_method().build();
		
		assertThat(result, is(instanceOf(IMethod.class)));
		assertThat(Flags.isPublic(result.getFlags()), is(true));
		assertThat(result.getReturnType(), is("V"));
		hasTestAnnotation(result);

	}

	@Test
	public void junit3_should_initialized_these_setting() throws Exception {
		
		IMethod result = builder.junit3_method().build();
		
		assertThat(result, is(instanceOf(IMethod.class)));
		assertThat(Flags.isPublic(result.getFlags()), is(true));
		assertThat(result.getReturnType(), is("V"));
		assertThat(result.getElementName().startsWith("test"),is(true));

	}

}

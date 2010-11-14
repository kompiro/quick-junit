package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
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
	public void should_initialized_by_normal() throws Exception {
		
		IMethod result = builder.normal_test3().build();
		assertThat(result, is(instanceOf(IMethod.class)));
		assertThat(Flags.isPublic(result.getFlags()), is(true));
		assertThat(result.getReturnType(), is("V"));
		assertThat(result.getElementName(), is("test_normal"));
		
	}
	
	@Test
	public void should_change_by_set_name() throws Exception {
		
		IMethod result = builder.normal_test3().setMethodName("testShouldChanged").build();
		assertThat(result.getElementName(), is("testShouldChanged"));
		
	}
	
	@Test
	public void should_by_add_test_annotation() throws Exception {
		
		IMethod result = builder.normal_test3().addTestAnnotation().build();
		
		assertThat(result.getSource().indexOf("@Test"),is(not(-1)));
		
	}
	
	@Test
	public void should_set_number_of_parameters() throws Exception {
		
		IMethod result = builder.normal_test3().setNumberOfParameters(2).build();
		assertThat(result.getNumberOfParameters(),is(2));
		
	}
	
	@Test
	public void should_set_private_mode() throws Exception {
		
		IMethod result = builder.normal_test3().setPrivate().build();
		assertThat(Flags.isPrivate(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(false));

	}

	@Test
	public void should_set_protcted_mode() throws Exception {
		
		IMethod result = builder.normal_test3().setProtected().build();
		assertThat(Flags.isProtected(result.getFlags()), is(true));
		assertThat(Flags.isPublic(result.getFlags()), is(false));

	}
	
	@Test
	public void should_set_static_mode() throws Exception {
		
		IMethod result = builder.normal_test3().setStatic().build();
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
	

}

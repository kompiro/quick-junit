package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.junit.Before;
import org.junit.Test;


public class ITypeMockBuilderTest {
	
	private ITypeMockBuilder builder;

	@Before
	public void before() throws Exception {
		builder = new ITypeMockBuilder();
	}
	
	@Test
	public void should_build_mock_object() throws Exception {
		
		IType build = builder.build();
		assertThat(build,is(notNullValue()));
		assertThat(build,is(instanceOf(IType.class)));
		
	}

	@Test
	public void should_return_public_accessor_object() throws Exception {
		
		IType result = builder.accPublic().build();
		assertThat(result.getFlags() & Flags.AccPublic, is(Flags.AccPublic));
		
	}
	
	@Test
	public void shold_return_super_hierarchy() throws Exception {

		IType result = builder.build();
		
		assertThat(result.newSupertypeHierarchy(new NullProgressMonitor()),is(notNullValue()));
		
	}

	@Test
	public void shold_return_methods() throws Exception {
		
		IType result = builder.build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(0));
		
	}

	@Test
	public void shold_initialized_by_call_normal() throws Exception {
		
		IType result = builder.normal().build();

		assertThat(result.isClass(),is(true));
		assertThat(result.getFlags() & Flags.AccPublic, is(Flags.AccPublic));
		assertThat(builder.build().getMethods(),is(instanceOf(IMethod[].class)));
		assertThat(builder.build().getMethods().length,is(0));
		assertThat(result.newSupertypeHierarchy(new NullProgressMonitor()),is(notNullValue()));
		
	}
	
	@Test
	public void shoud_add_method() throws Exception {
		
		IMethod method = new IMethodMockBuilder().build();
		IType result = builder.normal().addMethod(method).build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(1));
		
	}
	
	@Test
	public void shoud_add_2_methods() throws Exception {
	
		IMethod method1 = new IMethodMockBuilder().build();
		IMethod method2 = new IMethodMockBuilder().build();
		IType result = builder.normal().addMethod(method1).addMethod(method2).build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(2));
	}
	
}

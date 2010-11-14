package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
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
		
		IType result = builder.setPublic().build();
		assertThat(result.getFlags() & Flags.AccPublic, is(Flags.AccPublic));
		
	}
	
	@Test
	public void super_hierarchy_should_return_null_at_initialized() throws Exception {

		IType result = builder.build();
		
		assertThat(result.newSupertypeHierarchy(new NullProgressMonitor()),is(notNullValue()));
		
	}

	@Test
	public void get_methods_should_return_no_methods_at_initialized() throws Exception {
		
		IType result = builder.build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(0));
		
	}

	@Test
	public void normal_should_initialized() throws Exception {
		
		IType result = builder.normal_class().build();

		assertThat(result.isClass(),is(true));
		assertThat(result.getFlags() & Flags.AccPublic, is(Flags.AccPublic));
		assertThat(builder.build().getMethods(),is(instanceOf(IMethod[].class)));
		assertThat(builder.build().getMethods().length,is(0));
		assertThat(result.newSupertypeHierarchy(new NullProgressMonitor()),is(notNullValue()));
		
	}
	
	@Test
	public void add_method_should_be_enabled() throws Exception {
		
		IMethod method = new IMethodMockBuilder().build();
		IType result = builder.normal_class().addMethod(method).build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(1));
		
	}
	
	@Test
	public void should_add_2_methods() throws Exception {
	
		IMethod method1 = new IMethodMockBuilder().build();
		IMethod method2 = new IMethodMockBuilder().build();
		IType result = builder.normal_class().addMethod(method1).addMethod(method2).build();
		
		IMethod[] methods = result.getMethods();
		assertThat(methods,is(instanceOf(IMethod[].class)));
		assertThat(methods.length,is(2));
		
	}
	
	@Test
	public void junit3_class_should_extends_junit_framework_Test() throws Exception {
		
		IType result = builder.junit3_class().build();
		ITypeHierarchy hierarchy = result.newSupertypeHierarchy(new NullProgressMonitor());
		IType[] interfaces = hierarchy.getAllInterfaces();
		for (IType type : interfaces) {
			if(type.getFullyQualifiedName().equals(JavaTypes.TEST_INTERFACE_NAME)){
				return;
			}
		}
		fail("junit3 should extend org.junit.Test");
	}
	
	@Test
	public void junit3_class_should_be_public() throws Exception {
		
		IType result = builder.junit3_class().build();
		assertThat(Flags.isPublic(result.getFlags()),is(true));
		
	}
	
}

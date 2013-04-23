package junit.extensions.eclipse.quick.javadoc;

import static junit.extensions.eclipse.quick.javadoc.CreateTestProjectUtil.*;
import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.contribution.junit.javadoc.test.TestProject;
import org.eclipse.jdt.core.IType;
import org.junit.*;

public class TestContextTagCreaterTest {
	
	private TestProject project;
	private IType type;
	private TestContextTagCreater creator;
	private final String TEST_CONTEXT_ANNOTATION = QuickJUnitDocTagConstants.TestContext.toAnnotation();

	@Before
	public void setup() throws Exception{
		project = createTestProject();
		assertNotNull(project);
		String packageName = "testcontext";
		createPackageFragment(project, packageName);
		type = createType(project, packageName, "Target", 
				"class Target{}");
		assertNotNull(type);
		creator = new TestContextTagCreater();
	}
	
	@After
	public void teardown() throws Exception{
		project.dispose();
	}
	
	@Test
	public void createTestContextTag() throws Exception {
		creator.addTag(type,"test");
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);
	}

	@Test
	public void createTestContextTagFQCNClassName() throws Exception {
		creator.addTag(type,"test.Test");
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);
	}

	
	@Test
	public void assertIllegalCases() throws Exception{
		assertCreateTestContextTagTypeNull();
		assertCreateTestContextTagEmptyString();
	}
	
	private void assertCreateTestContextTagTypeNull() throws Exception{
		try{
			creator.addTag(null,null);
			fail();
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}

	private void assertCreateTestContextTagEmptyString() throws Exception {
		creator.addTag(type,"");
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);		
	}

	@Test
	public void assertCreateTestContextTagNull() throws Exception {
		creator.addTag(type,null);
		String source = type.getSource();
		assertTrue(source.contains(TEST_CONTEXT_ANNOTATION));
		assertStringCount(1,TEST_CONTEXT_ANNOTATION,source);		
	}

	@Test
	public void createTestContextTwiceAndThreeTimes() throws Exception{
		creator.addTag(type,"test");
		String source = type.getSource();
		creator.addTag(type,"test2");
		source = type.getSource();
		assertStringCount(2,TEST_CONTEXT_ANNOTATION,source);
		
		creator.addTag(type,"test3");
		source = type.getSource();
		assertStringCount(3,TEST_CONTEXT_ANNOTATION,source);
	}
	
	private void assertStringCount(int count,String target,String source){
        Pattern pattern = Pattern.compile(Pattern.quote(target));
        Matcher matcher = pattern.matcher(source);
        int hitCount = 0;
        while (matcher.find()) {
            hitCount++;
        }
        assertEquals(count,hitCount);		
	}
	
}

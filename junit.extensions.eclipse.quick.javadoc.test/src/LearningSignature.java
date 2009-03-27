import org.eclipse.jdt.core.Signature;
import org.junit.Test;


public class LearningSignature {
	@Test
	public void learningSigunature() throws Exception {
		String sig =Signature.createTypeSignature("void", true);
		System.out.println(sig);
		sig = Signature.createTypeSignature("String", true);
		System.out.println(sig);
		sig = Signature.createMethodSignature(new String[]{}, "void");
		System.out.println(sig);
		System.out.println(Signature.getParameterTypes(sig));
	}

}

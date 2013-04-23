package junit.extensions.eclipse.quick.javadoc;

public enum QuickJUnitDocTagConstants {
	TestContext;

	public String toAnnotation(){
		return "@" + name();
	}
}

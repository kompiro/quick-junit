package junit.extensions.eclipse.quick.notifications.internal;


import junit.extensions.eclipse.quick.notifications.ImageDesc;
import junit.extensions.eclipse.quick.notifications.internal.preference.Preference;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.mylyn.commons.ui.notifications.AbstractNotification;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
class JUnitNotification extends	AbstractNotification {
	private static final TemplateParser parser = new TemplateParser();
	private String resultLabel;
	private Image kind;
	private Result testResult;
	private String description;
	private String label;
	private static final String TEST_OK = "Test OK";
	private static final String TEST_FAILURE = "Test FAILED";
	private static final String TEST_ERROR = "Test ERROR";

	JUnitNotification(String eventId,
			Result testResult, ITestRunSession session) {
		super(eventId);
		String template = Preference.TEMPLATE.getValue();
		parser.setTemplate(template);
		this.testResult = testResult;
		if(Result.ERROR.equals(testResult)){
			resultLabel = TEST_ERROR;
			kind = ImageDesc.ERROR.getImage();
		}else if(Result.FAILURE.equals(testResult)){
			resultLabel = TEST_FAILURE;
			kind = ImageDesc.FAILURE.getImage();
		}else{
			resultLabel = TEST_OK;
			kind = ImageDesc.OK.getImage();
		}
		this.label = pickupTestClassAndMethod(session.getTestRunName());
		this.description = parser.parseTemplate(session);
	}

	String pickupTestClassAndMethod(String testName){
		if(testName == null) return null;
		if(testName.indexOf('.') != 0){
			String[] split = testName.split("\\."); //$NON-NLS-1$
			if(split.length > 2){
				return split[split.length - 2] + "." + split[split.length - 1]; //$NON-NLS-1$
			}
		}
		return testName;
	}


	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if(adapter == null) return null;
		if(adapter.equals(Result.class)){
			return testResult;
		}
		return null;
	}

	public int compareTo(AbstractNotification o) {
		return 0;
	}

	@Override
	public void open() {
	}

	@Override
	public Image getNotificationKindImage() {
		return kind;
	}

	@Override
	public Image getNotificationImage() {
		return ImageDesc.ICON.getImage();
	}
	
	public String getResultLabel(){
		return resultLabel;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
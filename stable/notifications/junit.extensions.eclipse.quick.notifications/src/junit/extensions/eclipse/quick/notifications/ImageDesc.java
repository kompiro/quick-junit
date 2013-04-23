package junit.extensions.eclipse.quick.notifications;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public enum ImageDesc {
	
	ERROR("/icons/tsuiteerror.gif"),
	FAILURE("/icons/tsuitefail.gif"),
	OK("/icons/tsuiteok.gif"),
	ICON("/icons/quickjunit.png");
	private String imageFilePath;
	ImageDesc(String imageFilePath){
		this.imageFilePath = imageFilePath;
	}
	public ImageDescriptor getIamgeDescriptor(){
		return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imageFilePath);
	}
	
	public Image getImage() {
		return Activator.getDefault().getImageRegistry().get(name());
	}

}

package junit.extensions.eclipse.quick.args;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class ExtensionSupport {

    public static ILaunchConfigurationWorkingCopy createLaunchConfigurationWorkingCopy(IJavaProject project) throws CoreException{
    	return createWorkingCopy("org.eclipse.jdt.junit.launchconfig",project);
    }
    
    public static ILaunchConfiguration getLaunchConfiguration(IJavaProject project) throws CoreException{
    	return getWorkingCopy("org.eclipse.jdt.junit.launchconfig",project);
    }

    protected static ILaunchShortcut createLaunchShortcut(final String namespace)
            throws CoreException {
        final IExtensionRegistry reg = Platform.getExtensionRegistry();
        final IExtensionPoint point = reg.getExtensionPoint("org.eclipse.debug.ui.launchShortcuts"); //$NON-NLS-1$
        final IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; ++i) {
            if (namespace.equals(extensions[i].getNamespaceIdentifier())) {
                final IConfigurationElement[] elements = extensions[i].getConfigurationElements();
                ILaunchShortcut shortcut = (ILaunchShortcut) elements[0]
                        .createExecutableExtension("class"); //$NON-NLS-1$
                if (shortcut != null) {
                    return shortcut;
                }
            }
        }
        throw new RuntimeException("LaunchShortcut not found. namespace:" + namespace); //$NON-NLS-1$
    }

    private static ILaunchConfigurationWorkingCopy createWorkingCopy(final String namespace, IJavaProject project)throws CoreException {
    	DebugPlugin debugPlugin = DebugPlugin.getDefault();
		ILaunchManager launchManager = debugPlugin.getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType(namespace);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null, defaultName(project));
    	if(launchConfiguration == null){
    		throw new RuntimeException("LaunchConfigurationTypes not found. namespace:" + namespace); //$NON-NLS-1$
    	}
    	launchConfiguration.setContainer(project.getProject());
    	launchConfiguration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());
    	launchConfiguration.setAttribute("org.eclipse.jdt.junit.CONTAINER", "=" + project.getElementName());
		return launchConfiguration;
	}

	private static String defaultName(IJavaProject project) {
		return project.getElementName() + ".default";
	}
    
    private static ILaunchConfiguration getWorkingCopy(String namespace, IJavaProject project) throws CoreException {
    	ILaunchConfigurationType type = createWorkingCopy(namespace,project).getType();
		ILaunchConfiguration[] configurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(type);
		ILaunchConfiguration launchConfiguration = null;
		for (int i = 0; i < configurations.length; i++){
			ILaunchConfiguration candidate = configurations[i];
			if(candidate.getName().equals(defaultName(project))){
				return candidate;
			}
		}
		return launchConfiguration;
	}

}

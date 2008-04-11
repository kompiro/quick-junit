package junit.extensions.eclipse.quick;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

public class ExtensionSupport {

    public static ILaunchShortcut createJUnitLaunchShortcut() throws CoreException {
        return createLaunchShortcut("org.eclipse.jdt.junit"); //$NON-NLS-1$
    }

    public static IWorkbenchWizard createNewClassCreationWizard() throws CoreException {
        return createWizard("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
    }

    public static IWorkbenchWizard createNewTestCaseCreationWizard() throws CoreException {
        return createWizard("org.eclipse.jdt.junit.wizards.NewTestCaseCreationWizard"); //$NON-NLS-1$
    }

    protected static ILaunchShortcut createLaunchShortcut(final String namespace)
            throws CoreException {
        final IExtensionRegistry reg = Platform.getExtensionRegistry();
        final IExtensionPoint point = reg.getExtensionPoint("org.eclipse.debug.ui.launchShortcuts"); //$NON-NLS-1$
        final IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; ++i) {
            // TODO 3.2API 
            // if (namespace.equals(extensions[i].getNamespaceIdentifier())) {
            if (namespace.equals(extensions[i].getNamespace())) {
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

    protected static IWorkbenchWizard createWizard(final String id) throws CoreException {
        final IWorkbench wb = PlatformUI.getWorkbench();
        final IWizardRegistry reg = wb.getNewWizardRegistry();
        final IWizardDescriptor desc = reg.findWizard(id);
        IWorkbenchWizard wizard = desc.createWizard();
        if (wizard != null) {
            return wizard;
        }
        throw new RuntimeException("Wizard not found. id:" + id); //$NON-NLS-1$
    }
}

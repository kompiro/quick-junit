package junit.extensions.eclipse.quick.resources;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.extensions.eclipse.quick.JavaElements;
import junit.extensions.eclipse.quick.TestingPair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.team.IMoveDeleteHook;
import org.eclipse.core.resources.team.IResourceTree;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class MoveDeleteHookForTestingPair implements IMoveDeleteHook {

	public MoveDeleteHookForTestingPair() {
	}

	public boolean deleteFile(IResourceTree tree, IFile file, int updateFlags,
			IProgressMonitor monitor) {
		return false;
	}

	public boolean deleteFolder(IResourceTree tree, IFolder folder,
			int updateFlags, IProgressMonitor monitor) {
		return false;
	}

	public boolean deleteProject(IResourceTree tree, IProject project,
			int updateFlags, IProgressMonitor monitor) {
		return false;
	}

	public boolean moveFile(IResourceTree tree, IFile source,
			IFile destination, int updateFlags, IProgressMonitor monitor) {
		IJavaElement element = JavaCore.create(source);
		if(element == null) return false;
		IType sourceType = JavaElements.getPrimaryTypeOf(element);
		if(sourceType == null) return false;
		TestingPair pair = new TestingPair();
        String[] pairNames = pair.getPairClassNames(sourceType.getFullyQualifiedName());
        List<IType> pairTypes;
		try {
			pairTypes = findPairTypes(pairNames);
			if (pairTypes.isEmpty()) {
				return false;
			}
			IJavaElement destElement = JavaCore.create(destination);
			return true;
		} catch (JavaModelException e) {
			return false;
		}
	}
	
    private List<IType> findPairTypes(String[] pairNames) throws JavaModelException {
        IJavaProject[] projects = getJavaProjects();
        Set<IType> result = new LinkedHashSet<IType>();
        for (int i = 0; i < projects.length; ++i) {
            IJavaProject project = projects[i];
            for (int j = 0; j < pairNames.length; ++j) {
                IType pairType= project.findType(pairNames[j]);
                if (pairType != null) {
                    result.add(pairType);
                }
            }
        }
        return new ArrayList<IType>(result);
    }
    
    protected IJavaProject[] getJavaProjects() throws JavaModelException {
        return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
    }


	public boolean moveFolder(IResourceTree tree, IFolder source,
			IFolder destination, int updateFlags, IProgressMonitor monitor) {
		return false;
	}

	public boolean moveProject(IResourceTree tree, IProject source,
			IProjectDescription description, int updateFlags,
			IProgressMonitor monitor) {
		return false;
	}

}

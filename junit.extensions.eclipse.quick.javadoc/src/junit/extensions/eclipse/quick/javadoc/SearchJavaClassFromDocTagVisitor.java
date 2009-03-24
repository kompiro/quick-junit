package junit.extensions.eclipse.quick.javadoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.extensions.eclipse.quick.javadoc.internal.JavaDocActivator;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class SearchJavaClassFromDocTagVisitor extends ASTVisitor {
	
	private static final String METHOD_PREFIX = "#";
	private SearchRequestor requestor;
	private TypeDeclaration type = null;
	private IType targetType;

	public SearchJavaClassFromDocTagVisitor(IType targetType, SearchRequestor requestor) {
		super(true);
		this.targetType = targetType;
		this.requestor = requestor;
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		if(node.getName().getIdentifier().equals("MISSING")) return true;
		this.type = node;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Javadoc node) {
		if(type != null && node.getParent() != type) return false;
		return super.visit(node);
	}

	public boolean visit(TagElement node) {
		if(TagElement.TAG_SEE.equals(node.getTagName())){
			for(Object obj:node.fragments()){
				String patternString = obj.toString();
				if(patternString.trim().equals("")) continue;
				if(patternString.contains(METHOD_PREFIX)){
					if(patternString.startsWith(METHOD_PREFIX)){
						acceptMethodInCurrentType(patternString);
						continue;
					}
					acceptMethodPattern(patternString);
				}else{
					acceptClassPattern(patternString);
				}
			}
		}
		return true;
	}

	private void acceptClassPattern(String patternString) {
		SearchPattern pattern = SearchPattern.createPattern(patternString, IJavaSearchConstants.CLASS_AND_INTERFACE, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_EQUIVALENT_MATCH);
		search(pattern);
	}

	private void acceptMethodPattern(String patternString) {
		SearchPattern pattern = createMethodPattern(patternString);
		search(pattern);
	}

	private void search(SearchPattern pattern) {
		try {
			SearchEngine engine = new SearchEngine();
			IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			SearchParticipant[] participants = new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()};
			engine.search(pattern, participants, scope, requestor, new NullProgressMonitor());
		} catch (CoreException e) {
			JavaDocActivator.getDefault().handleSystemError(e, this);
		}
	}

	private SearchPattern createMethodPattern(String patternString) {
		SearchPattern pattern;
		patternString = patternString.replace(METHOD_PREFIX, ".");
		pattern= SearchPattern.createPattern(patternString, IJavaSearchConstants.METHOD, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_FULL_MATCH | SearchPattern.R_EQUIVALENT_MATCH);
		return pattern;
	}

	private void acceptMethodInCurrentType(String patternString) {
		Pattern methodPattern = Pattern.compile("#(.*)\\((.*)\\)");
		Matcher matcher = methodPattern.matcher(patternString);
		if( matcher.matches() == false) return;
		String name = matcher.group(1);
		String paramString = matcher.group(2);
		String[] params = paramString == null || paramString.equals("") ? null : matcher.group(2).split(",");
		IMethod element = targetType.getMethod(name, params);
		try {
			if(element.exists() == false) return;
			ISourceRange range = element.getSourceRange();
			SearchMatch match = new SearchMatch(element, SearchMatch.A_ACCURATE, range.getOffset(), range.getLength(), null, null);
			requestor.acceptSearchMatch(match);
		} catch (CoreException e) {
			JavaDocActivator.getDefault().handleSystemError(e, this);
		}
	}
}

package junit.extensions.eclipse.quick.process.internal;

public enum ProcessKey {
	DETAIL("detail",		Messages.ProcessKey_DETAIL_DESCRIPTION), //$NON-NLS-1$
	SUMMARY	("summary",		Messages.ProcessKey_SUMMARY_DESCRIPTION); //$NON-NLS-1$

	private String key;
	private String description;

	ProcessKey(String key,String description){
		this.key = key;
		this.description = description;
	}
	
	public String regexKey(){
		return String.format("\\$\\{%s\\}",key);//$NON-NLS-1$
	}
	
	public String key(){
		return String.format("${%s}",key);//$NON-NLS-1$
	}
	
	public String descrpition(){
		return description;
	}
}

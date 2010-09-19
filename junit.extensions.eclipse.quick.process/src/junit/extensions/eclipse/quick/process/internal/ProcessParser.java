package junit.extensions.eclipse.quick.process.internal;

import java.util.ArrayList;

public class ProcessParser {
	private static final String START_DOUBLE_QUOTE = "\"";
	private static final String END_DOUBLE_QUOTE = ".*[^\\\\]\"$";
	private static final String SPACE = " ";

	public String[] parse(String command){
		String[] parse = parse(command,null,null);
		return parse;
	}
	
	public String[] parse(String command, String summary, String detail){
		if(command == null) return new String[]{};
		command = replaceResult(command,summary,detail);
		String[] splited = command.split(SPACE);
		ArrayList<String> results = new ArrayList<String>();
		StringBuilder builder = null;
		boolean appendMode = false;
		for (String item : splited) {
			if(appendMode){
				builder.append(SPACE);
				builder.append(item);
				if(item.matches(END_DOUBLE_QUOTE)){
					appendMode = false;
					String string = builder.toString();
					results.add(string.substring(0, string.length() - 1));
					builder = null;
				}
				continue;
			}
			if(item.startsWith(START_DOUBLE_QUOTE)){
				builder = new StringBuilder();
				builder.append(item.substring(1, item.length()));
				appendMode = true;
				continue;
			}
			results.add(item);			
		}
		if(appendMode){
			results.add(builder.toString());
		}
		return results.toArray(new String[]{});
	}
	
	private String replaceResult(String command, String summary, String detail) {
		command = command.replaceAll(key(ProcessKey.SUMMARY), quoted(summary));
		command = command.replaceAll(key(ProcessKey.DETAIL), quoted(detail));
		return command;
	}

	private String quoted(String target) {
		return "\""+ target + "\"";
	}

	private String key(ProcessKey key){
		return key.regexKey();
	}
}

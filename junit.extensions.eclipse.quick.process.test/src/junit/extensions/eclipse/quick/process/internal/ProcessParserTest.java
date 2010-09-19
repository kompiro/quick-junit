package junit.extensions.eclipse.quick.process.internal;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;



public class ProcessParserTest {
	@Test
	public void simple_command() throws Exception {
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("cd");
		assertArrayEquals(new String[]{"cd"}, parsed);
	}
	
	@Test
	public void null_string() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse(null);
		assertArrayEquals(new String[]{}, parsed);		
		
	}
	
	@Test
	public void empty_string() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("");
		assertArrayEquals(new String[]{""}, parsed);		
		
	}
	
	@Test
	public void single_arg() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("cd .");
		assertArrayEquals(new String[]{"cd","."}, parsed);

	}

	@Test
	public void multi_args() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("rm -R .");
		assertArrayEquals(new String[]{"rm","-R","."}, parsed);

	}

	@Test
	public void includes_double_quote_args() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("growlnotify -n \"Quick JUnit\"");
		assertArrayEquals(new String[]{"growlnotify","-n","Quick JUnit"}, parsed);

	}

	@Test
	public void includes_broken_double_quote_args() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("growlnotify -n \"Quick JUnit");
		assertArrayEquals(new String[]{"growlnotify","-n","Quick JUnit"}, parsed);

	}

	
	@Test
	public void includes_escaped_double_quote_args() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("growlnotify -n \"Quick JUnit\\\" -a\"");
		assertArrayEquals(new String[]{"growlnotify","-n","Quick JUnit\\\" -a"}, parsed);

	}

	@Test
	public void replaced_summary_and_detail() throws Exception {
		
		ProcessParser parsar = new ProcessParser();
		String[] parsed = parsar.parse("growlnotify -n \"Quick JUnit\" -m ${detail} ${summary}","test OK","pass 10:");
		assertArrayEquals(new String[]{"growlnotify","-n","Quick JUnit","-m","pass 10:","test OK"}, parsed);

	}

	
}

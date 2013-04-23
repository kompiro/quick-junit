import org.junit.Test;


public class Learning {
	@Test
	public void testname() throws Exception {
		ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/growlnotify","-n","Quick JUnit","Quick JUnit");
		builder.start();
	}
	
	@Test
	public void string() throws Exception {
		String com = "growlnotify -n \"Quick JUnit\" ";
		String[] split = com.split(" ");
		for (String string : split) {
			System.out.println(string);
		}
	}
}

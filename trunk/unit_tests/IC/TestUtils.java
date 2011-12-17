package IC;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class TestUtils {

	public static void executeMain(String[] args){
		IC.Compiler.main(args);
	}

	public static boolean runTestFile(String testFilename, String otherArgs){
		//set an expected boolean value to compare the test run
		boolean expected;
		if (testFilename.endsWith(".good")){
			expected = true;
		}
		else if (testFilename.endsWith(".bad")){
			expected = false;
		}
		else {
			return false;
		}
		
		
		try {
			// building the args String
			StringBuffer args = new StringBuffer();
			args.append(testFilename);
			if (!otherArgs.equals("")){
				args.append(" ");
				args.append(otherArgs);
			}
			executeMain(args.toString().split(" "));
			
			byte[] buffer = new byte[(int) new File("main.state").length()];
			BufferedInputStream f = new BufferedInputStream(new FileInputStream("main.state"));

			f.read(buffer);
			String state = new String(buffer);
			boolean ret = (state.startsWith("OK") == expected);
			return ret;

		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

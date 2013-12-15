package guiChat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ReadProp {

	static Properties prop = new Properties();

	public String propIP() throws FileNotFoundException, IOException{



		prop.load(new FileReader("res/prop.txt"));
		return prop.getProperty("ip");


		//		prop.setProperty("ip", "127.0.0.1");
		//		prop.setProperty("port", "50000");
		//		
		//		
		//		prop.store(new FileOutputStream("res/prop.txt"), null);


	}
	public int propPort() throws FileNotFoundException, IOException{



		prop.load(new FileReader("res/prop.txt"));
		return Integer.valueOf(prop.getProperty("port"));

	}
}
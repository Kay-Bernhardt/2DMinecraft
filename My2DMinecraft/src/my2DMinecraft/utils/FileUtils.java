package my2DMinecraft.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils
{
	private FileUtils(){} //static olny
	
	public static String loadAsString(String file)
	{
		StringBuilder result = new StringBuilder();
		try
		{
			InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(file);
			//InputStream is = FileUtils.class.getResourceAsStream(file);
			//BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String buffer = "";
			while((buffer = reader.readLine()) != null)
			{
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return result.toString();
	}
}

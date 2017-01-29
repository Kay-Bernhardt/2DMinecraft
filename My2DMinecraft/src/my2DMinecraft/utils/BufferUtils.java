package my2DMinecraft.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

//import org.lwjgl.BufferUtils;

public class BufferUtils
{
	private BufferUtils(){} //static only class. private so it can not be instantiated
	
	public static ByteBuffer createByteBuffer(byte[] array)
	{
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}
	
	public static FloatBuffer createFloatBuffer(float[] array)
	{
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();	// * 4 because float is 4 byte (same as <<2)
		result.put(array).flip();
		return result;
	}
	
	public static IntBuffer createIntBuffer(int[] array)
	{
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();	// * 4 because int is 4 byte (same as <<2)
		result.put(array).flip();
		return result;
	}
}

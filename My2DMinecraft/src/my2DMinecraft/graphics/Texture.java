package my2DMinecraft.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import my2DMinecraft.utils.BufferUtils;

public class Texture
{
	private int width, height;
	private int texture;
	
	public Texture(String path)
	{
		texture = load(path);
	}
	
	public Texture(int x, int y)
	{
		texture = load(x, y);
	}
	
	private int load(String path)
	{
		int[] pixels = null;		
		try
		{
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0,  0, width, height, pixels, 0, width);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //scale to nearest pixel instead of blending
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return result;
	}
	
	private int load(int x, int y)
	{
		int[] pixels = null;		
		try
		{
			final int BLOCK_SIZE = 32;
			width = BLOCK_SIZE;
			height = BLOCK_SIZE;
			
			if(y >= 100) //texture is 2 blocks tall
			{
				y-= 100;
				height = BLOCK_SIZE * 2;
			}
			
			BufferedImage image = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream("tilesheet.png"));
			int xStart = x * BLOCK_SIZE;
			int yStart = y * BLOCK_SIZE;
			
			pixels = new int[width * height];
			image.getRGB(xStart,  yStart, width, height, pixels, 0, width);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //scale to nearest pixel instead of blending
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return result;
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}

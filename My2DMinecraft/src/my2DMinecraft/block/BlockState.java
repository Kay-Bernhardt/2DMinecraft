package my2DMinecraft.block;

import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.math.Matrix4f;

public interface BlockState
{
	public void render(Matrix4f matrix);
	
	public void update();
	
	public BlockState setTexture(BlockTexture newTexture);
	
	public boolean containsTexture(BlockTexture otherTexture);
	
	public boolean isBackground();
	
	public BlockTexture getTexture();
}

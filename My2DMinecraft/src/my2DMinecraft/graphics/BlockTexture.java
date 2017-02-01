package my2DMinecraft.graphics;

public enum BlockTexture {
	
	GRASS	(0, 0, "s"), 
	DIRT	(1, 0, "s"),
	WOOD	(2, 0, "b"),
	STONE	(3, 0, "s"),
	SAND	(4, 0, "s"),
	GOLD	(5, 0, "s"),
	LEAF1	(6, 0, "f"),
	LEAF2	(7, 0, "f"),
	VINE	(8, 0, "f"),
	FLOWER(9, 0, "f"),
	
	LAVA_1 (0, 1, "3"),
	LAVA_2 (1, 1),
	LAVA_3 (2, 1),
	LAVA_4 (3, 1),
	LAVA_5 (4, 1),
	WATER_1(5, 1, "3"),
	WATER_2(6, 1),
	WATER_3(7, 1),
	WATER_4(8, 1),
	WATER_5(9, 1),
	
	POTION_RED	(0, 2, "f"),
	POTION_BLUE	(1, 2, "f"),
	CRYSTAL		(2, 2, "f"),
	CHEST_CLOSED(3, 2, "f"),
	CHEST_OPEN	(4, 2, "f"),
	CARROT		(5, 2, "f"),
	APPLE_GREEN	(6, 2, "f"),
	APPLE_RED	(7, 2, "f"),
	SIGN			(8, 2, "f"),
	
	BREAKING	(0, 5),
	
	AIR(9, 2);
	
	public Texture texture;
	public int steps;
	public boolean isBackground;
	public boolean isForeground;
	public boolean isSolid;
	
	private BlockTexture(int x, int y)
	{
		this.texture = new Texture(x, y);
		this.steps = 0;
	}
	
	private BlockTexture(int x, int y, String attributes)
	{
		this.texture = new Texture(x, y);
		
		char c;
		for(int i = 0; i < attributes.length(); i++)
		{
			c = attributes.charAt(i);
			if(Character.isDigit(c))
			{
				steps = Character.getNumericValue(c);
			}
			else
			{
				switch(c)
				{
					case 'b':
						isBackground = true;
						break;
					case 'f':
						isForeground = true;
						break;
					case 's':
						isSolid = true;
						break;
					default:
						System.out.println(c + " is not defined");	
				}
			}
		}
	}
	
	private BlockTexture(String path)
	{
		this.texture = new Texture(path);
	}
	
	public void bind()
	{
		texture.bind();
	}
	
	public void unbind()
	{
		texture.unbind();
	}
}

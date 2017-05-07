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
	HEMP			(9, 2, "f"),
	
	BREAKING_1				(0, 5, "3"),
	BREAKING_2				(1, 5),
	BREAKING_3				(2, 5),
	HOTBAR_1					(4, 5),
	HOTBAR_2					(5, 5),
	HOTBAR_3					(6, 5),
	HOTBAR_4					(7, 5),
	CRYSTAL_COUNT_LEFT	(8, 5),
	CRYSTAL_COUNT_RIGHT	(9, 5),
	
	HEART_FULL		(4, 6),
	HEART_EMPTY		(5, 6),
	HEART_BROKEN	(6, 6),
	HEALTH_FULL		(7, 6),
	HEALTH_EMPTY	(8, 6),
	HEALTH_HEART	(9, 6),
	
	HEMP_TOP		(0, 7, "f"),
	
	HEMP_BOTTOM		(0, 8, "f"),
	THORNY_PLANT	(1, 8, "f"),
	
	AIR				(9, 9);
	
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

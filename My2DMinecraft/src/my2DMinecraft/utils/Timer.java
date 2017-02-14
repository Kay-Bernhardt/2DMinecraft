package my2DMinecraft.utils;

public class Timer
{
	private long lastTime;
	private double delta;
	private double ns;
	
	private long timer;
	private int updates;
	private int frames;
	
	public Timer()
	{
		lastTime = System.nanoTime();
		delta = 0.0;
		ns = 1000000000.0 / 60.0;   // 1/60th of a second
		
		timer = System.currentTimeMillis();
		updates = 0;
		frames = 0;	
	}
	
	public void updateDelta()
	{
		long now = System.nanoTime();
		delta += (now - lastTime) / ns; //how many 1/60s it has been
		lastTime = now;
		/*
		if(delta >= 1.0)
		{
			updates++;
			delta--;
		}
		*/
	}
	
	public boolean shouldUpdate()
	{
		updateDelta();
		
		if(delta >= 1.0)
		{
			updates++;
			delta--;
			return true;
		}
		return false;
	}
	
	public void updateCounter()
	{
		if(System.currentTimeMillis() - timer > 1000) //one second
		{
			timer += 1000;
			Window.setTitle(updates + " ups, " + frames + " fps");
			System.out.println(updates + " ups, " + frames + " fps");
			frames = 0;
			updates = 0;
		}
	}
	
	public void addFrame()
	{
		frames++;
	}
}

/**
 * @(#)Vent.java
 *
 *
 * @author 
 * @version 1.00 2015/6/12
 */


public class Vent {
	private int x,y,width,height,dirX,dirY;
	private float vel;
    public Vent(String line) {
    	String [] data = line.split(",");
    	x = Integer.parseInt(data[0]);
		y = Integer.parseInt(data[1]);
		width = Integer.parseInt(data[2]);
		height = Integer.parseInt(data[3]);
		dirX = Integer.parseInt(data[4]);
		dirY = Integer.parseInt(data[5]);
		vel = Float.parseFloat(data[6]);
    }
    
    public int getX(){return x;}
	public int getY(){return y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getDirX(){return dirX;}
	public int getDirY(){return dirY;}
	public float getVel(){return vel;}
}
/**
 * @(#)Traps.java
 *
 *
 * @Iris Chang
 * @version 1.00 2015/5/5
 */
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.awt.Image;
import javax.swing.*;
import javax.swing.JPanel.*;

public class Traps {
	private int type;
	public static final int SPIKES = 0;
	public static final int CIRC_SAW = 1;
	public static final int NINJA_STAR = 2;
	public static final int LASER = 3;
	public static final int SWING_SAW =4;
	public static final int ROT_STAR = 5;
	private boolean stationary;
	private int scale;
	private int newWidth;
	private int newHeight;
	private int x,y;
	private int dx,dy;
	private int speed;
	private int angVel;
	private int ang=0;
	private Image pic;
	private Image resizedPic;
    public Traps(String line) {
    	//x, y, dx, dy, speed, angVel, newWidth, newHeight, TYPE
    	String [] data = line.split(",");
    	x=Integer.parseInt(data[0]);
    	y=Integer.parseInt(data[1]);
    	dx=Integer.parseInt(data[2]);
    	dy=Integer.parseInt(data[3]);
    	speed=Integer.parseInt(data[4]);
    	angVel=Integer.parseInt(data[5]);
    	newWidth=Integer.parseInt(data[6]);
    	newHeight=Integer.parseInt(data[7]);
    	type=Integer.parseInt(data[8]);
    	System.out.println("Images/Interactives/"+type+".png");
    	pic =new ImageIcon("Images/Interactives/"+type+".png").getImage();
    	resizedPic=pic.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    	
    }
    
    public void move(){
    	ang=(ang+angVel)%360;
    //should the main program check if the trap is stationary before calling this method or implement the distinction withint the method?
    	
    }
    public int getAng(){
    	return ang;
    }
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y;
    }
    public int getDX(){
    	return (int)(x-newWidth/2);
    }
    public int getDY(){
    	return (int)(y-newHeight/2);
    }
    public Image getPic(){
    	//System.out.println("GOTPIC");
    	return resizedPic;
    	
    }
}
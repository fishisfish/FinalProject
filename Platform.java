/**
 * @Platform.java
 * 	- stores information on all types of platforms besides normal and water
 *
 * @Iris Chang 
 * @version 1.00 2015/5/05
 */
 
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Platform{
	private int x,y,dx,dy,width,height,dir,vel;
	public Platform(String line){
		String [] data = line.split(",");
		x = Integer.parseInt(data[0]);
		y = Integer.parseInt(data[1]);
		width = Integer.parseInt(data[2]);
		height = Integer.parseInt(data[3]);
		dx = Integer.parseInt(data[4]);
		dy = Integer.parseInt(data[5]);
		dir = Integer.parseInt(data[6]);
		vel = Integer.parseInt(data[7]);
	}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	/*public boolean onTop(){
		//tells whether person is on top of the special block, which is a necessary condition for type 0, 1 to be effective
	}*/
	
	public void move(){
		
	}
	
	
}
/**
 * @Platform.java
 * 	- class information on all types of platforms besides normal and water
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

//Need to implement a platform loading method in Game.java

public class Platform{
	private int type;
	public static int final FALLING = 0;
	public static int final BOUNCING = 1;
	public static int final LATERAL = 2;
	public static int final VERTICAL = 3;
	public static int final ICE = 4;
	private int x,y;
	private boolean intact = true;
	private boolean regen;
	private int width, length;
	public Platform(/*data file format*/){
		/*x = data x
		 *y = data y
		 *type = data type
		 *regen = data type
		 *width = image.getWidth()
		 *length = image.getWideth()
		 */
	}
	
	
	public boolean onTop(){
		//tells whether person is on top of the special block, which is a necessary condition for type 0, 1 to be effective
	}
	
	public boolean move(){
		//for type 2 and 3, and 0 when triggered
	}
	
	
}
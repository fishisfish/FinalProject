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

public class Platform{
	private int type;
	public static int final FALLING = 1;
	public static int final BOUNCING = 2;
	public static int final LATERAL = 3;
	public static int final VERTICAL = 4;
	public static int final ICE = 5;
	private int x,y;
	private boolean intact = true;
	private boolean regen;
	private int width, length;
	
}
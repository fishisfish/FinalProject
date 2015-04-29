/**
 * @(#)Person.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import javax.swing.*;
import javax.swing.JPanel.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
public class Person {
	private int x,y;
	private Image temp= new ImageIcon("profile.png").getImage();
	private double xVel;
	private double yVel;
	private int direction=1;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private boolean inAir=false;

    public Person() {
    	x=400;
    	y=500;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    	
    public Image getPic(){
    	return temp;
    }
    
    public void changeDiection(int dir){
    	direction=dir;
    }
    public void moveHor(){
    	x+=(direction*5);
    	if (x<0){
    		x=0;
    	}
    	if (x>750){
    		x=750;
    	}
    }
    public void jump(){
    	if (inAir==false){
    		inAir=true;
    		yVel=10;
    	}
    }
    public void gravity(){
    	if (yVel!=0){
    		y=(int)(y-yVel);
    		yVel-=0.81;
    	}
    	if (y>=500){
    		y=500;
    		inAir=false;
    		System.out.println(yVel);
    		yVel=0;
    		
    	}
    }
}
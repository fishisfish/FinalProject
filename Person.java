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
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
public class Person {
	private int x,y,walkCount,picStall;
	private ArrayList<ArrayList<ArrayList<Image>>> allPics = new ArrayList<ArrayList<ArrayList<Image>>>();
	private ArrayList<ArrayList<Image>> leftPics = new ArrayList<ArrayList<Image>>();
	private ArrayList<ArrayList<Image>> rightPics = new ArrayList<ArrayList<Image>>();
	private String [] picNames = new String []{"Walk", "Jump", "Some", "Swim", "Dead"};
	private int [] frameTotal = new int []{0,0,0,0,0};
	private Image temp= new ImageIcon("profile.png").getImage();
	private double xVel;
	private double yVel;
	private int direction=1;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private boolean inAir=false;
	private boolean isWalking=false;

    public Person() {
    	x=400;
    	y=200;
    	xVel=0;
    	yVel=0;
    	walkCount=0;
    	picStall=0;
    	makePics();
    }
    public void makePics(){
    	for (int j=0; j<4; j++){
    		leftPics.add(new ArrayList<Image>());
    		rightPics.add(new ArrayList<Image>());
    	}
    	Scanner infile = null;
    	try{
    		infile=new Scanner(new File("Images/Chara/info.txt"));   	
    	}
    	catch(IOException ex){
    	}
    	int temp=0;
    	while (infile.hasNextLine()){
    		
    		int line = Integer.parseInt(infile.nextLine());
    		frameTotal[temp]=line;
    		System.out.println(line);
    		System.out.println(picNames[temp]);
    		for (int i=0;i<line;i++){
    			int b=i+1;
    			String a=b+"";
    			leftPics.get(temp).add(new ImageIcon("Images/Chara/Left/"+picNames[temp]+"/"+a+".png").getImage());
    			rightPics.get(temp).add(new ImageIcon("Images/Chara/Right/"+picNames[temp]+"/"+a+".png").getImage());
    		}
    		temp+=1;
    		
    	}
    	allPics.add(leftPics);
    	allPics.add(new ArrayList<ArrayList<Image>>());
    	allPics.add(rightPics);
    }
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    	
    public Image getPic(){
    	if (isWalking==true){
   	    	picStall+=1;
	    	if (picStall==5){
	    		walkCount=(walkCount+1)%frameTotal[0];
	    		picStall=0;
	    	}
	    	//System.out.println(walkCount);
	    	//System.out.println(allPics.get(direction+1).get(0).get(walkCount));
	    	return allPics.get(direction+1).get(0).get(walkCount);
	    }
	    else{
	    	walkCount=0;
	    }
	    //System.out.println(allPics.get(2).get(0).size());
    	return allPics.get(direction+1).get(0).get(0);
    }
    
    public void changeDir(int dir){
    	xVel=Math.min(5,xVel+0.4);
    	direction=dir;
    }
    public void move(){
    	x+=(int)(xVel*direction);
    	xVel=Math.max(0,xVel-0.2);
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
    	if (y>=200){
    		y=200;
    		yVel=(int)(yVel*(-0.3));
    			if (yVel<1){
    				inAir=false;
		    		yVel=0;
    			}
    		
    	}
    }
    public void checkWalking(boolean temp){
    	isWalking=temp;
    }
}
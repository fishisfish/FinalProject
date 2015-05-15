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
	private int x,y,walkCount,airCount,picStall;
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
	private boolean bouncing=false;

    public Person() {
    	x=400;
    	y=300;
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
    public double getxVel(){
    	return xVel;
    }
    public double getyVel(){
    	return yVel;
    }
    public double getxMoved(){
    	return xVel*direction;
    }
    public double getyMoved(){
    	return yVel;
    }
    public boolean getMoved(){
    	return isWalking;
    }
    
    public int getDir(){
    	return direction;
    }
    	
    public Image getPic(){
    	if (isWalking==true&&inAir==false){
   	    	picStall+=1;
	    	if (picStall==10){
	    		walkCount=(walkCount+1)%frameTotal[0];
	    		picStall=0;
	    	}
	    	//System.out.println(walkCount);
	    	//System.out.println(allPics.get(direction+1).get(0).get(walkCount));
	    	return allPics.get(direction+1).get(0).get(walkCount);
	    }
	    if (inAir==true){
	    	if (bouncing==false){
	    	
		    	if (yVel<=10&&yVel>=6){
		    		allPics.get(direction+1).get(1).get(0);
		    	}
		    	else if (yVel<6&&yVel>=0){
					return allPics.get(direction+1).get(1).get(1);
		    	}
		    	else if (yVel<0&&yVel>=-6){
					return allPics.get(direction+1).get(1).get(2);
		    	}
		    	else if (yVel<-6){
					return allPics.get(direction+1).get(1).get(3);
		    	}
	    	}
	    	
	    }
	    if(isWalking==false){
	    	walkCount=0;
	    }
	    //System.out.println(allPics.get(2).get(0).size());
    	return allPics.get(direction+1).get(0).get(0);
    }
    
    public void changeDir(int dir){
    	xVel=Math.min(5,xVel+0.15);
    	direction=dir;
    }
    public void move(){
    	if (inAir==false){
    		
	    	x+=(int)(xVel*direction);
			
    	}
    	else if (inAir==true&&isWalking==true){
    		x+=(int)(4*direction);
    	}
    	if (isWalking==false&&xVel>0){
    		xVel-=0.2;
    	}    	
    	
    	if (x<0){
    		x=0;
    	}
    	if (x>75000){
    		x=75000;
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
    		yVel-=0.55;
    	}
    	if (y>=500){
    		y=500;
    		yVel=(int)(yVel*(-0.3));
    		bouncing=true;
    			if (yVel<1){
    				inAir=false;
    				bouncing=false;
    				airCount=0;
		    		yVel=0;
    			}
    		
    	}
    }
    public void checkWalking(boolean temp){
    	isWalking=temp;
    }
    public void checkHit(){
    	
    }
}
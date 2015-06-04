/**
 * @(#)Person.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */
//You will need to add swinging to the list of motions

//load map as bufferedImage

import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
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
	private boolean doSome=false;
	private boolean isWalking=false;
	private boolean bouncing=false;
	private boolean runIntoWall,onGround,hitHead;
	private int contactRecty = 42;
	private int[][] contactPoints = {{-15,-20},{0,-20},{15,20},{-15,0},{0,0},{15,0},{-15,20},{0,20},{15,20}};
	
    public Person(){
    	x=200;
    	y=400;
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
    public boolean getinAir(){
    	return inAir;
    }
    public boolean getSome(){
    	return doSome;
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
	    if (inAir==true && bouncing==false){
	    	if (doSome==true){
	    		if (yVel<=10&&yVel>=7){
		    		allPics.get(direction+1).get(2).get(0);
		    	}
		    	else if (yVel<7&&yVel>=4){
					return allPics.get(direction+1).get(2).get(1);
		    	}
		    	else if (yVel<4&&yVel>=1){
					return allPics.get(direction+1).get(2).get(2);
		    	}
		    	else if (yVel<1&&yVel>=-2){
					return allPics.get(direction+1).get(2).get(3);
		    	}
		    	else if (yVel<-2&&yVel>=-5){
					return allPics.get(direction+1).get(2).get(4);
		    	}
		    	else if (yVel<-5&&yVel>=-8){
					return allPics.get(direction+1).get(2).get(5);
		    	}
		    	else if (yVel<-8){
					return allPics.get(direction+1).get(2).get(0);
		    	}
	    		
	    	}
	    	else{
	    	
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
    	if (inAir==false && runIntoWall == false){	//moving in general
	    	x+=(int)(xVel*direction);
    	}
    	else if (inAir==true&&isWalking==true&&runIntoWall==false){	//moving in air
    		x+=(int)(4*direction);
    	}
    	if (isWalking==false&&xVel>0&&runIntoWall == false){	//sliding
    		xVel-=0.2;
    	}
    	if (inAir){	//falling
    		
			
    	}
    	//temporary
    	if (x<0){
    		x=0;
    	}
    	if (x>75000){
    		x=75000;
    	}
    }
    public boolean jump(){
    	if (inAir==false){
    		inAir=true;
    		yVel=10;
    		return true;
    	}
    	return false;
    }
    public void some(){
    	if (inAir==true && doSome==false){
    		System.out.println("SOME");
    		doSome=true;
    		yVel=10;
    	}
    	
    }
    public void gravity(){
    	if (yVel!=0 && onGround == false){
    		y=(int)(y-yVel);
    		yVel-=0.55;
    	}
    	if (y>=500 || hitHead == true){	//check collide
    		y=500;
    		yVel=(int)(yVel*(-0.3));
    		bouncing=true;
    			if (yVel<1){
    				inAir=false;
    				bouncing=false;
    				doSome=false;
    				airCount=0;
		    		yVel=0;
    			}
    		
    	}
    }
    public void checkWalking(boolean temp){
    	isWalking=temp;
    }
    public void checkHit(BufferedImage map){
    	//starting in top left corner and going horizontally to right, start on left again in next row (9 points total)
    	boolean[] hits = new boolean[9];
    	runIntoWall = false;
    	onGround = false;
    	hitHead = false;
    	for (int i=0;i<9;i++){
    		Color colour = new Color (map.getRGB(Math.max(x+contactPoints[i][0],0),Math.max(y+contactPoints[i][1],0)));
    		hits[i] = colour.equals(Color.GREEN)?true:false;
    		if (hits[i] == true){
    			if(direction == RIGHT){
    				if (i == 2 || i == 5){
    					runIntoWall = true;
    				}
    			}
    			else if(direction == LEFT){
    				if (i == 0 || i == 3){
    					runIntoWall = true;
    				}
    			}
    			if (i==7){
    				System.out.println("onground");
    				onGround = true;
    			}
    			if (i == 1){
    				System.out.println("hitHead");
    				hitHead = true;
    			}
    			else{
    				System.out.println("not onground");
    			}
    		}
    	}
    }
    /*public boolean checkHor(){
    //checks collision in the horizontal
    	
    }
    public boolean offGround(){
    //checks collision in the vertical
    	
    }*/

}
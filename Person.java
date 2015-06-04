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
	private int x,y,walkCount,airCount,swimCount,breathCount,picStall;
	private ArrayList<ArrayList<ArrayList<Image>>> allPics = new ArrayList<ArrayList<ArrayList<Image>>>();
	private ArrayList<ArrayList<Image>> leftPics = new ArrayList<ArrayList<Image>>();
	private ArrayList<ArrayList<Image>> rightPics = new ArrayList<ArrayList<Image>>();
	private String [] picNames = new String []{"Walk", "Jump", "Some", "Slid", "Swim", "Dead"};
	private int [] frameTotal = new int []{0,0,0,0,0,0};
	private Image temp= new ImageIcon("profile.png").getImage();
	private double xVel;
	private double yVel;
	private int headAngle;
	private int direction=1;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private boolean inAir=false;
	private boolean doSome=false;
	private boolean isWalking=false;
	private boolean isSliding=false;
	private boolean isCrouching=false;
	private boolean isSwimming=true;
	private boolean gotDown=false;
	private int slideCount=1;
	private boolean bouncing=false;
	private boolean runIntoWall,onGround,hitHead;
	private int contactRecty = 42;
	private int[][] contactPoints = {{-15,-20},{0,-20},{15,20},{-15,0},{0,0},{15,0},{-15,20},{0,20},{15,20}};
	
    public Person(){
    	x=200;
    	y=400;
    	xVel=0;
    	yVel=0;
    	headAngle=90;
    	walkCount=0;
    	picStall=0;
    	makePics();
    }
    public void makePics(){
    	for (int j=0; j<5; j++){
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
    
    public void getUp(){
    	
    	if (slideCount==1){
    		isSliding=false;
    		isCrouching=false;
    		gotDown=false;
    	}
    	else{
    		slideCount-=1;
    	}	
    }
    public boolean getSwim(){
    	return isSwimming;
    }
    public int getAn(){
    	return headAngle;
    }	
    public Image getPic(){
    	if (isSwimming==true){
    		picStall+=1;
	    	if (picStall==10){
	    		swimCount=(swimCount+1)%6;
	    		picStall=0;
	    	}
	    	return allPics.get(direction+1).get(4).get(swimCount);
    		
    	}
    	 if (isSliding==true){

	    	if (slideCount<10){
	    		
		    	if (gotDown==false){
		    		return allPics.get(direction+1).get(3).get((int)slideCount/5);
		    	}
		    	else{
		    		if (slideCount>5){
		    			return allPics.get(direction+1).get(3).get(3);
		    		}
		    		else{
		    			return allPics.get(direction+1).get(3).get(4);
		    		}
		    	}
	    	}
	    	else{
	    		return allPics.get(direction+1).get(3).get(2);
	    	}
    	}
    	else if (isCrouching==true){
    		return allPics.get(direction+1).get(3).get(5);
    		
    	}
    	if (inAir==false&&xVel>0){
   	    	picStall+=1;
	    	if (picStall==10){
	    		walkCount=(walkCount+1)%10;
	    		picStall=0;
	    	}
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
    	return allPics.get(direction+1).get(0).get(10);
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
    		isCrouching=false;
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
    
    public void slide(){
    	xVel=Math.max(0,xVel-0.20);
    	if (xVel==0){
    		isWalking=false;
    	}
    	if (inAir==false){
	    	if (isWalking==true&&isCrouching==false){
	    		slideCount=Math.min(20,slideCount+1);
	    		isSliding=true;
	    	}
	    	else{
	    		isCrouching=true;
	    	}
    	}
    	
    	if (slideCount>10){
    		gotDown=true;
    	}
    	
    	System.out.println("BLOO");
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
    public void swim (String temp){
    	if (temp=="LEFT"){
    		headAngle=(headAngle+1)%360;
    	}
    	else if (temp=="RIGHT"){
    		headAngle=(360+headAngle-1)%360;
    	}
    	else{
    		double ang=Math.toRadians(headAngle);
    		if (temp=="UP"){
    			System.out.println("UP");
    			System.out.println("X:"+3*Math.cos(ang));
    			System.out.println("Y:"+(-3)*Math.sin(ang));
    			x=x+(int)(3*Math.cos(ang));
    			y=y-(int)(3*Math.sin(ang));
    		}
    		if (temp=="DOWN"){
    			System.out.println("X:"+(-3)*Math.cos(ang));
    			System.out.println("Y:"+3*Math.sin(ang));
    			System.out.println("DOWN");
    			x=x-(int)(3*Math.cos(ang));
    			y=y+(int)(3*Math.sin(ang));
    		}
    	}
    	System.out.println(headAngle);
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
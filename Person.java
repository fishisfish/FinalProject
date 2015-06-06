/**
 * @(#)Person.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */
//You will need to add swinging to the list of motions
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
	private int x,y,walkCount,airCount,swimCount,breathCount,picStall,stopSwim, jumpVel, propelVel,levelSizeX,levelSizeY;
	private ArrayList<ArrayList<ArrayList<Image>>> allPics = new ArrayList<ArrayList<ArrayList<Image>>>();
	private ArrayList<ArrayList<Image>> leftPics = new ArrayList<ArrayList<Image>>();
	private ArrayList<ArrayList<Image>> rightPics = new ArrayList<ArrayList<Image>>();
	private String [] picNames = new String []{"Walk", "Jump", "Some", "Slid", "Swim", "Dead"};
	private int [] frameTotal = new int []{0,0,0,0,0,0};
	private Image temp= new ImageIcon("profile.png").getImage();
	private double xVel,yVel,sVel,slideCoefficient,clingCoefficient;
	private int headAngle;
	private int direction=1;
	private int swimDir=1;
	private int slideCount=1;
	private int contactRecty = 42;
	private int apex;
	private int lastWallx;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private boolean inAir=false;
	private boolean doSome=false;
	private boolean isWalking=false;
	private boolean isSliding=false;
	private boolean isCrouching=false;
	private boolean isSwimming=false;
	private boolean isClinging=false;
	private boolean bouncing=false;
	private boolean gotDown=false;
    private boolean runIntoWall=false;
    private boolean hitGround=false;
    private boolean hitHead=false;
    private boolean isSinking=false;
    private boolean propel=false;
    private boolean leavingWater=false;
    private boolean hitApex=true;
    private boolean[] WallHits = new boolean[9];
 	private int[][] contactPoints = {{0,-25},{-15,-24},{15,-24},{-15,0},{0,0},{15,0},{0,22},{-15,21},{15,21}};
 	private int[][] rotatedPoints = {{0,-25},{-15,-24},{15,-24},{-15,0},{0,0},{15,0},{0,22},{-15,21},{15,21}};
 	private double [] distPoints ={25.0,Math.pow(24*24+15*15,0.5),Math.pow(21*21+15*15,0.5),15.0,0,15.0,22.0,Math.pow(21*21+15*15,0.5),Math.pow(21*21+15*15,0.5)};
 	private double [] cptStartAngles={Math.toRadians(90)+Math.atan(15/21), Math.toRadians(180)+Math.atan(24/15),Math.toRadians(270)+Math.atan(24/15),Math.toRadians(180),-10000,0,Math.toRadians(90),Math.toRadians(90)+Math.atan(15/21),Math.atan(21/15)};	
 	private Color BLUE = new Color (0,165,255);
	private Color PURPLE = new Color (200,0,200);
	private Color ORANGE = new Color (255,156,0);
    public Person(){
     	x=200;
     	y=490;
    	xVel=0;
    	yVel=0;
    	jumpVel=11;
    	propelVel=12;
    	headAngle=90;
    	walkCount=0;
    	picStall=0;
    	makePics();
    	levelSizeX=2000;
    	levelSizeY=1000;
    	slideCoefficient=0.5;
    	clingCoefficient=0.05;
    		
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
    	return xVel;
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
   	public int getApex(){
   		return apex;
   	}
   	public boolean getCling(){
   		return isClinging;
   	}
   	public boolean reachedApex(){
   		return hitApex;
   	}
   	public void reachingApex(boolean tmp){
   		hitApex=tmp;
   	}
    public boolean getleavingWater(){
    	return leavingWater;
    }
    public int [][] getPoints(){
    	return rotatedPoints;
    }
    public Image getPic(){
    	if (isSwimming==true&&isSinking==false){
    		if (stopSwim>0){
	    		picStall+=1;
		    	if (picStall==10){
		    		swimCount=(swimCount+1)%6;
		    		picStall=0;
		    	}
		    	return allPics.get(direction+1).get(4).get(swimCount);
    		}
    		return allPics.get(direction+1).get(4).get(2);
    		
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
    	if (inAir==false&&Math.abs(xVel)>0&&isWalking==true){
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
    	double tmp= xVel+0.15*dir;
    	//System.out.println("TMP: "+tmp);
    	if (tmp<-4){
    		xVel=-4;
    	}
    	else if (tmp>4){
    		xVel=4;
    	}
    	else{
    		xVel=tmp;
    	}
    	direction=dir;
    }
    public void move(){
    	if (inAir==false && runIntoWall == false){
	    	x=(int)(x+xVel);
    	}
    	
    	else if (inAir==true&&isWalking==true&&runIntoWall==false){
    		x+=(int)(4*direction);
    	}
    	if (isWalking==false&&Math.abs(xVel)>0){

    		double tmp=xVel+direction*-1*slideCoefficient;
    		if (tmp*xVel<0){
    			xVel=0;
    		}
    		else{
    			xVel=tmp;
    		}
    	}
    	if (inAir){	//falling	
    	}    	
    }
    public boolean jump(){
    	//System.out.println("JUMP FUNCTION CALLED. AAAA");
    	if (inAir==false){
    		System.out.println("JUMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMP");
    		isCrouching=false;
    		inAir=true;
    		if (propel==true){
    			yVel=propelVel;
    		}
    		else{
    			yVel=jumpVel;
    		}
    		
    		
    		return true;
 		
    	}
    	//System.out.println("INAIRALREADY");
    	return false;
    }
    public void some(String tmp){

    	if (inAir==true && doSome==false){
    		//System.out.println("SOMMMMMMMMMMMMME");
    		doSome=true;
    		if (tmp=="OFFWALL"){
    			isClinging=false;
    			yVel=jumpVel;
    		}
    	}	
    }
    
    public void slide(){
    	double tmp=xVel+direction*0.2*-1;
    	if (tmp*xVel<=0||direction*xVel<=0){
    		xVel=0;
    	}
    	else{
    		xVel=tmp;
    	}
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
    }
    public void gravity(){
    	if (inAir == true){
    		
    		//System.out.println("FALL");
    		
    		if (yVel>0){
    			if(isClinging==false){
    				yVel-=0.55;
    			}
    			else{
    				yVel=Math.max(-3,yVel-clingCoefficient);
    			}
    			
    		}
    		else if (yVel==0&&hitHead==true){
    			y+=1;
    		}
    		else{
    			if (isClinging==false){
    				yVel=Math.max(-10,yVel-0.55);
    			}
    			else{
    				yVel=Math.max(-3,yVel-clingCoefficient);
    			}
    			if (propel==true){
    				propel=false;
    			}
    		}
    	}
    	y=(int)(y-yVel);
    	if (y<apex){
    		apex=y;
    		hitApex=false;
    	}
    }
    public void swim (String temp){
    	sVel=Math.min(10,sVel+0.30);
    	stopSwim=1;
    	if (temp=="LEFT"){
    		
    		headAngle=(headAngle+1)%360;
    	}
    	else if (temp=="RIGHT"){
    		headAngle=(360+headAngle-1)%360;
    	}
    	else{
    		double ang=Math.toRadians(headAngle);
    		if (temp=="UP"){
    			x=x+(int)(3*Math.cos(ang));
    			y=y-(int)(3*Math.sin(ang));
    			swimDir=1;
    		}
    		if (temp=="DOWN"){
    			x=x-(int)(3*Math.cos(ang));
    			y=y+(int)(3*Math.sin(ang));
    			swimDir=-1; 
    		}
    	}
    }
    public void swimSlow(){
    	sVel=Math.max(0,sVel-0.20);
    	stopSwim-=1;
    }
    public void sink(){
    	if (isSinking==true){
    		System.out.println("SINKK");
    		y+=4;
    		
    	}
    }
    public boolean checkWallCling(int tmp){
    	if (tmp==LEFT&&(WallHits[1]==true||WallHits[3]==true||WallHits[7]==true)){
    		return true;
    	}
    	if (tmp==RIGHT&&(WallHits[2]==true||WallHits[5]==true||WallHits[8]==true)){
    		return true;
    	}
    	else{
    		return false;
    	}
    		
    }
    public void cling(){
    	if (((direction*xVel>0&&(xVel>0&&checkWallCling(RIGHT)==true||xVel<0&&checkWallCling(LEFT)==true))||(xVel==0&&(direction==LEFT&&checkWallCling(LEFT)==true)||(direction==RIGHT&&checkWallCling(RIGHT)==true)))&&(doSome==false||doSome==true&&lastWallx!=x+15*direction)){
			yVel=Math.min(yVel,0);
			xVel=0;
			isWalking=false;
			isClinging=true;
			doSome=false;
			lastWallx=x+15*direction;
			}
    }
    public void checkWalking(boolean temp){
    	isWalking=temp;
    }
    public void rotate(){
    	int angleRotated=headAngle-90;
    	for (int i=0;i<9;i++){
    		if (i!=5){
    			double newAng=angleRotated+cptStartAngles[i];
    			rotatedPoints[i][0]=(int)(distPoints[i]*Math.cos(newAng));
    			rotatedPoints[i][1]=(int)(distPoints[i]*Math.sin(newAng));
    		}
    		
    		
    	}
    }
    public void checkHit(BufferedImage map){
    	//System.out.println(y);
    	//starting in top left corner and going horizontally to right, start on left again in next row (9 points total)
    	
    	runIntoWall = false;
    	hitHead = false;
    	hitGround=false;
    	leavingWater=false;
    	//jumpOut=false;
		//System.out.println("IS walking: "+isWalking);
	//	System.out.println(y);
	//	System.out.println("INAIR: "+inAir);
    	for (int i=0;i<9;i++){
    		int tempX;
  			int tempY;
  			
    		if (isSwimming==false){
    			tempX=(int)(x+contactPoints[i][0]+xVel);
    		    tempY=(int)(y+contactPoints[i][1]+yVel);
    		}
    		else{
    			double ang=Math.toRadians(headAngle);
    			tempX=(int)(x+contactPoints[i][0]+3*Math.cos(ang)*swimDir);
    			tempY=(int)(y+contactPoints[i][1]-3*Math.sin(ang)*swimDir);
    		}
    		
    		if (tempX>=0&&tempX<=levelSizeX&&tempY>=0&&tempY<=levelSizeY){
    			Color c = new Color (map.getRGB(tempX,tempY));
    			WallHits[i]=c.equals(Color.GREEN);
    			if (i==6){
    					if (c.equals(BLUE)==true&&isSwimming==false&&propel==false){
    						System.out.println("HITWATER");
    						isSwimming=true;
    						isSinking=true;
    					}
    					if (c.equals(Color.GREEN)==false&&isSwimming==false&&isSinking==false){
    						//System.out.println("XVEL: "+xVel);
    						//System.out.println("yVEL: "+yVel);
    						//System.out.println("INAIR: "+tempX+","+tempY);
    						inAir=true;
    						isSliding=false;
    						
    					}
    			}
    			if (isSinking==true&&i==0){
    				if (c.equals(BLUE)==true){
    					System.out.println("ALLWATER");
    					y+=3;
    					apex=y;
    					isSinking=false;
    					inAir=false;
    					doSome=false;
    				}
    			}
    			if (i==0&&isSwimming==true&&isSinking==false&&c.equals(BLUE)==false&&c.equals(Color.GREEN)==false){
    				System.out.println("GETOUT");
    				leavingWater=true;
    				propel=true;
    				isSwimming=false;
//    				jumpOut=true;
    				headAngle=90;
    				jump();
    				
    			}
    			if (c.equals(Color.GREEN)==true){
    				
    				if (i==0&&inAir==true){
    					System.out.println("HITHEAD");
    					hitHead=true;
    					yVel=0;
    					int j=0;
    					while (true){
    						Color c1= new Color (map.getRGB(tempX,y-j));
    						if (c1.equals(Color.GREEN)==true){
    							y=y-j+26;
    							break;
    						}
    						j+=1;
    					}
    				}
    				if (i==6&&inAir==true){
    					System.out.println("HITGROUND");
    					inAir=false;
    					doSome=false;
    					yVel=0;
    					hitGround=true;
    					airCount=0;
    					apex=y;
    					int j=0;
    					while (true){
    						Color c2= new Color (map.getRGB(tempX,y+j));
    						if (c2.equals(Color.GREEN)==true){
    							y=y-22+j;
    							break;
    						}
    						j+=1;
    					}
    				}
					if (xVel<0){
						if (i==1||i==3||i==7){
							System.out.println("HIT-LEFT");
							int j=0;
    						while (true){
    							int temp=x-j;
    							Color c3= new Color (map.getRGB(x-j,tempY));
    							if (c3.equals(Color.GREEN)==true){
    								x=x-j+14;
       								break;
    							}
    							j+=1;
    						}
							runIntoWall=true;

						}
					}
					if (xVel>0){
						if (i==2||i==5||i==8){
							System.out.println("HIT-RIGHT");
							int j=0;
    						while (true){
    							Color c4= new Color (map.getRGB(x+j,tempY));
    							if (c4.equals(Color.GREEN)==true){
    								x=x-14+j;
    								break;
    							}
    							j+=1;
    						}
							runIntoWall=true;
						}	
    				}
    			}
    			
    		}
    			
    		
    	}
    	if (WallHits[1]==false&&WallHits[2]==false&&WallHits[3]==false&&WallHits[5]==false&&WallHits[7]==false&&WallHits[8]==false){
    		//System.out.println("NOT-CLINGG");
    		isClinging=false;
    	}
    	else{
    		if (inAir==true&&isSwimming==false){
    		//	System.out.println("CLINGG");
				cling();
				}
    	}
    	//System.out.println(x);
    	//System.out.println(runIntoWall);
    }
    /*public boolean checkHor(){
    //checks collision in the horizontal
     	
     }
    public boolean offGround(){
    //checks collision in the vertical
    	
    }*/

}
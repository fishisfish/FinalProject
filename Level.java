/**
 * @(#)Level.java
 *
 *
 * @author 
 * @version 1.00 2015/6/6
 */
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.awt.Image;
import javax.swing.*;
import javax.swing.JPanel.*;

public class Level {
	private BufferedImage map;
	private ArrayList<Platform> movingPlats = new ArrayList<Platform>();
	private ArrayList<Traps> traps = new ArrayList<Traps>();
	private ArrayList<Image> checkPics = new ArrayList<Image>();
	private ArrayList<int[]> checkPoints = new ArrayList<int[]>();
	private ArrayList<Boolean> checkPointsPassed = new ArrayList<Boolean>();
	private ArrayList<int[]> keyPoints = new ArrayList<int[]>();
	private ArrayList<Boolean> keyAvailable = new ArrayList<Boolean>();
	private int width,height,dropx,dropy,direction;
	private Scanner infile = null;
    private	Scanner trapfile = null;
    String levelNum="";
    public Level(int lev) {
    	levelNum = lev + "";
    	//import map as BufferedImage
    	try{
			map= ImageIO.read(new File("Maps/Act "+ levelNum+".png"));
		}
		catch(IOException e){
			System.out.println("map loading problem");
		}
    	infile = null;
    	trapfile = null;
    	//loading map info text file
    	try{
    		infile=new Scanner(new File("Maps/"+levelNum+".txt"));
    		trapfile=new Scanner(new File("Images/Interactives/"+levelNum+".txt"));
    		System.out.println(trapfile);
    			
    	}
    	catch(IOException ex){
    		System.out.println("level textfile problem");
    	}
    	loadPlat();
    	//load checkpoints
    	loadCP();
    	//load traps
    	loadTrap();	
    	loadKeys();		
    	System.out.println("DONE");//	<=============to be completed
    }
    public void loadTrap(){
    	int num=Integer.parseInt(trapfile.nextLine());
    	for (int i = 0; i<num;i++){
    		String line = trapfile.nextLine();
			Traps tmp=new Traps(line,this);
			traps.add(tmp);
    	}
    }
    public void loadPlat(){
    	direction = Integer.parseInt(infile.nextLine());
    	width = Integer.parseInt(infile.nextLine());
    	height = Integer.parseInt(infile.nextLine());
    	dropx = Integer.parseInt(infile.nextLine());
    	dropy = Integer.parseInt(infile.nextLine());
    	int num = Integer.parseInt(infile.nextLine());
    	for (int i = 0; i<num;i++){
    		String line = infile.nextLine();
    		Platform tmp = new Platform(line);
    		movingPlats.add(tmp);
    	}
    }
    public void loadCP(){
    	int num2 = Integer.parseInt(infile.nextLine());
    	for (int i = 0; i<num2;i++){
    		String line = infile.nextLine();
    		String [] data = line.split(",");
    		int x = Integer.parseInt(data[0]);
			int y = Integer.parseInt(data[1]);
			int [] temp = {x,y};
			checkPoints.add(temp);
			int j = i + 1;
		//	System.out.println("Maps/"+levelNum+"/"+j+".png");
			checkPics.add(new ImageIcon("Maps/"+levelNum+"/"+j+".png").getImage());
			checkPointsPassed.add(false);
    	}
    }
    public void loadKeys(){
    	int num3 = Integer.parseInt(infile.nextLine());
    	for (int i = 0; i<num3;i++){
    		String line = infile.nextLine();
    		String [] data = line.split(",");
    		int x = Integer.parseInt(data[0]);
			int y = Integer.parseInt(data[1]);
			int [] temp = {x,y};
			keyPoints.add(temp);

		//	System.out.println("Maps/"+levelNum+"/"+j+".png");
			keyAvailable.add(true);
    	}
    }
    public BufferedImage getMap(){
    	return map;
    }
    public ArrayList<Platform> getPlats(){
    	return movingPlats;
    }
    public ArrayList<Traps> getTraps(){
    	return traps;
    }
    public ArrayList<Image> getCheckPics(){
    	return checkPics;
    }
    public ArrayList<int[]> getCheckPoints(){
    	return checkPoints;
    }
    public ArrayList<Boolean> getCheckPassed(){
    	return checkPointsPassed;
    }
    public void setCheckPassed(int num){
    	checkPointsPassed.set(num,true);
    	System.out.println("changed");
    }
    public ArrayList<int[]> getkeyPoints(){
    	return keyPoints;
    }
    public ArrayList<Boolean> getkeyAvailable(){
    	return keyAvailable;
    }
    public void setkeyGot(int num){
    	keyAvailable.set(num,false);
    //	System.out.println("changed");
    }
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getDropx(){return dropx;}
    public int getDropy(){return dropy;}
    public int getDir(){return direction;}
    public void movePlatforms(){
    	for (int i=0;i<movingPlats.size();i++){
    		movingPlats.get(i).move();
    	}
    }
    public void moveTraps(){
    	for (int i=0;i<traps.size();i++){
    		traps.get(i).move();
    	}
    }
    public void meltIce(int camX, int camY){
    	for (int i=0;i<movingPlats.size();i++){
    		if((movingPlats.get(i).getType()).equals("ICE")){
    			if(checkInSight(camX,camY,i)==true&&movingPlats.get(i).getMelt()!=true){
    				movingPlats.get(i).melt();
    				//System.out.println("melttttttttt");
    			}
    		}
    	}
    }
    public void iceRegen(){
    	for (int i=0;i<movingPlats.size();i++){
    		if((movingPlats.get(i).getType()).equals("ICE")){
    			movingPlats.get(i).regen();
    		}
    	}
    }
    public boolean checkInSight(int camX, int camY, int platNum){
    	int iceX = movingPlats.get(platNum).getX();
    	int iceY = movingPlats.get(platNum).getY();
    	boolean hor = false;
    	boolean ver = false;
    	if(Math.abs(camX-iceX) <= 400){
    		hor = true;
    	}
    	if(Math.abs(camY-iceY) <= 300){
    		ver = true;
    	}
    	//System.out.println("HORIZONTAL: " +Math.abs(camX-iceX)+", "+hor);
    	//System.out.println("VERTICAL: "+Math.abs(camY-iceY)+", "+ver);
    	if (hor ==true && ver ==true /*|| (Math.abs(camX-(iceX+movingPlats.get(platNum).getWidth()))<=400 && Math.abs(camX-(iceY+movingPlats.get(platNum).getHeight()))<=300)*/){
    		//System.out.println("PEEEEEKABOO");
    		return true;
    	}
    	else{
    		return false;
    	}
    }
}
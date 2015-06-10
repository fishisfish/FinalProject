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
	private ArrayList<Image> checkPics = new ArrayList<Image>();
	private ArrayList<int[]> checkPoints = new ArrayList<int[]>();
	private ArrayList<Boolean> checkPointsPassed = new ArrayList<Boolean>();
	private int width,height,dropx,dropy,direction;
    public Level(int lev) {
    	String levelNum = lev + "";
    	//import map as BufferedImage
    	try{
			map= ImageIO.read(new File("Maps/Act "+ levelNum+".png"));
		}
		catch(IOException e){
			System.out.println("map loading problem");
		}
    	Scanner infile = null;
    	//loading map info text file
    	try{
    		infile=new Scanner(new File("Maps/"+levelNum+".txt"));
    			
    	}
    	catch(IOException ex){
    		System.out.println("level textfile problem");
    	}
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
    	//load checkpoints
    	int num2 = Integer.parseInt(infile.nextLine());
    	for (int i = 0; i<num2;i++){
    		String line = infile.nextLine();
    		String [] data = line.split(",");
    		int x = Integer.parseInt(data[0]);
			int y = Integer.parseInt(data[1]);
			int [] temp = {x,y};
			checkPoints.add(temp);
			int j = i + 1;
			System.out.println("Maps/"+levelNum+"/"+j+".png");
			checkPics.add(new ImageIcon("Maps/"+levelNum+"/"+j+".png").getImage());
			checkPointsPassed.add(false);
    	}
    	//load traps								<=============to be completed
    }
    public BufferedImage getMap(){
    	return map;
    }
    public ArrayList<Platform> getPlats(){
    	return movingPlats;
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
}
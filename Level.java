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

public class Level {
	private BufferedImage map;
	private ArrayList<Platform> movingPlats = new ArrayList<Platform>();
	private int width,height,dropx,dropy;
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
    	width = Integer.parseInt(infile.nextLine());
    	System.out.println(width);
    	height = Integer.parseInt(infile.nextLine());
    	dropx = Integer.parseInt(infile.nextLine());
    	dropy = Integer.parseInt(infile.nextLine());
    	int num = Integer.parseInt(infile.nextLine());
    	for (int i = 0; i<num;i++){
    		String line = infile.nextLine();
    		Platform tmp = new Platform(line);
    		movingPlats.add(tmp);
    	}
    	//load traps								<=============to be completed
    }
    public BufferedImage getMap(){
    	return map;
    }
    public ArrayList<Platform> getPlats(){
    	return movingPlats;
    }
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getDropx(){return dropx;}
    public int getDropy(){return dropy;}
    public void movePlatforms(){
    	for (int i=0;i<movingPlats.size();i++){
    		movingPlats.get(i).move();
    	}
    }
}
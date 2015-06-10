/*Loads pictures and sorts them. Was ugly so I didn't put it with my main program.
 *02/10/2015
  Elisa Chao
  */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class Pics{
	private final int FLYING=0;
	private final int SHOOTING=1;
	private final int DYING=2;
	private ArrayList<ArrayList<Image>> flyingSprites = new ArrayList<ArrayList<Image>>();
	private ArrayList<Image> dyingSprites = new ArrayList<Image>();
	private ArrayList<ArrayList<Image>> shootingSprites = new ArrayList<ArrayList<Image>>();

	public Pics(){
		String [] folderNames= new String [] {"Blue","Red","Green","Player"};

		for (int i=0; i<4;i++){
			flyingSprites.add(new ArrayList<Image>());
			shootingSprites.add(new ArrayList<Image>());
			for (int j=1; j<5;j++){
				flyingSprites.get(i).add( new ImageIcon("Images/Sprites/"+folderNames[i]+"/Flying/"+j+".png").getImage());
			}
			for (int m=1; m<4;m++){
				shootingSprites.get(i).add( new ImageIcon("Images/Sprites/"+folderNames[i]+"/Shooting/"+m+".png").getImage());
			}
		}
		
		for (int k=1; k<6;k++){
			dyingSprites.add(new ImageIcon("Images/Sprites/Death/"+k+".png").getImage());
		}
	}
	
	public ArrayList<Image> getPics(int type, int chara){ //returns the array of sprites based on what type of character (blue, red, player, etc) and type of sprites (flying,dying, normal, etc).
		 if (type==FLYING){
		 	return flyingSprites.get(chara);
		 }
		 else if (type==SHOOTING){
		 	return shootingSprites.get(chara);
		 }
		 else if (type==DYING){
		 	return dyingSprites;
		 }
		 return new ArrayList<Image>(); //incase i screwed up. 
		 
	}
	
}
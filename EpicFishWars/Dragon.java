/*Enemy of the fish are dragons. Fish will defeat the dragons. In here contains at the dragon's stats, sprite and methods
 *that does stuff to dragons (changing its stats)
 *02/10/2015
  Elisa Chao
  */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class Dragon{
	private boolean living=true;
	private int x, y;           //location
	private int arrayLocation; //column
	private int rowLocation;   //row
	private ArrayList<Image> flyingSprites = new ArrayList<Image>(); //array of different types of sprites
	private ArrayList<Image> dyingSprites = new ArrayList<Image>();
	private ArrayList<Image> shootingSprites = new ArrayList<Image>();
	private boolean dying=false;
	private boolean shooting=false;
	private int frameCount=0;//dragon's normal frame count
	private int shotCount=0; //dragon's shooting frame count
	private int deathCount=0;//dragon's dying frame count (also serves as a cooldown counter till regen).
	private int dir=-1;      //direction of travel
	private int speed=10;	//pixel per move
	private boolean canShoot=false; //can it shoot
	private int pts;  //how much its worth when killed
	

	public Dragon(String name, int column, int row, int points, Pics charaPics){
		pts=points;
		x=60+70*column; //spacing
		y=50+65*row;  
		arrayLocation=column; 
		rowLocation=row;
		flyingSprites=charaPics.getPics(0,column);
		shootingSprites=charaPics.getPics(1,column);
		dyingSprites=charaPics.getPics(2,column);
		
		if (column==2){   //in the beginging only front dragons can shoot.
			canShoot=true;
		}
		
	}

///////GETTING DRAGON STATS /////////////////////////////////////////////////////////////////////////////////////////////////
	public int getPoints(){
		return pts;
	}
	public int getLocc(){
		return arrayLocation;
	}
	public int getLocr(){
		return rowLocation;
	}
	public boolean alive(){
		return living;
	}
	public int getY(){
		return y;
	}
	public int getX(){ 
		return x;
	}
	public Image getPic(int gamePaused){ //gets the dragon sprite based on its state and frame count
		if (dying==false&&shooting==false){
			if (gamePaused==1){
				frameCount=(40+frameCount+1*dir)%40;
			}
			
			return flyingSprites.get(frameCount/10);
		}
		if (shooting==true&&dying==false){
			if (gamePaused==1){
				frameCount=(40+frameCount+1*dir)%40;
				shotCount=shotCount+1;
				}
			
			if (shotCount==29){
				shotCount=0;
				shooting=false;
				return shootingSprites.get(2);
			}
			return shootingSprites.get(shotCount/10);
		}
		else{
			if (gamePaused==1){
				deathCount+=1;
			}
			if (deathCount==49){
				living=false;
			}
			return dyingSprites.get(deathCount/10);
		}

	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setShooter(){
		canShoot=true;
	}
	
	public void moveX(){ //moves dragon forward
		x+=20;
	}
	
	public void moveY(boolean dirchange){ //moves dragon up/down
		if (dirchange==true){
			dir*=-1;
		}
		y+=dir*speed;
	}
	public boolean getHit(int shotx, int shoty){ //checks if the dragon was shot
		if (x<=shotx+19&&x+52>=shotx&&y<=shoty+17&&y+40>=shoty&&dying==false){
			dying=true;
			return true;
		}
		return false;
	}
	
	public boolean outofBounds(){ //checks if the dragon will go out of bounds
	if (y+dir*speed<30||y+60+dir*speed>495){
		return true;
	}		
		
	return false;
	}
	
	public ArrayList<int[]> shoot(ArrayList<int[]> shots){//randomly determins if the dragon shoots, adds the shot to an array of existing shots and returns that
		if (shooting==false&&canShoot==true&&dying==false){
			int rand=(int)(Math.random()*50);		
			if (rand<2){
				shooting=true;
				shots.add(new int []{0,40+x,25+y});
			}
		}
			
		return shots;
	}
	
	public void reload(int level){ //resets the stats, but it goes faster as levels go up
		x=60+70*arrayLocation;
		y=50+65*rowLocation;
		living=true;
		dying=false;
		shooting=false;
		frameCount=0;
		shotCount=0;
		deathCount=0;
		dir=-1;
		canShoot=false;
		speed+=1*level;  //wheeewhoo. zoom.zoom. acculmative. gets significantly faster later. 
		if (arrayLocation==2){
			canShoot=true;
		}
	}
}
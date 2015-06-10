/*
  This game in the style of space invaders. But with dragons. And fish.
  It has a menu screen, a highscore page, the game, and a help page.
  The player uses the up/down/space keys to move/shoot and use the mouse to navigate.
  02/10/2015
  Elisa Chao
  */
import java.awt.*;                 //importing like all the stuff.
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.*;
import java.io.*; 
import javax.sound.sampled.AudioSystem;

public class EpicFishWars extends JFrame implements ActionListener{
	Timer myTimer;
	GamePanel game;
	AudioClip back;  //the backgroudmusic
	
	public static void main (String [] args){
		EpicFishWars frame = new EpicFishWars();
	}
	
	public EpicFishWars(){
		super("Epic Fish Wars");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(595,620);
		back = Applet.newAudioClip(getClass().getResource("epicbackgroundmusic.wav"));
		game = new GamePanel(this);
		add(game);
		back.loop();  //starts backgroundmusic
		myTimer = new Timer (10,this);
		setResizable(false);
		setVisible(true);		
	}
	public void actionPerformed(ActionEvent evt){
		Object source= evt.getSource();
		if (source==myTimer){
			if (game.getsoundClicked()==true){  //only triggers when the sound button is hit.
				if (game.getSound()==true){		//turns sound on and off
					back.loop();
				}
				else{
					back.stop();
				}
			}
			if (game.onMenu()==false&&game.getPaused()==false&&game.getlevelStarting()==false &&game.getgameOver()==false){
				game.move();		//if the game is not paused/reloading/over move the stuff
			}
			
			if (game.getgotName()==true){ //exit when the game is over and highscore is inputted
				System.exit(0);
				
			}
			game.repaint();	
			if (game.getgameOver()==true&&game.getgotName()==false){  //that waiting period after the game ends and the user has yet to input their name.
				game.usegetName();
			}
		}
	}
	
	public void start(){
		myTimer.start();
	}
}

class GamePanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener{
	private EpicFishWars mainFrame;
	
	private String name;				//used for highscore
	private Font font;					//used to print highscores
	private Font fontpts;
		
	private int playerx,playery,mx,my; 	//player location, mouse location			
	private int sizey = 45;				//height of the player sprite 
	private int points=0;				
	private int life=3;
	private int level=1;	
	private int frameSizex = 590;		//size of the background
	private int frameSizey = 590;
	private int frameCount = 0;			//player's normal sprite frame count
	private int shotframeCount=0;		//player's shooting frame count
	private int deathCount=0;			//player's dying frame count (also serves as a cooldown counter till regen).
	private int abominationCount=0;     //""
	private int abominationdeathCount=0;//""
	private int movestall=0;			//counter to stall movement 
	private int dragonsKilled=0;
	private int gamePaused = 1;			// 1=true, -1 = false
	private int soundOn=1;
	private int soundBefore;			//Keeps track of the sound status before game is paused
	private int nothelping =1;				
	private int newlevelCount=0;		//cooldown countdown in between levels
	private int n;						//number of names in the highscore data
	
	private int [][] livingChart= new int [3][7];  //keeps track of which dragon is still alive
	
	private Point abomination = new Point (-45,445);	//location of the special enemy. starts at this point every time.
	private boolean advanced=false;						//did the enemies advance last move.
	private boolean shooting=false;						//is the player shooting (for sprites)
	private boolean dying=false;						//""  ""   ""   dying     ""    ""
	private boolean abominationExisting = false;		//keeps track of when to draw the special enemy
	private boolean abominationDying = false;			//draws the "poof"
	private boolean []keys;	
	private boolean helpPaused=false;					//is the game paused do to the help menu (not the pause button)
	private boolean startingnewLevel=false;				
	private boolean gameOver=false;	
	private boolean gotname=false;       //did the player enter their name
	private boolean soundclicked=false;	//did the player click the sound button this time
							
	private boolean pphover=false;						//is the mouse hovering over the certain button
	private boolean soundhover=false;					
	private boolean helphover = false;				
	
	private boolean backhover=false;
	private boolean starthover=false;
	private boolean highscorehover=false;
	
	private boolean atmenuScreen=true;   //what screen is displayed. game/menu/highscore
	private boolean highScorepage=false;
	
	private ArrayList<int []> enemyShots = new ArrayList<int []>();  //list of enemy bullets
	private ArrayList<int []> playerShot = new ArrayList<int []>();	 //list of player bullets
	private ArrayList<String []> pasthighScore = new ArrayList<String []>();  //list of highscore with players' names
	private ArrayList<ArrayList<Dragon>> allEnemy = new ArrayList<ArrayList<Dragon>>();		//list of all the enemy (sorted into columns) 				
    
    ////IMAGES////////////////////////////////////////////////////////////////////////////////////////////////
	private ArrayList<Image> flyingSprites=new ArrayList<Image>(); 
	private ArrayList<Image> shootingSprites=new ArrayList<Image>(); //player sprites
	private ArrayList<Image> dyingSprites=new ArrayList<Image>();
	
	private ArrayList<Image> abominationPicsr = new ArrayList<Image>();  //Right facing
	private ArrayList<Image> abominationPicsl = new ArrayList<Image>();  //Left facing
	
	private ArrayList<Image> enemyShotpics = new ArrayList<Image>();   //bullet pics
	private ArrayList<Image> Shotpics = new ArrayList<Image>();
	
	private ArrayList<Image> scoreNums=new ArrayList<Image>();        //pictures of the numbers (this was before i figured out how to use drawString)
	
    private ArrayList<Image>normalButtons =new ArrayList<Image>();    //buttons 
    private ArrayList<Image>hoverButtons =new ArrayList<Image>();
    
    private String [] buttonNames= new String [] {"play","pause","sound","mute","help","back","start","highscore","return"}; //order of the buttons
    private Image heart=new ImageIcon("Images/Other/heart.png").getImage(); //life image
    
    private BufferedImage ppIN=null;       //to check if the mouse if over the actual button (buttons are irregular shaped)
    private BufferedImage shield =null;	  // picture of the shields
    private BufferedImage back=null;	 //picture of the background (used to slowing replace the parts of the shields that has been "hit")
    private BufferedImage draghit=null;	 //a cut out of the dragon, used when they hit the shields
    		
	private Image background;         // all the full screen images
	private Image border;
	private Image fade;               //shade for when game is paused
	private Image help;
	private Image levelCleared;
	private Image over;
	private Image menu;
	private Image highscoreBG;
	
	///ACTUAL GAME///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GamePanel(EpicFishWars mf){
		addMouseMotionListener(this);
		addMouseListener(this);
		load();
		keys = new boolean[KeyEvent.KEY_LAST+1];
		playerx = 510;
        playery = 230-sizey/2;	//starts in the middle, adjust for height of the sprite
        addKeyListener(this);
        setSize(frameSizex,frameSizey);  //sets to the background size
        mainFrame=mf;
    
	}
	
	public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
	public void load(){ ///loads stuff. and sorts them. makes dragons.
		background = new ImageIcon("Images/BG1.png").getImage();
		border = new ImageIcon("Images/border.png").getImage();
		fade = new ImageIcon("Images/fadeout.png").getImage();
		help = new ImageIcon("Images/Gamehelp.png").getImage();
		levelCleared =new ImageIcon("Images/Other/levelCleared.png").getImage();
		over = new ImageIcon("Images/Other/gameOver.png").getImage();
		menu= new ImageIcon("Images/Menu.png").getImage();
		highscoreBG = new ImageIcon("Images/highscores.png").getImage();
		
		for (String name:buttonNames){ //making arrays of buttons
			normalButtons.add(new ImageIcon("Images/Buttons/"+name+".png").getImage());
			hoverButtons.add(new ImageIcon("Images/Buttons/"+name+"hover.png").getImage());
		}
		for (int i=0;i<10;i++){
			scoreNums.add(new ImageIcon("Images/TextStuff/"+i+".png").getImage());
			
		}
		Pics charaPics = new Pics();
		for (int i=0;i<3;i++){
			allEnemy.add(new ArrayList<Dragon>()); //making a 2D array of dragons
		}
		
		for (int i=0;i<6;i++){
			allEnemy.get(0).add(new Dragon("Blue",0,i,30,charaPics)); //make the different type of dragons
			allEnemy.get(1).add(new Dragon("Red",1,i,20,charaPics));
			allEnemy.get(2).add(new Dragon("Green",2,i,10,charaPics));
			
		}
		
		flyingSprites=charaPics.getPics(0,3); //gets the pic for the player's sprites from another method.
		shootingSprites=charaPics.getPics(1,3);
		dyingSprites=charaPics.getPics(2,3);
		
		
		for (int i=1;i<4;i++){
			enemyShotpics.add(new ImageIcon("Images/Sprites/Shots/"+i+".png").getImage());
		}
		
		for (int i=1;i<3;i++){
			Shotpics.add(new ImageIcon("Images/Sprites/Player/Shots/"+i+".png").getImage());
		}
		for (int i=1;i<5;i++){
			abominationPicsr.add(new ImageIcon("Images/Sprites/Special/Right/"+i+".png").getImage());
			abominationPicsl.add(new ImageIcon("Images/Sprites/Special/Left/"+i+".png").getImage());
			
		}
		try { //these ones have to be checked for ioexception (file)
			shield = ImageIO.read(new File("Images/Shield/shields.png"));
			ppIN=ImageIO.read(new File("Images/Buttons/ppIN.png"));
			back=ImageIO.read(new File("Images/BG1.png"));
			draghit=ImageIO.read(new File("Images/Sprites/hit.png"));
		}
		catch (IOException e) {
		}
		
		Scanner infile = null;   //loading the past high scores
		try{ 
			infile=new Scanner (new File ("highscores.txt"));
			}
		catch (IOException ex){
			}
		n = Integer.parseInt(infile.nextLine());
		for (int i=0;i<n;i++){
			
			pasthighScore.add(infile.nextLine().split(","));
			
		}
		try { //loading the font
			File file=new File("kirbyss.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, file);
     		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     		ge.registerFont(font);
     		font=font.deriveFont(Font.PLAIN,25);
     		fontpts=font.deriveFont(Font.PLAIN,20);
			} 
		catch (IOException|FontFormatException e) {
			}
		
		
	}
	
	public void edithighScore(){ //making the new highscore at the end, sorts and takes only the top ten and writes them into the highscore file
		ArrayList<String []> newhighScore = new ArrayList<String []>(); //new list of scores with the player's sorted in
		String [] spts=new String [] {name,points+""}; //the player's score+username
		boolean added=false;
		for (String [] score: pasthighScore){
			if (points>=Integer.parseInt(score[1])&&added==false){ //checks if the player's score is higher 
				newhighScore.add(spts);
				newhighScore.add(score);
				added=true;
				
			}
			else{
				newhighScore.add(score); 
			}
			
		}
		if (added==false){
			newhighScore.add(spts); //player didn't beat anyone :(
		}
		n=Math.min(10,n+1); //make sures only top 10 are stored
		try {
        	File file = new File("highscores.txt");
          	BufferedWriter output = new BufferedWriter(new FileWriter(file));
          	output.write(n+"");
          	output.newLine();
          	for (int i=0;i<n;i++){
          		output.write(newhighScore.get(i)[0]+","+newhighScore.get(i)[1]);
          		output.newLine();
          	}
          
          	output.close();
          	
        } 
        
        catch ( IOException e ) {
        	
        }
        
	}
	
	@Override 
	public void paintComponent(Graphics g){ //draws stuff.
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.white);
        g.setFont(font); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //makes the font pretty when its drawn
		
		if (atmenuScreen==true&&highScorepage==false){  //draws the menu screen
			g.drawImage(menu,0,0,this);					//menu bg
			g.drawImage(getStartpic(),0,0,this); 		//start button
			g.drawImage(gethighScorepic(),0,0,this);	//highscore button
		
		}
		else if (atmenuScreen==true&&highScorepage==true){ //draws the highscore page
			g.drawImage(highscoreBG,0,0,this);			   //highscore bg
			drawHighscores(g);							   //the scores
			g.drawImage(getBackpic(),0,0,this);			   //the back button
		} 
			
		else{						//draws the stuff in the game
			g.drawImage(background,0,0,this); 
				
			shotcollide(g);			//"erases" shields that's been struck by the player's bullet
			enemyshotcollide(g);    //"erases" shields that's been struck by the enemies' bullet
			dragoncollide(g);		//"erases" shields that's been struck by an enemy
			g.drawImage(shield,0,0,this); //draws the updated shields
			soundclicked=false;   //sound button is assumed not clicked.
			
			String score=points+""; 
				
			g.setFont(fontpts);
			g.drawString(score,87,570); //draws the score
	    	
	    	for (int i=0;i<life;i++){  //draws the life
	    		g.drawImage(heart,79+25*i,522,this);
	    	}
	    	
			for (ArrayList <Dragon> draglist: allEnemy){//draws the living draging
				for (Dragon drag: draglist){
					if (drag.alive()==true){ //if game is paused the frames don't change.
						g.drawImage(drag.getPic(gamePaused),drag.getX(),drag.getY(),this);
					}
				}
			}
	
			
			if (gameOver==false){ //only living enemies ^ exist when the game ends.
				for (int[] shot: enemyShots){//draws the bullets. stored in the form [framecount, x, y]
					if (shot[0]<8){ //the 1st frame (so the metal ball) is drawn for the first 7 instances
						g.drawImage(enemyShotpics.get(shot[0]/4),shot[1],shot[2],this);
					}
					else{ //FIREBALL.
						g.drawImage(enemyShotpics.get(2),shot[1],shot[2],this);
					}
					if (gamePaused==1){ //if the game is paused frames do not change
						shot[0]+=1;
					}
				} 
				if (playerShot.size()==1){ //if you shot a bullet, the program draws it. However there can only be ONE bullet. I kept in this format beacuse it was easier.
					if (playerShot.get(0)[0]<10){
						g.drawImage(Shotpics.get(0),playerShot.get(0)[1],playerShot.get(0)[2],this);
					}
					else{
						g.drawImage(Shotpics.get(1),playerShot.get(0)[1],playerShot.get(0)[2],this);
					}
					if (gamePaused==1){
						playerShot.get(0)[0]+=1;
					}
				}
				if (abominationExisting==true){ //if the spcecial enemy exist. draw it.
					g.drawImage(getabominationPic(),(int) abomination.getX(),(int) abomination.getY(),this);
				}
				g.drawImage(getplayerPic(),playerx,playery,this); //draw the player too.
			}
			
			g.drawImage(getSoundpic(),454,522,this); //draws the sound button
			if (nothelping==1){						//if not on the help popup screen, you can pause and go the the help screen (so the buttons can change)
				g.drawImage(getHelppic(),387,522,this); //draw help button
				if (gamePaused==-1){					//if the game is paused, draw the gray overtone
					g.drawImage(fade,0,0,this);	
				}
				g.drawImage(getPlayingpic(),519,522,this);	//draw play/pause button
			}
			else{
				g.drawImage(getPlayingpic(),519,522,this); //this will make the play/button appear under the help screen denoting that it can not be clicked
				g.drawImage(help,0,0,this);	
				g.drawImage(getHelppic(),387,522,this);
				
			}
			
			if (startingnewLevel==true){
				g.drawImage(levelCleared,0,0,this);	
				
				newlevelCount-=1;//counts down to next level - cool down time
				if (newlevelCount==0){
					newLevel();
					startingnewLevel=false;
				}
			}
			if (gameOver==true){
				g.drawImage(over,0,0,this); //draw the game over screen
			}
			
			g.drawImage(border,0,0,this);  //to make sure the the special enemy doesn't appear to be flying over the borders.
		}
	}
	public void drawHighscores(Graphics g){
		for (int i=0;i<n;i++){
			int y=144+30*i; //location of where to draw the words
			int namex=102;
			int numx=418;
			String [] line = pasthighScore.get(i);
			if (line[0].length()<=15){
				g.drawString(line[0], namex,  y);
			}
			else{
				String temp=line[0].substring(0,15)+"...";
				g.drawString(temp, namex,  y);
			}
			g.drawString(line[1], numx,  y);
			
			
		}
	}
	public void enemyshotcollide(Graphics g){ //checks if the shot hit a shield and makes holes if it did
		if (enemyShots.size()>0){
			ArrayList<int []> removeShots = new ArrayList<int []>(); 
			for (int [] shot:enemyShots){
				if (shot[1]>400&&shot[1]+32<590){ //only check for shots that CAN hit a shield
					int c = shield.getRGB(shot[1]+32,shot[2]); //if the front edge of the shot is on the shield it will return a color(!=0), otherwise its trasparent (so 0)
					int c2=0;
					for (int k=0;k<13;k++){
						c2 = shield.getRGB(shot[1]+32,shot[2]+k);
						if (c2!=0){
							break;
						}
					}
					if (c!=0||c2!=0){ //if it did hit
						removeShots.add(shot);
						for (int i=0;i<20;i++){ //the exposion is in a circle centered at the middle of the front edge
							for (int j=0;j<20;j++){ //goes through a squared centered at the same point with side length 20
								int x = shot[1]+22+i; //do math
								int y = shot[2]-6+j;
								int a=shot[1]+32;
								int b=shot[2]+6;
								double d=Math.pow(a-x,2);
								double e=Math.pow(b-y,2);
								double dist= Math.pow(d+e,0.5); //if the distance is 10 or less then its in the ratius of the circle
								if (dist<=10){
									shield.setRGB(x,y,0);   //erase the pixel (replace with transparent, it can't be hit again)
								}
							}	
						}	
					}
					
				}
			}
			for (int [] shot: removeShots){ 
				enemyShots.remove(shot);	
			}
		}
	}
	
	public void shotcollide(Graphics g){ //same thing here, but with the player's bullets
		if (playerShot.size()>0){
			int [] shot=playerShot.get(0);
			if (shot[1]>400){
				int c = shield.getRGB(shot[1],shot[2]);
				int c2=0;
				for (int k=0;k<19;k++){
						c2 = shield.getRGB(shot[1],shot[2]+k);
						if (c2!=0){
							break;
						}
					}
					if (c!=0||c2!=0){
						for (int i=0;i<20;i++){
							for (int j=0;j<20;j++){
								int x = shot[1]-10+i;
								int y = shot[2]+j;
								int a=shot[1];
								int b=shot[2]+10;
								double d=Math.pow(a-x,2);
								double e=Math.pow(b-y,2);
								double dist= Math.pow(d+e,0.5);
								if (dist<=10){
									int c3=back.getRGB(x,y);
									shield.setRGB(x,y,0);
								}	
							}
						}
						playerShot.remove(shot);
					}
				}
			}
	}
	
	public void dragoncollide(Graphics g){ //dragons destory shields
		for (ArrayList <Dragon> draglist: allEnemy){
			for (Dragon drag: draglist){
				if (drag.getX()+50>=playerx){
					gameOver=true;
				}
				else if (drag.getX()+50>400){

					for (int i=0;i<55;i++){     //if the dragon overplaps with the shield, the shield is erased
						for (int j=0;j<45;j++){
							int x = drag.getX()+i;
							int y = drag.getY()+j;
							int c = shield.getRGB(x,y);
							int c2=draghit.getRGB(i,j);
							if (c2!=0&&c!=0){
								shield.setRGB(x,y,0);
							}
						}
					}
				}
			}
		}
	}
	public void checkHover(){ //checks if the mouse is hovering over a button based on its location
		pphover=false;   	//only check when in game
		soundhover=false;	//only check when in game
		helphover=false;	//only check when in game
		starthover=false;	//only check when in menu
		highscorehover=false;//only check when in menu
		backhover=false;  //only check when in highscore
		if (highScorepage==true){
			if (mx-360>=0&&mx-360<=213&&my-537>=0&&my-537<=30){
				backhover=true;
			}
			
		}
		if (atmenuScreen==true&&highScorepage==false){
			if (mx>=211&&mx<=382&&my>=444&&my<=479){
				starthover=true;
			}
			else if (mx>=162&&mx<=427&&my>=515&&my<=542){
				highscorehover=true;
			}
		}
		if (my-522>=0&&my-522<=50 && startingnewLevel==false&&gameOver==false&&atmenuScreen==false){ 
			if (nothelping==1){
				if (mx-519>=0&&mx-519<=50){
					
					if (ppIN.getRGB(mx-519,my-522)!=0){
						pphover=true;
					}
				}
			}
			if (gamePaused==1&&nothelping==1){
				if (mx-454>=0&&mx-454<=50){
					if (ppIN.getRGB(mx-454,my-522)!=0){
						soundhover=true;
					}
				}
				
			}
			if (gamePaused==1||helpPaused==true){
				if (mx-387>=0&&mx-387<=50){
					if (ppIN.getRGB(mx-387,my-522)!=0){
						helphover=true;
					}
				}
				
			}
		}
	}
	public void checkDragDeath(){//checks if the player killed an enemy/secial enemy
		if (playerShot.size()==1){
			if (abominationExisting==true){
				if (abomination.getX()<=playerShot.get(0)[1]+19&&abomination.getX()+52>=playerShot.get(0)[1]&&abomination.getY()<=playerShot.get(0)[2]+17&&abomination.getY()+40>=playerShot.get(0)[2]&&abominationDying==false){//special enemy is just a point, so it has to be checked this way
					abominationDying=true;
					points+= (int) (Math.random()*500);  
					playerShot.remove(0);
				}
			}
			if (playerShot.size()==1){
				for (ArrayList <Dragon> draglist: allEnemy){
					for (Dragon drag: draglist){
						if (drag.alive()==true){ //dragon can only die if it is still alive
							if (drag.getHit(playerShot.get(0)[1],playerShot.get(0)[2])==true){
								playerShot.remove(0);  //there is only one bullet so removing it now won't mess up the loop
								livingChart[drag.getLocc()][drag.getLocr()]=1; //change its status in the chart
								points+=drag.getPoints();
								dragonsKilled+=1;
								for (int i=drag.getLocc()-1;i>=0;i--){ //sets the "shooter" in that row, is the first one not dead from the right
									if (livingChart[i][drag.getLocr()]==0){
										allEnemy.get(i).get(drag.getLocr()).setShooter();
										break;
									}
								}
								break;
							}
						}
					}
					if (playerShot.size()==0){
						break;
					}
				}
			}
		}
		//return temp;	
	}
	public int checkHit(){ //checks if the player got hit
		ArrayList<int []> remove= new ArrayList<int[]>();;
		for (int [] shot: enemyShots){
			if (playerx<=shot[1]+32&&playerx+52>=shot[1]&&playery<=shot[2]+19&&playery+40>=shot[2]&&dying==false){ //math. collide box 
				life-=1;
				dying=true;
				remove.add(shot);
			}
		}
		for (int [] shot: remove){
			enemyShots.remove(shot);
		}
		return life;
	}
	
	public void move(){ //move the stuff
		life=checkHit();
		if (life==0){ 
			gameOver=true;
		}
		
		checkDragDeath();
				
		if (dragonsKilled<18){
			if (movestall%(Math.max(90-dragonsKilled*5,30))==0){	//this causes dragons to move faster as more are killed
				moveDragons();
			}
		}
		if (dragonsKilled==18){
			newlevelCount=200;
			startingnewLevel=true;
		}
		
		moveAbomination();
		
		moveShots(enemyShots,1,540); //direction and boundary
		moveShots(playerShot,-1,10); 
		if (dying==false){
			movePlayer();
		}	
		movestall+=1;
	}
	public void moveAbomination(){
		if (abominationExisting==false){
			int rand=(int)(Math.random()*5000);	//randomly generates one (maybe) if no special enemy exists
			if (rand<2){
				abominationExisting=true;
			}
		}
		if (abominationExisting==true&&abominationDying==false){ //it exist and not going "poof"
			if (abomination.getY()<=45){ //if it is at the top
				if ((int) abomination.getX()-1>-45){ //move it to the left until its out of site
					abomination.move((int) abomination.getX()-1,(int) abomination.getY());
				}
				else{ //the reset it
					abomination.move(-45,445);
					abominationCount=0;
					abominationExisting=false;
				}
			}
			else if (abomination.getY()==445&&abomination.getX()<10){ //starting out, move it right
				abomination.move((int) abomination.getX()+1,(int) abomination.getY());
			}
			else{ //move it up
				abomination.move((int) abomination.getX(),(int) abomination.getY()-1);
			}
		}
		
	}
	public void moveDragons(){ //moving the dragons
		boolean atEnd=false;
		
		if (advanced==false){ //if it didn't go up last move (it can ONLY move in one direction)
			for (ArrayList <Dragon> draglist: allEnemy){
				for (Dragon drag: draglist){
					if (drag.alive()==true){ //checks all living dragon to set if it is at the end
						if (drag.outofBounds()==true){ 
							atEnd=true;
							break;
						}
					}
				}
			}
		}
		
		if (atEnd==true && advanced==false){ //moving the enemys forward.
			advanced=true;
			for (ArrayList <Dragon> draglist: allEnemy){
				for (Dragon drag: draglist){
					if (drag.alive()==true){
						drag.moveX();
					}
					
				}
			}
		}
		if (atEnd==false){ //regular up/down movement 
			for (ArrayList <Dragon> draglist: allEnemy){
				for (Dragon drag: draglist){
					if (drag.alive()==true){
						drag.moveY(advanced); //if advanced is true enemies change their direction
					}
				}
			}
			advanced=false;
		}
		for (ArrayList <Dragon> draglist: allEnemy){
			for (Dragon drag: draglist){
				if (drag.alive()==true){ //shooting is a type of movement. only alive dragons can shoot
					enemyShots=drag.shoot(enemyShots); //makes bullet (maybe) and adds it the the array of bullets
				}
			}
		}
	}
	
	public void moveShots(ArrayList<int[]> shots, int dir, int outofbounds){ //move the bullets (both enemy and players)
		ArrayList <int[]>temp= new ArrayList<int []>(); //direction of travel and boundary line are given
		
		for (int [] shot: shots){
			if (dir==1 && shot[1]>outofbounds ||dir==-1&& shot[1]<outofbounds){
				temp.add(shot);
			}
			shot[1]+=3*dir;
		}
		
		for (int [] outShots: temp){ //deletes the ones out of bounds
			shots.remove(outShots);
		}
	}
	
	public void movePlayer(){//move the player
		if(keys[KeyEvent.VK_S]||keys[KeyEvent.VK_DOWN] ){
			playery = Math.min(playery+3,490-(sizey+5));
		}
		if(keys[KeyEvent.VK_W]||keys[KeyEvent.VK_UP] ){
			playery = Math.max(playery-3,30);
		}
		if(keys[KeyEvent.VK_SPACE]||keys[KeyEvent.VK_A] ){
			if (shooting==false&&playerShot.size()==0){
				shooting=true;
				playerShot.add( new int [] {0, playerx,20+playery});
			}
		}
		
	}
	
	public void newLevel(){ //resets stuff for the new level
		level+=1;
		livingChart= new int [3][7];
		life=Math.min(life+1,5);
		dragonsKilled=0;
		enemyShots = new ArrayList<int []>();
		playerShot = new ArrayList<int []>();
		for (ArrayList <Dragon> draglist: allEnemy){
				for (Dragon drag: draglist){
					drag.reload(level); //reloads enemys (makes them faster)
				}
		}
			
	}
	
	public Image getabominationPic(){ //gets the frame of the special enemy based on the its state and frame count of that state
		if (gamePaused==1){
			abominationCount=(abominationCount+1)%40;
		}
		if (abominationDying==false){
			if (abomination.getY()>45){
				return abominationPicsr.get(abominationCount/10); //it goes to the left here
			}
			else{
				return abominationPicsl.get(abominationCount/10);
			}
		}
		else{
			abominationdeathCount+=1; //dying takes time. and different sprites. 
			if (abominationdeathCount==49){
				abominationDying=false;
				abominationExisting=false;
				abominationdeathCount=0;
				abomination.move(-45,445);
				return dyingSprites.get(4);
			}
			return dyingSprites.get(abominationdeathCount/10);
		}
		
		
	}
	public Image getplayerPic(){ //same thing here, but for the player
		if (gamePaused==1){
			frameCount=(frameCount+1)%40;
		}
		if (shooting==false && dying==false){
			return flyingSprites.get(frameCount/10);
		}
		
		else if (shooting==true && dying==false){
			if (gamePaused==1){
				shotframeCount=shotframeCount+1;
			}
			if (shotframeCount==29){
				shotframeCount=0;
				shooting=false;
				return shootingSprites.get(2);
			}
			return shootingSprites.get(shotframeCount/10);
		}
		else{
			if (gamePaused==1){
				deathCount+=1;
			}
			if (deathCount==49){ 
				deathCount=0;
				dying=false;
				return dyingSprites.get(4);
			}
			return dyingSprites.get(deathCount/10);
		}
		
	}
	
/////GETS THE PICTURE OF THE BUTTON. NORMAL OR HOVERING VERSION. //////////////////////////////////////////////////////////////////////////////////////////////
	public Image getBackpic(){ 
		if (backhover==true){
			return hoverButtons.get(8);
		}
		else{
			return normalButtons.get(8);
		}
	}
	public Image getStartpic(){
		if (starthover==true){
			return hoverButtons.get(6);
		}
		else{
			return normalButtons.get(6);				
		}
	}
	public Image gethighScorepic(){
		if (highscorehover==true){
			return hoverButtons.get(7);
		}
		else{
			return normalButtons.get(7);				
		}
	}
	public Image getHelppic(){
		if (helphover==true){
			if (nothelping==1){
				return hoverButtons.get(4);
			}
			else{
				return hoverButtons.get(5);
			}
		}
		else{
			if (nothelping==1){
				return normalButtons.get(4);	
				}
			else{
				return normalButtons.get(5);
			}	
		}
	}
	
	public Image getPlayingpic(){
		if (gamePaused==1){
			if (pphover==true){
			
				return hoverButtons.get(1);
			}	
			else{
				return normalButtons.get(1);
			}
		}
		
		else{
			if (pphover==true){
				return hoverButtons.get(0);
			}
			else{
				return normalButtons.get(0);
			}	
		}
	}
	public Image getSoundpic(){
		if (soundhover==true){
			if (soundOn==1){
				return hoverButtons.get(2);
			}
			else{
				return hoverButtons.get(3);
			}
		}
		else{
			if (soundOn==1){
				return normalButtons.get(2);	
				}
			else{
				return normalButtons.get(3);
			}	
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void usegetName(){ //get the user's name at the end
		name = JOptionPane.showInputDialog("Name:");
		edithighScore();
		gotname=true;	
	}	

//////Getting Stuff for Panel ////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean getgameOver(){
		return gameOver;
	}
	public boolean getPaused(){
		if (gamePaused==-1){
			return true;
		}
		return false;
	}
	public boolean getsoundClicked(){
		return soundclicked;
	}
	public boolean getSound(){
		if (soundOn==1){
			return true;
		}
		return false;
	}
	public boolean getgotName(){
		return gotname;
	}
	public boolean getlevelStarting(){
		return startingnewLevel;
	}
	public boolean onMenu(){
		return atmenuScreen;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseClicked(MouseEvent e){}  
    public void mousePressed(MouseEvent e){  //checks where the click was located, if it was on a button.
    	if (highScorepage==true&&backhover==true){
    		highScorepage=false;
    	}
    	if (atmenuScreen==true&&highScorepage==false){
    		if (starthover==true){
    			atmenuScreen=false;
    		}
    		if (highscorehover==true){
    			highScorepage=true;
    		}
    	}
    	if (pphover==true&&nothelping==1&&startingnewLevel==false&&gameOver==false&&atmenuScreen==false){ 
    		gamePaused*=-1;
    		soundclicked=true;
    		if (this.getPaused()==true){
    			soundBefore=soundOn;
    			soundOn=-1;
    		}
    		else{
    			soundOn=soundBefore;
    		}
    	}
    	
    	if (soundhover==true&&nothelping==1){
    		soundOn*=-1;
    		soundclicked=true;
    	}
    	if (helphover==true){
    		nothelping*=-1;
    		if (nothelping==-1){
    			gamePaused=-1;
    			helpPaused=true;
    		}
    		else{
    			helpPaused=false;
    			gamePaused=1;
    		}
    		
    	}
	}
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
    	mx=e.getX();
    	my=e.getY();
    	checkHover();
    }
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    } 
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
}
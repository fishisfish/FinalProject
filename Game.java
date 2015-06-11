/**
 * @(#)Game.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JPanel.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.LinkedList;
public class Game extends JFrame implements ActionListener {
	Timer myTimer,clockTimer;
 	GamePanel map;
 	public Game() {  //setting up graphic bits
 		super("Game");
	 	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		setSize(800,600);
 		map=new GamePanel(this);
 		map.setLocation(0,0);
 		map.setSize(800,600);
 		add(map);
 		myTimer = new Timer(10,this);
 		clockTimer = new Timer (1000,this);
 		setResizable(false);
 		setVisible(true);
 		
    }
    public static void main (String [] args){ 
		new Game();
	}
    public void start(){
    	myTimer.start();
    	clockTimer.start();
    }
    public void actionPerformed(ActionEvent evt){
    	Object source = evt.getSource();
    	if (source==clockTimer){
    		//System.out.println("CLOCKCLOCKCLOCKCLOCK");
    		map.second();
    	}
    	if (source==myTimer){
    		map.move();
    		map.repaint();
    	}
    	
    		
    	
		
    }

}
 
class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{ 
	private Image white;
	private Game mainFrame;
	private int mosX,mosY,ncamX,ncamY,camX,camY;
	private boolean keyPressed;
	private boolean mousePressed;
	private boolean droppedCam=true;
	private boolean [] keys;
	private boolean canSome=false;
	private boolean jumpOff;
	private Person chara;
	private Level level;
	private int timePassed=0;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private Color ORANGE = new Color (255,153,0);
	private Color INDIGO = new Color (138,0,255);
	private Color YELLOW = new Color(255,252,118);
	private Color ICE = new Color(170,255,236);
	private Font font;
	public GamePanel(Game m){
		mainFrame=m;
		load();
		mosX=0;
		mosY=0;
		
		int lev = 1; //chooseLevel();
		level = new Level(lev);
		chara=new Person(level);
		chara.setX(level.getDropx());
		chara.setY(level.getDropy());
		camX=level.getDropx();
		camY=level.getDropy()-300;
		white = new ImageIcon("white2.png").getImage();
	//	System.out.println("dd");
		keys = new boolean[65535];
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
	}
	public void load(){
		try { //loading the font
			File file=new File("kirbyss.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, file);
     		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     		ge.registerFont(font);
     		font=font.deriveFont(Font.PLAIN,25);
     		//fontpts=font.deriveFont(Font.PLAIN,20);
			} 
		catch (IOException|FontFormatException e) {
			}
	}
	public int chooseLevel(){
		Scanner kb = new Scanner(System.in);
		System.out.println("Choose a level:");
		int lev = kb.nextInt();
		return lev;
	}
	public void move(){
		chara.checkHit(level.getMap());
		moveLevel();
		moveChara();
		moveCam();
		
	} 
	public void second(){
		timePassed+=1;	
	}
	public String getTime(int time){
		int sec=time%60;
		String sSec=sec+"";
		if (sec<10){
			 sSec="0"+sSec;
		}
		int min=time/60;
		String sMin=min+"";
		if (min<10){
			 sMin="0"+sMin;
		}
		String tmp = sMin+":"+sSec;
		//System.out.println(tmp);
		return tmp;
	}
	public void moveChara(){
		//getTime(timePassed);
		if (chara.getleavingWater()==true){
				//	System.out.println("LEFTWATER");
					canSome=false;
					//jumpOff=false;
		}
		//System.out.println("CANSOME: "+canSome);
		//System.out.println("UP-PRESSED: "+keys[KeyEvent.VK_UP]);
		chara.checkWalking(false);
		if (chara.getSwim()==false){
			if (keys[KeyEvent.VK_LEFT]){
				chara.checkWalking(true);
				chara.changeDir(LEFT);
			}
			else if (keys[KeyEvent.VK_RIGHT]){
				chara.checkWalking(true);
				chara.changeDir(RIGHT);
			}
			if (keys[KeyEvent.VK_UP]){
				jumpOff=chara.jump();
				//System.out.println("JF: "+jumpOff);
				if (jumpOff==true){
					canSome=false;
				}
				if (jumpOff==false&&canSome==true&&chara.getCling()==false){
					///System.out.println("CHANGE");
					canSome=false;
					chara.some("");
				}
				if ((chara.getCling()==true||chara.getPCling()==true)&&canSome==true){
					canSome=false;
					System.out.println("OFFWALL");
					chara.some("OFFWALL");
				}
			}
			else if (keys[KeyEvent.VK_DOWN]){
				chara.slide();
			}
			
			if (keys[KeyEvent.VK_UP]==false){
				//System.out.println("CANSUM");
				canSome=true;
			}
			
			if (keys[KeyEvent.VK_DOWN]==false){		
				chara.getUp();
			}
			chara.move();
			chara.gravity();
		}
		else{
			chara.swimSlow();
			chara.sink();
			if (keys[KeyEvent.VK_LEFT]){
				chara.swim("LEFT", level.getMap());
			}
			if (keys[KeyEvent.VK_RIGHT]){
				chara.swim("RIGHT", level.getMap());
			}
			if (keys[KeyEvent.VK_UP]){
				chara.swim("UP", level.getMap());
				//System.out.println("UPPP");
				//System.out.println("PROPEL: "+chara.getleavingWater());
				
			}
			if (keys[KeyEvent.VK_DOWN]){
				chara.swim("DOWN", level.getMap());
			}
			chara.rotate();	
		}
		
	
			
	}
	public void moveCam(){
		if (chara.getMoved()==false){
			if (Math.abs(camX-chara.getX())>3){
				if (Math.abs(camX-chara.getX())>50){
					if (camX>chara.getX()){
						camX-=5;
					}
					else{
						camX+=5;
					}
				}
				else{
				
					if (camX>chara.getX()){
						camX-=2;
					}
					else{
						camX+=2;
					}
				}
			}
			else{
				camX=chara.getX();
			}
		}
		
		else{
			if (Math.abs(camX-chara.getX())>20){
				if (chara.getX()>600&&chara.getDir()==1){
						camX+=chara.getxMoved();
					}
				else if (chara.getX()<1400&&chara.getDir()==-1){
						camX+=chara.getxMoved();
				}
				}
			}
		
		camX=Math.max(400,camX);
		camX=Math.min(level.getWidth(),camX);
		if (chara.getinAir()==false){
			//System.out.println(camY-chara.getY());
			if (Math.abs(camY-chara.getY())>3){
				if (Math.abs(camY-chara.getY())>50){
					if (camY>chara.getY()){
						camY-=5;
					}
					else{
						if(camY<chara.getY()){
						camY+=5;
						}
					}
				}
				else{
					if (camY>chara.getY()){
						camY-=2;
					}
					else{
						if(camY<chara.getY()){
						camY+=2;
						}
					}
				}
				
			}
			else{
				camY=chara.getY();
			}
		}
		else{
		//	System.out.println("DER");
			if (chara.getyMoved()>0){
				chara.reachingApex(false);
			}
			if (camY>=300){
				chara.reachingApex(true);
			}
			if (Math.abs(camY-chara.getY())>20||chara.reachedApex()==false&&chara.getyMoved()<0){
				//System.out.println("HERP");
				//System.out.println(chara.reachedApex());
				//System.out.println(chara.getApex());
				//System.out.println(camY);
				if (chara.reachedApex()==false&&camY>chara.getApex()){
					System.out.println("MERP");
					if (Math.abs(camY-chara.getY())>15){
						camY+=-1*(Math.abs(chara.getyMoved())+3);
					}
					else{
						camY+=-1*Math.abs(chara.getyMoved());
					}
					if (camY<=chara.getApex()){
						chara.reachingApex(true);
					}
				}
					
				else{
					if (Math.abs(camY-chara.getY())>50){
						camY+=-1*(chara.getyMoved()+5);
					}
					else{
						camY+=-1*chara.getyMoved();
					}
				}
				
			} 
		}
		camY=Math.max(300,camY);
		camY=Math.min(level.getHeight(),camY);
		if (chara.getDeath()==true){
			System.out.println("DIE");
			if (camX>chara.getX()){
				camX=chara.getX()+100;
			}
			else{
				camX=chara.getX()-100;
			}
			if (camY>chara.getY()){
				camY=chara.getY()+100;
			}
			else{
				camY=chara.getY()-100;
			}
			camX=Math.max(400,camX);
			camX=Math.min(level.getWidth(),camX);
			camY=Math.max(300,camY);
			camY=Math.min(level.getHeight(),camY);
		}
	}
	public void moveLevel(){
		level.movePlatforms();
		level.moveTraps();
		level.meltIce(camX,camY);
	}
	public int camAdjust(String temp,int ori){
		if (temp=="X"){
			//System.out.println(temp+"CAM"+camX);
			return ori-camX+400;
		}
		
		if (temp=="Y"){
			return ori-camY+300;
		}
		return 0;
	}
	public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    @Override
    public void paintComponent(Graphics g){
    	Graphics2D g2 = (Graphics2D)g;
    	g.drawImage(white,0,0,this);
    	g.drawImage(level.getMap(),camAdjust("X",0),camAdjust("Y",0),this);
    	g.drawRect(camAdjust("X",chara.getX()-15),camAdjust("Y",chara.getY())-25,30,45);
    	ArrayList<Platform> tmpP = level.getPlats();
    	ArrayList<Traps> tmpT = level.getTraps();
    	for(int i =0;i< tmpT.size();i++){
			AffineTransform saveXform = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(tmpT.get(i).getAng()),camAdjust("X",tmpT.get(i).getX()),camAdjust("Y",tmpT.get(i).getY()));
			g2.transform(at);
			g2.drawImage(tmpT.get(i).getPic(),camAdjust("X",tmpT.get(i).getDX()),camAdjust("Y",tmpT.get(i).getDY()),this);
			//stem.out.println(this);
			g2.setTransform(saveXform);
    	}
    	for(int i =0;i< tmpP.size();i++){
    		
    	 //DRAW PLATFORMS
    	//	System.out.println("TYPE:"+tmpP.get(i).getType());
    	//	System.out.println();
    		if ((tmpP.get(i).getType()).equals("MOVING")==true){
    		//	System.out.println("ORANGE");
    			g.setColor(ORANGE);
    		}
    		if ((tmpP.get(i).getType()).equals("DROPPING")==true){
    			//System.out.println("INDIGO");
    			g.setColor(INDIGO);
    		}
    		if ((tmpP.get(i).getType()).equals("BOUNCING")==true){
    			g.setColor(YELLOW);
    		}
    		if ((tmpP.get(i).getType()).equals("ICE")==true){
    			g.setColor(ICE);
    			//System.out.println("ICE");
    		}
    		g.fillRect(camAdjust("X",tmpP.get(i).getX()),camAdjust("Y",tmpP.get(i).getY()),tmpP.get(i).getWidth(),tmpP.get(i).getHeight());
    	}
    	ArrayList<Image> checkPics = level.getCheckPics();
		ArrayList<int[]> checkPoints = level.getCheckPoints();
		for (int i=0;i < checkPics.size();i++){
			if(level.getCheckPassed().get(i) == true){
				g.drawImage(checkPics.get(i),camAdjust("X",0),camAdjust("Y",0),this);
			}
		}
    	Image pic=chara.getPic();
    	if (chara.getSwim()==false){
    		
    		if (chara.getCling()==false){
	    		g.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
    		}
    		else{
    			g.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)+chara.getDir()*5),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
    		}
    	}
    	else{
    	
			AffineTransform saveXform = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians((90-chara.getAn())%360),camAdjust("X",chara.getX()),camAdjust("Y",chara.getY()));
			g2.transform(at);
			g2.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
			g2.setTransform(saveXform);
			
    	}

    	g.setFont(font); 
		g.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //makes the font pretty when its drawn
		g.drawString(getTime(timePassed),700,40);
    }
    public void mouseReleased(MouseEvent e){
    	mousePressed=false;
    }
    public void mousePressed(MouseEvent e){
    	mousePressed=true;
    	mosX=e.getX();
    	mosY=e.getY();
    	}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseDragged(MouseEvent e){
    	mosX=e.getX();
    	mosY=e.getY();
    }
    public void mouseMoved(MouseEvent e){}
    
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e){
    	keyPressed=true;
    	keys[e.getKeyCode()]=true;
    }
    public void keyReleased (KeyEvent e){
    	keyPressed=false;
    	keys[e.getKeyCode()]=false;
    }
}
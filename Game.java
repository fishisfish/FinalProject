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
import javax.swing.*;
import javax.swing.JPanel.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
public class Game extends JFrame implements ActionListener {
	Timer myTimer;
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
 		setResizable(false);
 		setVisible(true);
 		
    }
    public static void main (String [] args){ 
		new Game();
	}
    public void start(){
    	myTimer.start();
    }
    public void actionPerformed(ActionEvent evt){
    	Object source = evt.getSource();
    	if (source==myTimer){
    		map.move();
    	}
		map.repaint();
    }

}
 
class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private Image background,actmap;//=new ImageIcon("Castiel.png").getImage();; 
	private Game mainFrame;
	private int mosX,mosY,ncamX,ncamY,camX,camY;
	private boolean keyPressed;
	private boolean mousePressed;
	private boolean droppedCam=true;
	private boolean [] keys;
	private Person chara;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	public GamePanel(Game m){
		mainFrame=m;
		mosX=0;
		mosY=0;
		chara=new Person();
		//ncamX=400;
		//ncamY=300;
		camX=400;
		camY=300;
		background= new ImageIcon("white.png").getImage();
		actmap= new ImageIcon("Maps/Act 1.png").getImage();
		keys = new boolean[65535];
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
	}
	public void move(){
		moveChara();
	}
	public void moveChara(){
		chara.checkWalking(false);
		if (keys[KeyEvent.VK_LEFT]){
			chara.checkWalking(true);
			chara.changeDir(LEFT);
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			chara.checkWalking(true);
			chara.changeDir(RIGHT);
		}
		if (keys[KeyEvent.VK_UP]){
			chara.jump();
		}
		
		chara.move();
		chara.gravity();
		if (chara.getMoved()==false){
			//ncamX=chara.getX();
			//ncamY=chara.getY();
			if (Math.abs(camX-chara.getX())>3){
				System.out.println("CATCHING UP");
				camX+=3*chara.getDir();
			}
			else{
				System.out.println("STOP");
				camX=chara.getX();
			}
			//camY=ncamY;	
			//droppedCam=true;
		}
		else{
			System.out.println("FOLLOW");
			if (Math.abs(camX-chara.getX())>100){
				camX+=chara.getxMoved();
			}
		}
		/*if (droppedCam==false&&Math.abs(ncamX-chara.getX())<100){
			System.out.println("MOVE AND DROP");
			ncamX=chara.getX();
			ncamY=chara.getY();
			camX=ncamX;
			camY=ncamY;	
			droppedCam=true;
		}
		if (droppedCam==true&&Math.abs(ncamX-chara.getX())>100){
			System.out.println("RUNNING WITH CAM");
			droppedCam=false;
			
		}
		if (droppedCam==false){
			System.out.println("MOVING CAM");
			camX+=chara.getxMoved();
			camY+=chara.getyMoved();
			System.out.println(camX);
		}
		//System.out.println("N"+ncamX);
		//System.out.println(camX);*/
			
	}
	public int camAdjust(String temp,int ori){
		if (temp=="X"){
			System.out.println(temp+"CAM"+camX);
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
    	g.drawImage(background,0,0,this);
    	g.drawImage(actmap,-(camX-400),0,this);
    	Image pic=chara.getPic();

    	g.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
    	System.out.println(camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)));
    	
    	//System.out.println(chara.getX());
    	//System.out.println(chara.getY());
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
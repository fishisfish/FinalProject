/**
 * @(#)Game.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */
//lolololololo
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
	private Image background;//=new ImageIcon("Castiel.png").getImage();; 
	private Game mainFrame;
	private int x,y;
	private boolean keyPressed;
	private boolean mousePressed;
	private boolean [] keys;
	private Person chara;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	public GamePanel(Game m){
		mainFrame=m;
		x=0;
		y=0;
		chara=new Person();
		background= new ImageIcon("Castiel.png").getImage();
		keys = new boolean[65535];
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
	}
	public void move(){
		moveChara();
	}
	public void moveChara(){
		if (keys[KeyEvent.VK_LEFT]){
			chara.changeDiection(LEFT);
			chara.moveHor();
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			chara.changeDiection(RIGHT);
			chara.moveHor();
		}
		if (keys[KeyEvent.VK_UP]){
			System.out.println("JUMP");
			chara.jump();
		}
		chara.gravity();
	}
	public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    @Override
    public void paintComponent(Graphics g){
    	g.drawImage(background,0,0,this);
    	g.drawImage(chara.getPic(),chara.getX(),chara.getY(),this);
    	//System.out.println(chara.getX());
    	//System.out.println(chara.getY());
    }
    
    public void mouseReleased(MouseEvent e){
    	mousePressed=false;
    }
    public void mousePressed(MouseEvent e){
    	mousePressed=true;
    	x=e.getX();
    	y=e.getY();
    	}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseDragged(MouseEvent e){
    	x=e.getX();
    	y=e.getY();
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
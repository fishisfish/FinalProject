/**
 * @(#)Person.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */


public class Person {
	private int x,y;
	private double vel;
	private int direction=1;
	private static final int RIGHT=1;
	private static final int LEFT=-1;

    public Person() {
    	x=400;
    	y=500;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    	
    
    
}
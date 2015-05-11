/**
 * @(#)Traps.java
 *
 *
 * @Iris Chang
 * @version 1.00 2015/5/5
 */


public class Traps {
	private int type;
	public static final int SPIKES = 0;
	public static final int CIRC_SAW = 1;
	public static final int NINJA_STAR = 2;
	public static final int LASER = 3;
	public static final int SWING_SAW =4;
	public static final int ROT_STAR = 5;
	private boolean stationary;
	private int x,y;
	private int dx,dy;
	private int speed;
    public Traps() {
    	
    }
    
    public move(){
    //should the main program check if the trap is stationary before calling this method or implement the distinction withint the method?
    	
    }
}
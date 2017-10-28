/*
 * TCSS 305 - Easy Street
 */

package model;

/**
 * @author Tanner Brown
 * @version 25 Oct 2017
 */
public class Atv extends AbstractVehicle {
    /*
     * Behavior:
     * 
     * ATVs can travel on any terrain except walls. They randomly select to go
     * straight, turn left, or turn right. ATV’s never reverse direction (they never need to). 
     * ATV’s drive through all traffic lights and crosswalk lights without stopping! 
     * 
     * Collision: An ATV dies if it collides with a living truck, car, or taxi,
     * and stays dead for 20 moves.
     * 
     */
    
    /**Constant that represents time vehicle takes to recover after collision. */
    private static final int COLLISION_RECOVERY_TIME = 20;
    
    /**
     * Constructor method that calls to the super constructor and creates an ATV object.
     * 
     * @param theX x value of the vehicle's spawn location
     * @param theY y value of the vehicles spawn location
     * @param theDir the direction the vehicle is facing upon spawntime.
     */
    public Atv(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, true, false);
    }
    
}

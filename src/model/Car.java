/*
 * TCSS 305 - Easy Street
 */

package model;

/**
 * This class creates a Vehicle object of type Car. 
 * for use with the easyStreet GUI.
 * 
 * @author Tanner Brown
 * @version 25 Oct 2017
 *
 */
public class Car extends AbstractVehicle {
    /*
     * Behavior:
     * This kept in code as quick reference to understand the behavior of this object.
     * 
     * A car prefers to drive straight ahead on the street if it can. 
     * If it cannot move straight ahead, it turns left if possible;
     * if it cannot turn left, it turns right if possible; as a last resort, it turns around.
     * 
     * Cars stop for red lights; if a traffic light is immediately ahead of the car and the 
     * light is red, the car stays still and does not move. It doesnt turn to avoid the light.
     * When the light turns green, the car resumes its original direction.
     * 
     * Cars ignore yellow and green lights
     * 
     * Cars stop for red and yellow crosswalk lights, but drive through green crosswalk
     * lights without stopping.
     * 
     * Collision: A car dies if it collides with a living truck, and stays dead for 10 moves.
     * 
     */
    /** Constant variable that represents how many turns the vehicle stays 'dead'.*/
    private static final int COLLISION_RECOVERY_TIME = 10;

    /**
     * Constructor method that calls to the super constructor.
     * 
     * @param theX x value of the vehicle's spawn location
     * @param theY y value of the vehicles spawn location
     * @param theDir the direction the vehicle is facing upon spawntime.
     */
    public Car(final int theX, final int theY, final Direction theDir) {

        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, false, false);
    }

    /**
     * Method that determines if this vehicle is able to move forward in the
     * direction its facing. This is dependent on its behavior and if it is
     * at a light or not. 
     * 
     * The Car type object will be unable to pass if it is
     * at a RED or YELLOW CROSS-WALK, or at a RED LIGHT 
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return True if this object can pass, false if not.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean canPass = true;

        //stops for: red Lights, & red or yellow crosswalk
        if ((theTerrain == Terrain.LIGHT && theLight == Light.RED) 
                        || ((theTerrain == Terrain.CROSSWALK) && (theLight == Light.YELLOW 
                        || theLight == Light.RED))) {
            canPass = false;
        } 
        return canPass;
    }

}

/*
 * TCSS 305 - Easy Street
 */

package model;

/**
 * This class creates a Vehicle object of type Taxi. 
 * @author Tanner Brown
 * @version 25 Oct 2017
 *
 */
public class Taxi extends AbstractVehicle {
    /*
     * Behavior:
     * This kept in code as quick reference to understand the behavior of this object.
     * 
     *  A taxi prefers to drive straight ahead if it can. If it cannot move straight ahead,
     *  it turns left if possible; if it cannot turn left, it turns right if possible; as a
     *  last resort, it turns around.
     * 
     * Taxis stop for red lights; if a traffic light is immediately ahead of the taxi and 
     * the light is red, the Taxi stays still and does not move until the light turns green.
     * It does not turn to avoid the light. When the light turns green the taxi resumes its 
     * original direction
     * 
     * Taxis ignore yellow and green lights
     * 
     * Taxis stop (temporarily) at red cross-walk lights. If a cross-walk light is immediately 
     * ahead of the taxi and the cross-walk light is red, the Taxi stays still and doesn't move
     * for 3 clock cycles or until the cross-walk light turns green, whichever occurs first. 
     * It does not turn to avoid the cross-walk light. When the cross-walk light turns green,
     * or after 3 clock cycles, whichever comes first, the taxi resumes its original direction.
     * A Taxi will drive through yellow or green cross-walk lights without stopping.
     * 
     * Collision: A taxi dies if it collides with a living truck, and stays dead for 10 moves.
     * 
     */
    /**Constant that represents time vehicle takes to recover after collision.*/
    private static final int COLLISION_RECOVERY_TIME = 10;

    /** default value for how long a taxi will wait at a red cross-walk light.  */
    private static final int LIGHT_WAIT_DEFAULT = 3;

    /** a counter that keeps track of how long the taxi has been waiting at a light.*/
    private int myLightCounter;


    /**
     * Constructor method that calls to the super constructor and creates a Taxi object.
     * 
     * @param theX x value of the vehicle's spawn location
     * @param theY y value of the vehicles spawn location
     * @param theDir the direction the vehicle is facing upon spawn-time.
     */
    public Taxi(final int theX, final int theY, final Direction theDir) {

        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, false, false);
        myLightCounter = LIGHT_WAIT_DEFAULT;

    }


    /**
     * Method that determines if this vehicle is able to move forward in the
     * direction its facing. This is dependent on its behavior and if it is
     * at a light or not. 
     * 
     * The Taxi will stop for red lights, red & yellow cross-walks, and will
     * continue through red cross-walk lights after waiting 3 turns.
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return whether or not this object may move onto the given type of
     *         terrain when the street lights are the given color.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean canPass = false;

        //stops for: red Lights, & red or yellow cross-walk
        if (myLightCounter > 0 && (theTerrain == Terrain.CROSSWALK && theLight == Light.RED)
                        || (theTerrain == Terrain.LIGHT && theLight == Light.RED)) {

            canPass = false;
            if (theTerrain == Terrain.CROSSWALK) {
                myLightCounter--;  
            }


        } else {
            canPass = true;
            myLightCounter = LIGHT_WAIT_DEFAULT;
        }
        return canPass;
    }
}

/*
 * TCSS 305 - Easy Street
 */

package model;

/**
 * This class creates a Vehicle object of type Bicycle. 
 * @author Tanner Brown
 * @version 25 Oct 2017
 *
 */
public class Bicycle extends AbstractVehicle {
    /*
     * Behavior:
     * This kept in code as quick reference to understand the behavior of this object.
     * 
     * Bicycles can travel on streets and through lights 
     * and cross-walk lights, but they prefer to travel on trails.
     * 
     * 
     * If the terrain in front of a bicycle is a trail, the bicycle 
     * always goes straight ahead in the direction it is facing. Trails are guaranteed 
     * to be straight (horizontal or vertical) lines that end at streets, and you are
     * guaranteed that a bicycle will never start on a trail facing terrain it cannot traverse.
     *
     *
     * • If a bicycle is not facing a trail, but there is a trail either to the left or to the 
     * right of the bicycle’s current direction, then the bicycle turns to face the trail and 
     * moves in that direction. You may assume that the map is laid out so that only one trail 
     * will neighbor a bicycle at any given time.
     *  
     *  
     * • If there is no trail straight ahead, to the left, or to the right, the bicycle prefers
     * to move straight ahead on a street (or light or crosswalk light) if it can. If it cannot
     * move straight ahead, it turns left if possible; if it cannot turn left, it turns right
     * if possible. As a last resort, if none of these three directions is legal (all
     * not streets or lights or crosswalk lights), the bicycle turns around.

     * • Bicycles ignore green lights.

     * • Bicycles stop for yellow and red lights; if a traffic light or crosswalk 
     * light is immediately ahead of the bicycle and the light is not green, the 
     * bicycle stays still and does not move unless a trail is to the left or right.
     * If a bicycle is facing a red or yellow light and there is a trail to the left or right, 
     * the bicycle will turn to face the trail.
     * 
     * Collision: A bicycle dies if it collides with a living truck, car, taxi, or ATV. 
     * It stays dead for 30 moves.
     * 
     */
    /**Constant that represents time vehicle takes to recover after collision.*/
    private static final int COLLISION_RECOVERY_TIME = 30;

    /**
     * Constructor method that calls to the super constructor and creates a Bicycle object.
     * 
     * @param theX x value of the vehicle's spawn location
     * @param theY y value of the vehicles spawn location
     * @param theDir the direction the vehicle is facing upon spawntime.
     */
    public Bicycle(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, false, true);
    }


    /**
     * Method that determines if this vehicle is able to move forward in the
     * direction its facing. This is dependent on its behavior and if it is
     * at a light or not. 
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return whether or not this object may move onto the given type of
     *         terrain when the street lights are the given color.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean canPass = true;

        //stops for: All Red and Yellow Lights
        if ((theTerrain == Terrain.LIGHT || theTerrain == Terrain.CROSSWALK) 
                        && theLight != Light.GREEN) {
            canPass = false;
        }
        return canPass;
    }
}

/*
 * TCSS 305 - Easy Street
 */
package model;

/**
 * This class creates a Vehicle object of type Truck. 
 * @author Tanner Brown
 * @version 23 Sep 2017
 */
public class Truck extends AbstractVehicle {
    /*
     * Behavior:
     * Trucks randomly select to go straight, turn left, or turn right. 
     * As a last resort, if none of these three directions is legal 
     * (all not streets, lights, or crosswalks), the truck turns around.
     * 
     * Trucks drive through all traffic lights without stopping!
     * 
     * Trucks stop for red crosswalk lights, but drive through 
     * yellow or green crosswalk lights without stopping.
     * 
     * Collision: A truck survives a collision with anything, living or dead.
     */
    /**Constant that represents time vehicle takes to recover after collision.*/
    private static final int COLLISION_RECOVERY_TIME = 0;


    /**
     * Constructor method that calls to the super constructor to create a Truck object.
     * @param theX x coordinate on the map
     * @param theY y coordinate on the map
     * @param theDir direction the vehicle is facing
     */
    public Truck(final int theX, final int theY, final Direction theDir) {

        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, true, false);

    }
 

    /**
     * Method that determines if this vehicle is able to move forward in the
     * direction its facing. This is dependent on its behavior and if it is
     * at a light or not. 
     * 
     * The truck will drive through any light except a red crosswalk light.
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return whether or not this object may move onto the given type of
     *         terrain when the street lights are the given color.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean canPass = true;
        //will drive through anything except red lights at crosswalks
        if ((theTerrain == Terrain.CROSSWALK && theLight == Light.RED)
                        || !verifyDirection(theTerrain)) {
            canPass = false;        
        }
        return canPass;
    }
} 
/*
 * TCSS 305 - Easy Street
 */

package model;

/**
 * This class creates a Human object. It inherits from AbstractVehicle that implements
 * the vehicle interface. 
 * 
 * @author Tanner Brown
 * @version 22 Oct 2017
 *
 */
public class Human extends AbstractVehicle {

    /*
     * Behavior:
     * This kept in code as quick reference to understand the behavior of this object.
     * 
     * Humans move in a random direction (straight, left, or right), 
     * always on grass or cross-walks.
     *
     * • A human never reverses direction unless there is no other option.
     *
     * • If a human is next to a cross-walks it will always choose to turn to face 
     * in the direction of the cross-walks. (The map of terrain will never contain
     * cross-walks that are so close together that a human might be adjacent to 
     * more than one at the same time.)
     *
     * • Humans do not travel through cross-walks when the cross-walks light is green. 
     * If a human is facing a green cross-walks, it will wait until the light changes
     * to yellow and then cross through the cross-walks. The human will not turn 
     * to avoid the cross-walks.
     *
     * • Humans travel through cross-walks when the cross-walks light is yellow or red.
     *
     * • Humans ignore the color of traffic lights
     * 
     * Collision: A human dies if it collides with any living 
     * vehicle except another human, and stays dead for 50 moves.
     * 
     */

    /** Constant variable that represents how many turns the vehicle stays 'dead'.*/
    private static final int COLLISION_RECOVERY_TIME = 50;


    /**
     * Constructor method that calls to the super constructor and creates a Bicycle object.
     * 
     * @param theX x value of the vehicle's spawn location
     * @param theY y value of the vehicles spawn location
     * @param theDir the direction the vehicle is facing upon spawntime.
     */
    public Human(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, COLLISION_RECOVERY_TIME, true, true);
    }

    /**
     * Method that determines if this vehicle is able to move forward in the
     * direction its facing. This is dependent on its behavior and if it is
     * at a light or not. 
     * 
     * The Human will only pass cross-walks when the light is not green 
     * and ignores yellow and red cross-walk lights. It will never travel
     * through a traffic light.
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return whether or not this object may move onto the given type of
     *         terrain when the street lights are the given color.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean canPass = true;

        if ((theTerrain == Terrain.CROSSWALK && theLight == Light.GREEN) 
                        || !verifyDirection(theTerrain))  {
            canPass = false;
        }
        return canPass;
    }
}

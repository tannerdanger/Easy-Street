/*
 * TCSS 305 - Easy Street
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This abstract class determines the majority of the behavior for the vehicle subclasses
 * in the Easy Street program. When a new vehicle object is created, it calls to this parent
 * class, which will create the object and assign its behavior. This class will also
 * determine the direction each vehicle will travel based on pre-determined behavior 
 * conditions.
 * 
 * @author Tanner Brown
 * @version 20 Oct 2017
 *
 */
public abstract class AbstractVehicle implements Vehicle {

    /** ArrayList that holds all of the valid terrain types for that vehicle. */
    private List<Terrain> myValidTerrainTypes;

    /** Boolean keeps track of if the vehicle is currently alive or not. */
    private boolean myIsAlive = true;

    /** Counter that keeps track of how many turns until a vehicle respawns. */
    private int myBeetlejuice;

    /** Direction vehicle is facing. */
    private Direction myOneDirection;

    /** The x coordinate of the vehicle. */
    private int myX;

    /** The x coordinate of the vehicle's spawn location. */
    private final int mySpawnX;

    /** The y coordinate of the vehicle. */
    private int myY;

    /** The y coordinate of the vehicle's spawn location. */
    private final int mySpawnY;


    /** Direction vehicle is moving at spawn time. */
    private final Direction mySpawnDirection;

    /** 
     * The constant variable that represents how many turns the vehicle is dead
     *  for, as well as its collision hierarchy.
     */
    private final int myDeathClock;

    /** Boolean that represents if this vehicle has random direction behavior. */
    private final boolean myRandomDriver;

    /** Boolean that represents if the vehicle prefers a specific terrain type. */
    private final boolean myGravitatingDriver;


    /**
     * Constructor that receives values from all vehicle child-classes and sets the basic
     * information for each vehicle object.
     * 
     * 
     * @param theX the X value location of the vehicle when it spawns
     * @param theY the Y value location of the vehicle when it spawns
     * @param theDir the direction the vehicle is facing when it spawns
     * @param theDeathClock the amount of time a vehicle stays dead before respawning
     * @param theRandom if it is a vehicle with random direction preference
     * @param theGravitating if the vehicle gravitates to a specific terrain type
     */
    protected AbstractVehicle(final int theX, final int theY, 
                              final Direction theDir, final int theDeathClock, 
                              final boolean theRandom, final boolean theGravitating) {

        myX = theX;
        mySpawnX = theX;
        myY = theY;
        mySpawnY = theY;
        myOneDirection = theDir;
        mySpawnDirection = theDir;
        myDeathClock = theDeathClock;
        validTerrain();       
        myRandomDriver = theRandom;
        myGravitatingDriver = theGravitating;

    }

    /**
     * Returns whether or not this object may move onto the given type of
     * terrain, when the street lights are the given color. The only vehicle
     * using this parent method is the ATV type, because it always is able to
     * pass regardless of terrain and light color.
     * 
     * @param theTerrain The terrain.
     * @param theLight The light color.
     * @return whether or not this object may move onto the given type of
     *         terrain when the street lights are the given color.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return true;
    }

    /**
     * Returns the direction this object would like to move, based on the given
     * map of the neighboring terrain.
     * 
     * @param theNeighbors The map of neighboring terrain.
     * @return the direction this object would like to move.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        boolean noDirectionFound = true;
        Direction newDirection = null;
        final Direction initDirection = getDirection();

        //First checks to ensure driver doesn't need to turn around
        if (shouldReverse(theNeighbors, initDirection)) {
            newDirection = initDirection.reverse();
            noDirectionFound = false;
        }

        //Instructions for drivers who gravitate to a specific terrain type
        if (noDirectionFound && myGravitatingDriver) {
            newDirection = gravitate(theNeighbors, initDirection);
            // if value is null, no direction is found
            noDirectionFound = newDirection == null; 

        }

        //Random drivers instructions
        while (noDirectionFound && myRandomDriver) {
            newDirection = Direction.random();
            if ((newDirection != getDirection().reverse()) 
                            && verifyDirection(theNeighbors.get(newDirection))) {
                noDirectionFound = false;
            }

        }
        //Non random drivers instructions
        if (noDirectionFound || newDirection == null) {
            newDirection = basicDirection(theNeighbors, initDirection);
        }
        return newDirection;

    }
    
    /**
     * Helper method that determines the direction that a basic vehicle should go.
     * @param theNeighbors a map of the surrounding terrain
     * @param theInitDirection the initial direction the vehicle is going
     * @return returns the new direction the vehicle should travel
     */
    private Direction basicDirection(final Map<Direction, Terrain> theNeighbors,
                                      final Direction theInitDirection) {
        Direction newDirection = null;

        if (verifyDirection(theNeighbors.get(theInitDirection))) {
            newDirection = theInitDirection;
        } else if (verifyDirection(theNeighbors.get(theInitDirection.left()))) {
            newDirection = theInitDirection.left();
        } else if (verifyDirection(theNeighbors.get(theInitDirection.right()))) {
            newDirection = theInitDirection.right();
        }



        return newDirection;
    }

    /**
     * Helper method that determines the direction a vehicle should go if it 
     * prefers a specific type.
     * 
     * @param theNeighbors a map of the terrain surrounding the vehicle
     * @param theDirection the vehicle the direction is going
     * @return the direction the vehicle will turn to, or null if a new direction 
     * isn't discovered.
     */
    private Direction gravitate(final Map<Direction, Terrain> theNeighbors, 
                                final Direction theDirection) {
        final Terrain targetTerrain;
        Direction newDirection = null;

        //Remedying this checkstyle problem will likely make the program less effective.
        if ("human".equals(toString())) {
            targetTerrain = Terrain.CROSSWALK;
        } else {
            targetTerrain = Terrain.TRAIL;
        }

        if (theNeighbors.get(theDirection) == targetTerrain) {
            newDirection = theDirection;
        } else if (theNeighbors.get(theDirection.left()) == targetTerrain) {
            newDirection = theDirection.left();
        } else if (theNeighbors.get(theDirection.right()) == targetTerrain) {
            newDirection = theDirection.right();
        }

        return newDirection;
    }


    /**Helper method that determines what terrain each vehicle can travel on. */
    private void validTerrain() {

        
        final String vehicleType = toString();
        myValidTerrainTypes = new ArrayList<Terrain>();

        /*
         * Truck: streets, lights, cross walks
         * Car: streets, lights, cross walks
         * Taxi: streets, lights, cross walks
         * ATV: all but wall
         * Bike: streets, lights, cross walk, trails
         * human: grass/cross walk
         */

        //Everyone can go on cross walk
        myValidTerrainTypes.add(Terrain.CROSSWALK); 

        if ("human".equals(vehicleType)) {
            myValidTerrainTypes.add(Terrain.GRASS);

        } else if ("car".equals(vehicleType) || "truck".equals(vehicleType) 
                        || "taxi".equals(vehicleType)) { //everyone else
            myValidTerrainTypes.add(Terrain.STREET);
            myValidTerrainTypes.add(Terrain.LIGHT);

        } else if ("bicycle".equals(vehicleType)) {
            myValidTerrainTypes.add(Terrain.TRAIL);
            myValidTerrainTypes.add(Terrain.STREET);
            myValidTerrainTypes.add(Terrain.LIGHT);


        } else if ("atv".equals(vehicleType)) {
            myValidTerrainTypes.add(Terrain.TRAIL);
            myValidTerrainTypes.add(Terrain.STREET);
            myValidTerrainTypes.add(Terrain.LIGHT);
            myValidTerrainTypes.add(Terrain.GRASS);
        }


    }


    /**
     * Helper method that determines if the vehicle should reverse. 
     * Used by all vehicle types.
     * 
     * @param theNeighbors a map of the surrounding terrain
     * @param theDirection the initial direction the vehicle is heading
     * @return true if the vehicle should reverse.
     */
    private boolean shouldReverse(final Map<Direction, Terrain> theNeighbors,
                                    final Direction theDirection) {
        boolean reverse = false;
        final Terrain init = theNeighbors.get(theDirection);
        final Terrain left = theNeighbors.get(theDirection.left());
        final Terrain right = theNeighbors.get(theDirection.right());


        if (!myValidTerrainTypes.contains(init) 
                        && !myValidTerrainTypes.contains(left) 
                        && !myValidTerrainTypes.contains(right)) {
            reverse = true;
        }

        return reverse;

    }

    /**
     * Helper method used by all child classes to determine if the direction
     * it chooses to go is one that it is able to travel on. 
     * 
     * This method is protected, and not private, because it is used in the child 
     * classes to determine if it can pass or not.
     * 
     * @param theTerrain the terrain the vehicle wants to travel on
     * @return True if the terrain is valid, false if not.
     */
    protected boolean verifyDirection(final Terrain theTerrain) {

        return myValidTerrainTypes.contains(theTerrain);
    }



    /**
     * This method is called when this Vehicle collides 
     * with the specified other Vehicle, determines which one is "dead"
     * and sets the respawn time for that vehicle.
     * 
     * @param theOther The other object.
     */
    @Override
    public void collide(final Vehicle theOther) {
        //If vehicle has a longer death time than other vehicle, it dies.
        if (myDeathClock > theOther.getDeathTime()) {
            myIsAlive = false;
            myBeetlejuice = myDeathClock;
        } 
    }

    /**
     * Returns the number of updates between this vehicle's death and when it
     * should be revived.
     * 
     * @return the number of updates.
     */
    @Override
    public int getDeathTime() {
        return myDeathClock;
    }


    /**
     * This method Returns the file name of the image for this Vehicle object.
     * 
     * @return the file name.
     */
    @Override
    public String getImageFileName() {
        String name = getClass().getSimpleName().toLowerCase() + ".gif"; 
        if (!(isAlive())) {
            name = getClass().getSimpleName().toLowerCase() + "_dead.gif";  
        }    

        return name;
    }


    /**
     * Returns this Vehicle object's direction.
     * 
     * @return the direction.
     */
    @Override
    public Direction getDirection() {
        return myOneDirection;
    }


    /**
     * Returns this Vehicle object's x-coordinate.
     * 
     * @return the x-coordinate.
     */
    @Override
    public int getX() {
        return myX;
    }


    /**
     * Returns this Vehicle object's y-coordinate.
     * 
     * @return the y-coordinate.
     */
    @Override
    public int getY() {
        return myY;
    }


    /**
     * This method returns whether this Vehicle object is 
     * alive and should move on the map.
     * 
     * @return true if the object is alive, false otherwise.
     */
    @Override
    public boolean isAlive() {
        return myIsAlive;
    }


    /**
     * This method is called by the UI to notify a dead vehicle that 1 
     * movement round has passed, so that it will become 1 move closer to revival.
     */
    @Override
    public void poke() {
        myBeetlejuice--;
        if (!myIsAlive && myBeetlejuice == 0) {
            myIsAlive = true;
            setDirection(Direction.random());

        }
    }


    /**
     * Moves this vehicle back to its original position and direction.
     */
    @Override
    public void reset() {
        myBeetlejuice = 0;
        myIsAlive = true;
        myX = mySpawnX;
        myY = mySpawnY;
        myOneDirection = mySpawnDirection;
    }


    /**
     * Sets this object's facing direction to the given value.
     * 
     * @param theDir The new direction.
     */
    @Override
    public void setDirection(final Direction theDir) {
        myOneDirection = theDir;
    }


    /**
     * Sets this object's x-coordinate to the given value.
     * 
     * @param theX The new x-coordinate.
     */
    @Override
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets this object's y-coordinate to the given value.
     * 
     * @param theY The new y-coordinate.
     */
    @Override
    public void setY(final int theY) {
        myY = theY;

    }

    /**
     * toString() method that returns the basic name of the vehicle by printing
     * a string value of its class. Because this method is used by another to
     * determine important information based on the name of the vehicle, this method
     * has been made final to prevent overriding. 
     */
    @Override
    public final String toString() {

        return getClass().getSimpleName().toLowerCase();

    }

}

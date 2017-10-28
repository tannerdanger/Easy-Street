/*
 * TCSS 305 - Easy Street
 */

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Direction;
import model.Light;
import model.Terrain;
import model.Truck;
import org.junit.Before;
import org.junit.Test;



/**
 * Test class for the Truck object. Ensures all methods operating properly. 
 * @author Tanner Brown
 * @version 24 Oct 2017
 *
 */
public class TruckTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored. Shamelessly stolen from provided
     * HumanTest class.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    /** Default truck object to be used in tests. */
    private Truck myTruck;


    /**
     * quick setup to initialize a common truck object to use in several classes.
     */
    @Before
    public void setUp() {
        myTruck = new Truck(10, 11, Direction.NORTH);
    }

    /** Test method for Truck constructor. */
    @Test
    public void testTruckConstructor()  {     
        assertEquals("Truck x coordinate not initialized correctly!", 10, myTruck.getX());
        assertEquals("Truck y coordinate not initialized correctly!", 11, myTruck.getY());
        assertEquals("Truck direction not initialized correctly!",
                     Direction.NORTH, myTruck.getDirection());
        assertEquals("Truck death time not initialized correctly!", 0, myTruck.getDeathTime());
        assertTrue("Truck isAlive() fails initially!", myTruck.isAlive());        
    }

    /** Test method for getters and setters in the Abstract class. */
    @Test
    public void testTruckSetters() {

        //test setX and getX
        myTruck.setX(20);
        assertEquals("Truck setX failed1", 20, myTruck.getX());

        //test setY and getY
        myTruck.setY(25);
        assertEquals("Truck setY failed!", 25, myTruck.getY());

        //test setDirection and getDirection
        myTruck.setDirection(Direction.EAST);
        assertEquals("Truck setDriection failed!", Direction.EAST, myTruck.getDirection());

        //test getDeathTime
        assertEquals("Truck returning incorrect death time!", 0, myTruck.getDeathTime());

        //test image file name
        assertEquals("Truck returning wrong name!", "truck.gif", myTruck.getImageFileName());

    }



    /**
     * Test method for the Truck canPass method.
     * It will determine if a truck can pass onto the next spot based
     * on the terrain and light conditions. 
     * 
     * The truck should be able to pass through any condition
     * except for red crosswalk lights.
     */
    @Test
    public void testCanPass() {
        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.STREET);
        validTerrain.add(Terrain.CROSSWALK);
        validTerrain.add(Terrain.LIGHT);

        final Truck t2 = new Truck(0, 0, Direction.NORTH);
        //Test each terrain type as a desitnation
        for (final Terrain destinationTerrain : Terrain.values()) {
            //Test each terrain type under each light condition
            for (final Light currentLightCondition : Light.values()) {

                if (destinationTerrain == Terrain.STREET) {

                    //Trucks can drive on streets under any light condition
                    assertTrue("Truck should always be able to pass on the street."
                                    + " Current condition - Light: " + currentLightCondition, 
                                    t2.canPass(destinationTerrain, currentLightCondition));

                } else if (destinationTerrain == Terrain.CROSSWALK) {

                    //Trucks should only stop for red lights at crosswalks
                    if (currentLightCondition == Light.RED) {   
                        assertFalse("Truck should not pass on red lights at crosswalks"
                                        + " Current light condition: " + currentLightCondition
                                        + " Current Terrain: " + destinationTerrain, 
                                        t2.canPass(destinationTerrain, currentLightCondition));
                    } else {
                        //if light is yellow or red, truck should be able to pass
                        assertTrue("Truck should be able to pass at a crosswalk with a " 
                                        + currentLightCondition + "light", 
                                        t2.canPass(destinationTerrain, currentLightCondition));
                    }
                } else if (destinationTerrain == Terrain.LIGHT) {
                    //Trucks will never stop for lights. Typical. 
                    assertTrue("Truck should never stop at a Light. Current light condition: " 
                                    + currentLightCondition, 
                                    t2.canPass(destinationTerrain, currentLightCondition));
                } else if (!validTerrain.contains(destinationTerrain)) {
                    assertFalse("The Truck should NOT be able to pass " + destinationTerrain
                                + "with light condition: " + currentLightCondition, 
                                t2.canPass(destinationTerrain, currentLightCondition));
                }
            }
        }
    }

    /**
     * Test case method that tests the chooseDirection() method for the Truck object.
     * It runs chooses a new direction 50 times to ensure that it runs enough times
     * to randomly pick each direction.
     */
    @Test
    public void testChooseDirection() {

        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();

        final Truck t2 = new Truck(0, 0, Direction.NORTH);
        neighbors.put(Direction.WEST, Terrain.LIGHT);
        neighbors.put(Direction.EAST, Terrain.CROSSWALK);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = t2.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true; 
            }
        }

        assertTrue("Truck chooseDirection() method fails to select randomly. Seen North: " 
                        + seenNorth + ", West: " + seenWest + ", East: " 
                        + seenEast, seenNorth && seenEast && seenWest);

        assertFalse("Truck should not be reversing", seenSouth);
    }


    /**
     * Test case for the chooseDirection method. Ensures a Truck object
     * reverses only when necessary. 
     */
    @Test
    public void testChooseDirectionReverse() {

        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();

        neighbors.put(Direction.NORTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.WALL);
        neighbors.put(Direction.WEST, Terrain.WALL);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        final Truck t2 = new Truck(0, 0, Direction.NORTH);
        assertEquals("Truck chooseDirection() failed "
                        + "when reverse was the only valid choice!",
                        Direction.SOUTH, t2.chooseDirection(neighbors));

    }


}



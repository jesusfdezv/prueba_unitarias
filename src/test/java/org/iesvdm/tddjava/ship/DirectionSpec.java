package org.iesvdm.tddjava.ship;

import org.testng.annotations.*;
import static org.testng.Assert.*;

@Test
public class DirectionSpec {
    @Test
    public void whenGetFromShortNameNThenReturnDirectionN() {
        assertEquals(Direction.getFromShortName('N'), Direction.NORTH);
    }
    @Test
    public void whenGetFromShortNameWThenReturnDirectionW() {
        assertEquals(Direction.getFromShortName('W'), Direction.WEST);
    }
    @Test
    public void whenGetFromShortNameBThenReturnNone() {
        assertEquals(Direction.getFromShortName('B'), Direction.NONE);
    }
    @Test
    public void givenSWhenLeftThenE() {
        assertEquals(Direction.SOUTH.turnLeft(), Direction.EAST);
    }
    @Test
    public void givenNWhenLeftThenW() {
        assertEquals(Direction.NORTH.turnLeft(), Direction.WEST);
    }
    @Test
    public void givenSWhenRightThenW() {
        assertEquals(Direction.SOUTH.turnRight(), Direction.WEST);
    }
    @Test
    public void givenWWhenRightThenN() {
        assertEquals(Direction.WEST.turnRight(), Direction.NORTH);
    }

}

package org.iesvdm.tddjava.ship;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

@Test
public class LocationSpec {

    private final int x = 12;
    private final int y = 32;
    private final Direction direction = Direction.NORTH;
    private Point max;
    private Location location;
    private List<Point> obstacles;

    @BeforeMethod
    public void beforeTest() {
        max = new Point(50, 50);
        location = new Location(new Point(x, y), direction);
        obstacles = new ArrayList<Point>();
    }
    @Test
    public void whenInstantiatedThenXIsStored() {
        assertEquals(location.getX(), x);
    }
    @Test
    public void whenInstantiatedThenYIsStored() {
        assertEquals(location.getY(), y);
    }
    @Test
    public void whenInstantiatedThenDirectionIsStored() {
        assertEquals(location.getDirection(), direction);
    }
    @Test
    public void givenDirectionNWhenForwardThenYDecreases() {
        location.forward(max);
        assertEquals(location.getY(), y - 1);
    }
    @Test
    public void givenDirectionSWhenForwardThenYIncreases() {
        location.setDirection(Direction.SOUTH);
        location.forward(max);
        assertEquals(location.getY(), y + 1);
    }
    @Test
    public void givenDirectionEWhenForwardThenXIncreases() {
        location.setDirection(Direction.EAST);
        location.forward(max);
        assertEquals(location.getX(), x + 1);
    }
    @Test
    public void givenDirectionWWhenForwardThenXDecreases() {
        location.setDirection(Direction.WEST);
        location.forward(max);
        assertEquals(location.getX(), x - 1);
    }
    @Test
    public void givenDirectionNWhenBackwardThenYIncreases() {
        location.backward(max);
        assertEquals(location.getY(), y + 1);
    }
    @Test
    public void givenDirectionSWhenBackwardThenYDecreases() {
        location.setDirection(Direction.SOUTH);
        location.backward(max);
        assertEquals(location.getY(), y - 1);
    }
    @Test
    public void givenDirectionEWhenBackwardThenXDecreases() {
        location.setDirection(Direction.EAST);
        location.backward(max);
        assertEquals(location.getX(), x - 1);
    }
    @Test
    public void givenDirectionWWhenBackwardThenXIncreases() {
        location.setDirection(Direction.WEST);
        location.backward(max);
        assertEquals(location.getX(), x + 1);
    }
    @Test
    public void whenTurnLeftThenDirectionIsSet() {
        location.turnLeft();
        assertEquals(location.getDirection(), Direction.WEST);
    }
    @Test
    public void whenTurnRightThenDirectionIsSet() {
        location.turnRight();
        assertEquals(location.getDirection(), Direction.EAST);
    }
    @Test
    public void givenSameObjectsWhenEqualsThenTrue() {
        Location otherLocation = location;
        assertTrue(location.equals(otherLocation));
    }
    @Test
    public void givenDifferentObjectWhenEqualsThenFalse() {
        Object obj = new Object();
        assertFalse(location.equals(obj));
    }
    @Test
    public void givenDifferentXWhenEqualsThenFalse() {
        Location otherLocation = new Location(new Point(x + 1, y), direction);
        assertFalse(location.equals(otherLocation));
    }
    @Test
    public void givenDifferentYWhenEqualsThenFalse() {
        Location otherLocation = new Location(new Point(x, y + 1), direction);
        assertFalse(location.equals(otherLocation));
    }
    @Test
    public void givenDifferentDirectionWhenEqualsThenFalse() {
        Location otherLocation = new Location(new Point(x, y), Direction.SOUTH);
        assertFalse(location.equals(otherLocation));
    }
    @Test
    public void givenSameXYDirectionWhenEqualsThenTrue() {
        Location otherLocation = new Location(new Point(x, y), direction);
        assertTrue(location.equals(otherLocation));
    }
    @Test
    public void whenCopyThenDifferentObject() {
        Location copy = location.copy();
        assertNotSame(location, copy);
    }
    @Test
    public void whenCopyThenEquals() {
        Location copy = location.copy();
        assertEquals(location, copy);
    }
    @Test
    public void givenDirectionEAndXEqualsMaxXWhenForwardThen1() {
        location = new Location(new Point(max.getX(), y), Direction.EAST);
        location.forward(max);
        assertEquals(location.getX(), 1);
    }
    @Test
    public void givenDirectionWAndXEquals1WhenForwardThenMaxX() {
        location = new Location(new Point(1, y), Direction.WEST);
        location.forward(max);
        assertEquals(location.getX(), max.getX());
    }
    @Test
    public void givenDirectionNAndYEquals1WhenForwardThenMaxY() {
        location = new Location(new Point(x, 1), Direction.NORTH);
        location.forward(max);
        assertEquals(location.getY(), max.getY());
    }
    @Test
    public void givenDirectionSAndYEqualsMaxYWhenForwardThen1() {
        location = new Location(new Point(x, max.getY()), Direction.SOUTH);
        location.forward(max);
        assertEquals(location.getY(), 1);
    }
    @Test
    public void givenObstacleWhenForwardThenReturnFalse() {
        obstacles.add(new Point(x, y - 1));
        assertFalse(location.forward(max, obstacles));
    }
    @Test
    public void givenObstacleWhenBackwardThenReturnFalse() {
        obstacles.add(new Point(x, y + 1));
        assertFalse(location.backward(max, obstacles));
    }
}

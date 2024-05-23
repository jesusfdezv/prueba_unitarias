package org.iesvdm.tddjava.ship;

import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

@Test
public class ShipSpec {

    private Ship ship;
    private Location location;
    private Planet planet;

    @BeforeMethod
    public void beforeTest() {
        Point max = new Point(50, 50);
        location = new Location(new Point(21, 13), Direction.NORTH);
        List<Point> obstacles = new ArrayList<>();
        obstacles.add(new Point(44, 44));
        obstacles.add(new Point(45, 46));
        planet = new Planet(max, obstacles);
        ship = new Ship(location, planet);
    }
    @Test
    public void whenInstantiatedThenLocationIsSet() {
        assertEquals(ship.getLocation(), location);
    }
    @Test
    public void whenMoveForwardThenForward() {
        assertTrue(ship.moveForward());
    }
    @Test
    public void whenMoveBackwardThenBackward() {
        assertTrue(ship.moveBackward());
    }
    @Test
    public void whenTurnLeftThenLeft() {
        ship.turnLeft();
        assertEquals(ship.getLocation().getDirection(), Direction.WEST);
    }
    @Test
    public void whenTurnRightThenRight() {
        ship.turnRight();
        assertEquals(ship.getLocation().getDirection(), Direction.EAST);
    }
    @Test
    public void whenReceiveCommandsFThenForward() {
        assertEquals(ship.receiveCommands("f"), "O");
    }
    @Test
    public void whenReceiveCommandsBThenBackward() {
        assertEquals(ship.receiveCommands("b"), "O");
    }
    @Test
    public void whenReceiveCommandsLThenTurnLeft() {
        assertEquals(ship.receiveCommands("l"), "");
        assertEquals(ship.getLocation().getDirection(), Direction.WEST);
    }
    @Test
    public void whenReceiveCommandsRThenTurnRight() {
        assertEquals(ship.receiveCommands("r"), "");
        assertEquals(ship.getLocation().getDirection(), Direction.EAST);
    }
    @Test
    public void whenReceiveCommandsThenAllAreExecuted() {
        assertEquals(ship.receiveCommands("frbl"), "OOOO");
    }
    @Test
    public void whenInstantiatedThenPlanetIsStored() {
        assertEquals(ship.getPlanet(), planet);
    }
    @Test
    public void givenDirectionEAndXEqualsMaxXWhenReceiveCommandsFThenWrap() {
        ship.getLocation().setDirection(Direction.EAST);
        ship.getLocation().getPoint().setX(50);
        assertTrue(ship.moveForward());
        assertEquals(ship.getLocation().getPoint().getX(), 1);
    }
    @Test
    public void givenDirectionEAndXEquals1WhenReceiveCommandsBThenWrap() {
        ship.getLocation().setDirection(Direction.EAST);
        ship.getLocation().getPoint().setX(1);
        assertTrue(ship.moveBackward());
        assertEquals(ship.getLocation().getPoint().getX(), 50);
    }
    @Test
    public void whenReceiveCommandsThenStopOnObstacle() {
        planet.getObstacles().add(new Point(21, 14));
        assertEquals(ship.receiveCommands("f"), "OX");
        assertEquals(ship.getLocation().getPoint().getY(), 13);
    }
    @Test
    public void whenReceiveCommandsThenOForOkAndXForObstacle() {
        planet.getObstacles().add(new Point(21, 14));
        assertEquals(ship.receiveCommands("fbrl"), "OXOX");
    }
}

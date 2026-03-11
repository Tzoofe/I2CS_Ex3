import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    @Test
    public void testAllDistance() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };

        Map map = new Map(grid);
        Map2D dist = map.allDistance(new Index2D(1, 1), 1);
        // Start = 0, neighbor = 1, corner = 2
        assertEquals(0, dist.getPixel(1, 1));
        assertEquals(1, dist.getPixel(1, 0));
        assertEquals(2, dist.getPixel(0, 0));

    }
    @Test
    public void testShortestPath() {
        //map with wall in the middle
        int[][] grid = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        Map map = new Map(grid);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(2, 2);

        Pixel2D[] path = map.shortestPath(start, end, 1);

        //tests
        assertNotNull(path);
        //check starting points and ending
        assertEquals(0, path[0].getX());
        assertEquals(0, path[0].getY());
        assertEquals(2, path[path.length-1].getX());
        assertEquals(2, path[path.length-1].getY());

        //check for length,
        assertEquals(3, path.length);
    }

    @Test
    public void testInitWithDimensions() {
        Map map = new Map(5, 3, 7);
        assertEquals(5, map.getWidth());
        assertEquals(3, map.getHeight());
        assertEquals(7, map.getPixel(0,0));
        assertEquals(7, map.getPixel(4,2));
    }
    @Test
    public void testSetAndGetPixel() {
        Map map = new Map(3, 3, 0);
        map.setPixel(1, 2, 42);
        assertEquals(42, map.getPixel(1, 2));

        Pixel2D p = new Index2D(2, 1);
        map.setPixel(p, 99);
        assertEquals(99, map.getPixel(p));
    }
    @Test
    public void testIsInside() {
        Map map = new Map(5, 5, 0);
        assertTrue(map.isInside(new Index2D(0, 0)));
        assertTrue(map.isInside(new Index2D(4, 4)));
        assertFalse(map.isInside(new Index2D(-1, 0)));
        assertFalse(map.isInside(new Index2D(5, 0)));
        assertFalse(map.isInside(new Index2D(0, 5)));
    }

    @Test
    public void testCyclicFlag() {
        Map map = new Map(3, 3, 0);
        assertTrue(map.isCyclic());  // default is true
        map.setCyclic(false);
        assertFalse(map.isCyclic());
    }

    @Test
    public void testFillBasic() {
        int[][] grid = {
                {0, 0, 1},
                {0, 0, 1},
                {1, 1, 1}
        };
        Map map = new Map(grid);
        int count = map.fill(new Index2D(0, 0), 5);

        assertEquals(4, count);  // 4 zeros connected
        assertEquals(5, map.getPixel(0, 0));
        assertEquals(5, map.getPixel(1, 0));
        assertEquals(5, map.getPixel(0, 1));
        assertEquals(5, map.getPixel(1, 1));
        assertEquals(1, map.getPixel(2, 0));  // wall unchanged
    }

    @Test
    public void testFillSameColor() {
        Map map = new Map(3, 3, 5);
        int count = map.fill(new Index2D(1, 1), 5);  // same color
        assertEquals(0, count);  // nothing to fill
    }

    @Test
    public void testFillOutside() {
        Map map = new Map(3, 3, 0);
        int count = map.fill(new Index2D(-1, 0), 5);  // outside
        assertEquals(0, count);
    }

    @Test
    public void testShortestPathNoPath() {
        int[][] grid = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };
        Map map = new Map(grid);
        map.setCyclic(false);
        Pixel2D[] path = map.shortestPath(new Index2D(0, 0), new Index2D(0, 2), 1);
        assertNull(path);  // wall blocks path
    }

    @Test
    public void testShortestPathCyclic() {
        int[][] grid = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };
        Map map = new Map(grid);
        map.setCyclic(true);
        Pixel2D[] path = map.shortestPath(new Index2D(0, 0), new Index2D(2, 0), 1);
        assertNotNull(path);  // can wrap around
    }

    @Test
    public void testAllDistanceUnreachable() {
        int[][] grid = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };
        Map map = new Map(grid);
        map.setCyclic(false);
        Map2D dist = map.allDistance(new Index2D(0, 0), 1);
        assertEquals(-1, dist.getPixel(0, 2));  // unreachable
    }


}

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

        //check for length, should be 5
        assertEquals(5, path.length);
    }

}
